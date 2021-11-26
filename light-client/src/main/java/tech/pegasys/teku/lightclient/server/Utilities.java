package tech.pegasys.teku.lightclient.server;



public class Utilities {
	
	public int sumBits(ArrayLike<Boolean> bits) {
		int sum = 0;
		for (boolean bit: bits) {
			if (bit) {
				sum++;
			}
		}
		return sum;
	}
	

}
