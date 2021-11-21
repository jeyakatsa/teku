package tech.pegasys.teku.lightclient.server;

import java.lang.reflect.Array;

import tech.pegasys.teku.ssz.cache.ArrayIntCache;

public class Utilities {
	
//	private ArrayIntCache<Boolean> bits;
//	public ArrayIntCache<Boolean> getBits() {
//		return bits;
//	}
//	public void setBits(ArrayIntCache<Boolean> bits) {
//		this.bits = bits;
//	}
	
	public int sumBits(ArrayIntCache<Boolean> bits) {
		int sum = 0;
		for (boolean bit: bits) {
			if (bit) {
				sum++;
			}
		}
		return sum;
	}
	

}
