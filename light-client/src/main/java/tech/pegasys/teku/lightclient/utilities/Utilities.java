package tech.pegasys.teku.lightclient.utilities;

import java.lang.reflect.Array;

import tech.pegasys.teku.ssz.cache.ArrayIntCache;

public class Utilities {
	
	private ArrayIntCache<Array>[] bits;
	public ArrayIntCache<Array>[] getBits() {
		return bits;
	}
	public void setBits(ArrayIntCache<Array>[] bits) {
		this.bits = bits;
	}
	
	public int sumBits() {
		int sum = 0;
		for (int bit: bits) {
			if (bit) {
				sum++;
			}
		}
		return sum;
	}
	

}