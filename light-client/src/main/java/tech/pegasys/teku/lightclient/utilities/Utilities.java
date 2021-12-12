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
    public boolean isZeroHash(byte root) {
        for (int i = 0; i < this.root.length; i++) {
            if (this.root[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public void assertZeroHashes(Bytes32 rootsArray, int expectedLength) {
        byte[] rootArray = rootsArray.toArray();
        if (rootArray.length != expectedLength) {
            throw new Error("Wrong Length");
        }

        for (byte root : rootArray) {
            if (!isZeroHash(root)) {
                throw new Error("Not zeroed");
            }
        }
   }

}
