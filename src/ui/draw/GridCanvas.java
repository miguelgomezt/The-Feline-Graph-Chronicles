package ui.draw;

import core.GridMap;
import core.PathResult;
import mission.MinefieldMission;
import ui.Theme;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GridCanvas extends JPanel implements MissionCanvas {

    private static final int LIMITE = 50;

    private MinefieldMission.MinefieldDrawing dibujo;

    public GridCanvas() {
        setBackground(Theme.PANEL);
        setPreferredSize(new Dimension(420, 420));
    }

    public void setDrawing(Object data) {
        if (data instanceof MinefieldMission.MinefieldDrawing) {
            this.dibujo = (MinefieldMission.MinefieldDrawing) data;
        } else {
            this.dibujo = null;
        }
        repaint();
    }

    public void limpiar() {
        this.dibujo = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (dibujo == null) {
            mensaje(g2, "Resuelva la mision para ver el mapa");
            return;
        }

        GridMap grid = dibujo.getGrid();

        if (grid.getRows() > LIMITE || grid.getCols() > LIMITE) {
            mensaje(g2, "Dibujo omitido: el mapa es de " + grid.getRows() + " x "
                    + grid.getCols() + ", mayor que " + LIMITE + " x " + LIMITE);
            return;
        }

        int margen = 12;
        int anchoDisponible = getWidth() - 2 * margen;
        int altoDisponible = getHeight() - 2 * margen;
        int lado = Math.min(anchoDisponible / grid.getCols(), altoDisponible / grid.getRows());
        if (lado < 1) {
            return;
        }

        int x0 = (getWidth() - lado * grid.getCols()) / 2;
        int y0 = (getHeight() - lado * grid.getRows()) / 2;

        for (int fila = 0; fila < grid.getRows(); fila++) {
            for (int col = 0; col < grid.getCols(); col++) {
                int x = x0 + col * lado;
                int y = y0 + fila * lado;
                if (grid.hasBomb(grid.toIndex(fila, col))) {
                    g2.setColor(Theme.MINA);
                } else {
                    g2.setColor(Theme.FONDO);
                }
                g2.fillRect(x, y, lado, lado);
                g2.setColor(Theme.BORDE);
                g2.drawRect(x, y, lado, lado);
            }
        }

        pintarCamino(g2, grid, dibujo.getDfs(), Theme.DFS, x0, y0, lado, lado / 3);
        pintarCamino(g2, grid, dibujo.getBfs(), Theme.HEROINAS, x0, y0, lado, lado / 2);

        marcarCelda(g2, grid, grid.getStartIndex(), "S", Theme.HEROINAS, x0, y0, lado);
        marcarCelda(g2, grid, grid.getTargetIndex(), "N", Theme.NINA, x0, y0, lado);
    }

    private void pintarCamino(Graphics2D g2, GridMap grid, PathResult camino,
                              Color color, int x0, int y0, int lado, int grosor) {
        if (camino == null || camino.isNoRoute() || camino.getPath() == null) {
            return;
        }
        if (grosor < 2) {
            grosor = 2;
        }
        g2.setColor(color);
        int[] path = camino.getPath();
        for (int i = 0; i < path.length; i++) {
            int fila = grid.rowOf(path[i]);
            int col = grid.colOf(path[i]);
            int centro = (lado - grosor) / 2;
            g2.fillRect(x0 + col * lado + centro, y0 + fila * lado + centro, grosor, grosor);
        }
    }

    private void marcarCelda(Graphics2D g2, GridMap grid, int indice, String letra,
                             Color color, int x0, int y0, int lado) {
        int fila = grid.rowOf(indice);
        int col = grid.colOf(indice);
        g2.setColor(color);
        g2.fillRect(x0 + col * lado + 1, y0 + fila * lado + 1, lado - 1, lado - 1);
        if (lado >= 12) {
            g2.setColor(Theme.FONDO);
            g2.setFont(Theme.NORMAL);
            int ancho = g2.getFontMetrics().stringWidth(letra);
            g2.drawString(letra, x0 + col * lado + (lado - ancho) / 2,
                    y0 + fila * lado + lado - (lado - g2.getFontMetrics().getAscent()) / 2 - 2);
        }
    }

    private void mensaje(Graphics2D g2, String texto) {
        g2.setColor(Theme.TEXTO_TENUE);
        g2.setFont(Theme.NORMAL);
        int ancho = g2.getFontMetrics().stringWidth(texto);
        g2.drawString(texto, (getWidth() - ancho) / 2, getHeight() / 2);
    }
}
