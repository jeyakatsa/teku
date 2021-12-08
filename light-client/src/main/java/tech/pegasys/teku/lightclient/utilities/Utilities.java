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

    private Bytes32[] root;
    private Bytes32[] getRoot() {
        return root;
    }
    private void setRoot(Bytes32[] root) {
        this.root = root;
    }


    public boolean isZeroHash() {
        for (int i = 0; i < getRoot().length; i++) {
            if (getRoot(i) != 0) {
                return false;
            }
        }
        return true;
    }
}
