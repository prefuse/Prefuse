package prefuse.demos.applets;

import prefuse.data.Table;
import prefuse.data.io.DataIOException;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.util.ui.JPrefuseApplet;


public class ZipDecode extends JPrefuseApplet {

    public void init() {
        DelimitedTextTableReader tr = new DelimitedTextTableReader();
        Table t = null;
        try {
            t = tr.readTable("/zipcode.txt");        
        } catch ( DataIOException e ) {
            e.printStackTrace();
            System.exit(1);
        }
        this.setContentPane(new prefuse.demos.ZipDecode(t));
    }
    
} // end of class ZipDecode
