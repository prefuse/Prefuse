package prefuse.data.column;

import java.util.BitSet;

import prefuse.data.DataReadOnlyException;
import prefuse.data.DataTypeException;
import prefuse.util.TypeLib;

/**
 * Column implementation storing boolean values. Uses a BitSet representation
 * for space efficient storage.
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class BooleanColumn extends AbstractColumn {

    private BitSet m_bits;
    private int m_size;

    /**
     * Create an empty BooleanColumn.
     */
    public BooleanColumn() {
        this(0, 10, false);
    }

    /**
     * Create a new BooleanColumn.
     *
     * @param nrows the initial size of the column
     */
    public BooleanColumn(int nrows) {
        this(nrows, nrows, false);
    }

    /**
     * Create a new BooleanColumn.
     *
     * @param nrows the initial size of the column
     * @param capacity the initial capacity of the column
     * @param defaultValue the default value for the column
     */
    public BooleanColumn(int nrows, int capacity, boolean defaultValue) {
        super(boolean.class, new Boolean(defaultValue));
        if (capacity < nrows) {
            throw new IllegalArgumentException(
                    "Capacity value can not be less than the row count.");
        }
        m_bits = new BitSet(capacity);
        m_bits.set(0, capacity, defaultValue);
        m_size = nrows;
    }

    // ------------------------------------------------------------------------
    // Column Metadata
    /**
     * @see prefuse.data.column.Column#getRowCount()
     */
    public int getRowCount() {
        return m_size;
    }

    /**
     * @see prefuse.data.column.Column#setMaximumRow(int)
     */
    public void setMaximumRow(int nrows) {
        if (nrows > m_size) {
            m_bits.set(m_size, nrows,
                    ((Boolean) m_defaultValue).booleanValue());
        }
        m_size = nrows;
    }

    // ------------------------------------------------------------------------
    // Data Access Methods    
    /**
     * @see prefuse.data.column.Column#get(int)
     */
    public Object get(int row) {
        return new Boolean(getBoolean(row));
    }

    /**
     * @see prefuse.data.column.Column#set(java.lang.Object, int)
     */
    public void set(Object val, int row) throws DataTypeException {
        if (m_readOnly) {
            throw new DataReadOnlyException();
        } else if (val != null) {
            if (val instanceof Boolean) {
                setBoolean(((Boolean) val).booleanValue(), row);
            } else if (val instanceof String) {
                setString((String) val, row);
            } else {
                throw new DataTypeException(val.getClass());
            }
        } else {
            throw new DataTypeException("Column does not accept null values");
        }
    }

    // ------------------------------------------------------------------------
    // Data Type Convenience Methods
    /**
     * @see prefuse.data.column.AbstractColumn#getBoolean(int)
     */
    public boolean getBoolean(int row) throws DataTypeException {
        if (row < 0 || row > m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        return m_bits.get(row);
    }

    /**
     * @see prefuse.data.column.AbstractColumn#setBoolean(boolean, int)
     */
    public void setBoolean(boolean val, int row) throws DataTypeException {
        if (m_readOnly) {
            throw new DataReadOnlyException();
        } else if (row < 0 || row >= m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        // get the previous value
        boolean prev = m_bits.get(row);

        // exit early if no change
        if (prev == val) {
            return;
        }

        // set the new value
        m_bits.set(row, val);

        // fire a change event
        fireColumnEvent(row, prev);
    }

    @Override
    public void setInt(int val, int row) throws DataTypeException {
        if (m_readOnly) {
            throw new DataReadOnlyException();
        } else if (row < 0 || row >= m_size) {
            throw new IllegalArgumentException("Row index out of bounds: " + row);
        }
        // get the previous value
        boolean prev = m_bits.get(row);

        boolean boolVal = val >= 1 ? true : false;

        // exit early if no change
        if (prev == boolVal) {
            return;
        }

        // set the new value
        m_bits.set(row, boolVal);

        // fire a change event
        fireColumnEvent(row, prev);
    }

//    /**
//     * @see prefuse.data.column.AbstractColumn#getString(int)
//     */
//    public String getString(int row) throws DataTypeException {
//        return String.valueOf(getBoolean(row));
//    }
//
//    /**
//     * @see prefuse.data.column.AbstractColumn#setString(java.lang.String, int)
//     */
//    public void setString(String val, int row) throws DataTypeException {
//        boolean b;
//        if ( "true".equalsIgnoreCase(val) ) {
//            b = true;
//        } else if ( "false".equalsIgnoreCase(val) ) {
//            b = false;
//        } else {
//            throw new IllegalArgumentException(
//                "Input string does not represent a boolean value.");
//        }
//        setBoolean(b, row);
//    }
    @Override
    public boolean canSet(Class type) {
        Class wrapperType = TypeLib.getWrapperType(type);
        if (wrapperType != null && wrapperType == Integer.class) {
            return true;
        } else {
            return super.canSet(type);
        }
    }
} // end of class BooleanColumn
