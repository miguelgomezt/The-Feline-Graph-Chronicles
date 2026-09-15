package algoritmos.maxwalk;

import core.Graph;
import core.Sentinels;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FloydWarshallTest {

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

        long[][] matrix = FloydWarshall.solve(graph);
        assertEquals(110L, matrix[0][4], "Caso 1 (0 -> 4)");
    }

    @Test
    void case2CicloPositivoNoAcotado() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 20);
        graph.addEdge(1, 2, 30);
        graph.addEdge(2, 1, -10);
        graph.addEdge(2, 3, 15);

        long[][] matrix = FloydWarshall.solve(graph);
        assertEquals(Sentinels.UNBOUNDED, matrix[0][3], "Caso 2 (0 -> 3) deberia quedar no acotado");
    }

    @Test
    void case3MaximoNegativo() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, -40);
        graph.addEdge(1, 2, -25);
        graph.addEdge(0, 2, -80);

        long[][] matrix = FloydWarshall.solve(graph);
        assertEquals(-65L, matrix[0][2], "Caso 3 (0 -> 2)");
    }
}