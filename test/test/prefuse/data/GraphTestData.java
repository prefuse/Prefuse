package test.prefuse.data;

/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface GraphTestData {

    public static final int NNODES = 5;
    public static final int NNODECOLS = 2;
    public static final int NEDGES = 7;
    public static final int NEDGECOLS = 3;
    
    // -- Nodes ---------------------------------------------------------------
    
    public static final Class[] NTYPES = 
        { int.class, String.class };
    
    public static final String[] NHEADERS =
        { "id", "label" };
    
    public static final Integer[] NCOLUMN1 =
        { new Integer(1), new Integer(20), new Integer(300), 
          new Integer(4000), new Integer(5) };
    
    public static final String[] NCOLUMN2 =
        { "A", "B", "C", "D", "E" };
    
    public static final Object[][] NODES =
        { NCOLUMN1, NCOLUMN2 };
    
    
    // -- Edges ---------------------------------------------------------------
    
    public static final Class[] ETYPES =
        { int.class, int.class, double.class };
    
    public static final String[] EHEADERS =
        { "id1", "id2", "weight" };
    
    public static final Integer[] ECOLUMN1 =
        { new Integer(1), new Integer(20), new Integer(300), new Integer(300),
          new Integer(4000), new Integer(5), new Integer(5) };
    
    public static final Integer[] ECOLUMN2 =
        { new Integer(20), new Integer(300), new Integer(1), new Integer(4000),
          new Integer(5), new Integer(300), new Integer(20) };
    
    public static final Double[] ECOLUMN3 =
        { new Double(1.0), new Double(0.5), new Double(0.5), new Double(0.5),
          new Double(1.0), new Double(0.5), new Double(0.25) };
    
    public static final Object[][] EDGES =
        { ECOLUMN1, ECOLUMN2, ECOLUMN3 };

    
    // -- Test Data -----------------------------------------------------------
    
    public static int[] INDEGREE =
        { 1, 2, 2, 1, 1 };
    
    public static int[] OUTDEGREE =
        { 1, 1, 2, 1, 2 };
}
