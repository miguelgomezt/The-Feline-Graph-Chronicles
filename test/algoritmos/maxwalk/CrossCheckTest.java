package algoritmos.maxwalk;

import core.Sentinels;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CrossCheckTest {

    @Test
    void coincidenCuandoLosValoresSonIguales() {
        long[][] matrix = {{0, 110}, {Sentinels.NO_ROUTE, 0}};
        BellmanFord.Result bellmanFord = new BellmanFord.Result(
                new long[]{0, 110}, new boolean[]{false, false});

        CrossCheck.Result result = CrossCheck.compare(matrix, bellmanFord, 0, 1);
        assertTrue(result.agree());
    }

    @Test
    void detectaLaDiscrepancia() {

        long[][] matrix = {{0, 110}, {Sentinels.NO_ROUTE, 0}};

        BellmanFord.Result bellmanFord = new BellmanFord.Result(
                new long[]{0, Sentinels.UNBOUNDED}, new boolean[]{false, true});

        CrossCheck.Result result = CrossCheck.compare(matrix, bellmanFord, 0, 1);
        assertFalse(result.agree());
        assertNotNull(result.mismatchMessage(0, 1));
    }
}