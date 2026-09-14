package ui;

import io.InputFormatException;
import mission.Mission;
import mission.MissionOutcome;
import ui.draw.GridCanvas;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MissionPanel extends JPanel {

    private final Mission mission;
    private final JTextArea areaEntrada = new JTextArea();
    private final JTextArea areaSalida = new JTextArea();
    private final JLabel etiquetaEstado = new JLabel(" ");
    private final JComboBox<String> selectorCaso = new JComboBox<String>();
    private final GridCanvas canvas = new GridCanvas();
    private MissionOutcome ultimoResultado;

    public MissionPanel(Mission mission) {
        this.mission = mission;
        setLayout(new BorderLayout(8, 8));
        setBackground(Theme.FONDO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(construirBarraSuperior(), BorderLayout.NORTH);

        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                construirLadoTexto(), construirLadoDibujo());
        division.setResizeWeight(0.55);
        division.setBorder(null);
        division.setBackground(Theme.FONDO);
        add(division, BorderLayout.CENTER);

        etiquetaEstado.setFont(Theme.NORMAL);
        etiquetaEstado.setForeground(Theme.TEXTO_TENUE);
        add(etiquetaEstado, BorderLayout.SOUTH);
    }

    private JPanel construirBarraSuperior() {
        JPanel barra = new JPanel();
        barra.setLayout(new BoxLayout(barra, BoxLayout.X_AXIS));
        barra.setBackground(Theme.FONDO);

        JButton botonResolver = new JButton("Resolver");
        JButton botonEjemplo = new JButton("Cargar ejemplo");
        JButton botonLimpiar = new JButton("Limpiar");

        estilizar(botonResolver);
        estilizar(botonEjemplo);
        estilizar(botonLimpiar);

        botonResolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resolver();
            }
        });
        botonEjemplo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                areaEntrada.setText(mission.getSampleInput());
                estado("Ejemplo cargado. Pulse Resolver.", Theme.TEXTO_TENUE);
            }
        });
        botonLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });

        barra.add(botonResolver);
        barra.add(Box.createHorizontalStrut(8));
        barra.add(botonEjemplo);
        barra.add(Box.createHorizontalStrut(8));
        barra.add(botonLimpiar);
        barra.add(Box.createHorizontalGlue());

        JLabel etiquetaCaso = new JLabel("Caso a dibujar: ");
        etiquetaCaso.setFont(Theme.NORMAL);
        etiquetaCaso.setForeground(Theme.TEXTO_TENUE);
        barra.add(etiquetaCaso);

        selectorCaso.setFont(Theme.NORMAL);
        selectorCaso.setMaximumSize(new Dimension(140, 26));
        selectorCaso.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarCasoSeleccionado();
            }
        });
        barra.add(selectorCaso);

        barra.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return barra;
    }

    private JSplitPane construirLadoTexto() {
        areaEntrada.setFont(Theme.MONO);
        areaEntrada.setBackground(Theme.PANEL);
        areaEntrada.setForeground(Theme.TEXTO);
        areaEntrada.setCaretColor(Theme.TEXTO);

        areaSalida.setFont(Theme.MONO);
        areaSalida.setEditable(false);
        areaSalida.setBackground(Theme.PANEL);
        areaSalida.setForeground(Theme.HEROINAS);

        JSplitPane division = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                conTitulo("Entrada", areaEntrada),
                conTitulo("Salida", areaSalida));
        division.setResizeWeight(0.6);
        division.setBorder(null);
        return division;
    }

    private JPanel construirLadoDibujo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.FONDO);
        panel.add(tituloDe("Visualizacion"), BorderLayout.NORTH);
        panel.add(canvas, BorderLayout.CENTER);
        panel.add(construirLeyenda(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirLeyenda() {
        JPanel leyenda = new JPanel();
        leyenda.setLayout(new BoxLayout(leyenda, BoxLayout.X_AXIS));
        leyenda.setBackground(Theme.FONDO);
        leyenda.setBorder(BorderFactory.createEmptyBorder(6, 4, 0, 4));
        leyenda.add(itemLeyenda("Camino BFS", Theme.HEROINAS));
        leyenda.add(Box.createHorizontalStrut(12));
        leyenda.add(itemLeyenda("Camino DFS", Theme.DFS));
        leyenda.add(Box.createHorizontalStrut(12));
        leyenda.add(itemLeyenda("Mina", Theme.MINA));
        leyenda.add(Box.createHorizontalGlue());
        return leyenda;
    }

    private JLabel itemLeyenda(String texto, java.awt.Color color) {
        JLabel etiqueta = new JLabel("■ " + texto);
        etiqueta.setFont(Theme.NORMAL);
        etiqueta.setForeground(color);
        return etiqueta;
    }

    private JPanel conTitulo(String titulo, JTextArea area) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.FONDO);
        panel.add(tituloDe(titulo), BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDE));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JLabel tituloDe(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(Theme.NORMAL);
        etiqueta.setForeground(Theme.TEXTO_TENUE);
        etiqueta.setBorder(BorderFactory.createEmptyBorder(0, 2, 4, 0));
        return etiqueta;
    }

    private void estilizar(JButton boton) {
        boton.setFont(Theme.NORMAL);
        boton.setFocusPainted(false);
    }

    private void resolver() {
        try {
            ultimoResultado = mission.solve(areaEntrada.getText());
            areaSalida.setForeground(Theme.HEROINAS);
            areaSalida.setText(ultimoResultado.getFullOutput());
            llenarSelectorCasos(ultimoResultado.getCaseCount());
            mostrarCasoSeleccionado();
            estado("Resueltos " + ultimoResultado.getCaseCount() + " caso(s).", Theme.TEXTO_TENUE);
        } catch (InputFormatException ex) {
            ultimoResultado = null;
            canvas.limpiar();
            selectorCaso.removeAllItems();
            areaSalida.setForeground(Theme.MINA);
            areaSalida.setText("Error en la entrada:\n" + ex.getMessage());
            estado(ex.getMessage(), Theme.MINA);
        } catch (RuntimeException ex) {
            ultimoResultado = null;
            canvas.limpiar();
            selectorCaso.removeAllItems();
            areaSalida.setForeground(Theme.MINA);
            areaSalida.setText("No se pudo resolver la mision.\nRevise el formato de la entrada.");
            estado("No se pudo resolver la mision.", Theme.MINA);
        }
    }

    private void limpiar() {
        areaEntrada.setText("");
        areaSalida.setText("");
        selectorCaso.removeAllItems();
        canvas.limpiar();
        ultimoResultado = null;
        estado(" ", Theme.TEXTO_TENUE);
    }

    private void llenarSelectorCasos(int cantidad) {
        selectorCaso.removeAllItems();
        for (int i = 1; i <= cantidad; i++) {
            selectorCaso.addItem("Caso #" + i);
        }
        if (cantidad > 0) {
            selectorCaso.setSelectedIndex(0);
        }
    }

    private void mostrarCasoSeleccionado() {
        if (ultimoResultado == null) {
            return;
        }
        int indice = selectorCaso.getSelectedIndex();
        if (indice < 0 || indice >= ultimoResultado.getCaseCount()) {
            return;
        }
        canvas.setDrawing(ultimoResultado.getDrawings().get(indice));
    }

    private void estado(String texto, java.awt.Color color) {
        etiquetaEstado.setText(texto);
        etiquetaEstado.setForeground(color);
    }
}
