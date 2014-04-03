/**
 * 
 */

import java.util.Vector;
import java.util.Map;

/**
 * @author nitish
 *
 */
public interface BaysianClassifierInterface {
	

	/*----------------------------------------------------------------
	-----------------------PUBLIC METHODS-----------------------------
	----------------------------------------------------------------*/
	/**
	 * Construct the classifier from the RawTrainingData in the file.
	 *
	 * Beware : The file must not have an empty line at the end.
	 */
	public void constructClassifier(String filename);

	/**
	 * Calculate the probabilities for each possibility of inputs.
	 */
	public void calculateProbabilitiesOfInputs();

	/**
	 * Calculate the probability of P(effectColum:effectValue | lastColumn:causeValue)
	 * It saves data into the variable probabilitiesOfInputs.
	 */
	public void calculateProbability(int effectColumn, int effectValue,
					int causeValue);

	/**
	 * Calculate P(Output) of each output.
	 * It saves data into the variable probabilitiesOfOuputs.
	 */
	public void calculateProbabilitiesOfOutputs();

	/**
	 * Calculate the map key for each value in the variable probabilitiesOfInputs
	 */
	public long calculateMapKey(int effectColumn, int effectValue, int causeValue);

	/**
	 * Update the output probabilities from a new set of raw training data.
	 */
	public void updateOutputProbabilities(int output);

	/**
	 * Update the probabilities after adding one set of training data.
	 */
	public void updateProbabilities(Vector<Integer> trainingData);

	/**
	 * Convert a vector<float> into a vector<int> by discretizing the values
	 * using the domain for each column.
	 */
	public Vector<Integer> convertRawTrainingData(Vector<Float> floatVector);
	
	/**
	 * Returns the domain of the output column.
	 */
	public Domain getOuputDomain();

	/**
	 * Calculate the most probable output given this input with this formula :
	 * P(Output | Input) = 1/Z * P(Output) * P(InputValue1 | Ouput) * P(InputValue2 | Ouput) * ...
	 * The output with the highest probability is returned.
	 */
	int calculateOutput(Vector<Float> input);

	/**
	 * Calculate the probability of this output given this input.
 	 * P(Output | Input) = 1/Z * P(Output) * P(InputValue1 | Ouput) * P(InputValue2 | Ouput) * ...
	 */
	float calculateProbabilityOfOutput(Vector<Float> input, float output);

	/**
	 * Add raw training data from a file to adapt the classifier. 
	 * It updates the variables containing the probabilities.
	 *
	 * Beware : The file must not have an empty line at the end.
	 */
	void addRawTrainingData(String filename);

	/**
	 * Add one set of raw training data to adapt the classifier
	 * It updates the variables containing the probabilities.
	 */
	void addRawTrainingData(Vector<Float> rawTrainingData);
}


