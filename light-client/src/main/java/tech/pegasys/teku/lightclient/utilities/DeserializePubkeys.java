package tech.pegasys.teku.lightclient.utilities;

import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes48;
import tech.pegasys.teku.bls.impl.PublicKey;
import tech.pegasys.teku.bls.impl.Signature;
import tech.pegasys.teku.infrastructure.unsigned.UInt64;

import java.util.Arrays;

public class DeserializePubkeys {

    public DeserializePubkeys() {

        UInt64 pk;
        Object[] mapped = Arrays.stream(new PublicKey[]{pubkeys})
                .map(
                        function(pk)
                            PublicKey.fromBytes(pk.valueOf() as Uint8Array)

                        );
//        return Array.from(pubkeys).map((pk) => PublicKey.fromBytes(pk.valueOf() as Uint8Array));
    }

    private Object function(UInt64 pk) {
    }

    //Functions below extends from PublicKey interface.
    PublicKey pubkeys = new PublicKey() {
        @Override
        public Bytes48 toBytesCompressed() {
            return null;
        }

        @Override
        public boolean verifySignature(Signature signature, Bytes message) {
            return PublicKey.super.verifySignature(signature, message);
        }

        @Override
        public boolean verifySignature(Signature signature, Bytes message, String dst) {
            return PublicKey.super.verifySignature(signature, message, dst);
        }

        @Override
        public void forceValidation() throws IllegalArgumentException {

        }

        @Override
        public boolean isInGroup() {
            return false;
        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    };
}
