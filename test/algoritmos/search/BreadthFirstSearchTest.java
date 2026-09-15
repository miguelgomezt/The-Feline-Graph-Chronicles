package algoritmos.search;

import core.GridMap;
import core.PathResult;
import io.MinefieldReader;
import io.TokenStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BreadthFirstSearchTest {
    private static final String MUESTRA_ENUNCIADO =
            "10 10\n" + "9\n" + "0 1 2\n" + "1 1 2\n" + "2 2 2 9\n" + "3 2 1 7\n" +
                    "5 3 3 6 9\n" + "6 4 0 1 2 7\n" + "7 3 0 3 8\n" + "8 2 7 9\n" +
                    "9 3 2 3 4\n" + "0 0\n" + "9 9\n" + "0 0\n";

    private GridMap leerPrimerCaso(String entrada) {
        return MinefieldReader.readCase(new TokenStream(entrada));
    }

    @Test
    void laMuestraDelEnunciadoDa18Movimientos() {
        PathResult resultado = BreadthFirstSearch.solve(leerPrimerCaso(MUESTRA_ENUNCIADO));
        assertEquals(18, resultado.getMove());
    }

    @Test
    void inicioIgualADestinoDaCero() {
        PathResult resultado = BreadthFirstSearch.solve(
                leerPrimerCaso("3 3\n0\n1 1\n1 1\n0 0\n"));
        assertEquals(0, resultado.getMove());
    }

    @Test
    void destinoEncerradoPorMinasEsInalcanzable() {
        PathResult resultado = BreadthFirstSearch.solve(
                leerPrimerCaso("3 3\n2\n0 1 1\n1 2 0 2\n0 0\n2 2\n0 0\n"));
        assertTrue(resultado.isNoRoute());
    }

    @Test
    void elCaminoTieneUnaCeldaMasQueLosMovimientos() {
        PathResult resultado = BreadthFirstSearch.solve(leerPrimerCaso(MUESTRA_ENUNCIADO));
        assertEquals(resultado.getMove() + 1, resultado.getPath().length);
    }
}