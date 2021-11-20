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

import tech.pegasys.teku.bls.impl.PublicKey;
import tech.pegasys.teku.spec.datastructures.type.SszPublicKey;
import tech.pegasys.teku.ssz.SszVector;

public class SyncCommittee {

	//PubKey functions below are immutable 
	//the below functions are used by the lightClientSnapshot class
	
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
