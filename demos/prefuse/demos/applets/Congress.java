package prefuse.demos.applets;

import prefuse.data.Table;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.util.ui.JPrefuseApplet;


public class Congress extends JPrefuseApplet {

    public void init() {
        // load the data
        Table t = null;
        try {
            t = new DelimitedTextTableReader().readTable("/fec.txt");
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(1);
        }
        this.getContentPane().add(new prefuse.demos.Congress(t));
    }
    
} // end of class Congress
