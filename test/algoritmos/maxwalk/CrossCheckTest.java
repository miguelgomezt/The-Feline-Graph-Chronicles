package algoritmos.maxwalk;

import core.Sentinels;


public class CrossCheckTest {

    public static void main(String[] args) {
        checkAgreesWhenEqual();
        checkDetectsMismatch();
        System.out.println("CrossCheckTest: todas las pruebas pasaron.");
    }

    static void checkAgreesWhenEqual() {
        long[][] matrix = {{0, 110}, {Sentinels.NO_ROUTE, 0}};
        BellmanFord.Result bellmanFord = new BellmanFord.Result(
                new long[]{0, 110}, new boolean[]{false, false});

        CrossCheck.Result result = CrossCheck.compare(matrix, bellmanFord, 0, 1);
        if (!result.agree()) {
            throw new AssertionError("Deberian coincidir cuando los dos valores son iguales.");
        }
        System.out.println("OK - CrossCheck.compare coincide cuando los valores son iguales");
    }

    static void checkDetectsMismatch() {
        long[][] matrix = {{0, 110}, {Sentinels.NO_ROUTE, 0}};
        BellmanFord.Result bellmanFord = new BellmanFord.Result(
                new long[]{0, Sentinels.UNBOUNDED}, new boolean[]{false, true});

        CrossCheck.Result result = CrossCheck.compare(matrix, bellmanFord, 0, 1);
        if (result.agree()) {
            throw new AssertionError("Deberia detectar la discrepancia entre 110 y no acotado.");
        }
        String message = result.mismatchMessage(0, 1);
        if (message == null || message.isEmpty()) {
            throw new AssertionError("El mensaje de discrepancia no deberia ser nulo ni vacio.");
        }
        System.out.println("OK - CrossCheck.compare detecta la discrepancia: " + message);
    }
}