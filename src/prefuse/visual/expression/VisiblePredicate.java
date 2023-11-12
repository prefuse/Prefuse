package prefuse.visual.expression;

import java.lang.ref.WeakReference;

import prefuse.data.expression.ColumnExpression;
import prefuse.data.expression.Expression;
import prefuse.data.expression.Function;
import prefuse.data.expression.NotPredicate;
import prefuse.data.expression.Predicate;
import prefuse.visual.VisualItem;

/**
 * Expression that indicates if an item's visible flag is set.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class VisiblePredicate extends ColumnExpression implements Predicate, Function {

    private static WeakReference TRUE_REFERENCE;
    private static WeakReference FALSE_REFERENCE;

    public static synchronized Predicate getTruePredicate() {
        if (TRUE_REFERENCE != null) {
            Object item = TRUE_REFERENCE.get();
            if (item instanceof Predicate) {
                return (Predicate) item;
            }
        }
        VisiblePredicate TRUE = new VisiblePredicate();
        TRUE_REFERENCE = new WeakReference(TRUE);
        return TRUE;
    }

    public static synchronized Predicate getFalsePredicate() {
        if (FALSE_REFERENCE != null) {
            Object item = FALSE_REFERENCE.get();
            if (item instanceof Predicate) {
                return (Predicate) item;
            }
        }
        Predicate FALSE = new NotPredicate(getTruePredicate());
        TRUE_REFERENCE = new WeakReference(FALSE);
        return FALSE;
    }

    /**
     * Create a new VisiblePredicate.
     */
    public VisiblePredicate() {
        super(VisualItem.VISIBLE);
    }

    /**
     * @see prefuse.data.expression.Function#getName()
     */
    public String getName() {
        return "VISIBLE";
    }

    /**
     * @see prefuse.data.expression.Function#addParameter(prefuse.data.expression.Expression)
     */
    public void addParameter(Expression e) {
        throw new IllegalStateException("This function takes 0 parameters");
    }

    /**
     * @see prefuse.data.expression.Function#getParameterCount()
     */
    public int getParameterCount() {
        return 0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName() + "()";
    }

} // end of class VisiblePredicate
