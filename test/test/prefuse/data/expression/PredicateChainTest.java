package test.prefuse.data.expression;

import junit.framework.TestCase;
import prefuse.data.Table;
import prefuse.data.expression.IfExpression;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.util.PredicateChain;
import test.prefuse.data.TableTest;

public class PredicateChainTest extends TestCase {

    private PredicateChain m_chain;
    private Table m_table;
    
    private Predicate p1, p2, p3;
    
    protected void setUp() throws Exception {
        m_table = TableTest.getTestCaseTable();
        
        p1 = (Predicate)ExpressionParser.parse("id=3");
        p2 = (Predicate)ExpressionParser.parse("float<2");
        p3 = (Predicate)ExpressionParser.parse("id>3");
        
        m_chain = new PredicateChain();
        m_chain.add(p1, new Integer(1));
        m_chain.add(p2, new Integer(2));
        m_chain.add(p3, new Integer(3));
    }

    protected void tearDown() throws Exception {
        m_chain = null;
        m_table = null;
        p1 = null;
        p2 = null;
        p3 = null;
    }

    /*
     * Test method for 'prefuse.util.PredicateChain.get(Tuple)'
     */
    public void testGet() {
        assertEquals(new Integer(1), m_chain.get(m_table.getTuple(2)));
        assertEquals(new Integer(2), m_chain.get(m_table.getTuple(0)));
        assertEquals(new Integer(3), m_chain.get(m_table.getTuple(3)));
        assertEquals(null, m_chain.get(m_table.getTuple(1)));
    }

    /*
     * Test method for 'prefuse.util.PredicateChain.add(Predicate, Object)'
     */
    public void testAdd() {
        Predicate p = (Predicate)ExpressionParser.parse("id=2");
        m_chain.add(p, new Integer(4));
        assertEquals(new Integer(4), m_chain.get(m_table.getTuple(1)));
    }

    /*
     * Test method for 'prefuse.util.PredicateChain.remove(Predicate)'
     */
    public void testRemove() {
        assertTrue(m_chain.getExpression() instanceof IfExpression);
        assertTrue(m_chain.remove(p1));
        assertEquals(new Integer(2), m_chain.get(m_table.getTuple(2)));
        
        assertTrue(m_chain.getExpression() instanceof IfExpression);
        assertTrue(m_chain.remove(p2));
        assertEquals(null, m_chain.get(m_table.getTuple(0)));
        assertEquals(null, m_chain.get(m_table.getTuple(1)));
        assertEquals(null, m_chain.get(m_table.getTuple(2)));
        
        assertTrue(m_chain.getExpression() instanceof IfExpression);
        assertTrue(m_chain.remove(p3));
        assertEquals(null, m_chain.get(m_table.getTuple(3)));
        
        assertFalse(m_chain.getExpression() instanceof IfExpression);
        assertFalse(m_chain.remove(p1));
        assertFalse(m_chain.remove(p2));
        assertFalse(m_chain.remove(p3));
    }

    /*
     * Test method for 'prefuse.util.PredicateChain.clear()'
     */
    public void testClear() {
        assertTrue(m_chain.getExpression() instanceof IfExpression);
        m_chain.clear();
        assertEquals(null, m_chain.get(m_table.getTuple(0)));
        assertEquals(null, m_chain.get(m_table.getTuple(1)));
        assertEquals(null, m_chain.get(m_table.getTuple(2)));
        assertEquals(null, m_chain.get(m_table.getTuple(3)));
        assertFalse(m_chain.getExpression() instanceof IfExpression);
        assertFalse(m_chain.remove(p1));
        assertFalse(m_chain.remove(p2));
        assertFalse(m_chain.remove(p3));
    }

    public void testRemove2() {
        PredicateChain pc = new PredicateChain();
        Predicate p1 = (Predicate) ExpressionParser.parse("_fixed");
        Predicate p2 = (Predicate) ExpressionParser.parse("_highlight");
        pc.add(p1, new Integer(1));
        pc.add(p2, new Integer(2));
        assertTrue(pc.remove(p2));
    }
    
}
