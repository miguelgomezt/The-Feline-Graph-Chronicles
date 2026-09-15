package mission;

import algoritmos.maxwalk.BellmanFord;
import algoritmos.maxwalk.CrossCheck;
import algoritmos.maxwalk.FloydWarshall;
import core.Graph;
import io.StashReader;
import io.TokenStream;


public class StashMission implements Mission {

    private static final String SAMPLE =
            "3\n"
                    + "5 7 0 4\n"
                    + "0 1 50\n"
                    + "0 2 10\n"
                    + "1 2 -30\n"
                    + "1 3 40\n"
                    + "2 1 -5\n"
                    + "2 3 60\n"
                    + "3 4 20\n"
                    + "4 4 0 3\n"
                    + "0 1 20\n"
                    + "1 2 30\n"
                    + "2 1 -10\n"
                    + "2 3 15\n"
                    + "3 3 0 2\n"
                    + "0 1 -40\n"
                    + "1 2 -25\n"
                    + "0 2 -80\n";

    @Override
    public String getName() {
        return "Mision 3: La reserva definitiva de churun";
    }

    @Override
    public String getSampleInput() {
        return SAMPLE;
    }

    @Override
    public MissionOutcome solve(String input) {
        TokenStream in = new TokenStream(input);
        MissionOutcome outcome = new MissionOutcome();


        int totalCases = in.nextIntInRange(0, 1_000_000, "La cantidad de casos de prueba (T).");

        for (int caseNumber = 1; caseNumber <= totalCases; caseNumber++) {
            StashReader.StashCase testCase = StashReader.readCase(in);
            Graph graph = testCase.getGraph();
            int source = testCase.getSource();
            int destination = testCase.getDestination();

            long[][] matrix = FloydWarshall.solve(graph);
            BellmanFord.Result bellmanFord = BellmanFord.solve(graph, source);
            CrossCheck.Result crossCheck = CrossCheck.compare(matrix, bellmanFord, source, destination);

            String prefix = "Case #" + caseNumber + ": ";

            String line;
            if (bellmanFord.isUnreachable(destination)) {
                line = prefix + "Limon blocked the way";
            } else if (bellmanFord.isUnbounded(destination)) {
                line = prefix + "Infinite churun!";
            } else {
                line = prefix + bellmanFord.distanceTo(destination);
            }

            String mismatchWarning = crossCheck.agree()
                    ? null
                    : prefix + crossCheck.mismatchMessage(source, destination);

            outcome.addCase(line, new StashDrawing(matrix, source, destination, mismatchWarning));
        }

        return outcome;
    }


    public static class StashDrawing {
        private final long[][] matrix;
        private final int source;
        private final int destination;
        private final String mismatchWarning; // null si los dos algoritmos coincidieron

        public StashDrawing(long[][] matrix, int source, int destination, String mismatchWarning) {
            this.matrix = matrix;
            this.source = source;
            this.destination = destination;
            this.mismatchWarning = mismatchWarning;
        }

        public long[][] getMatrix() {
            return matrix;
        }

        public int getSource() {
            return source;
        }

        public int getDestination() {
            return destination;
        }

        public boolean hasMismatch() {
            return mismatchWarning != null;
        }

        public String getMismatchWarning() {
            return mismatchWarning;
        }
    }
}