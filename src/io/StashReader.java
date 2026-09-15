package io;

import core.Graph;

public class StashReader {

    public static StashCase readCase(TokenStream in) {
        int n = in.nextIntInRange(1, 100, "La cantidad de nodos (N).");
        int m = in.nextIntInRange(0, 5000, "La cantidad de pasajes (M).");
        int source = in.nextIntInRange(0, n - 1, "El nodo de origen (S).");
        int destination = in.nextIntInRange(0, n - 1, "El nodo de destino (D).");

        Graph graph = new Graph(n, true); // mision 3: los pasajes son dirigidos

        for (int i = 0; i < m; i++) {
            int a = in.nextIntInRange(0, n - 1, "El origen de un pasaje.");
            int b = in.nextIntInRange(0, n - 1, "El destino de un pasaje.");
            long weight = in.nextLongInRange(-1000, 1000, "El churun de un pasaje.");
            graph.addEdge(a, b, weight);
        }

        return new StashCase(graph, source, destination);
    }


    public static class StashCase {
        private final Graph graph;
        private final int source;
        private final int destination;

        public StashCase(Graph graph, int source, int destination) {
            this.graph = graph;
            this.source = source;
            this.destination = destination;
        }

        public Graph getGraph() {
            return graph;
        }

        public int getSource() {
            return source;
        }

        public int getDestination() {
            return destination;
        }
    }
}