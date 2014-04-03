/**
 * 
 */

/**
 * @author nitish
 *
 */
public class Domain implements DomainInterface{

    float max;
    float min;
    int numberOfValues;
	/**
	 * 
	 */
	public Domain() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * Constructor with max, min value and number of values
	 */
	 
	public  Domain(float _min, float _max, int _numberOfValues) {
		// TODO Auto-generated method stub
		max = _max;
		min = _min;
		numberOfValues = _numberOfValues;
		
	}

	@Override
	public void setMax(float _max) {
		// TODO Auto-generated method stub
		max = _max;
	}

	@Override
	public void setMin(float _min) {
		// TODO Auto-generated method stub
		min = _min;
	}

	@Override
	public void setNumberOfValues(int _numberOfValues) {
		// TODO Auto-generated method stub
		numberOfValues = _numberOfValues;
	}

	@Override
	public float getMax() {
		// TODO Auto-generated method stub
		return max;
	}

	@Override
	public float getMin() {
		// TODO Auto-generated method stub
		return min;
	}

	@Override
	public int getNumberOfValues() {
		// TODO Auto-generated method stub
		return numberOfValues;
	}

	@Override
	public int calculateDiscreteValue(float value) {
		// TODO Auto-generated method stub
		float discreteValue;
		
		discreteValue = (value - min) / (max - min);
		discreteValue *= numberOfValues;
		
		if (discreteValue > max) {
			return numberOfValues - 1;
		} else if (discreteValue < min) {
			return 0;
		}

		return (int) discreteValue;
	}

}
