package prefuse.demos;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.SizeAction;
import prefuse.action.layout.RandomLayout;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.activity.ActivityAdapter;
import prefuse.activity.ActivityListener;
import prefuse.controls.ControlAdapter;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.PrefuseLib;
import prefuse.util.force.DragForce;
import prefuse.util.force.ForceItem;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.util.force.SpringForce;
import prefuse.util.ui.BrowserLauncher;
import prefuse.visual.VisualItem;
import prefuse.visual.sort.ItemSorter;

/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class DataMountain extends Display {
    
    public DataMountain(Table t) {
        super(new Visualization());
        m_vis.addTable("data", t);
        
        LabelRenderer nodeRenderer = new LabelRenderer(null, "image");
        nodeRenderer.setTextField(null);
        nodeRenderer.setVerticalAlignment(Constants.BOTTOM);
        nodeRenderer.setHorizontalPadding(0);
        nodeRenderer.setVerticalPadding(0);
        nodeRenderer.setMaxImageDimensions(100,100);
        
        m_vis.setRendererFactory(new DefaultRendererFactory(nodeRenderer));
        
        ActionList init = new ActionList();
        init.add(new RandomLayout());
        init.add(new DataMountainSizeAction());
        m_vis.putAction("init", init);
        
        ActionList update = new ActionList();
        update.add(new DataMountainSizeAction());
        update.add(new ColorAction("data", VisualItem.STROKECOLOR) {
            public int getColor(VisualItem item) {
                return ColorLib.rgb((item.isHover() ? 255 : 0), 0, 0);
            }
        });
        update.add(new RepaintAction());
        m_vis.putAction("update", update);

        // we run this to make sure the forces are stabilized
        ActionList preforce = new ActionList(1000);
        preforce.add(new DataMountainForceLayout(true));
        m_vis.putAction("preforce", preforce);

        // this will cause docs to move out of the way when dragging
        final ForceDirectedLayout fl = new DataMountainForceLayout(false);
        ActivityListener fReset = new ActivityAdapter() {
            public void activityCancelled(Activity a) {
                fl.reset(); 
             } 
        };
        ActionList forces = new ActionList(Activity.INFINITY);
        forces.add(fl);
        forces.add(update);
        forces.addActivityListener(fReset);
        m_vis.putAction("forces", forces);
        
        setSize(640,450);
        setDamageRedraw(false); // disable due to Java2D image clipping errors
        setBorder(BorderFactory.createEmptyBorder(30,20,5,20));
        setItemSorter(new DataMountainSorter());
        addControlListener(new DataMountainControl());
        
        // pre-load images, otherwise they will be loaded asynchronously
        nodeRenderer.getImageFactory().preloadImages(m_vis.items(),"image");
        
        // initialize and present the interface
        m_vis.run("init");
        m_vis.runAfter("preforce", "update");
        m_vis.run("preforce");
    }

    // ------------------------------------------------------------------------
    
    public static void main(String[] args) {       
        JFrame frame = new JFrame("p r e f u s e  |  d a t a m o u n t a i n");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(demo());
        frame.pack();
        frame.setVisible(true);
    }
    
    public static JComponent demo() {
        String datafile = "/amazon.txt";
        Table data = null;
        try {
            data = (new DelimitedTextTableReader()).readTable(datafile);
            data.addColumn("image","CONCAT('/images/',id,'.01.MZZZZZZZ.jpg')");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return new DataMountain(data);
    }
    
    // ------------------------------------------------------------------------
    
    public static class DataMountainSorter extends ItemSorter {
        public int score(VisualItem item) {
            return (int)(10000*item.getY());
        }
    } // end of inner class DataMountainComparator
    
    public class DataMountainSizeAction extends SizeAction {
        public double getSize(VisualItem item) {
            double y = item.getEndY();
            return 0.2 + y/1400;
        }
    } // end of inner class DataMountainSizeAction
    
    private static final String ANCHORITEM = "_anchorItem";
    private static final Schema ANCHORITEM_SCHEMA = new Schema();
    static {
        ANCHORITEM_SCHEMA.addColumn(ANCHORITEM, ForceItem.class);
    }
    
    public class DataMountainForceLayout extends ForceDirectedLayout {
        
        public DataMountainForceLayout(boolean enforceBounds) {
            super("data",enforceBounds,false);
            
            ForceSimulator fsim = new ForceSimulator();
            fsim.addForce(new NBodyForce(-0.4f, 25f, NBodyForce.DEFAULT_THETA));
            fsim.addForce(new SpringForce(1e-5f,0f));
            fsim.addForce(new DragForce());
            setForceSimulator(fsim);
            
            m_nodeGroup = "data";
            m_edgeGroup = null;
        }
        
        protected float getMassValue(VisualItem n) {
            return n.isHover() ? 5f : 1f;
        }

        public void reset() {
            Iterator iter = m_vis.visibleItems(m_nodeGroup);
            while ( iter.hasNext() ) {
                VisualItem item = (VisualItem)iter.next();
                ForceItem aitem = (ForceItem)item.get(ANCHORITEM);
                if ( aitem != null ) {
                    aitem.location[0] = (float)item.getEndX();
                    aitem.location[1] = (float)item.getEndY();
                }
            }
            super.reset();
        }
        protected void initSimulator(ForceSimulator fsim) {
            // make sure we have force items to work with
            TupleSet t = (TupleSet)m_vis.getGroup(m_group);
            t.addColumns(ANCHORITEM_SCHEMA);
            t.addColumns(FORCEITEM_SCHEMA);
            
            Iterator iter = m_vis.visibleItems(m_nodeGroup);
            while ( iter.hasNext() ) {
                VisualItem item = (VisualItem)iter.next();
                // get force item
                ForceItem fitem = (ForceItem)item.get(FORCEITEM);
                if ( fitem == null ) {
                    fitem = new ForceItem();
                    item.set(FORCEITEM, fitem);
                }
                fitem.location[0] = (float)item.getEndX();
                fitem.location[1] = (float)item.getEndY();
                fitem.mass = getMassValue(item);
                
                // get spring anchor
                ForceItem aitem = (ForceItem)item.get(ANCHORITEM);
                if ( aitem == null ) {
                    aitem = new ForceItem();
                    item.set(ANCHORITEM, aitem);
                    aitem.location[0] = fitem.location[0];
                    aitem.location[1] = fitem.location[1];
                }
                
                fsim.addItem(fitem);
                fsim.addSpring(fitem, aitem, 0);
            }     
        }       
    } // end of inner class DataMountainForceLayout
    
    public class DataMountainControl extends ControlAdapter {
        public static final String URL = "http://www.amazon.com/exec/obidos/tg/detail/-/";
        private VisualItem activeItem;
        private Point2D down = new Point2D.Double();
        private Point2D tmp = new Point2D.Double();
        private boolean wasFixed, dragged;
        private boolean repaint = false;
        
        public void itemEntered(VisualItem item, MouseEvent e) {
            Display d = (Display)e.getSource();
            d.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            d.setToolTipText(item.getString("id"));
            activeItem = item;
            wasFixed = item.isFixed();
        }
        
        public void itemExited(VisualItem item, MouseEvent e) {
            if ( activeItem == item ) {
                activeItem = null;
                item.setFixed(wasFixed);
            }
            Display d = (Display)e.getSource();
            d.setToolTipText(null);
            d.setCursor(Cursor.getDefaultCursor());
        }
        
        public void itemPressed(VisualItem item, MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) return;
            
            // set the focus to the current node
            Visualization vis = item.getVisualization();
            vis.getFocusGroup(Visualization.FOCUS_ITEMS).setTuple(item);
            
            item.setFixed(true);
            dragged = false;
            Display d = (Display)e.getComponent();
            down = d.getAbsoluteCoordinate(e.getPoint(), down);
            
            vis.run("forces");
        }
        
        public void itemReleased(VisualItem item, MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) return;
            if ( dragged ) {
                activeItem = null;
                item.setFixed(wasFixed);
                dragged = false;
            }
            // clear the focus
            Visualization vis = item.getVisualization();
            vis.getFocusGroup(Visualization.FOCUS_ITEMS).clear();

            vis.cancel("forces");
        }
        
        public void itemClicked(VisualItem item, MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) return;
            if ( e.getClickCount() == 2 ) {
                String id = item.getString("id");
                BrowserLauncher.showDocument(URL+id);
            }
        }
        
        public void itemDragged(VisualItem item, MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) return;
            dragged = true;
            Display d = (Display)e.getComponent();
            tmp = d.getAbsoluteCoordinate(e.getPoint(), tmp);
            double dx = tmp.getX()-down.getX();
            double dy = tmp.getY()-down.getY();
            
            PrefuseLib.setX(item, null, item.getX()+dx);
            PrefuseLib.setY(item, null, item.getY()+dy);
            down.setLocation(tmp);
            if ( repaint )
                item.getVisualization().repaint();
        }
    } // end of inner class DataMountainControl
    
} // end of class DataMountain

