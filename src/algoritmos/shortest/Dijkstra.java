package algoritmos.shortest;

import core.Edge;
import core.Graph;
import core.ShortestPathResult;

import java.util.PriorityQueue;

/**
 * Mision 2: Dijkstra sobre un grafo no dirigido con pesos >= 0.
 * QUE HACE: encuentra el costo MINIMO para ir del nodo start
 * al nodo destino.
 *
 * Mantiene una cola de prioridad con los nodos pendientes,
 * ordenados por la distancia acumulada mas chica conocida.
 * Siempre saca el mas cercano y relaja sus vecinos. Que los
 * pesos sean no negativos es justo la condicion que hace esto
 * correcto: garantiza que cuando un nodo sale de la cola su
 * distancia ya es definitiva y no se puede mejorar despues.
 *
 * Complejidad temporal: O((N + C) log N), con N nodos y C conexiones,
 * por la cola de prioridad (cada arista puede insertar una vez).
 * Complejidad espacial: O(N + C).
 */
public class Dijkstra {

    public static ShortestPathResult solve(Graph graph, int start, int destination) {
        int n = graph.getNodeCount();

        // Si inicio y destino coinciden el enunciado dice que la respuesta es 0.
        if (start == destination) {
            return new ShortestPathResult(0L, new int[]{start});
        }

        long[] distance = new long[n];
        int[] parent = new int[n];
        boolean[] settled = new boolean[n]; // true cuando la distancia ya es definitiva

        for (int i = 0; i < n; i++) {
            distance[i] = Long.MAX_VALUE; // "todavia no lo hemos alcanzado"
            parent[i] = -1;
        }
        distance[start] = 0L;

        // Cada elemento de la cola es {distancia acumulada, nodo}.
        // PriorityQueue permitida por el enunciado. Puede quedar con
        // entradas "viejas" de un nodo (insertamos de nuevo cuando
        // mejora su distancia); por eso se descartan con el chequeo
        // de "settled" al sacarlas.
        PriorityQueue<long[]> pending = new PriorityQueue<long[]>(
                (a, b) -> Long.compare(a[0], b[0]));
        pending.add(new long[]{0L, start});

        while (!pending.isEmpty()) {
            long[] top = pending.poll();
            int node = (int) top[1];

            if (settled[node]) {
                continue; // entrada obsoleta: ya salio antes con distancia igual o mejor
            }
            settled[node] = true;

            if (node == destination) {
                break; // su distancia ya es definitiva, no hace falta seguir
            }

            for (Edge edge : graph.getNeighbors(node)) {
                int neighbor = edge.getTo();
                if (settled[neighbor]) {
                    continue;
                }
                long candidate = distance[node] + edge.getWeight();
                if (candidate < distance[neighbor]) {
                    distance[neighbor] = candidate;
                    parent[neighbor] = node;
                    pending.add(new long[]{candidate, neighbor});
                }
            }
        }

        if (distance[destination] == Long.MAX_VALUE) {
            return ShortestPathResult.noRoute();
        }

        return new ShortestPathResult(distance[destination], buildPath(parent, destination));
    }

    /*
     * Reconstruimos el camino hacia atras por el arreglo de padres,
     * desde el destino hasta el inicio (parent[start] == -1, ahi paramos),
     * y lo dejamos en orden inicio -> destino. Igual que en BFS/DFS.
     */
    private static int[] buildPath(int[] parent, int destination) {
        int length = 0;
        int cursor = destination;
        while (cursor != -1) {
            length++;
            cursor = parent[cursor];
        }

        int[] path = new int[length];
        cursor = destination;
        for (int i = length - 1; i >= 0; i--) {
            path[i] = cursor;
            cursor = parent[cursor];
        }
        return path;
    }
}
