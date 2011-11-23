package test.prefuse.data;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import prefuse.util.TimeLib;

public interface TableTestData {

    public static final Calendar CAL = GregorianCalendar.getInstance();
    
    public static final int NROWS = 4;
    public static final int NCOLS = 6;
    
    public static final String[]  HEADERS
        = { "id", "date", "time", "float", "double", "text" };
    
    public static final Class[]   TYPES
        = { int.class,   Date.class,   Time.class, 
            float.class, double.class, String.class };
    
    public static final Integer[] COLUMN1 
        = { new Integer(1), new Integer(2), 
            new Integer(3), new Integer(4) };
    
    public static final Date[]    COLUMN2 
        = { new Date(TimeLib.getDate(CAL,2001,0,1)),
            new Date(TimeLib.getDate(CAL,1979,5,15)), 
            new Date(TimeLib.getDate(CAL,1982,2,19)),
            new Date(TimeLib.getDate(CAL,2053,4,13)) };
    
    public static final Time[]    COLUMN3 
        = { new Time(TimeLib.getTime(CAL,1,12,0)),
            new Time(TimeLib.getTime(CAL,3,14,0)), 
            new Time(TimeLib.getTime(CAL,1,12,0)),
            new Time(TimeLib.getTime(CAL,19,12,0)) };
    
    public static final Float[]   COLUMN4 
        = { new Float(1.1f), new Float(2.3f),
            new Float(1e-4f), new Float(1e6f) };
    
    public static final Double[]  COLUMN5
        = { new Double(12.34), new Double(3.3334), 
            new Double(1e4), new Double(1.3e-2) };
    
    public static final String[] COLUMN6
        = { "This is some text.", "13f", "12.3", 
            "This is some \"quoted\", comma'd text" };
    
    
    public static final Object[][] TABLE
        = { COLUMN1, COLUMN2, COLUMN3, COLUMN4, COLUMN5, COLUMN6 };
    
    public static final String CSV_DATA =
        "id,date,time,float,double,text\n" +
        "1,1/1/2001,1:12 AM,1.1f,12.34,This is some text.\n" +
        "2,6/15/1979,3:14 AM,2.3f,3.3334,13f\n" +
        "3,3/19/1982,1:12 AM,1e-4f,1.00E+04,12.3\n" +
        "4,5/13/2053,7:12 PM,1e6f,1.30E-02,\"This is some \"\"quoted\"\", comma'd text\"\n";
    
    public static final String TAB_DELIMITED_DATA =
        "id\tdate\ttime\tfloat\tdouble\ttext\n" +
        "1\t1/1/2001\t1:12 AM\t1.1f\t12.34\tThis is some text.\n" +
        "2\t6/15/1979\t3:14 AM\t2.3f\t3.3334\t13f\n" +
        "3\t3/19/1982\t1:12 AM\t1e-4f\t1.00E+04\t12.3\n" +
        "4\t5/13/2053\t7:12 PM\t1e6f\t1.30E-02\tThis is some \"quoted\", comma'd text\n";
    
}
