package ui.draw;

import ui.Theme;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;


public class StashCanvas extends JPanel implements MissionCanvas {

    private final MatrixPanel matrixPanel = new MatrixPanel();
    private final StashGraphCanvas graphCanvas = new StashGraphCanvas();

    public StashCanvas() {
        super(new BorderLayout());
        setBackground(Theme.PANEL);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setBackground(Theme.PANEL);
        pestanas.setForeground(Theme.TEXTO);
        pestanas.setFont(Theme.NORMAL);
        pestanas.addTab("Matriz", matrixPanel);
        pestanas.addTab("Grafo (ruta / ciclo)", graphCanvas);

        add(pestanas, BorderLayout.CENTER);
    }

    @Override
    public void setDrawing(Object data) {
        matrixPanel.setDrawing(data);
        graphCanvas.setDrawing(data);
    }

    @Override
    public void limpiar() {
        matrixPanel.limpiar();
        graphCanvas.limpiar();
    }
}