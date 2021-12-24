package tech.pegasys.teku.lightclient.utilities;

import org.apache.tuweni.bytes.Bytes32;

import java.util.*;

public class Utilities<T> {

    // IMPORTANT: bits List was supposed to be a Generic Array,
    // but Java does not support Generic Arrays cohesively
    public int sumBits() {
        List<Boolean> bits = new ArrayList<Boolean>();
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
        for (byte b : this.root) {
            if (b != 0) {
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

    /**
     * Function below is to guarantee that all bits have a corresponding pubkey
     * @return
     */
    public Stack<T> getParticipantPubkeys() {
//        bits could not convert to array succinctly as a sole Vector
//        had to be converted to an object array.
        Vector<Boolean> vector = new Vector<Boolean>();
        Boolean[] bits = (Boolean[]) vector.toArray();
        List<T> pubkeys = new ArrayList<T>();
        Stack<T> participantPubkeys = new Stack<T>();
        for (int i = 0; i < bits.length; i++){
            if (bits[i]){
                if (pubkeys.get(i) == null) {
                    throw new Error("No such pubkey in syncCommittee");
                }
                participantPubkeys.push(pubkeys.get(i));
            }
        }
        return participantPubkeys;
    }
}
