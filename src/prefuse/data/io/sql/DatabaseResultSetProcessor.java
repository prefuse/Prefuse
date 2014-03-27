/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prefuse.data.io.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.io.DataIOException;
import prefuse.data.util.Index;
import prefuse.data.util.TableIterator;

/**
 * Is responible fpr processing a ResultSet and transfering
 * it into a Prefuse-Table.
 * 
 * Is just a refactoring of the <code>DatabaseDataSource</code>.
 * 
 * @author Sascha Thielemann
 */
public class DatabaseResultSetProcessor {

    private static final Logger s_logger = Logger.getLogger(DatabaseResultSetProcessor.class.getName());
    protected SQLDataHandler m_handler;

    /**
     * Creates a new DatabaseResultSetProcessor for reading data from a SQL relational
     * database. 
     */
    public DatabaseResultSetProcessor(SQLDataHandler handler) {
        m_handler = handler;
    }

    /**
     * Process the results of a SQL query, putting retrieved data into a Table
     * instance. If a null table is provided, a new table with the appropriate
     * schema will be created.
     *
     * @param t the Table to store results in
     * @param rset the SQL query result set
     * @param remove decides of rows which have no counterpart in the ResultSet 
     *               should get removed
     * @param lock object used for syncronization
     * @return a Table containing the query results
     */
    public Table process(Table t, ResultSet rset, String key, Object lock, boolean remove)
            throws DataIOException {
        // clock in
        int count = 0;
        long timein = System.currentTimeMillis();

        boolean tableIsNew = false;
        HashSet<Tuple> rowsToRemove = new HashSet<>();

        if (t != null) {
            TableIterator iterator = t.iterator();
            while (iterator.hasNext()) {
                Integer row = iterator.nextInt();
                rowsToRemove.add(t.getTuple(row));
            }
        }

        try {
            ResultSetMetaData metadata = rset.getMetaData();
            int ncols = metadata.getColumnCount();

            // create a new table if necessary
            if (t == null) {
                t = getSchema(metadata, m_handler).instantiate();
                tableIsNew = true;
                if (key != null) {
                    try {
                        t.index(key);
                        s_logger.info("Indexed field: " + key);
                    } catch (Exception e) {
                        s_logger.warning("Error indexing field: " + key);
                    }
                }
            }

            // set the lock, lock on the table itself if nothing else provided
            lock = (lock == null ? t : lock);

            // process the returned rows
            while (rset.next()) {
                synchronized (lock) {
                    // determine the table row index to use
                    int row = getExistingRow(t, rset, key);
                    if (row < 0) {
                        row = t.addRow();
                    }
                    rowsToRemove.remove(row);
                    //process each value in the current row
                    for (int i = 1; i <= ncols; ++i) {
                        m_handler.process(t, row, rset, i);
                    }
                }

                // increment row count
                ++count;
            }

            if (!tableIsNew && remove) {
                // remove all no longer relevant columns
                Iterator<Tuple> iterator = rowsToRemove.iterator();
                while (iterator.hasNext()) {
                    Tuple tuple = iterator.next();
                    boolean removeRow = t.removeTuple(tuple);
                    assert removeRow : "maybe some syncronization problems";
                }
            }
        } catch (SQLException e) {
            throw new DataIOException(e);
        }

        // clock out
        long time = System.currentTimeMillis() - timein;
        s_logger.info("Internal query processing completed: " + count + " rows, "
                + (time / 1000) + "." + (time % 1000) + " seconds.");

        return t;
    }

    /**
     * See if a retrieved database row is already represented in the given
     * Table.
     *
     * @param t the prefuse Table to check for an existing row
     * @param rset the ResultSet, set to a particular row, which may or may not
     * have a matching row in the prefuse Table
     * @param keyField the key field to look up to check for an existing row
     * @return the index of the existing row, or -1 if no match is found
     * @throws SQLException
     */
    protected int getExistingRow(Table t, ResultSet rset, String keyField)
            throws SQLException {
        // check if we have a keyField, bail if not
        if (keyField == null) {
            return -1;
        }

        // retrieve the column data type, bail if column is not found
        Class type = t.getColumnType(keyField);
        if (type == null) {
            return -1;
        }

        // get the index and perform the lookup
        Index index = t.index(keyField);
        if (type == int.class) {
            return index.get(rset.getInt(keyField));
        } else if (type == long.class) {
            return index.get(rset.getLong(keyField));
        } else if (type == float.class) {
            return index.get(rset.getFloat(keyField));
        } else if (type == double.class) {
            return index.get(rset.getDouble(keyField));
        } else if (!type.isPrimitive()) {
            return index.get(rset.getObject(keyField));
        } else {
            return -1;
        }
    }

    /**
     * Given the metadata for a SQL result set and a data value handler for that
     * result set, returns a corresponding schema for a prefuse table.
     *
     * @param metadata the SQL result set metadata
     * @param handler the data value handler
     * @return the schema determined by the metadata and handler
     * @throws SQLException if an error occurs accessing the metadata
     */
    public Schema getSchema(ResultSetMetaData metadata, SQLDataHandler handler)
            throws SQLException {
        int ncols = metadata.getColumnCount();
        Schema schema = new Schema(ncols);

        // determine the table schema
        for (int i = 1; i <= ncols; ++i) {
            String name = metadata.getColumnName(i);
            int sqlType = metadata.getColumnType(i);
            Class type = handler.getDataType(name, sqlType);
            if (type != null) {
                schema.addColumn(name, type);
            }
        }

        return schema;
    }
}
