package test.prefuse.data.expression;

import junit.framework.Test;
import junit.framework.TestSuite;

public class All_PrefuseDataExpression_Tests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.prefuse.data.expression");
        //$JUnit-BEGIN$
        suite.addTestSuite(ExpressionTest.class);
        suite.addTestSuite(PredicateChainTest.class);
        //$JUnit-END$
        return suite;
    }

}
