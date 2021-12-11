package tech.pegasys.teku.lightclient.client;

import tech.pegasys.teku.bls.impl.blst.BlstSignature;
import tech.pegasys.teku.spec.SpecVersion;
import tech.pegasys.teku.spec.datastructures.blocks.BeaconBlockHeader;
import tech.pegasys.teku.infrastructure.ssz.SszVector;
import tech.pegasys.teku.infrastructure.ssz.collections.SszBitvector;
import tech.pegasys.teku.infrastructure.ssz.primitive.SszBytes32;

public class LightClientUpdate {

    /** Update beacon block header */
    private BeaconBlockHeader header;

    public BeaconBlockHeader getHeader() {
        return header;
    }

    public void setHeader(BeaconBlockHeader header) {
        this.header = header;
    }

    /** Next sync committee corresponding to the header */
    private SyncCommittee nextSyncCommittee;

    public SyncCommittee getNextSyncCommittee() {
        return nextSyncCommittee;
    }

    public void setNextSyncCommittee(SyncCommittee nextSyncCommittee) {
        this.nextSyncCommittee = nextSyncCommittee;
    }

    private SszVector<SszBytes32> nextSyncCommitteeBranch;

    public SszVector<SszBytes32> getNextSyncCommitteeBranch() {
        return nextSyncCommitteeBranch;
    }

    public void setNextSyncCommitteeBranch(SszVector<SszBytes32> nextSyncCommitteeBranch) {
        this.nextSyncCommitteeBranch = nextSyncCommitteeBranch;
    }

    /** Finality proof for the update header */
    private BeaconBlockHeader finalityHeader;

    public BeaconBlockHeader getFinalityHeader() {
        return finalityHeader;
    }

    public void setFinalityHeader(BeaconBlockHeader finalityHeader) {
        this.finalityHeader = finalityHeader;
    }

    private SszVector<SszBytes32> finalityBranch;

    public SszVector<SszBytes32> getFinalityBranch() {
        return finalityBranch;
    }

    public void setFinalityBranch(SszVector<SszBytes32> finalityBranch) {
        this.finalityBranch = finalityBranch;
    }

    /** Sync committee aggregate signature */
    private SszBitvector syncCommitteeBits;

    public SszBitvector getSyncCommitteeBits() {
        return syncCommitteeBits;
    }

    public void setSyncCommitteeBits(SszBitvector syncCommitteeBits) {
        this.syncCommitteeBits = syncCommitteeBits;
    }

    private BlstSignature syncCommitteeSignature;

    public BlstSignature getSyncCommitteeSignature() {
        return syncCommitteeSignature;
    }

    public void setSyncCommitteeSignature(BlstSignature syncCommitteeSignature) {
        this.syncCommitteeSignature = syncCommitteeSignature;
    }

    /** Fork version for the aggregate signature */
    private SpecVersion forkVersion;

    public SpecVersion getForkVersion() {
        return forkVersion;
    }

    public void setForkVersion(SpecVersion forkVersion) {
        this.forkVersion = forkVersion;
    }
}
