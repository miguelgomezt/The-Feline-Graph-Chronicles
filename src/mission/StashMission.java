package mission;

import algoritmos.maxwalk.BellmanFord;
import algoritmos.maxwalk.CrossCheck;
import algoritmos.maxwalk.FloydWarshall;
import io.StashReader;

import java.util.ArrayList;
import java.util.List;


public class StashMission {


    public static final String SAMPLE_INPUT =
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

    public String name() {
        return "Mision 3: La reserva definitiva de churun";
    }

    public String sampleInput() {
        return SAMPLE_INPUT;
    }


    public static class CaseResult {
        private final String outputLine;
        private final long[][] matrix;
        private final int source;
        private final int destination;
        private final String mismatchWarning; // null si los dos algoritmos coinciden

        CaseResult(String outputLine, long[][] matrix, int source, int destination,
                   String mismatchWarning) {
            this.outputLine = outputLine;
            this.matrix = matrix;
            this.source = source;
            this.destination = destination;
            this.mismatchWarning = mismatchWarning;
        }

        public String getOutputLine() {
            return outputLine;
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

        /** null si Floyd-Warshall y Bellman-Ford coincidieron. */
        public String getMismatchWarning() {
            return mismatchWarning;
        }
    }

    public static class StashOutcome {
        private final List<CaseResult> cases;

        StashOutcome(List<CaseResult> cases) {
            this.cases = cases;
        }

        public List<CaseResult> getCases() {
            return cases;
        }

         public String outputText() {
            StringBuilder sb = new StringBuilder();
            for (CaseResult c : cases) {
                sb.append(c.getOutputLine()).append('\n');
            }
            return sb.toString();
        }
    }

    public StashOutcome solve(String rawInput) {
        List<StashReader.StashCase> testCases = StashReader.parse(rawInput);
        List<CaseResult> results = new ArrayList<>(testCases.size());

        int caseNumber = 1;
        for (StashReader.StashCase testCase : testCases) {
            results.add(solveOne(caseNumber, testCase));
            caseNumber++;
        }

        return new StashOutcome(results);
    }

    private CaseResult solveOne(int caseNumber, StashReader.StashCase testCase) {
        int source = testCase.getSource();
        int destination = testCase.getDestination();

        long[][] matrix = FloydWarshall.solve(testCase.getGraph());
        BellmanFord.Result bellmanFord = BellmanFord.solve(testCase.getGraph(), source);

        CrossCheck.Result crossCheck = CrossCheck.compare(matrix, bellmanFord, source, destination);
        String mismatchWarning = crossCheck.agree()
                ? null
                : "Case #" + caseNumber + ": " + crossCheck.mismatchMessage(source, destination);

        String prefix = "Case #" + caseNumber + ": ";


        String outputLine;
        if (bellmanFord.isUnreachable(destination)) {
            outputLine = prefix + "Limon blocked the way";
        } else if (bellmanFord.isUnbounded(destination)) {
            outputLine = prefix + "Infinite churun!";
        } else {
            outputLine = prefix + bellmanFord.distanceTo(destination);
        }

        return new CaseResult(outputLine, matrix, source, destination, mismatchWarning);
    }
}