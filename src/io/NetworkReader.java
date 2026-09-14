package io;

import core.Graph;

/**
 * Parser para la mision 4: convierte el texto pegado en un grafo
 * no dirigido (nodos de 0 a N-1) listo para pasarle a Kruskal.
 *
 * Formato de UN caso, segun el enunciado:
 * N                     -> cantidad de intersecciones, en su propia linea
 * C                     -> cantidad de cables disponibles, en su propia linea
 * A B W (x C veces)     -> cable entre las intersecciones A y B con costo W
 *
 * OJO: a diferencia de las misiones 2 y 3, aqui las intersecciones
 * vienen numeradas de 1 a N (no de 0 a N-1), asi que a cada A y B
 * hay que restarles 1 antes de llamar a Graph.addEdge.
 *
 * Los cables son bidireccionales. Pueden repetirse pares A-B y
 * pueden aparecer cables de una interseccion a si misma: no deben
 * hacer fallar el parser, asi que se agregan todos tal cual.
 *
 * Igual que en la Mision 2, aqui no hay caso sentinela: la cantidad
 * de casos (T) la lee NetworkMission antes de llamar a este metodo,
 * T veces.
 */
public class NetworkReader {

    public static NetworkCase readCase(TokenStream in) {
        int n = in.nextIntInRange(1, 10_000, "La cantidad de intersecciones (N).");
        int c = in.nextIntInRange(0, 100_000, "La cantidad de cables (C).");

        Graph graph = new Graph(n, false); // no dirigido

        for (int i = 0; i < c; i++) {
            int a = in.nextIntInRange(1, n, "La interseccion inicial de un cable.");
            int b = in.nextIntInRange(1, n, "La interseccion final de un cable.");
            long weight = in.nextLongInRange(0, 1_000_000, "El costo de un cable.");
            graph.addEdge(a - 1, b - 1, weight); // 1..N en la entrada -> 0..N-1 internamente
        }

        return new NetworkCase(graph);
    }

    /**
     * Datos de UN caso de la Mision 4: el grafo ya armado (0-indexado),
     * listo para pasarle directo a Kruskal.solve(graph).
     */
    public static class NetworkCase {
        private final Graph graph;

        public NetworkCase(Graph graph) {
            this.graph = graph;
        }

        public Graph getGraph() {
            return graph;
        }
    }
}
