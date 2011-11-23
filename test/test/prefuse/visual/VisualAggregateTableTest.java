package test.prefuse.visual;

import java.util.Iterator;

import junit.framework.TestCase;
import prefuse.Visualization;
import prefuse.visual.AggregateTable;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;
import test.prefuse.data.TableTest;

public class VisualAggregateTableTest extends TestCase {

    private AggregateTable m_agg;
    private VisualTable m_items;
    
    protected void setUp() throws Exception {
        Visualization v = new Visualization();
        m_items = v.addTable("items", TableTest.getTestCaseTable());
        
        m_agg = v.addAggregates("aggregates", VisualItem.SCHEMA);
        m_agg.addRow();
        m_agg.addRow();
        
        Iterator iter = m_items.tuples();
        for ( int i=0, count=m_items.getRowCount(); iter.hasNext(); ++i ) {
            VisualItem item = (VisualItem)iter.next();
            int j = i<count/2 ? 0 : 1;
            m_agg.addToAggregate(j, item);
        }
    }

    protected void tearDown() throws Exception {
        m_items = null;
        m_agg = null;
    }

    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.getAggregateSize(int)'
     */
    public void testGetAggregateSize() {
        int cc = m_items.getRowCount();
        int s1 = cc/2, s2 = cc-s1;
        assertEquals(s1, m_agg.getAggregateSize(0));
        assertEquals(s2, m_agg.getAggregateSize(1));
    }

    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.addToAggregate(int, Tuple)'
     */
    public void testAddToAggregate() {
        VisualItem t = m_items.getItem(0);
        int size = m_agg.getAggregateSize(1);
        assertFalse(m_agg.aggregateContains(1, t));
        m_agg.addToAggregate(1, t);
        assertTrue(m_agg.aggregateContains(1, t));
        assertEquals(size+1, m_agg.getAggregateSize(1));
    }

    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.removeFromAggregate(int, Tuple)'
     */
    public void testRemoveFromAggregate() {
        int s = m_agg.getAggregateSize(0);
        
        assertTrue(m_agg.aggregateContains(0, m_items.getItem(0)));
        m_agg.removeFromAggregate(0, m_items.getItem(0));
        assertFalse(m_agg.aggregateContains(0, m_items.getItem(0)));
        assertEquals(--s, m_agg.getAggregateSize(0));
        
        assertTrue(m_agg.aggregateContains(0, m_items.getItem(1)));
        m_agg.removeFromAggregate(0, m_items.getItem(1));
        assertFalse(m_agg.aggregateContains(0, m_items.getItem(1)));
        assertEquals(--s, m_agg.getAggregateSize(0));
    }

    public void testRemoveFromAggregateUnderIteration() {
        int s = m_agg.getAggregateSize(0);
        Iterator iter = m_agg.aggregatedTuples(0);
        while ( iter.hasNext() ) {
            VisualItem t = (VisualItem)iter.next();
            assertTrue(m_agg.aggregateContains(0, t));
            m_agg.removeFromAggregate(0, t);
            assertEquals(--s, m_agg.getAggregateSize(0));
            assertFalse(m_agg.aggregateContains(0, t));
        }
    }
    
    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.removeAllFromAggregate(int)'
     */
    public void testRemoveAllFromAggregate() {
        m_agg.removeAllFromAggregate(0);
        m_agg.removeAllFromAggregate(1);
        assertEquals(0, m_agg.getAggregateSize(0));
        assertEquals(0, m_agg.getAggregateSize(1));
    }

    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.aggregateContains(int, Tuple)'
     */
    public void testAggregateContains() {
        VisualItem vi0 = m_items.getItem(0);
        VisualItem vi1 = m_items.getItem(m_items.getRowCount()-1);
        assertTrue(m_agg.aggregateContains(0, vi0));
        assertTrue(m_agg.aggregateContains(1, vi1));
    }

    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.aggregatedTuples(int)'
     */
    public void testAggregatedTuples() {
        int s = m_agg.getAggregateSize(0);
        Iterator iter = m_agg.aggregatedTuples(0);
        int count = 0;
        for ( ; iter.hasNext(); ++count ) {
            VisualItem t = (VisualItem)iter.next();
            assertTrue(m_agg.aggregateContains(0, t));
        }
        assertEquals(s, count);
    }
    
    /*
     * Test method for 'prefuse.data.tuple.AggregateTable.getAggregates(Tuple)'
     */
    public void testGetAggregates() {
        for ( int i=0; i<2; ++i ) {
            Iterator iter = m_agg.aggregatedTuples(0);
            while ( iter.hasNext() ) {
                VisualItem t = (VisualItem)iter.next();
                Iterator aggr = m_agg.getAggregates(t);
                assertEquals(m_agg.getTuple(0), aggr.next());
            }
        }
    }

}
