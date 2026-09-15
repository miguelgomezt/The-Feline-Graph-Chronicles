package ui.draw;

import core.Sentinels;
import mission.StashMission;
import ui.Theme;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;


public class MatrixPanel extends JPanel implements MissionCanvas {

    private final JLabel mensajeVacio = new JLabel("Sin datos todavia. Pulse Resolver.");
    private final JLabel avisoDiscrepancia = new JLabel(" ");
    private final JScrollPane scrollPane = new JScrollPane();

    public MatrixPanel() {
        super(new BorderLayout(0, 6));
        setBackground(Theme.PANEL);

        mensajeVacio.setForeground(Theme.TEXTO_TENUE);
        mensajeVacio.setFont(Theme.NORMAL);

        avisoDiscrepancia.setForeground(Theme.MINA);
        avisoDiscrepancia.setFont(Theme.NORMAL);
        avisoDiscrepancia.setVisible(false);

        add(mensajeVacio, BorderLayout.CENTER);
        add(avisoDiscrepancia, BorderLayout.SOUTH);
    }

    @Override
    public void setDrawing(Object data) {
        if (data instanceof StashMission.StashDrawing) {
            mostrar((StashMission.StashDrawing) data);
        } else {
            limpiar();
        }
    }

    @Override
    public void limpiar() {
        removeAll();
        avisoDiscrepancia.setVisible(false);
        add(mensajeVacio, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrar(StashMission.StashDrawing dibujo) {
        removeAll();

        long[][] matrix = dibujo.getMatrix();
        int n = matrix.length;

        String[] columnNames = new String[n + 1];
        columnNames[0] = "";
        for (int j = 0; j < n; j++) {
            columnNames[j + 1] = String.valueOf(j);
        }

        Object[][] rows = new Object[n][n + 1];
        for (int i = 0; i < n; i++) {
            rows[i][0] = String.valueOf(i);
            for (int j = 0; j < n; j++) {
                rows[i][j + 1] = formatCell(matrix[i][j]);
            }
        }

        DefaultTableModel model = new DefaultTableModel(rows, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // la matriz es solo de lectura
            }
        };

        JTable table = new JTable(model);
        table.setEnabled(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFont(Theme.MONO);

        scrollPane.setViewportView(table);
        add(scrollPane, BorderLayout.CENTER);

        if (dibujo.hasMismatch()) {
            avisoDiscrepancia.setText(dibujo.getMismatchWarning());
            avisoDiscrepancia.setVisible(true);
        } else {
            avisoDiscrepancia.setVisible(false);
        }

        revalidate();
        repaint();
    }

    private static String formatCell(long value) {
        if (value == Sentinels.NO_ROUTE) {
            return "-";
        }
        if (value == Sentinels.UNBOUNDED) {
            return "inf";
        }
        return String.valueOf(value);
    }
}