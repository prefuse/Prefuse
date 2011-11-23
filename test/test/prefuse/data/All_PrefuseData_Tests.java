package test.prefuse.data;

import junit.framework.Test;
import junit.framework.TestSuite;

public class All_PrefuseData_Tests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.prefuse.data");
        //$JUnit-BEGIN$
        suite.addTestSuite(VisualItemTableTest.class);
        suite.addTestSuite(CascadedTableTest.class);
        suite.addTestSuite(TableTest.class);
        suite.addTestSuite(GraphTest.class);
        suite.addTestSuite(TreeTest.class);
        //$JUnit-END$
        return suite;
    }

}
