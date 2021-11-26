package tech.pegasys.teku.lightclient.utilities;
import java.util.*;


public class Utilities {
	
	//IMPORTANT: List below was supposed to be a Generic Array, 
	//but Java does not support Generic Arrays cohesively
	List<Boolean> bits = new ArrayList<Boolean>();
	
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