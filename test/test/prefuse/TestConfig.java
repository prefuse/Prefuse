package test.prefuse;

public class TestConfig {
    
    public static boolean verbose() {
        String v = System.getProperty("verbose");
        return ( v != null && v.equalsIgnoreCase("true") );
    }
    
    public static String memUse() {
        // memory usage
        long total = Runtime.getRuntime().totalMemory() / (2<<10);
        long free  = Runtime.getRuntime().freeMemory() / (2<<10);
        long max   = Runtime.getRuntime().maxMemory() / (2<<10);
        return "Memory: " + (total-free) + "K used / "
                          + total + "K avail / "
                          + max + "K max";
    }
    
}
