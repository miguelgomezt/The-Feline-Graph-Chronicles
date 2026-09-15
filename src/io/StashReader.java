package io;

import core.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class StashReader {

    private StashReader() {
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

    /**
     * Complejidad: O(N + M) por caso de prueba (una pasada por las
     * aristas para construirlas); O(N + M) en espacio por el grafo que
     * arma cada caso.
     */
    public static List<StashCase> parse(String rawInput) {
        TokenCursor tokens = new TokenCursor(rawInput);
        List<StashCase> cases = new ArrayList<>();

        int testCaseCount = tokens.nextInt("el numero de casos de prueba (T)");
        if (testCaseCount < 0) {
            throw new IllegalArgumentException("El numero de casos de prueba no puede ser negativo.");
        }

        for (int caseIndex = 1; caseIndex <= testCaseCount; caseIndex++) {
            cases.add(parseOneCase(tokens, caseIndex));
        }

        return cases;
    }

    private static StashCase parseOneCase(TokenCursor tokens, int caseIndex) {
        int n = tokens.nextInt("N en el caso " + caseIndex);
        int m = tokens.nextInt("M en el caso " + caseIndex);
        int s = tokens.nextInt("S en el caso " + caseIndex);
        int d = tokens.nextInt("D en el caso " + caseIndex);

        if (n < 1 || n > 100) {
            throw new IllegalArgumentException("N = " + n + " fuera de rango en el caso "
                    + caseIndex + " (debe estar entre 1 y 100).");
        }
        if (m < 0 || m > 5000) {
            throw new IllegalArgumentException("M = " + m + " fuera de rango en el caso "
                    + caseIndex + " (debe estar entre 0 y 5000).");
        }
        requireNode(s, n, "S", caseIndex);
        requireNode(d, n, "D", caseIndex);

        Graph graph = new Graph(n, true); // mision 3: los pasajes son dirigidos

        for (int edgeIndex = 1; edgeIndex <= m; edgeIndex++) {
            int a = tokens.nextInt("el origen de la arista " + edgeIndex + " del caso " + caseIndex);
            int b = tokens.nextInt("el destino de la arista " + edgeIndex + " del caso " + caseIndex);
            long w = tokens.nextLong("el peso de la arista " + edgeIndex + " del caso " + caseIndex);

            requireNode(a, n, "el origen de una arista", caseIndex);
            requireNode(b, n, "el destino de una arista", caseIndex);
            if (w < -1000 || w > 1000) {
                throw new IllegalArgumentException("El peso " + w + " de la arista " + edgeIndex
                        + " del caso " + caseIndex + " esta fuera del rango permitido (-1000 a 1000).");
            }
            graph.addEdge(a, b, w);
        }

        return new StashCase(graph, s, d);
    }

    private static void requireNode(int node, int n, String label, int caseIndex) {
        if (node < 0 || node >= n) {
            throw new IllegalArgumentException("El nodo " + node + " (" + label
                    + ") esta fuera de rango en el caso " + caseIndex + "; los nodos validos van de 0 a "
                    + (n - 1) + ".");
        }
    }


    private static class TokenCursor {
        private final Scanner scanner;

        TokenCursor(String rawInput) {
            this.scanner = new Scanner(rawInput == null ? "" : rawInput);
        }

        int nextInt(String label) {
            return (int) nextLong(label);
        }

        long nextLong(String label) {
            if (!scanner.hasNext()) {
                throw new IllegalArgumentException("Entrada incompleta: falta " + label + ".");
            }
            String token = scanner.next();
            try {
                return Long.parseLong(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Se esperaba un numero para " + label
                        + ", pero se encontro \"" + token + "\".");
            }
        }
    }
}