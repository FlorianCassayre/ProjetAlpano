package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntervalTest
{
    @Test (expected = IllegalArgumentException.class)
    public void testIllegalArgument()
    {
        final Interval1D interval = new Interval1D(5, 2);
    }

    @Test
    public void testIncludedFromTo()
    {
        final Interval1D interval = new Interval1D(1, 2);
        assertEquals(1, interval.includedFrom());
        assertEquals(2, interval.includedTo());
    }

    @Test
    public void testSize()
    {
        final Interval1D interval = new Interval1D(50, 100);
        assertEquals(51, interval.size());
    }

    @Test
    public void testContains()
    {
        assertTrue(new Interval1D(0, 3).contains(2));
        assertTrue(new Interval1D(0, 3).contains(0));
        assertTrue(new Interval1D(0, 3).contains(3));
        assertFalse(new Interval1D(0, 3).contains(4));
        assertFalse(new Interval1D(0, 3).contains(-1));
    }

    @Test
    public void testSizeOfIntersection()
    {
        assertEquals(0, new Interval1D(0, 1).sizeOfIntersectionWith(new Interval1D(4, 5)));
        assertEquals(1, new Interval1D(0, 3).sizeOfIntersectionWith(new Interval1D(3, 5)));
        assertEquals(2, new Interval1D(0, 2).sizeOfIntersectionWith(new Interval1D(1, 5)));
        assertEquals(4, new Interval1D(0, 3).sizeOfIntersectionWith(new Interval1D(-1, 5)));
    }

    @Test
    public void testEqualsHashCode()
    {
        final Interval1D interval1 = new Interval1D(27, 48);
        final Interval1D interval2 = new Interval1D(27, 48);
        final Interval1D interval3 = new Interval1D(27, 49);

        assertFalse(interval1.equals(null));
        assertFalse(interval1.equals(new Object()));

        assertTrue(interval1.equals(interval2));
        assertTrue(interval2.equals(interval1));

        assertFalse(interval1.equals(interval3));
        assertFalse(interval3.equals(interval1));

        assertEquals(interval1.hashCode(), interval2.hashCode());
        assertNotEquals(interval1.hashCode(), interval3.hashCode());
    }

    @Test
    public void testToString()
    {
        final Interval1D interval = new Interval1D(3, 14);
        assertEquals("[3..14]", interval.toString());
    }
}
