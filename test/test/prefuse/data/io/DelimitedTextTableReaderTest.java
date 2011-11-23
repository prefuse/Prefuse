package test.prefuse.data.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;
import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.data.io.TableReader;
import test.prefuse.TestConfig;
import test.prefuse.data.TableTestData;

public class DelimitedTextTableReaderTest extends TestCase implements TableTestData {
 
    public void testReadTableInputStream() {
        // prepare data
        byte[] data = TAB_DELIMITED_DATA.getBytes();
        InputStream is = new ByteArrayInputStream(data);
        
        // parse data
        TableReader ctr = new DelimitedTextTableReader();
        Table t = null;
        try {
            t = ctr.readTable(is);
        } catch ( DataIOException e ) {
            e.printStackTrace();
            fail("Data Read Exception");
        }
        
        boolean verbose = TestConfig.verbose();
        
        // text-dump
        if (verbose) System.out.println("** TAB DELIMITED DATA TEST **");
        if (verbose) System.out.println("-- Data Types -------------");
        for (int c = 0, idx = -1; c < t.getColumnCount(); ++c) {
            String name = t.getColumnType(c).getName();
            if ( (idx=name.lastIndexOf('.')) >= 0 )
                name = name.substring(idx+1);
            assertEquals(t.getColumnType(c), TYPES[c]);
            if (verbose) System.out.print(name + "\t");
        }
        if (verbose) System.out.println();
        
        if (verbose) System.out.println();
        
        if (verbose) System.out.println("-- Table Data -------------");
        for (int c = 0; c < t.getColumnCount(); ++c) {
            if (verbose) System.out.print(t.getColumnName(c) + "\t");
            assertEquals(t.getColumnName(c), HEADERS[c]);
        }
        if (verbose) System.out.println();
        for (int r = 0; r < t.getRowCount(); ++r) {
            for (int c = 0; c < t.getColumnCount(); ++c) {
                Object o = t.get(r, c);
                if (verbose) System.out.print(o + "\t");
                assertEquals(TABLE[c][r], o);
            }
            if (verbose) System.out.println();
        }
        if (verbose) System.out.println();
        
//        // interface
//        JFrame f = new JFrame("CSV Loader Test");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JTable jt = new JTable(t) {
//            TableCellRenderer defr = new DefaultTableCellRenderer();
//            public TableCellRenderer getCellRenderer(int r, int c) {
//                return defr;
//            }
//        };
//        JScrollPane jsp = new JScrollPane(jt);
//        f.getContentPane().add(jsp);
//        f.pack();
//        f.setVisible(true);
    }

}
