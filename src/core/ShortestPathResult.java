package core;

/**
 * Resultado de Dijkstra (Mision 2): el costo minimo para llegar
 * del inicio al destino, y el camino de nodos que lo logra.
 *
 * Es el equivalente de PathResult pero para grafos: usa long
 * (los pesos se acumulan hasta 1.000.000 x 100.000 conexiones y
 * un int se desbordaria) y el camino es una lista de NODOS, no
 * de celdas de una grilla.
 */
public class ShortestPathResult {
    private final long cost;
    private final int[] path;

    public ShortestPathResult(long cost, int[] path) {
        this.cost = cost;
        this.path = path;
    }

    public static ShortestPathResult noRoute() {
        return new ShortestPathResult(Sentinels.NO_ROUTE, null);
    }

    public long getCost() {
        return cost;
    }

    public int[] getPath() {
        return path;
    }

    public boolean isNoRoute() {
        return Sentinels.isNoRoute(cost);
    }
}
