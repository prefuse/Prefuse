package test.prefuse.data.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class All_PrefuseDataUtil_Tests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.prefuse.data.util");
        //$JUnit-BEGIN$
        suite.addTestSuite(IntIntTreeMapTest.class);
        suite.addTestSuite(LongIntTreeMapTest.class);
        suite.addTestSuite(FloatIntTreeMapTest.class);
        suite.addTestSuite(DoubleIntTreeMapTest.class);
        suite.addTestSuite(ObjectIntTreeMapTest.class);
        //$JUnit-END$
        return suite;
    }

}
