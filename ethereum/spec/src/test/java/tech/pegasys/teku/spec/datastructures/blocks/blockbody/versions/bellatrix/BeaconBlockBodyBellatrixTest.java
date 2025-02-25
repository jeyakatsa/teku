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

package tech.pegasys.teku.spec.datastructures.blocks.blockbody.versions.bellatrix;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.pegasys.teku.spec.SpecMilestone;
import tech.pegasys.teku.spec.datastructures.blocks.blockbody.BeaconBlockBodyBuilder;
import tech.pegasys.teku.spec.datastructures.blocks.blockbody.BeaconBlockBodySchema;
import tech.pegasys.teku.spec.datastructures.blocks.blockbody.common.AbstractBeaconBlockBodyTest;
import tech.pegasys.teku.spec.datastructures.blocks.blockbody.versions.altair.SyncAggregate;
import tech.pegasys.teku.spec.datastructures.execution.ExecutionPayload;

class BeaconBlockBodyBellatrixTest extends AbstractBeaconBlockBodyTest<BeaconBlockBodyBellatrix> {

  protected SyncAggregate syncAggregate;
  protected ExecutionPayload executionPayload;

  @BeforeEach
  void setup() {
    super.setUpBaseClass(
        SpecMilestone.BELLATRIX,
        () -> {
          syncAggregate = dataStructureUtil.randomSyncAggregate();
          executionPayload = dataStructureUtil.randomExecutionPayload();
        });
  }

  @Test
  void equalsReturnsFalseWhenExecutionPayloadIsDifferent() {
    executionPayload = dataStructureUtil.randomExecutionPayload();
    BeaconBlockBodyBellatrix testBeaconBlockBody = createBlockBody();

    assertNotEquals(defaultBlockBody, testBeaconBlockBody);
  }

  @Override
  protected BeaconBlockBodyBellatrix createBlockBody(
      final Consumer<BeaconBlockBodyBuilder> contentProvider) {
    return (BeaconBlockBodyBellatrix) getBlockBodySchema().createBlockBody(contentProvider);
  }

  @Override
  protected BeaconBlockBodySchema<? extends BeaconBlockBodyBellatrix> getBlockBodySchema() {
    return (BeaconBlockBodySchemaBellatrix<?>)
        spec.getGenesisSchemaDefinitions().getBeaconBlockBodySchema();
  }

  @Override
  protected Consumer<BeaconBlockBodyBuilder> createContentProvider() {
    return super.createContentProvider()
        .andThen(
            builder ->
                builder
                    .syncAggregate(() -> syncAggregate)
                    .executionPayload(() -> executionPayload));
  }
}
