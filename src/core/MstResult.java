package core;

import java.util.List;

public class MstResult {
    private final long totalCost;
    private final List<Edge> edgesUsed;
    private final boolean connected;

    public MstResult(long totalCost, List<Edge> edgesUsed, boolean connected) {
        this.totalCost = totalCost;
        this.edgesUsed = edgesUsed;
        this.connected = connected;
    }

    public static MstResult disconnected() {
        return new MstResult(Sentinels.NO_ROUTE, null, false);
    }

    public long getTotalCost() { return totalCost; }
    public List<Edge> getEdgesUsed() { return edgesUsed; }
    public boolean isConnected() { return connected; }
}
