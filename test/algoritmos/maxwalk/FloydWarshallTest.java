package algoritmos.maxwalk;

import core.Graph;
import core.Sentinels;

public class FloydWarshallTest {

    public static void main(String[] args) {
        checkCase1();
        checkCase2();
        checkCase3();
        System.out.println("FloydWarshallTest: todas las pruebas pasaron.");
    }


    static void checkCase1() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 50);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 2, -30);
        graph.addEdge(1, 3, 40);
        graph.addEdge(2, 1, -5);
        graph.addEdge(2, 3, 60);
        graph.addEdge(3, 4, 20);

        long[][] matrix = FloydWarshall.solve(graph);
        expect("Caso 1 (0 -> 4)", 110L, matrix[0][4]);
    }

    static void checkCase2() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 20);
        graph.addEdge(1, 2, 30);
        graph.addEdge(2, 1, -10);
        graph.addEdge(2, 3, 15);

        long[][] matrix = FloydWarshall.solve(graph);
        expect("Caso 2 (0 -> 3) no acotado", Sentinels.UNBOUNDED, matrix[0][3]);
    }

    static void checkCase3() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, -40);
        graph.addEdge(1, 2, -25);
        graph.addEdge(0, 2, -80);

        long[][] matrix = FloydWarshall.solve(graph);
        expect("Caso 3 (0 -> 2)", -65L, matrix[0][2]);
    }

    private static void expect(String label, long expected, long actual) {
        if (expected != actual) {
            throw new AssertionError(label + ": se esperaba " + expected + " pero dio " + actual);
        }
        System.out.println("OK - " + label + " = " + actual);
    }
}