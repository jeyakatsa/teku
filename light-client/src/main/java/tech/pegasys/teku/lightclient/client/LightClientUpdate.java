package tech.pegasys.teku.lightclient.client;

import tech.pegasys.teku.spec.datastructures.blocks.BeaconBlockHeader;
import tech.pegasys.teku.ssz.SszVector;
import tech.pegasys.teku.ssz.primitive.SszBytes32;

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
//	  syncCommitteeBits: BitVector;
//	  syncCommitteeSignature: BLSSignature;
		
		
	  /** Fork version for the aggregate signature */
//	  forkVersion: Version;

}
