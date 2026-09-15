package algoritmos.search;

import core.GridMap;
import core.PathResult;

/**
 * Mision 1: Busqueda en profundidad DFS Iterativa sobre
 * la grilla.
 *
 * Buscamos un camino valido hasta NINA, no el mas corto
 * , expandiendo los vecinos en el orden fijo:
 * ARRIBA ABAJO IZQUIERDA DERECHA.
 *
 * Es ITERATIVA porque hace hacemos menos llamadas a comparacion
 * si fuera recursiva. La recursion se reemplaza por un ciclo
 * WHILE.
 *
 * El ciclo va al reves, es decir d= 3, 2, 1, 0. Porque
 * en la pila lo ultimo que entra es lo primero que sale.
 *
 * Se usa una PILA (LIFO) hecha con tres arreglos, que avanzan juntos
 * (celda, profundidad y padre).
 */

public class IterativeDepthFirstSearch {
    /** Direcciones que se exigen. ARRIBA, ABAJO,
     * IZQUIERDA; DERECHA.*/
    private static final int[] DELTA_ROW = {-1,1,0,0};
    private static final int[] DELTA_COL = {0,0,-1,1};

    public static PathResult solve(GridMap grid){
        int start = grid.getStartIndex();
        int target = grid.getTargetIndex();

        if (grid.hasBomb(start) || grid.hasBomb(target)){
            return PathResult.noRoute();
        }
        if(start==target){
            return new PathResult(0, new int[] { start, target });
        }

        int total = grid.getCellCount();

        /**
         * LA PILA: Cadad posicion guarda una celda pendiente,
         * com que profundidad llegariamos a ella y desde donde.
         * Cada celda puede ser empujada una vez por cada uno de
         * sus cuatros vecinos.
         */

        int[] stackCell =  new int[4*total];
        int[] stackDepth =  new int[4*total];
        int[] stackParent = new int[4*total];

        int top = 0;

        boolean[]visited = new boolean[total];
        int[] parent = new int[total];

        stackCell[top] = start;
        stackDepth[top] = 0;
        stackParent[top] = -1;
        top++;

        while (top > 0){
            top--; //Sacamos el ultimo que entro.
            int current = stackCell[top];
            int depth = stackDepth[top];
            int from = stackParent[top];

            //Una misma celda pudo entrar varias veces a la pila
            //desde vecinos distintos. Si ya paso, la ignoramos.
            if(visited[current]){
                continue;
            }

            visited[current] = true;
            parent[current] = from;

            /**
             * Si llegamos a NINA, la profundidad es la cantidad
             * de movimientos con la que llegamos.
             */
            if(current==target){
                return new PathResult(depth, buildPath(parent, target));
            }

            int row = grid.rowOf(current); //Lo pasamos fila columna
            int col = grid.colOf(current); //para poder movernos por el tablero.

            /**Aqui esta el truco de el orden, recorremos las
             * direcciones de atras a adelante para hacer su recorrido.
             */

            for (int d = 3; d>= 0; d--){
                int newRow = row + DELTA_ROW[d];
                int newCol = col + DELTA_COL[d];

                if(!grid.canEnter(newRow, newCol)){
                    continue;
                }

                int neighbor = grid.toIndex(newRow, newCol);

                if(visited[neighbor]){
                    continue;
                }

                stackCell[top] = neighbor;
                stackDepth[top] = depth +1;
                stackParent[top] = current;
                top++;
            }
        }
        //No hay cambio, la pila se vacio sin llegar al destino.
        return PathResult.noRoute();
    }

    private static int[] buildPath(int[] parent, int target){
        int lenght = 0;
        int cursor = target;
        while (cursor != -1){
            lenght++;
            cursor = parent[cursor];
        }

        int[] path = new int[lenght];
        cursor = target;
        for(int i = lenght - 1; i >= 0; i--){
            path[i] = cursor;
            cursor = parent[cursor];
        }
        return path;
    }
}
