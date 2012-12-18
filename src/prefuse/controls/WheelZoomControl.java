package prefuse.controls;

import java.awt.Point;
import java.awt.event.MouseWheelEvent;

import prefuse.Display;
import prefuse.visual.VisualItem;

/**
 * Zooms the display using the mouse scroll wheel, changing the scale of the
 * viewable region.
 *
 * @author Kevin Krumwiede
 * @author bobruney
 * @author mathis ahrens
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class WheelZoomControl extends AbstractZoomControl {
    
    private Point m_point = new Point();
    private final boolean inverted;
    private final boolean atPointer;
    
    /**
     * Creates a new <tt>WheelZoomControl</tt>.  If <tt>inverted</tt> is true,
     * scrolling the mouse wheel toward you will make the graph appear
     * smaller.  If <tt>atPointer</tt> is true, zooming will be centered on
     * the mouse pointer instead of the center of the display.
     * 
     * @param inverted true if the scroll direction should be inverted
     * @param atPointer true if zooming should be centered on the mouse pointer
     */
    public WheelZoomControl(boolean inverted, boolean atPointer) {
    	this.inverted = inverted;
    	this.atPointer = atPointer;
    }
    
    /**
     * Creates a new <tt>WheelZoomControl</tt> with the default zoom direction
     * and zooming on the center of the display.
     */
    public WheelZoomControl() {
    	this(false, false);
    }
    
    /**
     * @see prefuse.controls.Control#itemWheelMoved(prefuse.visual.VisualItem, java.awt.event.MouseWheelEvent)
     */
    public void itemWheelMoved(VisualItem item, MouseWheelEvent e) {
        if ( m_zoomOverItem )
            mouseWheelMoved(e);
    }
    
    /**
     * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        Display display = (Display)e.getComponent();
        if(atPointer) m_point = e.getPoint();
        else {
        	m_point.x = display.getWidth()/2;
        	m_point.y = display.getHeight()/2;
        }
        if(inverted) zoom(display, m_point, 1 - 0.1f * e.getWheelRotation(), false);
        else zoom(display, m_point, 1 + 0.1f * e.getWheelRotation(), false);
    }
    
} // end of class WheelZoomControl
