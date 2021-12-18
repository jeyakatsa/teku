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

package tech.pegasys.teku.reference.phase0.ssz_generic.containers;

import tech.pegasys.teku.infrastructure.ssz.SszList;
import tech.pegasys.teku.infrastructure.ssz.SszVector;
import tech.pegasys.teku.infrastructure.ssz.collections.SszByteList;
import tech.pegasys.teku.infrastructure.ssz.containers.Container7;
import tech.pegasys.teku.infrastructure.ssz.containers.ContainerSchema7;
import tech.pegasys.teku.infrastructure.ssz.primitive.SszByte;
import tech.pegasys.teku.infrastructure.ssz.tree.TreeNode;

public class ComplexTestStruct
    extends Container7<
        ComplexTestStruct,
        SszUInt16,
        SszList<SszUInt16>,
        SszByte,
        SszByteList,
        VarTestStruct,
        SszVector<FixedTestStruct>,
        SszVector<VarTestStruct>> {

  protected ComplexTestStruct(
      final ContainerSchema7<
              ComplexTestStruct,
              SszUInt16,
              SszList<SszUInt16>,
              SszByte,
              SszByteList,
              VarTestStruct,
              SszVector<FixedTestStruct>,
              SszVector<VarTestStruct>>
          schema,
      final TreeNode backingNode) {
    super(schema, backingNode);
  }
}
