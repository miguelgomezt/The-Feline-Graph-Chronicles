package ui.draw;

import core.Edge;
import core.Graph;
import core.MstResult;
import mission.AccountsMission;
import mission.NetworkMission;
import ui.Theme;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

public class GraphCanvas extends JPanel implements MissionCanvas {

    private static final double RADIO_NODO = 10;

    private Object dibujo;

    public GraphCanvas() {
        setBackground(Theme.PANEL);
        setPreferredSize(new Dimension(420, 420));
    }

    public void setDrawing(Object data) {
        this.dibujo = data;
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
            mensaje(g2, "Resuelva la mision para ver el grafo");
            return;
        }

        if (dibujo instanceof AccountsMission.AccountsDrawing) {
            pintarAccounts(g2, (AccountsMission.AccountsDrawing) dibujo);
        } else if (dibujo instanceof NetworkMission.NetworkDrawing) {
            pintarNetwork(g2, (NetworkMission.NetworkDrawing) dibujo);
        } else {
            mensaje(g2, "No se sabe dibujar este tipo de caso.");
        }
    }

    private void pintarAccounts(Graphics2D g2, AccountsMission.AccountsDrawing dibujo) {
        Graph graph = dibujo.getGraph();
        int n = graph.getNodeCount();

        if (DrawingLimits.exceedsShortestPathLimit(n)) {
            mensaje(g2, DrawingLimits.shortestPathOmittedMessage(n));
            return;
        }

        Point2D.Double[] positions = layoutFor(n);
        Set<Long> resaltadas = edgeKeysOfPath(dibujo.getResult().getPath());

        dibujarAristas(g2, graph, positions, resaltadas);
        dibujarNodos(g2, positions, n, dibujo.getStart(), dibujo.getDestination());
    }

    private void pintarNetwork(Graphics2D g2, NetworkMission.NetworkDrawing dibujo) {
        Graph graph = dibujo.getGraph();
        int n = graph.getNodeCount();
        int cantidadAristas = graph.getEdgeCount();

        if (DrawingLimits.exceedsNetworkLimit(n, cantidadAristas)) {
            mensaje(g2, DrawingLimits.networkOmittedMessage(n, cantidadAristas));
            return;
        }

        Point2D.Double[] positions = layoutFor(n);

        Set<Long> resaltadas = new HashSet<Long>();
        MstResult resultado = dibujo.getResult();
        if (resultado.isConnected() && resultado.getEdgesUsed() != null) {
            for (Edge edge : resultado.getEdgesUsed()) {
                resaltadas.add(edgeKey(edge.getFrom(), edge.getTo()));
            }
        }

        dibujarAristas(g2, graph, positions, resaltadas);
        dibujarNodos(g2, positions, n, -1, -1);
    }

    private Point2D.Double[] layoutFor(int nodeCount) {
        int margen = 40;
        double radio = (Math.min(getWidth(), getHeight()) - 2 * margen) / 2.0;
        if (radio < 10) {
            radio = 10;
        }
        return GraphLayout.circular(nodeCount, getWidth() / 2.0, getHeight() / 2.0, radio);
    }

    private void dibujarAristas(Graphics2D g2, Graph graph, Point2D.Double[] positions, Set<Long> resaltadas) {
        for (Edge edge : graph.getAllEdges()) {
            if (edge.getFrom() == edge.getTo()) {
                continue;
            }
            if (!resaltadas.contains(edgeKey(edge.getFrom(), edge.getTo()))) {
                trazarLinea(g2, positions[edge.getFrom()], positions[edge.getTo()], Theme.BORDE, 1.5f);
            }
        }
        for (Edge edge : graph.getAllEdges()) {
            if (edge.getFrom() == edge.getTo()) {
                continue;
            }
            if (resaltadas.contains(edgeKey(edge.getFrom(), edge.getTo()))) {
                trazarLinea(g2, positions[edge.getFrom()], positions[edge.getTo()], Theme.HEROINAS, 3f);
            }
        }
    }

    private void trazarLinea(Graphics2D g2, Point2D.Double a, Point2D.Double b, Color color, float grosor) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(grosor));
        g2.draw(new Line2D.Double(a, b));
    }

    private void dibujarNodos(Graphics2D g2, Point2D.Double[] positions, int n, int start, int destination) {
        g2.setFont(Theme.NORMAL);
        for (int i = 0; i < n; i++) {
            Color relleno = Theme.PANEL;
            if (i == start) {
                relleno = Theme.HEROINAS;
            } else if (i == destination) {
                relleno = Theme.NINA;
            }

            Ellipse2D.Double circulo = new Ellipse2D.Double(
                    positions[i].x - RADIO_NODO, positions[i].y - RADIO_NODO,
                    RADIO_NODO * 2, RADIO_NODO * 2);
            g2.setColor(relleno);
            g2.fill(circulo);
            g2.setColor(Theme.TEXTO_TENUE);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(circulo);

            g2.setColor(Theme.TEXTO);
            String etiqueta = String.valueOf(i);
            int ancho = g2.getFontMetrics().stringWidth(etiqueta);
            g2.drawString(etiqueta, (float) (positions[i].x - ancho / 2.0),
                    (float) (positions[i].y + RADIO_NODO + 14));
        }
    }

    private static long edgeKey(int a, int b) {
        int menor = Math.min(a, b);
        int mayor = Math.max(a, b);
        return ((long) menor << 32) | (mayor & 0xFFFFFFFFL);
    }

    private static Set<Long> edgeKeysOfPath(int[] path) {
        Set<Long> keys = new HashSet<Long>();
        if (path == null) {
            return keys;
        }
        for (int i = 0; i + 1 < path.length; i++) {
            keys.add(edgeKey(path[i], path[i + 1]));
        }
        return keys;
    }

    private void mensaje(Graphics2D g2, String texto) {
        g2.setColor(Theme.TEXTO_TENUE);
        g2.setFont(Theme.NORMAL);
        int ancho = g2.getFontMetrics().stringWidth(texto);
        g2.drawString(texto, (getWidth() - ancho) / 2, getHeight() / 2);
    }
}
