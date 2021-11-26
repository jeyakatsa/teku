package tech.pegasys.teku.lightclient.utilities;


public class Utilities {
	
	ArrayLike<Boolean>bits = new ArrayLike(number);
	
	public int sumBits() {
		int sum = 0;
		for (boolean bit: bits) {
			if (bit) {
				sum++;
			}
		}
		return sum;
	}	
	

	

}