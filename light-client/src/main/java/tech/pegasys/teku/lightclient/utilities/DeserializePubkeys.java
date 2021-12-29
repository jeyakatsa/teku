package tech.pegasys.teku.lightclient.utilities;

import org.apache.tuweni.bytes.Bytes48;
import tech.pegasys.teku.bls.impl.PublicKey;
import tech.pegasys.teku.lightclient.client.LightClientUpdate;

public interface DeserializePubkeys extends PublicKey {

    //nextSyncCommittee function to be added

    //Functions below extend from PublicKey interface.
    LightClientUpdate pubkeys = new LightClientUpdate() {
        public Bytes48 toBytesCompressed() {
            return null;
        }
        public void forceValidation() throws IllegalArgumentException {

        }
        public boolean isInGroup() {
            return false;
        }
        public boolean isValid() {
            return false;
        }
        public int hashCode() {
            return 0;
        }
        public boolean equals(Object obj) {
            return false;
        }
    };


}
