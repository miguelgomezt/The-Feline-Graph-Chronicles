package core;

public class GridMap {
    private final int rows;
    private final int cols;
    private final boolean[] bomb;
    private final int startIndex;
    private final int targetIndex;

    public GridMap(int rows, int cols, boolean[] bomb, int startIndex, int targetIndex) {
        this.rows = rows;
        this.cols = cols;
        this.bomb = bomb;
        this.startIndex = startIndex;
        this.targetIndex = targetIndex;
    }
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }

    public int getCellCount() {
        return rows * cols;
    }
    public int getStartIndex() {
        return startIndex;
    }
    public int getTargetIndex() {
        return targetIndex;
    }

    /** Convierte un par (Fila Columna) en el indice
     * unico de la celda*/
    public int toIndex(int row, int col) {
        return row * cols + col;
    }

    /** Devuelve la fila a la que pertence
     * un indice de celda*/
    public int rowOf(int index){
        return index / cols;
    }

    /**Devuelve la columna a la que pertenece un indice
     * de celda */
    public int colOf(int index){
        return index % cols;
    }

    /**True si una celda tiene bomba*/
    public boolean hasBomb(int index){
        return bomb[index];
    }

    /** True si la fila y columna caen dentro del
     * mapa*/
    public boolean isInside(int row, int col){
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /** true si a la celda se puede entrar, es decir
     * esta dentro del mapa y no tiene bomba.
     * BFS y DFS llaman este metodo siempre, antes de dar cualquier paso.*/
    public boolean canEnter(int row, int col){
        if(!isInside(row, col)){
            return false;
        }
        return !bomb[toIndex(row, col)];
    }
}
