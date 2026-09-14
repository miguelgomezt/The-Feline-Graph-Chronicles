package mission;

import algoritmos.shortest.Dijkstra;
import core.Graph;
import core.ShortestPathResult;
import io.AccountsReader;
import io.TokenStream;

/**
 * Mision 2: Recuperar las cuentas de Claude (algoritmo de Dijkstra).
 *
 * Esta clase es solo para coordinar. Lee T, llama al parser T veces,
 * llama a Dijkstra sobre cada grafo y convierte el resultado en las
 * lineas de texto que pide el enunciado.
 */
public class AccountsMission implements Mission {

    private static final String SAMPLE =
            "3\n" +
                    "2 1 0 1\n" +
                    "0 1 100\n" +
                    "3 3 2 0\n" +
                    "0 1 100\n" +
                    "0 2 200\n" +
                    "1 2 50\n" +
                    "2 0 0 1\n";

    @Override
    public String getName() {
        return "Mision 2: Recuperar las cuentas de Claude.";
    }

    @Override
    public String getSampleInput() {
        return SAMPLE;
    }

    @Override
    public MissionOutcome solve(String input) {
        TokenStream in = new TokenStream(input);
        MissionOutcome outcome = new MissionOutcome();

        // A diferencia de la Mision 1, aqui la entrada empieza con T:
        // no hay un caso sentinela que marque el final.
        int totalCases = in.nextIntInRange(0, 1_000_000, "La cantidad de casos de prueba (T).");

        for (int caseNumber = 1; caseNumber <= totalCases; caseNumber++) {

            AccountsReader.AccountsCase testCase = AccountsReader.readCase(in);
            Graph graph = testCase.getGraph();

            ShortestPathResult result = Dijkstra.solve(
                    graph, testCase.getStart(), testCase.getDestination());

            String line;
            if (result.isNoRoute()) {
                // Mensaje exacto del enunciado: sin tildes, sin emojis, sin punto final.
                line = "Case #" + caseNumber + ": Nina is very sad";
            } else {
                line = "Case #" + caseNumber + ": " + result.getCost();
            }

            // Guardamos la linea y, junto a ella, lo que GraphCanvas va a necesitar
            // para pintar este caso: el grafo, el resultado y los nodos de inicio/destino
            // (utiles para marcarlos aunque el destino sea inalcanzable).
            outcome.addCase(line, new AccountsDrawing(
                    graph, result, testCase.getStart(), testCase.getDestination()));
        }

        return outcome;
    }

    /**
     * Datos de dibujo de un caso de la Mision 2.
     * GraphCanvas recibe un objeto de este tipo y pinta el grafo,
     * resaltando el camino minimo encontrado (si existe).
     */
    public static class AccountsDrawing {

        private final Graph graph;
        private final ShortestPathResult result;
        private final int start;
        private final int destination;

        public AccountsDrawing(Graph graph, ShortestPathResult result, int start, int destination) {
            this.graph = graph;
            this.result = result;
            this.start = start;
            this.destination = destination;
        }

        public Graph getGraph() {
            return graph;
        }

        public ShortestPathResult getResult() {
            return result;
        }

        public int getStart() {
            return start;
        }

        public int getDestination() {
            return destination;
        }
    }
}
