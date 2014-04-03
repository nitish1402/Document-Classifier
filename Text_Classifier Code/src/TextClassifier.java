import java.util.Vector;

/**
 * 
 */

/**
 * @author nitish,Piyush Raman,Amritansh ,Piyush Agarwal
 */
public class TextClassifier {

	/**
	 * @param args
	 */
	

    public void testBayesianClassifier() {
	   int errors = 0;
	
	   Vector<Domain> domains = new Vector<Domain>();
	
	   // The input domains
	   domains.add(new Domain(0.0f, 2.0f, 3));
	   domains.add(new Domain(0.0f, 2.0f, 3));
	   
	   // The ouput domain
	   domains.add(new Domain(0.0f, 1.0f, 2));
	   BaysianClassifier classifier=new BaysianClassifier("/home/nitish/Documents/projects/Group-10 ( Complete Submission )/Text_Classifier Code/src/data/bayesianClassifierValues.txt", domains);
	   // It is possible to not give any data and add it later
	   // BayesianClassifier classifier(domains);
	
	   Vector<Float> input1 = new Vector<Float>();
	
	   input1.add(1.0f);
	   input1.add(2.0f);
	
	   // Calculates the probability of obtaining the output 0.0 given the input (1.0, 2.0)
	   if(classifier.calculateProbabilityOfOutput(input1, 0.0f) != 0.5) {
		  System.out.println("The probability should be 0.5");
		  errors++;
	   }
	   
	   // Updates the classifier with new values (1.0, 2.0, 0.0)
	   classifier.addRawTrainingData("/home/nitish/Documents/projects/Group-10 ( Complete Submission )/Text_Classifier Code/src/data/newBayesianClassifierValues.txt");
	   
	
	   if(classifier.calculateProbabilityOfOutput(input1, 0.0f) <= 0.727272 || classifier.calculateProbabilityOfOutput(input1, 0.0f) >= 0.727274) {
		 System.out.println("The probability should be 0.727273");
		errors++;
	   }
	   
	   // The most probable output should be 0.0 given the input (1.0, 2.0)
	   if (classifier.calculateOutput(input1) != 0.0) {
		 System.out.println("The output should be 0.0");
		errors++;
	   }
	
	   Vector<Float> newRawTrainingData = new Vector<Float>();
	
	   newRawTrainingData.add((float)1.0);
	   newRawTrainingData.add((float)2.0);
	   newRawTrainingData.add((float)1.0);
	   classifier.addRawTrainingData(newRawTrainingData);

	   if(classifier.calculateProbabilityOfOutput(input1, 0.0f) <= 0.45f || classifier.calculateProbabilityOfOutput((Vector<Float>)input1, 0.0f) >= 0.51) {
		 System.out.println("The probability should be 0.5");
		errors++;
	   }
	
	   if (errors == 0) {
		 System.out.println("BayesianClassifier Test passed");
	}
	   else System.exit(0);
}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TextClassifier c=new TextClassifier();
		c.testBayesianClassifier();

	}

}
