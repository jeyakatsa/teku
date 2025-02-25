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

package tech.pegasys.teku.spec;

import java.util.function.Consumer;
import tech.pegasys.teku.infrastructure.unsigned.UInt64;
import tech.pegasys.teku.spec.config.SpecConfig;
import tech.pegasys.teku.spec.config.SpecConfigAltair;
import tech.pegasys.teku.spec.config.SpecConfigBellatrix;
import tech.pegasys.teku.spec.config.SpecConfigBuilder;
import tech.pegasys.teku.spec.config.SpecConfigLoader;
import tech.pegasys.teku.spec.networks.Eth2Network;

public class TestSpecFactory {

  public static Spec createDefault() {
    return createDefault(__ -> {});
  }

  public static Spec createDefault(final Consumer<SpecConfigBuilder> modifier) {
    final SpecConfig config =
        SpecConfigLoader.loadConfig(Eth2Network.MINIMAL.configName(), modifier);
    return create(config, SpecMilestone.PHASE0);
  }

  public static Spec createMinimal(final SpecMilestone specMilestone) {
    switch (specMilestone) {
      case PHASE0:
        return createMinimalPhase0();
      case ALTAIR:
        return createMinimalAltair();
      case BELLATRIX:
        return createMinimalBellatrix();
      default:
        throw new IllegalStateException("unsupported milestone");
    }
  }

  public static Spec createMainnet(final SpecMilestone specMilestone) {
    switch (specMilestone) {
      case PHASE0:
        return createMainnetPhase0();
      case ALTAIR:
        return createMainnetAltair();
      case BELLATRIX:
        return createMainnetBellatrix();
      default:
        throw new IllegalStateException("unsupported milestone");
    }
  }

  public static Spec createMinimalBellatrix() {
    final SpecConfigBellatrix specConfig = getBellatrixSpecConfig(Eth2Network.MINIMAL);
    return create(specConfig, SpecMilestone.BELLATRIX);
  }

  public static Spec createMinimalAltair() {
    final SpecConfigAltair specConfig = getAltairSpecConfig(Eth2Network.MINIMAL);
    return create(specConfig, SpecMilestone.ALTAIR);
  }

  /**
   * Create a spec that forks to altair at the provided slot
   *
   * @param altairForkEpoch The altair fork epoch
   * @return A spec with phase0 and altair enabled, forking to altair at the given epoch
   */
  public static Spec createMinimalWithAltairForkEpoch(final UInt64 altairForkEpoch) {
    final SpecConfigAltair config = getAltairSpecConfig(Eth2Network.MINIMAL, altairForkEpoch);
    return create(config, SpecMilestone.ALTAIR);
  }

  /**
   * Create a spec that forks to bellatrix at the provided slot (altair genesis)
   *
   * @param bellatrixForkEpoch The bellatrix fork epoch
   * @return A spec with altair and bellatrix enabled, forking to bellatrix at the given epoch
   */
  public static Spec createMinimalWithBellatrixForkEpoch(final UInt64 bellatrixForkEpoch) {
    final SpecConfigBellatrix config =
        getBellatrixSpecConfig(Eth2Network.MINIMAL, UInt64.ZERO, bellatrixForkEpoch);
    return create(config, SpecMilestone.BELLATRIX);
  }

  public static Spec createMinimalPhase0() {
    final SpecConfig specConfig = SpecConfigLoader.loadConfig(Eth2Network.MINIMAL.configName());
    return create(specConfig, SpecMilestone.PHASE0);
  }

  public static Spec createMainnetBellatrix() {
    final SpecConfigBellatrix specConfig = getBellatrixSpecConfig(Eth2Network.MAINNET);
    return create(specConfig, SpecMilestone.BELLATRIX);
  }

  public static Spec createMainnetAltair() {
    final SpecConfigAltair specConfig = getAltairSpecConfig(Eth2Network.MAINNET);
    return create(specConfig, SpecMilestone.ALTAIR);
  }

  public static Spec createMainnetPhase0() {
    final SpecConfig specConfig = SpecConfigLoader.loadConfig(Eth2Network.MAINNET.configName());
    return create(specConfig, SpecMilestone.PHASE0);
  }

  public static Spec createPhase0(final String configName) {
    final SpecConfig specConfig = SpecConfigLoader.loadConfig(configName);
    return createPhase0(specConfig);
  }

  public static Spec createPhase0(final SpecConfig config) {
    return create(config, SpecMilestone.PHASE0);
  }

  public static Spec createAltair(final SpecConfig config) {
    return create(config, SpecMilestone.ALTAIR);
  }

  public static Spec createBellatrix(final SpecConfig config) {
    return create(config, SpecMilestone.BELLATRIX);
  }

  public static Spec create(final SpecMilestone specMilestone, final Eth2Network network) {
    switch (specMilestone) {
      case PHASE0:
        return create(SpecConfigLoader.loadConfig(network.configName()), specMilestone);
      case ALTAIR:
        return create(getAltairSpecConfig(network), specMilestone);
      case BELLATRIX:
        return create(getBellatrixSpecConfig(network), specMilestone);
      default:
        throw new IllegalStateException("unsupported milestone");
    }
  }

  private static Spec create(
      final SpecConfig config, final SpecMilestone highestSupportedMilestone) {
    return Spec.create(config, highestSupportedMilestone);
  }

  private static SpecConfigAltair getAltairSpecConfig(final Eth2Network network) {
    return getAltairSpecConfig(network, UInt64.ZERO);
  }

  private static SpecConfigAltair getAltairSpecConfig(
      final Eth2Network network, final UInt64 altairForkEpoch) {
    return SpecConfigAltair.required(
        SpecConfigLoader.loadConfig(
            network.configName(), c -> c.altairBuilder(a -> a.altairForkEpoch(altairForkEpoch))));
  }

  private static SpecConfigBellatrix getBellatrixSpecConfig(final Eth2Network network) {
    return getBellatrixSpecConfig(network, UInt64.ZERO, UInt64.ZERO);
  }

  private static SpecConfigBellatrix getBellatrixSpecConfig(
      final Eth2Network network, final UInt64 altairForkEpoch, UInt64 bellatrixForkEpoch) {
    return SpecConfigBellatrix.required(
        SpecConfigLoader.loadConfig(
            network.configName(),
            c ->
                c.altairBuilder(a -> a.altairForkEpoch(altairForkEpoch))
                    .bellatrixBuilder(m -> m.bellatrixForkEpoch(bellatrixForkEpoch))));
  }
}
