package algoritmos.mst;

import core.Graph;
import core.MstResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KruskalTest {

    @Test
    void muestraDelEnunciado_costoCincuentaYCinco() {
        // N=4, cables (ya 0-indexados como los deja NetworkReader):
        // 0-1 10, 1-2 20, 2-3 30, 3-0 40, 0-2 15
        Graph graph = new Graph(4, false);
        graph.addEdge(0, 1, 10);
        graph.addEdge(1, 2, 20);
        graph.addEdge(2, 3, 30);
        graph.addEdge(3, 0, 40);
        graph.addEdge(0, 2, 15);

        MstResult result = Kruskal.solve(graph);

        assertTrue(result.isConnected());
        assertEquals(55L, result.getTotalCost());
        assertEquals(3, result.getEdgesUsed().size()); // N-1 = 3 cables en el MST
    }

    @Test
    void redQueNoAlcanzaParaConectarTodo_esDesconectada() {
        // N=3, pero solo hay un cable: el nodo 2 queda aislado.
        Graph graph = new Graph(3, false);
        graph.addEdge(0, 1, 10);

        MstResult result = Kruskal.solve(graph);

        assertFalse(result.isConnected());
    }

    @Test
    void unaSolaInterseccion_esTrivialmenteConectadaConCostoCero() {
        Graph graph = new Graph(1, false);

        MstResult result = Kruskal.solve(graph);

        assertTrue(result.isConnected());
        assertEquals(0L, result.getTotalCost());
        assertEquals(0, result.getEdgesUsed().size());
    }

    @Test
    void bucleYCablesRepetidos_noHacenFallarYUsanElMasBarato() {
        Graph graph = new Graph(2, false);
        graph.addEdge(0, 0, 999);  // bucle propio: nunca puede formar parte del MST
        graph.addEdge(0, 1, 20);
        graph.addEdge(0, 1, 5);    // mismo par, mas barato: debe ganar este

        MstResult result = Kruskal.solve(graph);

        assertTrue(result.isConnected());
        assertEquals(5L, result.getTotalCost());
        assertEquals(1, result.getEdgesUsed().size());
    }
}
