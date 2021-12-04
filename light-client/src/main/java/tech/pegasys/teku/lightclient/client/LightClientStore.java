package tech.pegasys.teku.lightclient.client;

package tech.pegasys.teku.lightclient.client;

public class LightClientStore {

    private LightClientSnapshot snapshot;

    public LightClientSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(LightClientSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    private LightClientUpdate validUpdates;

    public LightClientUpdate getValidUpdates() {
        return validUpdates;
    }

    public void setValidUpdates(LightClientUpdate validUpdates) {
        this.validUpdates = validUpdates;
    }
}
