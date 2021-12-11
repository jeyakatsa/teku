package tech.pegasys.teku.lightclient.utilities;

import org.apache.tuweni.bytes.Bytes32;

import java.util.*;

public class Utilities {

    // IMPORTANT: bits List was supposed to be a Generic Array,
    // but Java does not support Generic Arrays cohesively
    List<Boolean> bits = new ArrayList<Boolean>();
    public int sumBits() {
        int sum = 0;
        for (boolean bit : bits) {
            if (bit) {
                sum++;
            }
        }
        return sum;
    }

    //byte function below replaced in place of
    //Bytes32 class that could not be implemented
    //within for loop below.
    byte[] root = new byte[32];
    public boolean isZeroHash() {
        for (int i = 0; i < root.length; i++) {
            if (root[i] != 0) {
                return false;
            }
        }
        return true;
    }

    List<Bytes32> rootArray = new ArrayList<Bytes32>();
    public void assertZeroHashes(int expectedLength, String errorMessage) {
        if (rootArray.length != expectedLength) {
            throw new IllegalArgumentException(`Wrong Length`);
        }

        for (const root of rootArray) {
            if (!isZeroHash(root)) {
                throw Error(`Not zeroed ${errorMessage}`);
            }
        }
   }

}
