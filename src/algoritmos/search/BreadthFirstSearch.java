package algoritmos.search;

import core.GridMap;

import java.lang.annotation.Target;
import java.util.Arrays;

/**
 * Mision 1: Busqueda de anchura (BFS) sobre la grilla.
 * QUE HACE: Encuentra el caminos MAS CORTO
 * desde el inicio hasta Nina, moviendose
 * en 4 direcciones sin pisar bombas.
 */


public class BreadthFirstSearch {
    private static final int[] DELTA_ROW = {-1, 1, 0, 0};
    private static final int[] DELTA_COL = {0, 0, -1, 1};

    public static PathResult solve(GridMap grid) {
        int start = grid.getStartIndex(); //Celda donde arrancan Pola y Minerva
        int target = grid.getTargetIndex(); //Celda donde esta NINA


        /*
        Si el inicio o el fin tienen bomba,
        el destino se considera inalcanzable
         */
        if (grid.hasBomb(start) || grid.hasBomb(target)) {
            return PathResult.noRoute();
        }

        /*
        Si el inicio y destino son la misma celda
        son 0 movimientos para llegar a donde esta NINA
         */
        if (start == target) {
            return new PathResult(0, new int[]{start});
        }

        int total = grid.getCellCount();
        int[] queue = new int[total]; //La cola guarda los indices
        int head = 0; //de aqui se saca (frente)
        int tail = 0; //aqui se mete (final)

        boolean[] visited = new boolean[total]; // la celda i ya entro en la cola
        int[] distances = new int[total]; //movimientos desde el inicio
        int[] parent =  new int[total]; //desde donde llegue a i

        //Encolamos la celda inicial: distancia 0 y sin padre(-1)
        queue[tail] = start;
        tail++;
        visited[start] = true;
        distances[start] = 0;
        parent[start] = -1;

        while (head < total) {
            int cur = queue[head];
            head++;

            int row = grid.rowOf[cur];
            int col = grid.colOf[cur];

            for (int d = 0; d < 4; d++){
                int newRow = row + DELTA_ROW[d];
                int newCol = col + DELTA_COL[d];

                if(!grid.canEnter(newRow, newCol)){
                    continue;
                }

                int neighbor = grid.toIndex(newRow, newCol);
                if(visited[neighbor]){
                   continue;
                }

                visited[neighbor] = true;
                distances[neighbor] = distances[cur] + 1;
                parent[cur] = neighbor;

                //Si el vecino NINA terminamos: No hace falta seguir
                if(neighbor == target){
                    return new PathResult(distances[target], buildPath(parent,target));
                }
                queue[tail] = neighbor; //Lo encolamos
                tail++;
            }
        }
        //La cola se vacio.
        return PathResult.noRoute();
    }

    /*
    Reconstruimos el camino hacia atras por el
    arreglo de padres, desde el destino hasta el
    inicio, y lo deja en orden inicio -> destino
     */

    private static int[] buildPath(int[] parent, int target){
        int lenght = 0;  //Contamos las celdas que tiene
        int cursor = target;
        while (cursor != -1){ //-1 es el padre del inicio, es decir ahi paramos
            lenght++;
            cursor = parent[cursor];
        }

        int[] path = new int[lenght]; //Tomamos el tamaño exacto
        cursor = target;
        for(int i = lenght - 1; i >= 0; i--){ //Lo llenamos del final al principio
            path[i] = cursor;
            cursor = parent[cursor];
        }
        return path;
    }
}
