package mission;

import algoritmos.mst.Kruskal;
import core.Graph;
import core.MstResult;
import io.NetworkReader;
import io.TokenStream;

public class NetworkMission implements Mission {

    private static final String SAMPLE =
            "1\n" +
                    "4\n" +
                    "5\n" +
                    "1 2 10\n" +
                    "2 3 20\n" +
                    "3 4 30\n" +
                    "4 1 40\n" +
                    "1 3 15\n";

    @Override
    public String getName() {
        return "Mision 4: Reconectar la red.";
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

            NetworkReader.NetworkCase testCase = NetworkReader.readCase(in);
            Graph graph = testCase.getGraph();

            MstResult result = Kruskal.solve(graph);

            String line;
            if (!result.isConnected()) {
                line = "Case #" + caseNumber + ": Limon cut too many cables";
            } else {
                line = "Case #" + caseNumber + ": " + result.getTotalCost();
            }

            outcome.addCase(line, new NetworkDrawing(graph, result));
        }

        return outcome;
    }

    public static class NetworkDrawing {

        private final Graph graph;
        private final MstResult result;

        public NetworkDrawing(Graph graph, MstResult result) {
            this.graph = graph;
            this.result = result;
        }

        public Graph getGraph() {
            return graph;
        }

        public MstResult getResult() {
            return result;
        }
    }
}
