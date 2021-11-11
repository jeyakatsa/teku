/*
 * Copyright 2021 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */



package tech.pegasys.teku.lightclient.client;

import tech.pegasys.teku.spec.datastructures.blocks.BeaconBlockHeader;

public class Types {
	
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
	  
	  
	  //Main Functions for Types
	  public void lightClientSnapshot(
			  BeaconBlockHeader header,
			  SyncCommittee currentSyncCommittee, 
			  SyncCommittee nextSyncCommittee) {
		  //Beacon Block Header
		  this.setHeader(header);		  
	    // Sync committees corresponding to the header
	    this.setCurrentSyncCommittee(currentSyncCommittee);
	    this.setCurrentSyncCommittee(nextSyncCommittee);
	  }

}
