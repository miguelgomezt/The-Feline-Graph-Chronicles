package algoritmos.maxwalk;

import core.Graph;
import core.Sentinels;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BellmanFordTest {

    @Test
    void case1MaximoFinito() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 50);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 2, -30);
        graph.addEdge(1, 3, 40);
        graph.addEdge(2, 1, -5);
        graph.addEdge(2, 3, 60);
        graph.addEdge(3, 4, 20);

        BellmanFord.Result result = BellmanFord.solve(graph, 0);
        assertEquals(110L, result.distanceTo(4), "Caso 1 (0 -> 4)");
        assertFalse(result.isUnbounded(4));
        assertFalse(result.isUnreachable(4));
        assertArrayEquals(new int[] {0, 1, 3, 4}, result.routeTo(4));
    }

    @Test
    void case2CicloPositivoNoAcotado() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 20);
        graph.addEdge(1, 2, 30);
        graph.addEdge(2, 1, -10);
        graph.addEdge(2, 3, 15);

        BellmanFord.Result result = BellmanFord.solve(graph, 0);
        assertTrue(result.isUnbounded(3), "Caso 2 (0 -> 3) deberia quedar no acotado");
        assertEquals(Sentinels.UNBOUNDED, result.distanceTo(3));
        assertNull(result.routeTo(3));

        // El ciclo responsable es 1 -> 2 -> 1 (ganancia 30 - 10 = 20 > 0).
        // Se verifica por las ARISTAS que contiene, no por el nodo donde
        // arranca la lista, porque el mismo ciclo se puede describir
        // empezando en 1 o en 2 sin que eso cambie cual es el ciclo.
        int[] cycle = result.responsibleCycle(3);
        assertTrue(cycle != null && cycle.length >= 2, "Deberia existir un ciclo responsable");
        assertTrue(contieneArista(cycle, 1, 2), "El ciclo deberia usar la arista 1 -> 2");
        assertTrue(contieneArista(cycle, 2, 1), "El ciclo deberia usar la arista 2 -> 1");
        assertEquals(cycle[0], cycle[cycle.length - 1], "El ciclo debe volver a cerrarse en el mismo nodo");
    }

    @Test
    void case3MaximoNegativo() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, -40);
        graph.addEdge(1, 2, -25);
        graph.addEdge(0, 2, -80);

        BellmanFord.Result result = BellmanFord.solve(graph, 0);
        assertEquals(-65L, result.distanceTo(2), "Caso 3 (0 -> 2)");
        // -65 (via 0 -> 1 -> 2) es mayor que -80 (via 0 -> 2 directo),
        // asi que la ruta que logra el maximo pasa por el nodo 1.
        assertArrayEquals(new int[] {0, 1, 2}, result.routeTo(2));
    }

    @Test
    void nodoSueltoQuedaInalcanzable() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 10);
        // el nodo 2 no tiene ninguna arista que llegue a el

        BellmanFord.Result result = BellmanFord.solve(graph, 0);
        assertTrue(result.isUnreachable(2), "El nodo 2 deberia ser inalcanzable desde 0");
    }

    private static boolean contieneArista(int[] cycle, int from, int to) {
        for (int i = 0; i < cycle.length - 1; i++) {
            if (cycle[i] == from && cycle[i + 1] == to) {
                return true;
            }
        }
        return false;
    }
}