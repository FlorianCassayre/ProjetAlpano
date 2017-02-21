package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntervalTest
{
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
}
