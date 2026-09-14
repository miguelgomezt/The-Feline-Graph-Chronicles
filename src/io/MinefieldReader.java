package io;

import core.GridMap;

/**
 * Parser para la mision 1: Convierte el texto pegado en GridMap.
 * Formato de UN caso de prueba, segun el enunciado:
 * R C                      -> filas y columnas (1..1000 cada uno)
 * filasConBombas           -> cuantas filas contienen bombas (0..R)
 * fila cantidad c1 c2 ...  -> una linea por cada fila con bombas
 * filaInicio colInicio     -> donde estan Pola y Minerva
 * filaDestino colDestino   -> donde esta Nina.
 *
 * La entrada termina cuando R = 0 y C = 0.
 */
public class MinefieldReader {

    public static GridMap readCase(TokenStream in){
        int rows = in.nextInt(); //Cantidad de filas
        int cols = in.nextInt(); //Cantidad de columnas
        if(rows == 0 &&  cols == 0){
            return null;
        }

        //validamos los limites.
        if(rows < 1 || rows > 1000 || cols < 1 || cols > 1000){
            throw new InputFormatException("Las divisiones del mapa deben estar entre 1 y 1000");
        }

        boolean[] bomb = new boolean[rows*cols];

        //Cuantas filas traen bombas.
        int rowsWithBombs = in.nextIntInRange(0, rows, "La cantidad de filas con bombas. ");

        //Por cada fila: Numero de fila, cuantas bombas y las columnas.
        for (int i = 0; i < rowsWithBombs; i++){
            int row = in.nextIntInRange(0, rows-1, "El numero de fila de una bomba.");
            int bombCount = in.nextIntInRange(0, cols, "La cantidad de bombas de una fila.");

            for (int j = 0; j < bombCount; j++){
                int col = in.nextIntInRange(0, cols-1, "La columna de una bomba.");
                bomb[row*cols + col] = true; //Celda minada
            }
        }

        //Posicion de inicio: Fila y Columna de Pola y Minerva.
        int startRow = in.nextIntInRange(0, rows-1, "La fila del inicio. ");
        int startCol =  in.nextIntInRange(0, cols-1, "La columna del inicio.");


        //Posicion de destino: Fila y Columan donde esta NINA.
        int targetRow = in.nextIntInRange(0, rows-1, "La fila del destino.");
        int targetCol = in.nextIntInRange(0, cols-1, "La columna del destino.");

        //Convertimos los pares fila columna a los indices que usa GridMap.
        int startIndex = startRow*cols + startCol;
        int targetIndex = targetRow*cols + targetCol;

        return new GridMap(rows, cols, bomb, startIndex, targetIndex);
    }
}
