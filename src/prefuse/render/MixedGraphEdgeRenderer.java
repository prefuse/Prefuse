/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prefuse.render;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import prefuse.Constants;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

/**
 * Render which takes into account that an edge can have a
 *
 *
 * @author Sascha
 */
public class MixedGraphEdgeRenderer extends EdgeRenderer {

    final double offsetStrength = 1 / 4;

    public MixedGraphEdgeRenderer(int edgeType) {
        super(edgeType);
    }

    @Override
    protected Shape getRawShape(VisualItem item) {
        Shape rawShape = super.getRawShape(item);
        if (super.getEdgeType() == Constants.EDGE_TYPE_LINE) {
            Line2D line = (Line2D) rawShape;
            EdgeItem edge = (EdgeItem) item;
            NodeItem sourceItem = edge.getSourceItem();
            NodeItem targetItem = edge.getTargetItem();
            VisualGraph graph = (VisualGraph) edge.getGraph();
            EdgeItem opposingEdge = (EdgeItem) graph.getEdge(targetItem, sourceItem);

            if (opposingEdge != null) {
                Rectangle2D sourceNodeBounds = sourceItem.getBounds();
                Rectangle2D targetNodeBounds = targetItem.getBounds();
                double scX = sourceNodeBounds.getCenterX();
                double scY = sourceNodeBounds.getCenterY();
                double tcX = targetNodeBounds.getCenterX();
                double tcY = targetNodeBounds.getCenterY();

                double dX = tcX - scX;
                double dY = tcY - scY;

                double length = Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));

                double nX = -dY / length;
                double nY = dX / length;

                line.setLine(
                        line.getX1() + nX * (sourceNodeBounds.getWidth() / 2) * offsetStrength,
                        line.getY1() + nY * (sourceNodeBounds.getHeight() / 2) * offsetStrength,
                        line.getX2() + nX * (targetNodeBounds.getWidth() / 2) * offsetStrength,
                        line.getY2() + nY * (targetNodeBounds.getHeight() / 2) * offsetStrength);
            }
        }
        return rawShape;
    }
}
