package test.prefuse.data.util;

import java.util.Arrays;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import prefuse.util.collections.DoubleIntTreeMap;
import prefuse.util.collections.LiteralIterator;

public class DoubleIntTreeMapTest extends TestCase {
    
    DoubleIntTreeMap map = new DoubleIntTreeMap(true);
    int[] keys = { 1, 2, 5, 3, 4, 5, 10 };
    int[] sort;
    
    public DoubleIntTreeMapTest() {
        sort = (int[])keys.clone();
        Arrays.sort(sort);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        for ( int i=0; i<keys.length; ++i ) {
            map.put(keys[i],keys[i]);
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
        assertEquals(map.get(1),Integer.MIN_VALUE);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.get(int)'
     */
    public void testGet() {
        for ( int i=0; i<map.size(); ++i ) {
            assertEquals(map.get(keys[i]),keys[i]);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.put(int, int)'
     */
    public void testPut() {
        map.clear();
        int size = 0;
        for ( int i=0; i<keys.length; ++i ) {
            map.put(keys[i],keys[i]);
            assertEquals(++size, map.size());
            assertEquals(map.get(keys[i]), keys[i]);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.remove(int)'
     */
    public void testRemoveInt() {
        int size = map.size();
        for ( int i=0; i<keys.length; ++i ) {
            int val = map.remove(keys[i]);
            assertEquals(keys[i], val);
            assertEquals(--size, map.size());
        }
        for ( int i=0; i<keys.length; ++i ) {
            assertEquals(map.get(keys[i]), Integer.MIN_VALUE);
        }
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.firstKey()'
     */
    public void testFirstKey() {
        assertEquals((int)map.firstKey(), sort[0]);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.lastKey()'
     */
    public void testLastKey() {
        assertEquals((int)map.lastKey(), sort[sort.length-1]);
    }

    /*
     * Test method for 'edu.berkeley.guir.prefuse.data.util.IntIntTreeMap.keyIterator()'
     */
    public void testKeyIterator() {
        LiteralIterator iter = map.keyIterator();
        for ( int i=0; iter.hasNext(); ++i ) {
            double key = iter.nextDouble();
            assertEquals(sort[i], (int)key);
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
        
        LiteralIterator iter = map.keyRangeIterator(k1, true, sort[len], false);
        for ( i=i1; iter.hasNext() && i <= i2; ++i ) {
            assertEquals((int)iter.nextDouble(), sort[i]);
        }
        assertTrue(!iter.hasNext() && i == i2+1);
        
        iter = map.valueRangeIterator(k1, true, sort[len], false);
        for ( i=i1; iter.hasNext() && i <= i2; ++i ) {
            assertEquals(iter.nextInt(), sort[i]);
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
