package ui.draw;

import core.Edge;
import core.Graph;
import mission.StashMission;
import ui.Theme;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StashGraphCanvas extends JPanel implements MissionCanvas {

    private static final double RADIO_NODO = 10;
    private static final double SEPARACION_PARALELA = 10;

    private Object dibujo;

    public StashGraphCanvas() {
        setBackground(Theme.PANEL);
        setPreferredSize(new Dimension(420, 420));
    }

    @Override
    public void setDrawing(Object data) {
        this.dibujo = data;
        repaint();
    }

    @Override
    public void limpiar() {
        this.dibujo = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!(dibujo instanceof StashMission.StashDrawing)) {
            mensaje(g2, "Resuelva la mision para ver el grafo");
            return;
        }

        StashMission.StashDrawing datos = (StashMission.StashDrawing) dibujo;
        Graph graph = datos.getGraph();
        int n = graph.getNodeCount();

        if (DrawingLimits.exceedsShortestPathLimit(n)) {
            mensaje(g2, DrawingLimits.shortestPathOmittedMessage(n));
            return;
        }

        Point2D.Double[] positions = layoutFor(n);

        Set<String> resaltadas = new HashSet<String>();
        Color colorResaltado = Theme.HEROINAS;
        if (datos.getCycle() != null) {
            agregarSecuencia(resaltadas, datos.getCycle());
            colorResaltado = Theme.LIMON;
        } else if (datos.getRoute() != null) {
            agregarSecuencia(resaltadas, datos.getRoute());
        }

        dibujarAristas(g2, graph, positions, resaltadas, colorResaltado);
        dibujarNodos(g2, positions, n, datos.getSource(), datos.getDestination());
    }

    private Point2D.Double[] layoutFor(int nodeCount) {
        int margen = 40;
        double radio = (Math.min(getWidth(), getHeight()) - 2 * margen) / 2.0;
        if (radio < 10) {
            radio = 10;
        }
        return GraphLayout.circular(nodeCount, getWidth() / 2.0, getHeight() / 2.0, radio);
    }

    private static void agregarSecuencia(Set<String> resaltadas, int[] secuencia) {
        for (int i = 0; i + 1 < secuencia.length; i++) {
            resaltadas.add(edgeKey(secuencia[i], secuencia[i + 1]));
        }
    }

    private void dibujarAristas(Graphics2D g2, Graph graph, Point2D.Double[] positions,
                                Set<String> resaltadas, Color colorResaltado) {

        Map<String, Integer> vistos = new HashMap<String, Integer>();


        for (Edge edge : graph.getAllEdges()) {
            if (!resaltadas.contains(edgeKey(edge.getFrom(), edge.getTo()))) {
                dibujarUnaArista(g2, positions, edge, Theme.BORDE, 1.5f, vistos);
            }
        }
        for (Edge edge : graph.getAllEdges()) {
            if (resaltadas.contains(edgeKey(edge.getFrom(), edge.getTo()))) {
                dibujarUnaArista(g2, positions, edge, colorResaltado, 3f, vistos);
            }
        }
    }

    private void dibujarUnaArista(Graphics2D g2, Point2D.Double[] positions, Edge edge,
                                  Color color, float grosor, Map<String, Integer> vistos) {
        int from = edge.getFrom();
        int to = edge.getTo();

        if (from == to) {
            dibujarBucle(g2, positions[from], color, grosor, String.valueOf(edge.getWeight()));
            return;
        }

        String parKey = parKey(from, to);
        int indice = vistos.getOrDefault(parKey, 0);
        vistos.put(parKey, indice + 1);
        // La primera arista entre estos dos nodos (en cualquier
        // sentido) va recta; si ya hay otra, esta se separa un poco
        // para que 1 -> 2 y 2 -> 1 no queden pintadas una sobre otra.
        double lado = (from < to) ? 1 : -1;
        double desplazamiento = (indice == 0) ? 0 : SEPARACION_PARALELA * lado;

        Point2D.Double[] desplazadas = desplazar(positions[from], positions[to], desplazamiento);
        Point2D.Double aOffset = desplazadas[0];
        Point2D.Double bOffset = desplazadas[1];

        Point2D.Double[] recortados = recortarEnBorde(aOffset, bOffset);

        g2.setColor(color);
        g2.setStroke(new BasicStroke(grosor));
        g2.draw(new Line2D.Double(recortados[0], recortados[1]));
        dibujarFlecha(g2, recortados[0], recortados[1], color);

        etiquetarPeso(g2, aOffset, bOffset, String.valueOf(edge.getWeight()));
    }

    /** Desplaza el segmento A-B perpendicularmente, para separar dos
     * aristas que van entre los mismos dos nodos en sentidos opuestos. */
    private static Point2D.Double[] desplazar(Point2D.Double a, Point2D.Double b, double cantidad) {
        if (cantidad == 0) {
            return new Point2D.Double[]{a, b};
        }
        double dx = b.x - a.x;
        double dy = b.y - a.y;
        double largo = Math.hypot(dx, dy);
        if (largo == 0) {
            return new Point2D.Double[]{a, b};
        }
        double px = -dy / largo * cantidad;
        double py = dx / largo * cantidad;
        return new Point2D.Double[]{
                new Point2D.Double(a.x + px, a.y + py),
                new Point2D.Double(b.x + px, b.y + py)
        };
    }


    private static Point2D.Double[] recortarEnBorde(Point2D.Double a, Point2D.Double b) {
        double dx = b.x - a.x;
        double dy = b.y - a.y;
        double largo = Math.hypot(dx, dy);
        if (largo == 0) {
            return new Point2D.Double[]{a, b};
        }
        double ux = dx / largo;
        double uy = dy / largo;
        Point2D.Double inicio = new Point2D.Double(a.x + ux * RADIO_NODO, a.y + uy * RADIO_NODO);
        Point2D.Double fin = new Point2D.Double(b.x - ux * RADIO_NODO, b.y - uy * RADIO_NODO);
        return new Point2D.Double[]{inicio, fin};
    }

    private static void dibujarFlecha(Graphics2D g2, Point2D.Double desde, Point2D.Double hasta, Color color) {
        double dx = hasta.x - desde.x;
        double dy = hasta.y - desde.y;
        double angulo = Math.atan2(dy, dx);
        double largoFlecha = 9;
        double aberturaFlecha = Math.toRadians(24);

        GeneralPath flecha = new GeneralPath();
        flecha.moveTo(hasta.x, hasta.y);
        flecha.lineTo(hasta.x - largoFlecha * Math.cos(angulo - aberturaFlecha),
                hasta.y - largoFlecha * Math.sin(angulo - aberturaFlecha));
        flecha.lineTo(hasta.x - largoFlecha * Math.cos(angulo + aberturaFlecha),
                hasta.y - largoFlecha * Math.sin(angulo + aberturaFlecha));
        flecha.closePath();

        g2.setColor(color);
        g2.fill(flecha);
    }


    private void dibujarBucle(Graphics2D g2, Point2D.Double centro, Color color, float grosor, String peso) {
        double radioLazo = 16;
        double cx = centro.x;
        double cy = centro.y - RADIO_NODO - radioLazo;

        Ellipse2D.Double lazo = new Ellipse2D.Double(cx - radioLazo, cy - radioLazo, radioLazo * 2, radioLazo * 2);
        g2.setColor(color);
        g2.setStroke(new BasicStroke(grosor));
        g2.draw(lazo);

        Point2D.Double puntaDesde = new Point2D.Double(cx + radioLazo * 0.55, cy + radioLazo * 0.75);
        Point2D.Double puntaHasta = new Point2D.Double(cx + radioLazo * 0.15, cy + radioLazo * 0.98);
        dibujarFlecha(g2, puntaDesde, puntaHasta, color);

        g2.setColor(Theme.TEXTO_TENUE);
        g2.setFont(Theme.NORMAL);
        int ancho = g2.getFontMetrics().stringWidth(peso);
        g2.drawString(peso, (float) (cx - ancho / 2.0), (float) (cy - radioLazo - 4));
    }

    private static void etiquetarPeso(Graphics2D g2, Point2D.Double a, Point2D.Double b, String peso) {
        double mx = (a.x + b.x) / 2.0;
        double my = (a.y + b.y) / 2.0;
        g2.setColor(Theme.TEXTO_TENUE);
        g2.setFont(Theme.NORMAL);
        int ancho = g2.getFontMetrics().stringWidth(peso);
        g2.drawString(peso, (float) (mx - ancho / 2.0), (float) (my - 4));
    }

    private void dibujarNodos(Graphics2D g2, Point2D.Double[] positions, int n, int source, int destination) {
        g2.setFont(Theme.NORMAL);
        for (int i = 0; i < n; i++) {
            Color relleno = Theme.PANEL;
            if (i == source) {
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

    private static String edgeKey(int from, int to) {
        return from + "->" + to;
    }

    private static String parKey(int a, int b) {
        int menor = Math.min(a, b);
        int mayor = Math.max(a, b);
        return menor + ":" + mayor;
    }

    private void mensaje(Graphics2D g2, String texto) {
        g2.setColor(Theme.TEXTO_TENUE);
        g2.setFont(Theme.NORMAL);
        int ancho = g2.getFontMetrics().stringWidth(texto);
        g2.drawString(texto, (getWidth() - ancho) / 2, getHeight() / 2);
    }
}