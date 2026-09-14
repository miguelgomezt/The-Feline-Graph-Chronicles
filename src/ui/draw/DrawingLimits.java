package ui.draw;

public final class DrawingLimits {

    private DrawingLimits() {
    }

    public static final int MAX_NODES_SHORTEST_PATH = 60; // Misiones 2 y 3
    public static final int MAX_NODES_NETWORK = 100;       // Mision 4
    public static final int MAX_EDGES_NETWORK = 300;       // Mision 4

    public static boolean exceedsShortestPathLimit(int nodeCount) {
        return nodeCount > MAX_NODES_SHORTEST_PATH;
    }

    public static boolean exceedsNetworkLimit(int nodeCount, int edgeCount) {
        return nodeCount > MAX_NODES_NETWORK || edgeCount > MAX_EDGES_NETWORK;
    }

    public static String shortestPathOmittedMessage(int nodeCount) {
        return "Dibujo omitido: el grafo tiene " + nodeCount + " nodos, mas de "
                + MAX_NODES_SHORTEST_PATH + ".";
    }

    public static String networkOmittedMessage(int nodeCount, int edgeCount) {
        return "Dibujo omitido: la red tiene " + nodeCount + " intersecciones y "
                + edgeCount + " cables (el limite es " + MAX_NODES_NETWORK
                + " intersecciones y " + MAX_EDGES_NETWORK + " cables).";
    }
}
