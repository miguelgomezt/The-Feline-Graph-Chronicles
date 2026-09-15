package ui;

import mission.MinefieldMission;
import mission.Mission;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class ChroniclesWindow extends JFrame {

    private final JTabbedPane pestanas = new JTabbedPane();
    private final List<JLabel> etiquetas = new ArrayList<JLabel>();

    public ChroniclesWindow() {
        setTitle("The Feline Graph Chronicles - Universidad EIA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Theme.FONDO);
        setLayout(new BorderLayout());

        add(construirEncabezado(), BorderLayout.NORTH);

        List<Mission> misiones = new ArrayList<Mission>();
        misiones.add(new MinefieldMission());

        pestanas.setFont(Theme.NORMAL);
        pestanas.setBackground(Theme.FONDO);
        pestanas.setForeground(Theme.TEXTO);
        pestanas.setFocusable(false);
        pestanas.setBorder(BorderFactory.createEmptyBorder(4, 8, 0, 8));

        for (int i = 0; i < misiones.size(); i++) {
            Mission mision = misiones.get(i);
            pestanas.addTab("Mision " + (i + 1), new MissionPanel(mision));
            pestanas.setToolTipTextAt(i, mision.getName());

            JLabel etiqueta = new JLabel("Mision " + (i + 1));
            etiqueta.setFont(Theme.NORMAL);
            etiqueta.setOpaque(true);
            etiqueta.setBorder(BorderFactory.createEmptyBorder(7, 18, 7, 18));
            etiquetas.add(etiqueta);
            pestanas.setTabComponentAt(i, etiqueta);
        }

        pestanas.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                pintarPestanas();
            }
        });
        pintarPestanas();

        add(pestanas, BorderLayout.CENTER);
    }

    private void pintarPestanas() {
        int seleccionada = pestanas.getSelectedIndex();
        for (int i = 0; i < etiquetas.size(); i++) {
            JLabel etiqueta = etiquetas.get(i);
            if (i == seleccionada) {
                etiqueta.setBackground(Theme.HEROINAS);
                etiqueta.setForeground(Theme.FONDO);
            } else {
                etiqueta.setBackground(Theme.PANEL);
                etiqueta.setForeground(Theme.TEXTO_TENUE);
            }
        }
    }

    private JPanel construirEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Theme.FONDO);
        encabezado.setBorder(BorderFactory.createEmptyBorder(14, 16, 6, 16));

        JLabel titulo = new JLabel("The Feline Graph Chronicles");
        titulo.setFont(Theme.TITULO);
        titulo.setForeground(Theme.HEROINAS);

        JLabel subtitulo = new JLabel(
                "Pola y Minerva contra Limon - Lenguajes y Compiladores, Universidad EIA");
        subtitulo.setFont(Theme.NORMAL);
        subtitulo.setForeground(Theme.TEXTO_TENUE);

        encabezado.add(titulo, BorderLayout.NORTH);
        encabezado.add(subtitulo, BorderLayout.SOUTH);
        return encabezado;
    }
}