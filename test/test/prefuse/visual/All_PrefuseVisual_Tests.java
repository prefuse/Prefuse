package test.prefuse.visual;

import junit.framework.Test;
import junit.framework.TestSuite;

public class All_PrefuseVisual_Tests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.prefuse.visual");
        //$JUnit-BEGIN$
        suite.addTestSuite(VisualizationTest.class);
        suite.addTestSuite(VisualAggregateTableTest.class);
        //$JUnit-END$
        return suite;
    }

}
