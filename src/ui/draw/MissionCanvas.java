package ui.draw;

/**
 * Lo minimo que necesita MissionPanel de cualquier canvas de dibujo
 * (GridCanvas, GraphCanvas, MatrixPanel...): recibir el objeto de
 * dibujo de la mision resuelta, y poder limpiarse cuando no hay nada
 * que mostrar. Cada canvas ya es un JPanel por su cuenta; esta interfaz
 * solo le agrega esas dos operaciones en comun para que MissionPanel
 * pueda trabajar con cualquiera de ellos sin saber cual es cual.
 */
public interface MissionCanvas {
    void setDrawing(Object data);
    void limpiar();
}
