package algoritmos.maxwalk;

import core.Edge;
import core.Graph;
import core.Sentinels;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;


public class BellmanFord {

    private BellmanFord() {
    }


    public static class Result {
        private final long[] distance;
        private final boolean[] unbounded;
        private final int[] predecessor; // para reconstruir rutas y ciclos
        private final int[] viaSeed;     // por que nodo se propago el "no acotado"
        private final int source;
        private final int nodeCount;


        public Result(long[] distance, boolean[] unbounded) {
            this.distance = distance;
            this.unbounded = unbounded;
            this.predecessor = new int[distance.length];
            Arrays.fill(this.predecessor, -1);
            this.viaSeed = new int[distance.length];
            Arrays.fill(this.viaSeed, -1);
            this.source = -1;
            this.nodeCount = distance.length;
        }

        Result(long[] distance, boolean[] unbounded, int[] predecessor, int[] viaSeed,
               int source, int nodeCount) {
            this.distance = distance;
            this.unbounded = unbounded;
            this.predecessor = predecessor;
            this.viaSeed = viaSeed;
            this.source = source;
            this.nodeCount = nodeCount;
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


        public int[] routeTo(int node) {
            if (isUnreachable(node) || isUnbounded(node)) {
                return null;
            }
            List<Integer> route = new ArrayList<>();
            int current = node;
            while (current != source) {
                route.add(current);
                current = predecessor[current];
            }
            route.add(source);
            Collections.reverse(route);
            return toArray(route);
        }

        public int[] responsibleCycle(int node) {
            if (!isUnbounded(node)) {
                return null;
            }

            int seed = node;
            while (viaSeed[seed] != -1) {
                seed = viaSeed[seed];
            }

            int onCycle = seed;
            for (int i = 0; i < nodeCount; i++) {
                onCycle = predecessor[onCycle];
            }

            List<Integer> cycle = new ArrayList<>();
            int current = onCycle;
            for (int i = 0; i <= nodeCount; i++) {
                cycle.add(current);
                current = predecessor[current];
                if (current == onCycle) {
                    cycle.add(onCycle);
                    break;
                }
            }
            return toArray(cycle);
        }

        private static int[] toArray(List<Integer> list) {
            int[] array = new int[list.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = list.get(i);
            }
            return array;
        }
    }

    public static Result solve(Graph graph, int source) {
        int n = graph.getNodeCount();
        List<Edge> edges = graph.getAllEdges();

        long[] dist = new long[n];
        int[] predecessor = new int[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Sentinels.NO_ROUTE;
            predecessor[i] = -1;
        }
        dist[source] = 0L;

        // N - 1 rondas de relajacion. Con N nodos, cualquier camino
        // SIMPLE (sin repetir nodos) tiene a lo sumo N - 1 aristas, asi
        // que N - 1 rondas alcanzan para que dist[] converja en todo
        // nodo que no dependa de un ciclo. Lo que quede sin converger
        // despues de esto es, por construccion, cosa de un ciclo.
        for (int round = 0; round < n - 1; round++) {
            boolean changed = false;
            for (Edge edge : edges) {
                if (relax(dist, predecessor, edge)) {
                    changed = true;
                }
            }
            if (!changed) {
                break;
            }
        }

        // Ronda extra: cualquier arista que todavia mejora pertenece a
        // un ciclo de ganancia positiva, o sale de un nodo que si
        // pertenece a uno. Actualizamos su predecesor tambien aqui:
        // es justo el salto que despues permite "entrar" al ciclo.
        boolean[] unbounded = new boolean[n];
        int[] viaSeed = new int[n];
        Arrays.fill(viaSeed, -1);
        Deque<Integer> pending = new ArrayDeque<>();
        for (Edge edge : edges) {
            int from = edge.getFrom();
            int to = edge.getTo();
            if (dist[from] == Sentinels.NO_ROUTE) {
                continue; // no hacemos aritmetica sobre el centinela
            }
            long candidate = dist[from] + edge.getWeight();
            if (candidate > dist[to]) {
                predecessor[to] = from;
                if (!unbounded[to]) {
                    unbounded[to] = true; // semilla original: viaSeed queda en -1
                    pending.push(to);
                }
            }
        }

        while (!pending.isEmpty()) {
            int current = pending.pop();
            for (Edge edge : graph.getNeighbors(current)) {
                int next = edge.getTo();
                if (!unbounded[next]) {
                    unbounded[next] = true;
                    viaSeed[next] = current;
                    pending.push(next);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (unbounded[i]) {
                dist[i] = Sentinels.UNBOUNDED;
            }
        }

        return new Result(dist, unbounded, predecessor, viaSeed, source, n);
    }

    private static boolean relax(long[] dist, int[] predecessor, Edge edge) {
        int from = edge.getFrom();
        int to = edge.getTo();
        if (dist[from] == Sentinels.NO_ROUTE) {
            return false;
        }
        long candidate = dist[from] + edge.getWeight();
        if (candidate > dist[to]) {
            dist[to] = candidate;
            predecessor[to] = from;
            return true;
        }
        return false;
    }
}