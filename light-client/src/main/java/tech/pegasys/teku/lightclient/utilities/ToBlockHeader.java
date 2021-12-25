package tech.pegasys.teku.lightclient.utilities;

import tech.pegasys.teku.spec.datastructures.blocks.BeaconBlockHeader;
import tech.pegasys.teku.spec.datastructures.blocks.NodeSlot;
import tech.pegasys.teku.spec.datastructures.blocks.SlotAndBlockRoot;

public class ToBlockHeader extends BeaconBlockHeader {
    private NodeSlot block;
    public NodeSlot toBlockHeader () {
        //"ProposerIndex" might not be necessary here, but to be added if need be.
        SlotAndBlockRoot slotAndBlockRoot;
        return block;
    }

}
