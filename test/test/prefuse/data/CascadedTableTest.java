package test.prefuse.data;

import junit.framework.TestCase;
import prefuse.data.CascadedTable;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.AbstractPredicate;
import prefuse.data.expression.AndPredicate;
import prefuse.data.expression.ArithmeticExpression;
import prefuse.data.expression.ColumnExpression;
import prefuse.data.expression.ComparisonPredicate;
import prefuse.data.expression.Expression;
import prefuse.data.expression.NumericLiteral;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.RangePredicate;
import prefuse.data.io.DelimitedTextTableWriter;
import prefuse.data.util.TableIterator;
import test.prefuse.TestConfig;

public class CascadedTableTest extends TestCase implements TableTestData {

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
    
    public void testCascadedTableRangeFilters() {
        double lo = 1;
        double hi = 20;

        // index the double column
        t.index(HEADERS[4]);
        
        // where id > 0 && dub > 1 && dub < 20
        ColumnExpression id = new ColumnExpression(HEADERS[0]);
        ColumnExpression dub = new ColumnExpression(HEADERS[4]);
        NumericLiteral x0 = new NumericLiteral(0);
        NumericLiteral xlo = new NumericLiteral(lo);
        NumericLiteral xhi1 = new NumericLiteral(hi-1.0);
        NumericLiteral xhi2 = new NumericLiteral(1.0f);
        Expression xhi = new ArithmeticExpression(
                ArithmeticExpression.ADD, xhi1, xhi2);

        ComparisonPredicate idCmp = new ComparisonPredicate(
                ComparisonPredicate.GTEQ, id, x0);
        RangePredicate dubRg = new RangePredicate(RangePredicate.IN_EX, dub,
                xlo, xhi);
        AndPredicate filter = new AndPredicate(idCmp, dubRg);

        if ( TestConfig.verbose() ) {
            System.out.println(filter.toString());
        }
        
        CascadedTable ft = new CascadedTable(t, filter);

        TableIterator iter = ft.iterator();
        while ( iter.hasNext() ) {
            int row = iter.nextInt();
            try {
                for (int c = 0; c < NCOLS; ++c) {
                    Object o1 = TABLE[c][ft.getColumnRow(row,c)];
                    Object o2 = ft.get(row,HEADERS[c]);
                    assertEquals(o1, o2);
                }
            } catch (Exception e) {
                fail("Caught exception: " + e);
            }
        }

        // add an extra column to the cascaded table
        String name = "test";
        ft.addColumn(name, double.class, new Double(Math.PI));
        iter = ft.iterator();
        while (iter.hasNext()) {
            int row = iter.nextInt();
            iter.setDouble(name, Math.E);
            assertTrue(Math.E == ft.getDouble(row, name));
        }

        int nr = t.addRow();
        t.setInt(nr, HEADERS[0], nr+1);
        t.setDouble(nr, HEADERS[4], 2.5);

        if ( TestConfig.verbose() ) {
            try {
                new DelimitedTextTableWriter().writeTable(ft, System.out);
            } catch ( Exception e ) { e.printStackTrace(); }
        }
    }
    
    /*
     * Test method for
     * 'prefuse.data.CascadedTable.CascadedTable(Table,RowFilter)'
     */
    public void testCascadedTableTableRowFilter() {
        final float thresh = 5.0f;
        
        Predicate p = new AbstractPredicate() {
            public boolean getBoolean(Tuple t) {
                return t.getFloat(HEADERS[3]) < thresh;
            }
        };
        
        CascadedTable ft = new CascadedTable(t, p);
        
        for ( int i=0, r=0; i<NROWS; ++i ) {
            float val = ((Float)TABLE[3][i]).floatValue();
            if ( val < thresh ) {
                try {
                    for ( int c=0; c<NCOLS; ++c ) {
                        Object o1 = TABLE[c][i];
                        Object o2 = ft.get(r, HEADERS[c]);
                        assertEquals(o1, o2);
                    }
                    ++r;
                } catch ( Exception e ) {
                    fail("Caught exception: "+e);
                }
            }
        }
        
        // add an extra column to the cascaded table
        String name = "test";
        ft.addColumn(name, double.class, new Double(Math.PI));
        TableIterator iter = ft.iterator();
        while ( iter.hasNext() ) {
            int row = iter.nextInt();
            iter.setDouble(name, Math.E);
            assertTrue(Math.E == ft.getDouble(row, name));
        }
        
        int nr = t.addRow();
        t.setFloat(nr, HEADERS[3], 0.5f);
        t.setInt(nr, HEADERS[0], 77);
        nr = t.addRow();
        t.setFloat(nr, HEADERS[3], 0.5f);
        t.setInt(nr, HEADERS[0], 99);
        
        if ( TestConfig.verbose() ) {
            try {
                new DelimitedTextTableWriter().writeTable(ft, System.out);
            } catch ( Exception e ) { e.printStackTrace(); }
        }
    }

}
