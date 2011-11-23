/**
 * Copyright (c) 2004-2006 Regents of the University of California.
 * See "license-prefuse.txt" for licensing terms.
 */
package prefuse.demos;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.distortion.Distortion;
import prefuse.action.distortion.FisheyeDistortion;
import prefuse.action.layout.Layout;
import prefuse.controls.AnchorUpdateControl;
import prefuse.controls.ControlAdapter;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
 * <p>A prefuse-based implementation of Fisheye Menus, showcasing the use of
 * visual distortion to provide access to a large number of data items
 * without scrolling.</p>
 * 
 * <p>This implementation is inspired by the Fisheye Menu research conducted
 * by Ben Bederson at the University of Maryland. See the
 * <a href="http://www.cs.umd.edu/hcil/fisheyemenu/">Fisheye Menu project
 * web site</a> for more details.</p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class FisheyeMenu extends Display {

    /** The data group name of menu items. */
    public static final String ITEMS = "items";
    /** The label data field for menu items. */
    public static final String LABEL = "label";
    /** The action data field for menu items. */
    public static final String ACTION = "action";
    
    /**
     * This schema holds the data representation for internal storage of
     * menu items.
     */
    protected static final Schema ITEM_SCHEMA = new Schema();
    static {
        ITEM_SCHEMA.addColumn(LABEL, String.class);
        ITEM_SCHEMA.addColumn(ACTION, ActionListener.class);
    }
    
    private Table m_items = ITEM_SCHEMA.instantiate(); // table of menu items
    
    private double m_maxHeight = 500; // maximum menu height in pixels
    private double m_scale = 7;       // scale parameter for fisheye distortion
    
    /**
     * Create a new, empty FisheyeMenu.
     * @see #addMenuItem(String, javax.swing.Action)
     */
    public FisheyeMenu() {
        super(new Visualization());
        m_vis.addTable(ITEMS, m_items);
        
        // set up the renderer to use
        LabelRenderer renderer = new LabelRenderer(LABEL);
        renderer.setHorizontalPadding(0);
        renderer.setVerticalPadding(1);
        renderer.setHorizontalAlignment(Constants.LEFT);
        m_vis.setRendererFactory(new DefaultRendererFactory(renderer));
        
        // set up this display
        setSize(100,470);
        setHighQuality(true);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,5));
        addControlListener(new ControlAdapter() {
            // dispatch an action event to the menu item
            public void itemClicked(VisualItem item, MouseEvent e) {
                ActionListener al = (ActionListener)item.get(ACTION);
                al.actionPerformed(new ActionEvent(item, e.getID(),
                    "click", e.getWhen(), e.getModifiers()));
            }
        });
        
        // text color function
        // items with the mouse over printed in red, otherwise black
        ColorAction colors = new ColorAction(ITEMS, VisualItem.TEXTCOLOR);
        colors.setDefaultColor(ColorLib.gray(0));
        colors.add("hover()", ColorLib.rgb(255,0,0));
        
        // initial layout and coloring
        ActionList init = new ActionList();
        init.add(new VerticalLineLayout(m_maxHeight));
        init.add(colors);
        init.add(new RepaintAction());
        m_vis.putAction("init", init);

        // fisheye distortion based on the current anchor location
        ActionList distort = new ActionList();
        Distortion feye = new FisheyeDistortion(0,m_scale);
        distort.add(feye);
        distort.add(colors);
        distort.add(new RepaintAction());
        m_vis.putAction("distort", distort);
        
        // update the distortion anchor position to be the current
        // location of the mouse pointer
        addControlListener(new AnchorUpdateControl(feye, "distort"));
    }
    
    /**
     * Adds a menu item to the fisheye menu.
     * @param name the menu label to use
     * @param action the ActionListener to notify when the item is clicked
     * The prefuse VisualItem corresponding to this menu item will
     * be returned by the ActionEvent's getSource() method.
     */
    public void addMenuItem(String name, ActionListener listener) {
        int row = m_items.addRow();
        m_items.set(row, LABEL, name);
        m_items.set(row, ACTION, listener);
    }
    
    /**
     * Run a demonstration of the FisheyeMenu
     */
    public static final void main(String[] argv) {
        // only log warnings
        Logger.getLogger("prefuse").setLevel(Level.WARNING);

        FisheyeMenu fm = demo();
        
        // create and display application window
        JFrame f = new JFrame("p r e f u s e  |  f i s h e y e");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(fm);
        f.pack();
        f.setVisible(true);
    }
    
    public static FisheyeMenu demo() {
        // create a new fisheye menu and populate it
        FisheyeMenu fm = new FisheyeMenu();
        for ( int i=1; i<=72; ++i ) {
            // add menu items that simply print their label when clicked
            fm.addMenuItem(String.valueOf(i), new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("clicked item: "+
                        ((VisualItem)e.getSource()).get(LABEL));
                    System.out.flush();
                }
            });
        }
        fm.getVisualization().run("init");
        return fm;
    }
    
    /**
     * Lines up all VisualItems vertically. Also scales the size such that
     * all items fit within the maximum layout size, and updates the
     * Display to the final computed size.
     */
    public class VerticalLineLayout extends Layout {
        private double m_maxHeight = 600;
        
        public VerticalLineLayout(double maxHeight) {
            m_maxHeight = maxHeight;
        }
        
        public void run(double frac) {
            // first pass
            double w = 0, h = 0;
            Iterator iter = m_vis.items();
            while ( iter.hasNext() ) {
                VisualItem item = (VisualItem)iter.next();
                item.setSize(1.0);
                h += item.getBounds().getHeight();
            }
            double scale = h > m_maxHeight ? m_maxHeight/h : 1.0;
            
            Display d = m_vis.getDisplay(0);
            Insets ins = d.getInsets();
            
            // second pass
            h = ins.top;
            double ih, y=0, x=ins.left;
            iter = m_vis.items();
            while ( iter.hasNext() ) {
                VisualItem item = (VisualItem)iter.next();
                item.setSize(scale); item.setEndSize(scale);
                Rectangle2D b = item.getBounds();
                
                w = Math.max(w, b.getWidth());
                ih = b.getHeight();
                y = h+(ih/2);
                setX(item, null, x);
                setY(item, null, y);
                h += ih;
            }
            
            // set the display size to fit text
            setSize(d, (int)Math.round(2*m_scale*w + ins.left + ins.right),
                       (int)Math.round(h + ins.bottom));
        }
        
        private void setSize(final Display d, final int width, final int height)
        {
        	SwingUtilities.invokeLater(new Runnable() {
        		public void run() {
        			d.setSize(width, height);
        		}
        	});
        }
    } // end of inner class VerticalLineLayout
    
} // end of class FisheyeMenu
