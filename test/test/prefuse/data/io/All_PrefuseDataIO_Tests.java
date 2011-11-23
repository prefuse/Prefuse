package test.prefuse.data.io;

import junit.framework.Test;
import junit.framework.TestSuite;

public class All_PrefuseDataIO_Tests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.prefuse.data.io");
        //$JUnit-BEGIN$
        suite.addTestSuite(CSVTableReaderTest.class);
        suite.addTestSuite(DelimitedTextTableReaderTest.class);
        //$JUnit-END$
        return suite;
    }

}
