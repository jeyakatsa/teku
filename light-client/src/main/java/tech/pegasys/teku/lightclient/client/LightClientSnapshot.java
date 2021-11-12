package tech.pegasys.teku.lightclient.client;

import tech.pegasys.teku.spec.datastructures.blocks.BeaconBlockHeader;

public class LightClientSnapshot {
	
	// BeaconBlockHeader Getters & Setters
	//If test fails, import BeaconBlockHeader from api schema.
	private BeaconBlockHeader header;
	public BeaconBlockHeader getHeader() {
		return header;
	}
	public void setHeader(BeaconBlockHeader header) {
		this.header = header;
	}	

	  // SyncCommittee Getters & Setters
	  private SyncCommittee currentSyncCommittee;
	  private SyncCommittee nextSyncCommittee;
	  public SyncCommittee getCurrentSyncCommittee() {
	    return currentSyncCommittee;
	  }
	  public void setCurrentSyncCommittee(SyncCommittee currentSyncCommittee) {
	    this.currentSyncCommittee = currentSyncCommittee;
	  }
	  public SyncCommittee getNextSyncCommittee() {
	    return nextSyncCommittee;
	  }
	  public void setNextSyncCommittee(SyncCommittee nextSyncCommittee) {
	    this.nextSyncCommittee = nextSyncCommittee;
	  }

}
