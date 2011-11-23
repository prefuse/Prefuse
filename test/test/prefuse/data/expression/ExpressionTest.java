package test.prefuse.data.expression;

import junit.framework.TestCase;
import prefuse.data.expression.AndPredicate;
import prefuse.data.expression.ArithmeticExpression;
import prefuse.data.expression.ColumnExpression;
import prefuse.data.expression.ComparisonPredicate;
import prefuse.data.expression.Expression;
import prefuse.data.expression.FunctionExpression;
import prefuse.data.expression.IfExpression;
import prefuse.data.expression.ObjectLiteral;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.expression.parser.ParseException;
import prefuse.data.expression.parser.TokenMgrError;

public class ExpressionTest extends TestCase {

    private static String[] expr = {
        "District <= 0",
        "IF x < 0 THEN x^2 ELSE x%0",
        "ABS(3+5*EXP(3)/SIN(5)*SIGN(32.3f)-ROUND(23.2e-1))",
        "x && y",
        "x & y",
        "if x<0 else x^2 then x%0",
        "((3+6)*(4+1))",
        "2*3%6",
        "2+3%6",
        "2*3^6",
        "2 + 3 3 + 4",
        "[One Long Column]",
        "An Invalid Column",
        "\"Double Quoted String\"",
        "'A tab\\t andd\\b a newline \\n'"
    };
    private static Class[] type = {
        ComparisonPredicate.class,
        IfExpression.class,
        FunctionExpression.class,
        AndPredicate.class,
        null,
        null,
        ArithmeticExpression.class,
        ArithmeticExpression.class,
        ArithmeticExpression.class,
        ArithmeticExpression.class,
        null,
        ColumnExpression.class,
        null,
        ObjectLiteral.class,
        ObjectLiteral.class
    };
    
    public void testExpressionParser() {
        for ( int i=0; i<expr.length; ++i ) {
            try {
                Expression e = ExpressionParser.parse(expr[i], true);
                assertEquals(true, type[i]!=null);
                assertEquals(true, type[i].isAssignableFrom(e.getClass()));
            } catch ( TokenMgrError tme ) {
                System.out.println(tme);
                assertEquals(null, type[i]);
            } catch ( ParseException pe ) {
                System.out.println(pe);
                assertEquals(null, type[i]);
            }
        }
    }
    
}
