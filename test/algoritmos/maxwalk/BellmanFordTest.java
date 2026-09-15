package algoritmos.maxwalk;

import core.Graph;
import core.Sentinels;

public class BellmanFordTest {

    public static void main(String[] args) {
        checkCase1();
        checkCase2();
        checkCase3();
        checkUnreachable();
        System.out.println("BellmanFordTest: todas las pruebas pasaron.");
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

        BellmanFord.Result result = BellmanFord.solve(graph, 0);
        expect("Caso 1 (0 -> 4)", 110L, result.distanceTo(4));
        if (result.isUnbounded(4) || result.isUnreachable(4)) {
            throw new AssertionError("Caso 1: el nodo 4 no deberia estar marcado como no acotado ni inalcanzable.");
        }
    }

    static void checkCase2() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 20);
        graph.addEdge(1, 2, 30);
        graph.addEdge(2, 1, -10);
        graph.addEdge(2, 3, 15);

        BellmanFord.Result result = BellmanFord.solve(graph, 0);
        if (!result.isUnbounded(3)) {
            throw new AssertionError("Caso 2: el nodo 3 deberia quedar marcado como no acotado.");
        }
        expect("Caso 2 (0 -> 3) no acotado", Sentinels.UNBOUNDED, result.distanceTo(3));
        System.out.println("OK - Caso 2 (0 -> 3) = Infinite churun!");
    }

    static void checkCase3() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, -40);
        graph.addEdge(1, 2, -25);
        graph.addEdge(0, 2, -80);

        BellmanFord.Result result = BellmanFord.solve(graph, 0);
        expect("Caso 3 (0 -> 2)", -65L, result.distanceTo(2));
    }


    static void checkUnreachable() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 10);
        // el nodo 2 no tiene ninguna arista que llegue a el

        BellmanFord.Result result = BellmanFord.solve(graph, 0);
        if (!result.isUnreachable(2)) {
            throw new AssertionError("El nodo 2 deberia ser inalcanzable desde 0.");
        }
        System.out.println("OK - nodo suelto queda inalcanzable");
    }

    private static void expect(String label, long expected, long actual) {
        if (expected != actual) {
            throw new AssertionError(label + ": se esperaba " + expected + " pero dio " + actual);
        }
        System.out.println("OK - " + label + " = " + actual);
    }
}