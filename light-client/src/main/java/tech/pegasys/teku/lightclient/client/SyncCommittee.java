package tech.pegasys.teku.lightclient.client;

import tech.pegasys.teku.bls.impl.PublicKey;
import tech.pegasys.teku.infrastructure.ssz.SszVector;
import tech.pegasys.teku.spec.datastructures.type.SszPublicKey;

public class SyncCommittee {

    // PubKey functions below are immutable
    // the below functions are used by the lightClientSnapshot class

    private PublicKey pubKey;

    public PublicKey getPubkey() {
        return pubKey;
    }

    public void setPubkey(PublicKey pubKey) {
        this.pubKey = pubKey;
    }

    private SszVector<SszPublicKey> pubKeys;

    public SszVector<SszPublicKey> getPubKeys() {
        return pubKeys;
    }

    public void setPubKeys(SszVector<SszPublicKey> pubKeys) {
        this.pubKeys = pubKeys;
    }
}
