package test.prefuse.data.column;

import junit.framework.Test;
import junit.framework.TestSuite;

public class All_PrefuseDataColumn_Tests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.prefuse.data.column");
        //$JUnit-BEGIN$
        suite.addTestSuite(ExpressionColumnTest.class);
        //$JUnit-END$
        return suite;
    }

}
