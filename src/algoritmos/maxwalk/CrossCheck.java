package algoritmos.maxwalk;

import core.Sentinels;


public class CrossCheck {

    private CrossCheck() {
    }
    public static class Result {
        private final boolean agree;
        private final long floydWarshallValue;
        private final long bellmanFordValue;

        public Result(boolean agree, long floydWarshallValue, long bellmanFordValue) {
            this.agree = agree;
            this.floydWarshallValue = floydWarshallValue;
            this.bellmanFordValue = bellmanFordValue;
        }

        public boolean agree() {
            return agree;
        }

        public long getFloydWarshallValue() {
            return floydWarshallValue;
        }

        public long getBellmanFordValue() {
            return bellmanFordValue;
        }

        public String mismatchMessage(int source, int destination) {
            if (agree) {
                return null;
            }
            return "Floyd-Warshall y Bellman-Ford no coinciden para (" + source + " -> "
                    + destination + "): Floyd-Warshall dice " + describe(floydWarshallValue)
                    + ", Bellman-Ford dice " + describe(bellmanFordValue) + ".";
        }

        private static String describe(long value) {
            if (value == Sentinels.NO_ROUTE) {
                return "sin ruta";
            }
            if (value == Sentinels.UNBOUNDED) {
                return "no acotado (infinito)";
            }
            return String.valueOf(value);
        }
    }


    public static Result compare(long[][] floydWarshallMatrix, BellmanFord.Result bellmanFordResult,
                                 int source, int destination) {
        long fwValue = floydWarshallMatrix[source][destination];
        long bfValue = bellmanFordResult.distanceTo(destination);
        return new Result(fwValue == bfValue, fwValue, bfValue);
    }
}