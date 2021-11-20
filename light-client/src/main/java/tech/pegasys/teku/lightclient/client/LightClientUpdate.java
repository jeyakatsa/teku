package tech.pegasys.teku.lightclient.client;

import tech.pegasys.teku.spec.datastructures.blocks.BeaconBlockHeader;

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

//	  nextSyncCommitteeBranch: Vector<Bytes32>;
	  /** Finality proof for the update header */
//	  finalityHeader: phase0.BeaconBlockHeader;
//	  finalityBranch: Vector<Bytes32>;
	  /** Sync committee aggregate signature */
//	  syncCommitteeBits: BitVector;
//	  syncCommitteeSignature: BLSSignature;
	  /** Fork version for the aggregate signature */
//	  forkVersion: Version;

}
