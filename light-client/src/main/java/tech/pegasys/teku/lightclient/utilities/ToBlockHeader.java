package tech.pegasys.teku.lightclient.utilities;

import tech.pegasys.teku.spec.datastructures.blocks.BeaconBlockHeader;
import tech.pegasys.teku.spec.datastructures.blocks.NodeSlot;
import tech.pegasys.teku.spec.datastructures.blocks.SlotAndBlockRoot;
import tech.pegasys.teku.spec.datastructures.blocks.blockbody.versions.altair.BeaconBlockBodyAltair;

public class ToBlockHeader extends BeaconBlockHeader {

    public ToBlockHeader(NodeSlot block, SlotAndBlockRoot slotandBlockRoot, BeaconBlockBodyAltair bodyRoot) {
        this.block = block;
        this.slotandBlockRoot = slotandBlockRoot;
        this.bodyRoot = bodyRoot;
    }

    public final NodeSlot block;
    public NodeSlot block () {
        return block;
    }
    public final SlotAndBlockRoot slotandBlockRoot;
    public SlotAndBlockRoot slotandBlockRoot () {
        return slotandBlockRoot;
    }
    public final BeaconBlockBodyAltair bodyRoot;
    public BeaconBlockBodyAltair bodyRoot () {
        return bodyRoot;
    }


}
