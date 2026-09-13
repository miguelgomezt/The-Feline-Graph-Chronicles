package core;
/**
 * En esta clase esta lo que devuelve BFS y DFS:
 * Es decir cuantos movimientos tiene el camino
 * encontrado y cuale celdas lo forman.
 */

public class PathResult {
    private final int move;
    private final int[] path;

    public PathResult(int move, int[] path) {
        this.move = move;
        this.path = path;
    }

    public static PathResult noRoute(){
    return new PathResult(Sentinels.NO_ROUTE_INT, null);
    }

    public int getMove() {
        return move;
    }

    public int[] getPath() {
        return path;
    }

    public boolean isNoRoute() {
        return Sentinels.isNoRoute(move);
    }
}


