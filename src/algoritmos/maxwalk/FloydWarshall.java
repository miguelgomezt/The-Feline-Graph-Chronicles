
package algoritmos.maxwalk;

import core.Edge;
import core.Graph;
import core.Sentinels;


    public class FloydWarshall {

        private FloydWarshall() {
        }

        public static long[][] solve(Graph graph) {
            int n = graph.getNodeCount();
            long[][] d = new long[n][n];


            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    d[i][j] = (i == j) ? 0L : Sentinels.NO_ROUTE;
                }
            }

            // 2) Cargamos las aristas directas. Si el mismo par ordenado
            // aparece mas de una vez (bucles i -> i incluidos), nos
            // quedamos con el peso mas grande de los repetidos: de las
            // aristas paralelas solo se puede usar una en un paso dado.
            for (Edge edge : graph.getAllEdges()) {
                int from = edge.getFrom();
                int to = edge.getTo();
                long weight = edge.getWeight();

                if (d[from][to] == Sentinels.NO_ROUTE || weight > d[from][to]) {
                    d[from][to] = weight;
                }
            }

            // 3) Triple ciclo: para cada nodo intermedio k, intentamos
            // mejorar cada par (i, j) pasando por k. Sentinels.safeAdd
            // nunca hace aritmetica real si alguno de los dos tramos no
            // existe (NO_ROUTE); como NO_ROUTE es Long.MIN_VALUE, la
            // comparacion de abajo descarta esos casos sola.
            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    if (d[i][k] == Sentinels.NO_ROUTE) {
                        continue;
                    }
                    for (int j = 0; j < n; j++) {
                        long viaK = Sentinels.safeAdd(d[i][k], d[k][j]);
                        if (viaK != Sentinels.NO_ROUTE && viaK > d[i][j]) {
                            d[i][j] = viaK;
                        }
                    }
                }
            }


            boolean[][] unbounded = new boolean[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    unbounded[i][j] = isUnbounded(d, i, j, n);
                }
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (unbounded[i][j]) {
                        d[i][j] = Sentinels.UNBOUNDED;
                    }
                }
            }

            return d;
        }

        private static boolean isUnbounded(long[][] d, int i, int j, int n) {
            for (int k = 0; k < n; k++) {
                if (d[i][k] != Sentinels.NO_ROUTE && d[k][k] > 0 && d[k][j] != Sentinels.NO_ROUTE) {
                    return true;
                }
            }
            return false;
        }
    }

