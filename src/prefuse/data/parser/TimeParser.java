package prefuse.data.parser;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * DataParser instance that parses Date values as java.util.Time instances,
 * representing a particular time (but no specific date). This class uses a
 * backing {@link java.text.DateFormat} instance to perform parsing. The
 * DateFormat instance to use can be passed in to the constructor, or by default
 * the DateFormat returned by {@link java.text.DateFormat#getTimeInstance(int)}
 * with an argument of {@link java.text.DateFormat#SHORT} is used.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TimeParser extends DateParser {

    /**
     * Create a new TimeParser.
     */
    public TimeParser() {
        this(new DateFormat[]{
            new SimpleDateFormat("KK:mm a"),
            new SimpleDateFormat("HH:mm")
        });
    }

    /**
     * Create a new TimeParser.
     *
     * @param dateFormat the DateFormat instance to use for parsing
     */
    public TimeParser(DateFormat[] dateFormats) {
        super(dateFormats);
    }

    /**
     * Returns java.sql.Time.class.
     *
     * @see prefuse.data.parser.DataParser#getType()
     */
    public Class getType() {
        return Time.class;
    }

    /**
     * @see prefuse.data.parser.DataParser#canParse(java.lang.String)
     */
    public boolean canParse(String val) {
        try {
            parseTime(val);
            return true;
        } catch (DataParseException e) {
            return false;
        }
    }

    /**
     * @see prefuse.data.parser.DataParser#parse(java.lang.String)
     */
    public Object parse(String val) throws DataParseException {
        return parseTime(val);
    }

    /**
     * Parse a Time value from a text string.
     *
     * @param text the text string to parse
     * @return the parsed Time value
     * @throws DataParseException if an error occurs during parsing
     */
    public Time parseTime(String text) throws DataParseException {
        m_pos.setErrorIndex(0);
        m_pos.setIndex(0);

        // parse the data value, convert to the wrapper type
        Time t = null;
        try {
            t = Time.valueOf(text);
            m_pos.setIndex(text.length());
        } catch (IllegalArgumentException e) {
            t = null;
        }
        if (t == null) {
            // interate through all known time formats and try to parse
            for (int i = 0; i < m_dfmt.length; i++) {
                DateFormat dateFormat = m_dfmt[i];
                java.util.Date d1 = dateFormat.parse(text, m_pos);
                if (d1 != null) {
                    t = new Time(d1.getTime());
                    break;
                }
            }
        }

        // date format will parse substrings successfully, so we need
        // to check the position to make sure the whole value was used
        if (t == null || m_pos.getIndex() < text.length()) {
            throw new DataParseException("Could not parse Date: " + text);
        } else {
            return t;
        }
    }
} // end of class TimeParser
