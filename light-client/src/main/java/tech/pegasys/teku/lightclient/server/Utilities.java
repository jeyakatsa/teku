package tech.pegasys.teku.lightclient.server;



public class Utilities {
	
//	private ArrayIntCache<Boolean> bits;
//	public ArrayIntCache<Boolean> getBits() {
//		return bits;
//	}
//	public void setBits(ArrayIntCache<Boolean> bits) {
//		this.bits = bits;
//	}
	
	
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
