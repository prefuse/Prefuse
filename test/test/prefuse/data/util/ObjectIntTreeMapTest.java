package test.prefuse.data.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import prefuse.util.collections.IntIterator;
import prefuse.util.collections.LiteralIterator;
import prefuse.util.collections.ObjectIntTreeMap;

public class ObjectIntTreeMapTest extends TestCase {
    
    ObjectIntTreeMap map = new ObjectIntTreeMap(true);
    int[] keys = { 1, 2, 5, 3, 4, 5, 10 };
    int[] sort;
    
    public ObjectIntTreeMapTest() {
        sort = (int[])keys.clone();
        Arrays.sort(sort);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        for ( int i=0; i<keys.length; ++i ) {
            map.put(new Integer(keys[i]),keys[i]);
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        map.clear();
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.clear()'
     */
    public void testClear() {
        map.clear();
        assertTrue(map.isEmpty());
        try {
            map.keyIterator().next();
            fail("Iterator should be empty");
        } catch ( NoSuchElementException success ) {
        }
        assertEquals(map.get(new Integer(1)),Integer.MIN_VALUE);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.get(int)'
     */
    public void testGet() {
        for ( int i=0; i<map.size(); ++i ) {
            assertEquals(map.get(new Integer(keys[i])),keys[i]);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.put(int, int)'
     */
    public void testPut() {
        map.clear();
        int size = 0;
        for ( int i=0; i<keys.length; ++i ) {
            map.put(new Integer(keys[i]),keys[i]);
            assertEquals(++size, map.size());
            assertEquals(map.get(new Integer(keys[i])), keys[i]);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.remove(int)'
     */
    public void testRemoveInt() {
        int size = map.size();
        for ( int i=0; i<keys.length; ++i ) {
            int val = map.remove(new Integer(keys[i]));
            assertEquals(keys[i], val);
            assertEquals(--size, map.size());
        }
        for ( int i=0; i<keys.length; ++i ) {
            assertEquals(map.get(new Integer(keys[i])), Integer.MIN_VALUE);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.firstKey()'
     */
    public void testFirstKey() {
        assertEquals(((Integer)map.firstKey()).intValue(), sort[0]);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.lastKey()'
     */
    public void testLastKey() {
        assertEquals(((Integer)map.lastKey()).intValue(), sort[sort.length-1]);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.keyIterator()'
     */
    public void testKeyIterator() {
        Iterator iter = map.keyIterator();
        for ( int i=0; iter.hasNext(); ++i ) {
            Integer key = (Integer)iter.next();
            assertEquals(sort[i], key.intValue());
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.subMap(int, int)'
     */
    public void testSubMap() {
        int k1, i1, i2, i, k, len = sort.length-1;
        for ( i=0, k=sort[0]; k==sort[0]; ++i, k=sort[i] );
        k1 = k; i1 = i;
        for ( i=len, k=sort[len]; i>=0 && k==sort[len]; --i, k=sort[i] );
        i2 = i;
        
        Object loKey = new Integer(k1);
        Object hiKey = new Integer(sort[len]);
        
        Iterator iter = map.keyRangeIterator(loKey, true, hiKey, false);
        for ( i=i1; iter.hasNext() && i <= i2; ++i ) {
            assertEquals(((Integer)iter.next()).intValue(), sort[i]);
        }
        assertTrue(!iter.hasNext() && i == i2+1);
        
        IntIterator liter = map.valueRangeIterator(loKey, true, hiKey, false);
        for ( i=i1; liter.hasNext() && i <= i2; ++i ) {
            assertEquals(liter.nextInt(), sort[i]);
        }
        assertTrue(!iter.hasNext() && i == i2+1);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.AbstractTreeMap.size()'
     */
    public void testSize() {
        assertEquals(map.size(), keys.length);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.AbstractTreeMap.isEmpty()'
     */
    public void testIsEmpty() {
        assertFalse(map.isEmpty());
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.AbstractTreeMap.valueIterator()'
     */
    public void testValueIterator() {
        LiteralIterator iter = map.valueIterator(true);
        for ( int i=0; iter.hasNext(); ++i ) {
            int val = iter.nextInt();
            assertEquals(sort[i], val);
        }
    }

}
