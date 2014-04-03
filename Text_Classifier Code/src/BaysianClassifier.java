
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;


/**
 * The Naive-Bayes Class  
 */

/**
 * @author nitish
 *
 */
public class BaysianClassifier implements BaysianClassifierInterface {

	// The threshold to get to select whether an output is valid
	final float outputProbabilityTreshold= 0.003f;
	// There is a minimum denominator value to remove the possibility of INF and NaN
    final float  minimumDenominatorValue= 0.0000000001f;
    
    /**
	 * Number of columns in the training data.
	 */
	int numberOfColumns=0;
	
	/**
	 * Domains for each column of the training data
	 */
	Vector<Domain> domains=new Vector<Domain>();
	
	/**
	 * TrainingData used to calculate the probabilities. It is flushed after the constructor
	 */
	Vector<Vector<Integer>> data=new Vector<Vector<Integer>>();
	
	/**
	 * The number of training data set.
	 */
	int numberOfTrainingData=0;
	
	/**
	 * Probabilities of each output -> P(Output).
	 */
	Vector<Float> probabilitiesOfOutputs=new Vector<Float>(0);
	
	/**
	 * Probabilities of each input -> P(effectColum:effectValue | lastColumn:causeValue).
	 */
	Map<Long, Float> probabilitiesOfInputs=new HashMap<Long, Float>();
	
	
    /**
	 * BayesianClassifier constructor. It constructs the classifier with raw training data from the file
	 * and uses domains to generate discrete values (TrainingData).
	 *
	 * Beware : The file must not have an empty line at the end.
	 */
    public BaysianClassifier(String filename,Vector<Domain> _domains) {
    	domains = _domains;
    	numberOfColumns = _domains.size();
    	constructClassifier(filename); 
		// TODO Auto-generated constructor stub
	}
	
    /**
     * BayesianClassifier constructor. It constructs a classifier with the specified domain.
     * Raw training data are not given, it is possible to add data after the construction.
     * @return 
     */
    public  BaysianClassifier(Vector<Domain> _domains) {
    	domains = _domains;
    	numberOfColumns = _domains.size();
    	calculateProbabilitiesOfInputs();
    	calculateProbabilitiesOfOutputs();
    	numberOfTrainingData = data.size();
    	data.clear();
    }
    /**
	 * Construct the classifier from the RawTrainingData in the file.
	 *
	 * Beware : The file must not have an empty line at the end.
	 */
	public  void constructClassifier(String filename)
	{
		InputStream input = new InputStream() {
			
			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		try {
			input = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        Scanner inputFile=new Scanner(input);
		while (inputFile.hasNextLine()) {
			Vector<Integer> trainingData = new Vector<Integer>();
			float value;
			for (int i = 0; i < numberOfColumns; i++) {
				value=inputFile.nextFloat();
				trainingData.add(domains.get(i).calculateDiscreteValue(value)); //calculates discrete value here
			}

			data.add(trainingData);
		}

		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inputFile.close();
		calculateProbabilitiesOfInputs();
		calculateProbabilitiesOfOutputs();
		numberOfTrainingData = data.size();
		data.clear();
	}


    /**
     * Calculate the probabilities for each possibility of inputs.
     */
	@Override
	public void calculateProbabilitiesOfInputs() {
		// TODO Auto-generated method stub
		for (int i = 0; i < numberOfColumns - 1; i++) {
			for (int j = 0; j < domains.get(i).getNumberOfValues(); j++) {
				for (int k = 0; k < getOuputDomain().getNumberOfValues(); k++) {
					calculateProbability(i, j, k);
				}
			}
		}
		
	}
	

    /**
     * Calculate the probability of P(effectColum:effectValue | lastColumn:causeValue)
     * It saves data into the variable probabilitiesOfInputs.
     */

	@Override
	public void calculateProbability(int effectColumn, int effectValue,int causeValue) {
		// TODO Auto-generated method stub

		// The numerator is the number of TrainingData with this effectValue given this causeValue
		float numerator = 0.0f;
		// The denominator is the number of TrainingData with this causeValue
		float denominator = 0.0f;

		//Calculate the numerator and denominator by scanning the TrainingData
		for (int i = 0; i < data.size(); i++) {
			Vector<Integer> trainingData = data.get(i);
			if (trainingData.get(numberOfColumns - 1) == causeValue) {
				denominator++;
				if (trainingData.get(effectColumn) == effectValue) {
					numerator++;
				}
			}
		}

		float probability = 0.0f;
		if (denominator != 0) {
			probability = numerator / denominator;
		}
		long key = calculateMapKey(effectColumn, effectValue, causeValue);
		probabilitiesOfInputs.put(key, probability);
		
		
	}


   /**
    * Calculate P(Output) of each output.
    * It saves data into the variable probabilitiesOfOuputs.
    */
	@Override
	public void calculateProbabilitiesOfOutputs() {
		// TODO Auto-generated method stub
		for (int i = 0; i < getOuputDomain().getNumberOfValues(); i++) {
			float count = 0.0f;
			
			for (int j = 0; j < data.size(); j++) {
				if (data.get(j).get(numberOfColumns - 1) == i) {
					count++;
				}
			}
			//System.out.println(count / (float) data.size());
			if (data.size() != 0) {
				probabilitiesOfOutputs.add(count / (float) data.size());
			} else {
				probabilitiesOfOutputs.add((float) 0);
			}
		}
		
	}

	/**
	 * Calculate the map key for each value in the variable probabilitiesOfInputs
	 */
	@Override
	public long calculateMapKey(int effectColumn, int effectValue,int causeValue) {
		// TODO Auto-generated method stub
		return causeValue * 100000 + effectColumn * 100 + effectValue;
	}
	
	/**
	 * Calculate the most probable output given this input with this formula :
	 * P(Output | Input) = 1/Z * P(Output) * P(InputValue1 | Ouput) * P(InputValue2 | Ouput) * ...
	 * The output with the highest probability is returned.
	 */
	@Override
	public int calculateOutput(Vector<Float> input) {
		// TODO Auto-generated method stub
		float highestProbability = (float) outputProbabilityTreshold;
		int highestOutput = (int) (Math.random()%getOuputDomain().getNumberOfValues());   //getOuputDomain().getNumberOfValues();
		long key = 0;

		for (int i = 0; i < getOuputDomain().getNumberOfValues(); i++) {
			float probability = probabilitiesOfOutputs.get(i);

			for (int j = 0; j < input.size(); j++) {
				key = calculateMapKey(j, domains.get(j).calculateDiscreteValue(input.get(j)), i);
				probability *= probabilitiesOfInputs.get(key);
			}

			//System.out.println("highest"+probability);
			
			if (probability > highestProbability) {
				highestProbability = probability;
				highestOutput = i;
			}
		}
		return highestOutput;
	}
	

    /**
     * Calculate the probability of this output given this input.
     * P(Output | Input) = 1/Z * P(Output) * P(InputValue1 | Ouput) * P(InputValue2 | Ouput) * ...
     */
	// Actual Baysian Formula wiki
	@Override
	public float calculateProbabilityOfOutput(Vector<Float> input,float output) {
		// TODO Auto-generated method stub
		long key = 0;

		Vector<Float> probabilities = new Vector<Float>();

		for(int i = 0; i < getOuputDomain().getNumberOfValues(); i++) {
			float probability = probabilitiesOfOutputs.get(i);
            // System.out.println(probability+"-----"+getOuputDomain().getNumberOfValues());
			for (int j = 0; j < input.size(); j++) {
				key = calculateMapKey(j,domains.get(j).calculateDiscreteValue(input.get(j)), i);
				probability *= probabilitiesOfInputs.get(key);
			}
			probabilities.add(probability);
		}
		
		float sumOfProbabilities = (float) 0.0;
		for(int i = 0; i < probabilities.size(); i++) {
			sumOfProbabilities += probabilities.get(i);
		}

		float alpha = (float) 0.0;

		if(sumOfProbabilities > minimumDenominatorValue) {
			alpha = (float) (1.0 / sumOfProbabilities);
		}
		float probability = probabilities.get(getOuputDomain().calculateDiscreteValue(output))*alpha;
		if(probability > 1.0) {
			return  1.0f;
		} else {
			return probability;
		}
	}

	

	/**
	 * Add raw training data from a file to adapt the classifier. 
	 * It updates the variables containing the probabilities.
	 *
	 * Beware : The file must not have an empty line at the end.
	 */	

	@Override
	public void addRawTrainingData(String filename) {
		// TODO Auto-generated method stub
		InputStream input = new InputStream() {
			
			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		try {
			input = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        Scanner inputFile=new Scanner(input);
		while (inputFile.hasNextLine()) {
			Vector<Float> rawTrainingData = new Vector<Float>();
			float value;
			for (int i = 0; i < numberOfColumns; i++) {
				value=inputFile.nextFloat();
				rawTrainingData.add(value);
				
			}
			addRawTrainingData(rawTrainingData);
		}

		inputFile.close();
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Add one set of raw training data to adapt the classifier
	 * It updates the variables containing the probabilities.
	 */
	@Override
	public void addRawTrainingData(Vector<Float> rawTrainingData) {
		// TODO Auto-generated method stub
		Vector<Integer> trainingData = convertRawTrainingData(rawTrainingData);

		updateProbabilities(trainingData);
		updateOutputProbabilities(domains.get(numberOfColumns-1).calculateDiscreteValue(rawTrainingData.get(numberOfColumns - 1)));
		numberOfTrainingData++;
	}
	
	/**
	 * Convert a vector<float> into a vector<int> by discretizing the values
	 * using the domain for each column.
	 */
	@Override
	public Vector<Integer> convertRawTrainingData(Vector<Float> floatVector) {
		// TODO Auto-generated method stub
		Vector<Integer> trainingData = new Vector<Integer>();

		for(int i = 0; i < floatVector.size(); i++) {
			trainingData.add(domains.get(i).calculateDiscreteValue(floatVector.get(i)));
		}

		return trainingData;
	}


    /**
     * Update the output probabilities from a new set of raw training data.
     */
	@Override
	public void updateOutputProbabilities(int output) {
		// TODO Auto-generated method stub
		float denominator = numberOfTrainingData;
		for (int i = 0; i < probabilitiesOfOutputs.size(); i++) {
			float numberOfOutput = probabilitiesOfOutputs.get(i)*denominator;
			if(i == output) {
				numberOfOutput++;
			}
			
			probabilitiesOfOutputs.add(i,(float) (numberOfOutput/(denominator + (float)1.0)));
			if(probabilitiesOfOutputs.size()>24) break;
		}
		
	}

	/**
	 * Update the probabilities after adding one set of training data.
	 */
	@Override
	public void updateProbabilities(Vector<Integer> trainingData) {
		// TODO Auto-generated method stub
		float denominator = probabilitiesOfOutputs.get(trainingData.get(numberOfColumns - 1))*numberOfTrainingData;
		
		for(int i = 0; i < numberOfColumns - 1; i++) {
			for(int j = 0; j < domains.get(i).getNumberOfValues(); j++) {
				float numerator = probabilitiesOfInputs.get(calculateMapKey(i, j, trainingData.get(numberOfColumns - 1)))*denominator;

				if(j == trainingData.get(i)) {
					numerator++;
				}

				probabilitiesOfInputs.put(calculateMapKey(i, j, trainingData.get(numberOfColumns - 1)), (float) (numerator/(denominator + 1.0)));
			}
		}
		
	}


    /**
     * Returns the domain of the output column.
     */
	@Override
	public Domain getOuputDomain() {
		// TODO Auto-generated method stub
		return domains.get(numberOfColumns - 1);
	}

}
