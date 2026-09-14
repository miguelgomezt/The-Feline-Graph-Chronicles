package algoritmos.shortest;

import core.Graph;
import core.ShortestPathResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DijkstraTest {

    @Test
    void muestraUno_conexionDirecta() {
        // N=2, C=1, S=0, D=1 : 0 1 100
        Graph graph = new Graph(2, false);
        graph.addEdge(0, 1, 100);

        ShortestPathResult result = Dijkstra.solve(graph, 0, 1);

        assertFalse(result.isNoRoute());
        assertEquals(100L, result.getCost());
        assertArrayEquals(new int[]{0, 1}, result.getPath());
    }

    @Test
    void muestraDos_rutaDeDosSaltosMasBarata() {
        // N=3, C=3, S=2, D=0 : 0-1 100, 0-2 200, 1-2 50
        Graph graph = new Graph(3, false);
        graph.addEdge(0, 1, 100);
        graph.addEdge(0, 2, 200);
        graph.addEdge(1, 2, 50);

        ShortestPathResult result = Dijkstra.solve(graph, 2, 0);

        assertFalse(result.isNoRoute());
        assertEquals(150L, result.getCost());
        assertArrayEquals(new int[]{2, 1, 0}, result.getPath());
    }

    @Test
    void muestraTres_sinConexionesEsInalcanzable() {
        // N=2, C=0, S=0, D=1 : grafo sin ninguna arista.
        Graph graph = new Graph(2, false);

        ShortestPathResult result = Dijkstra.solve(graph, 0, 1);

        assertTrue(result.isNoRoute());
        assertNull(result.getPath());
    }

    @Test
    void inicioIgualADestino_esCeroSinImportarLasAristas() {
        Graph graph = new Graph(3, false);
        graph.addEdge(0, 1, 100);
        graph.addEdge(1, 2, 100);

        ShortestPathResult result = Dijkstra.solve(graph, 1, 1);

        assertFalse(result.isNoRoute());
        assertEquals(0L, result.getCost());
        assertArrayEquals(new int[]{1}, result.getPath());
    }

    @Test
    void bucleYConexionesRepetidas_noHacenFallarYUsanLaMasBarata() {
        Graph graph = new Graph(2, false);
        graph.addEdge(0, 0, 999);  // bucle propio, no debe afectar el resultado
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 1, 999); // misma conexion, mas cara: debe ganar la de costo 10

        ShortestPathResult result = Dijkstra.solve(graph, 0, 1);

        assertFalse(result.isNoRoute());
        assertEquals(10L, result.getCost());
    }
}
