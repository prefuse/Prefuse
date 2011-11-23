package test.prefuse.data;

import junit.framework.TestCase;
import prefuse.Visualization;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.AbstractPredicate;
import prefuse.data.expression.Predicate;
import prefuse.data.io.DelimitedTextTableWriter;
import prefuse.data.util.TableIterator;
import prefuse.visual.VisualTable;
import test.prefuse.TestConfig;

public class VisualItemTableTest extends TestCase implements TableTestData {

    Table t;
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        t = TableTest.getTestCaseTable();
    }
    
    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        t = null;
    }
    
    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.VisualItemTable.VisualItemTable(Table, RowFilter)'
     */
    public void testVisualItemTableTableRowFilter() {
        final float thresh = 5.0f;
        
        Predicate p = new AbstractPredicate() {
            public boolean getBoolean(Tuple t) {
                return t.getFloat(HEADERS[3]) < thresh;
            }
        };
        
        Visualization vis = new Visualization();
        VisualTable vt = new VisualTable(t, vis, "data", p);
        
        for ( int i=0, r=0; i<NROWS; ++i ) {
            float val = ((Float)TABLE[3][i]).floatValue();
            if ( val < thresh ) {
                try {
                    for ( int c=0; c<NCOLS; ++c ) {
                        Object o1 = TABLE[c][i];
                        Object o2 = vt.get(r, HEADERS[c]);
                        assertEquals(o1, o2);
                    }
                    ++r;
                } catch ( Exception e ) {
                    fail("Caught exception: "+e);
                }
            }
        }
        
        // add an extra column to the filtered table
        String name = "test";
        vt.addColumn(name, double.class, new Double(Math.PI));
        TableIterator iter = vt.iterator();
        while ( iter.hasNext() ) {
            int row = iter.nextInt();
            iter.setDouble(name, Math.E);
            assertTrue(Math.E == vt.getDouble(row, name));
        }
        
        int nr = t.addRow();
        t.setFloat(nr, HEADERS[3], 0.5f);
        
        if ( TestConfig.verbose() ) {
            try {
                new DelimitedTextTableWriter().writeTable(vt, System.out);
            } catch ( Exception e ) { e.printStackTrace(); }
        }
    }

}
