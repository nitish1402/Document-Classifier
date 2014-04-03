/**
 * 
 */

/**
 * @author nitish
 *
 */
public interface DomainInterface
{
		 
     public void setMax(float _max);
 	 public void setMin(float _min);
	 public void setNumberOfValues(int _numberOfValues);

	 public float getMax();
	 public float getMin();
	 public int getNumberOfValues();

	/**
	 * Calculate the discrete value from a float using the max, min and number of values
	 * The minimum discrete value is 0 and maximum is numberOfValues - 1
	 */
	 public int calculateDiscreteValue(float value);
	 
 }
	


