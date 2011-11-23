package test.prefuse.data;

import java.net.URL;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import junit.framework.TestCase;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Tree;
import prefuse.data.io.TreeMLReader;
import prefuse.data.util.TreeNodeIterator;
import prefuse.demos.TreeMap;
import prefuse.util.GraphLib;
import prefuse.util.ui.JPrefuseTable;

public class TreeTest extends TestCase {

    public static final String TREE_CHI = "/chi-ontology.xml.gz";
    
    public void testTreeReader() {
        // load tree
        URL url = TreeMap.class.getResource(TREE_CHI);
        Tree t = null;
        try {
            GZIPInputStream gzin = new GZIPInputStream(url.openStream());
            t = (Tree) new TreeMLReader().readGraph(gzin);
        } catch ( Exception e ) {
            e.printStackTrace();
            fail();
        }
        
        assertEquals(true, t.isValidTree());
        
        Node[] nodelist = new Node[t.getNodeCount()];
        
        Iterator nodes = t.nodes();
        for ( int i=0; nodes.hasNext(); ++i ) {
            nodelist[i] = (Node)nodes.next();
        }
        assertEquals(false, nodes.hasNext());
    }
    
    public void testAddChild() {
        Tree tree = GraphLib.getBalancedTree(2,1);
        Node r = tree.getRoot();
        Node n = tree.addChild(r);
        assertEquals(true, n!=null);
        n.setString("label", "new node");
        assertEquals(r.getLastChild(), n);
    }
    
    public void testRemoveChild() {
        Tree tree = GraphLib.getBalancedTree(2,1);
        int size = tree.getNodeCount();
        Node r = tree.getRoot();
        Node c = r.getFirstChild();
        Edge e = c.getParentEdge();
        
        assertEquals(true, tree.removeChild(c));
        assertEquals(tree.getNodeCount(), size-1);
        assertEquals(false, c.isValid());
        assertEquals(false, e.isValid());
        assertEquals(true, r.getFirstChild() != c);
    }
    
    public void testRemoveSubtree() {
        Tree tree = GraphLib.getBalancedTree(3,3);
        int size = tree.getNodeCount();
        Node r = tree.getRoot();
        Node c = r.getFirstChild();
        
        Node[] nodes = new Node[13];
        Edge[] edges = new Edge[13];
        Iterator iter = new TreeNodeIterator(c);
        for ( int i=0; iter.hasNext(); ++i ) {
            nodes[i] = (Node)iter.next();
            edges[i] = (Edge)nodes[i].getParentEdge();
        }
        
        assertEquals(true, tree.removeChild(c));
        assertEquals(tree.getNodeCount(), size-13);
        assertEquals(true, r.getFirstChild() != c);
        for ( int i=0; i<nodes.length; ++i ) {
            assertEquals(false, nodes[i].isValid());
            assertEquals(false, edges[i].isValid());
        }
        
        assertEquals(true, tree.isValidTree());
    }
    
    public static void main(String[] argv) {
        URL url = TreeMap.class.getResource(TREE_CHI);
        Tree t = null;
        try {
            GZIPInputStream gzin = new GZIPInputStream(url.openStream());
            t = (Tree) new TreeMLReader().readGraph(gzin);
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }
        
        JPrefuseTable table = new JPrefuseTable(t.getEdgeTable());
        JFrame frame = new JFrame("edges");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(table));
        frame.pack();
        frame.setVisible(true);
    }
    
}
