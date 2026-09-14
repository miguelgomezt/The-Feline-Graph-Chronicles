package ui.draw;

import java.awt.geom.Point2D;

public final class GraphLayout {

    private GraphLayout() {
    }

    public static Point2D.Double[] circular(int nodeCount, double centerX, double centerY, double radio) {
        Point2D.Double[] positions = new Point2D.Double[nodeCount];

        if (nodeCount == 1) {
            positions[0] = new Point2D.Double(centerX, centerY);
            return positions;
        }

        for (int i = 0; i < nodeCount; i++) {
            double angulo = (2 * Math.PI * i) / nodeCount - (Math.PI / 2);
            double x = centerX + radio * Math.cos(angulo);
            double y = centerY + radio * Math.sin(angulo);
            positions[i] = new Point2D.Double(x, y);
        }
        return positions;
    }
}
