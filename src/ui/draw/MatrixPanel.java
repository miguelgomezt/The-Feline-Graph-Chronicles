package ui.draw;

import core.Sentinels;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;


public class MatrixPanel extends JPanel {

    public MatrixPanel() {
        super(new BorderLayout());
    }

    public MatrixPanel(long[][] matrix) {
        this();
        setMatrix(matrix);
    }

     public void setMatrix(long[][] matrix) {
        removeAll();

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

        add(new JScrollPane(table), BorderLayout.CENTER);

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