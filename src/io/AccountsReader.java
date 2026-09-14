package io;

import core.Graph;

/**
 * Parser para la mision 2: convierte el texto pegado en un grafo
 * no dirigido (nodos de 0 a N-1), mas el nodo de inicio y el de
 * destino de ese caso puntual.
 *
 * Formato de UN caso, segun el enunciado:
 * N C S D              -> nodos, conexiones, inicio y destino
 * A B W (x C veces)    -> conexion entre A y B con costo W
 *
 * Las conexiones son bidireccionales. Pueden repetirse pares A-B
 * y pueden aparecer bucles (A == B): no deben hacer fallar el parser,
 * asi que se agregan todas tal cual - Graph.addEdge no falla con ellas.
 *
 * A diferencia de la Mision 1, aqui no hay un caso "sentinela" que
 * marque el final: la cantidad de casos (T) la lee AccountsMission
 * antes de llamar a este metodo, T veces.
 */
public class AccountsReader {

    public static AccountsCase readCase(TokenStream in) {
        int n = in.nextIntInRange(1, 10_000, "La cantidad de nodos (N).");
        int c = in.nextIntInRange(0, 100_000, "La cantidad de conexiones (C).");
        int start = in.nextIntInRange(0, n - 1, "El nodo de inicio (S).");
        int destination = in.nextIntInRange(0, n - 1, "El nodo de destino (D).");

        Graph graph = new Graph(n, false); // no dirigido

        for (int i = 0; i < c; i++) {
            int a = in.nextIntInRange(0, n - 1, "El nodo A de una conexion.");
            int b = in.nextIntInRange(0, n - 1, "El nodo B de una conexion.");
            long weight = in.nextLongInRange(0, 1_000_000, "El costo de una conexion.");
            graph.addEdge(a, b, weight);
        }

        return new AccountsCase(graph, start, destination);
    }

    /**
     * Datos de UN caso de la Mision 2: el grafo ya armado, mas el
     * nodo de inicio y el de destino que le va a pasar a Dijkstra.
     */
    public static class AccountsCase {
        private final Graph graph;
        private final int start;
        private final int destination;

        public AccountsCase(Graph graph, int start, int destination) {
            this.graph = graph;
            this.start = start;
            this.destination = destination;
        }

        public Graph getGraph() {
            return graph;
        }

        public int getStart() {
            return start;
        }

        public int getDestination() {
            return destination;
        }
    }
}
