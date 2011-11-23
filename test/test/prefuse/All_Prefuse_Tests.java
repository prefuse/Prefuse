package test.prefuse;

import junit.framework.Test;
import junit.framework.TestSuite;

public class All_Prefuse_Tests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.prefuse");
        //$JUnit-BEGIN$
        suite.addTest(test.prefuse.data.All_PrefuseData_Tests.suite());
        suite.addTest(test.prefuse.data.column.All_PrefuseDataColumn_Tests.suite());
        suite.addTest(test.prefuse.data.expression.All_PrefuseDataExpression_Tests.suite());
        suite.addTest(test.prefuse.data.io.All_PrefuseDataIO_Tests.suite());
        suite.addTest(test.prefuse.data.util.All_PrefuseDataUtil_Tests.suite());
        suite.addTest(test.prefuse.visual.All_PrefuseVisual_Tests.suite());
        //$JUnit-END$
        return suite;
    }

}
