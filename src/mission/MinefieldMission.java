package mission;

import algoritmos.search.BreadthFirstSearch;
import algoritmos.search.IterativeDepthFirstSearch;
import core.GridMap;
import core.PathResult;
import io.InputFormatException;
import io.MinefieldReader;
import io.TokenStream;

/**
 * Mision 1: Rescatar a NINA del campo minado.
 *
 * Esta clase es solo para coordinar. Llama al parser, llama a los algoritmos
 * y convierte los numeros que devuelven en las lineas de texto.
 */

public class MinefieldMission implements Mission {
    private static final String SAMPLE =
            "10 10\n" +
                    "9\n" +
                    "0 1 2\n" +
                    "1 1 2\n" +
                    "2 2 2 9\n" +
                    "3 2 1 7\n" +
                    "5 3 3 6 9\n" +
                    "6 4 0 1 2 7\n" +
                    "7 3 0 3 8\n" +
                    "8 2 7 9\n" +
                    "9 3 2 3 4\n" +
                    "0 0\n" +
                    "9 9\n" +
                    "0 0\n";
    @Override
    public String getName() {
        return "Mision 1: Rescatar a NINA del campo minado.";
    }
    @Override
    public String getSampleInput() {
        return SAMPLE;
    }

    @Override
    public MissionOutcome solve(String input) {

        TokenStream in = new TokenStream(input); // partimos el texto en tokens
        MissionOutcome outcome = new MissionOutcome();

        int caseNumber = 1; // el enunciado dice que k empieza en 1

        while (true) {

            GridMap grid = MinefieldReader.readCase(in);// leemos UN caso

            if (grid == null) {  // null significa que llego el "0 0" final
                break;           // y ese caso NO se procesa
            }

            // Los dos algoritmos recorren EL MISMO grafo. Lo unico que cambia es
            // el orden en que expanden la frontera, y por eso dan numeros distintos.
            PathResult bfs = BreadthFirstSearch.solve(grid);
            PathResult dfs = IterativeDepthFirstSearch.solve(grid);

            String line;

            if (bfs.isNoRoute()) {
                // Mensaje exacto del enunciado. Se compara caracter por caracter:
                // sin tildes, sin emojis, sin punto final.
                line = "Case #" + caseNumber + ": Nina is unreachable";
            } else {
                line = "Case #" + caseNumber + ": BFS " + bfs.getMove()
                        + " DFS " + dfs.getMove();
            }

            // Guardamos la linea y, junto a ella, lo que GridCanvas necesita
            // para pintar este caso: el mapa y los dos caminos.
            outcome.addCase(line, new MinefieldDrawing(grid, bfs, dfs));

            caseNumber++;                          // siguiente caso
        }

        if (outcome.getCaseCount() == 0) {
            // No se leyo ningun caso valido: se lo avisamos al usuario en la ventana.
            throw new InputFormatException(
                    "No se encontro ningun caso de prueba. Recuerde que la entrada "
                            + "debe terminar con una linea \"0 0\".");
        }

        return outcome;
    }

    /**
     * Datos de dibujo de un caso de la Mision 1.
     * Es una clase interna estatica porque solo tiene sentido para esta mision:
     * GridCanvas recibe un objeto de este tipo y pinta la grilla, las minas
     * y los dos caminos superpuestos.
     */
    public static class MinefieldDrawing {

        private final GridMap grid;   // el mapa con sus bombas
        private final PathResult bfs; // camino minimo, para pintar en un color
        private final PathResult dfs; // camino del DFS, para pintar en otro

        public MinefieldDrawing(GridMap grid, PathResult bfs, PathResult dfs) {
            this.grid = grid;
            this.bfs = bfs;
            this.dfs = dfs;
        }

        public GridMap getGrid() {
            return grid;
        }

        public PathResult getBfs() {
            return bfs;
        }

        public PathResult getDfs() {
            return dfs;
        }
    }
}

