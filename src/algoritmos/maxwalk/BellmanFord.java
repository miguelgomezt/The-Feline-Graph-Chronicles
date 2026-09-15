package algoritmos.maxwalk;

import core.Edge;
import core.Graph;
import core.Sentinels;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;


public class BellmanFord {

    private BellmanFord() {
    }


    public static class Result {
        private final long[] distance;
        private final boolean[] unbounded;

        public Result(long[] distance, boolean[] unbounded) {
            this.distance = distance;
            this.unbounded = unbounded;
        }

        public long distanceTo(int node) {
            return distance[node];
        }

        public boolean isUnbounded(int node) {
            return unbounded[node];
        }


        public boolean isUnreachable(int node) {
            return distance[node] == Sentinels.NO_ROUTE && !unbounded[node];
        }
    }

    public static Result solve(Graph graph, int source) {
        int n = graph.getNodeCount();
        List<Edge> edges = graph.getAllEdges();

        long[] dist = new long[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Sentinels.NO_ROUTE;
        }
        dist[source] = 0L;


        for (int round = 0; round < n - 1; round++) {
            boolean changed = false;
            for (Edge edge : edges) {
                if (relax(dist, edge)) {
                    changed = true;
                }
            }
            if (!changed) {
                break;
            }
        }


        boolean[] unbounded = new boolean[n];
        Deque<Integer> pending = new ArrayDeque<>();
        for (Edge edge : edges) {
            int from = edge.getFrom();
            int to = edge.getTo();
            if (dist[from] == Sentinels.NO_ROUTE) {
                continue; // no hacemos aritmetica sobre el centinela
            }
            long candidate = dist[from] + edge.getWeight();
            if (candidate > dist[to] && !unbounded[to]) {
                unbounded[to] = true;
                pending.push(to);
            }
        }

        // Propagamos la marca: todo lo alcanzable desde un nodo no
        // acotado tambien es no acotado, porque primero se puede dar
        // tantas vueltas como se quiera en el ciclo positivo y despues
        // seguir camino desde ahi.
        while (!pending.isEmpty()) {
            int current = pending.pop();
            for (Edge edge : graph.getNeighbors(current)) {
                int next = edge.getTo();
                if (!unbounded[next]) {
                    unbounded[next] = true;
                    pending.push(next);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (unbounded[i]) {
                dist[i] = Sentinels.UNBOUNDED;
            }
        }

        return new Result(dist, unbounded);
    }

    private static boolean relax(long[] dist, Edge edge) {
        int from = edge.getFrom();
        int to = edge.getTo();
        if (dist[from] == Sentinels.NO_ROUTE) {
            return false;
        }
        long candidate = dist[from] + edge.getWeight();
        if (candidate > dist[to]) {
            dist[to] = candidate;
            return true;
        }
        return false;
    }
}