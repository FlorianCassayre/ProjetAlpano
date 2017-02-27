package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval2DTest
{

    @Test
    public void testIXIY()
    {
        final Interval1D intervalX = new Interval1D(1,2);
        final Interval1D intervalY = new Interval1D(3,4);
        final Interval2D interval = new Interval2D(intervalX,intervalY);
        assertEquals(intervalX, interval.iX());
        assertEquals(intervalY, interval.iY());
    }

    @Test
    public void testContains()
    {
        assertTrue(new Interval2D(new Interval1D(0, 3), new Interval1D(0, 3)).contains(0,0));
        assertTrue(new Interval2D(new Interval1D(0, 3), new Interval1D(0, 3)).contains(3,3));
        assertTrue(new Interval2D(new Interval1D(0, 3), new Interval1D(0, 3)).contains(1,1));
        assertFalse(new Interval2D(new Interval1D(0, 3), new Interval1D(0, 3)).contains(-1,1));
        assertFalse(new Interval2D(new Interval1D(0, 3), new Interval1D(0, 3)).contains(1,-1));
    }

    @Test
    public void testSize()
    {
        final Interval2D interval = new Interval2D(new Interval1D(1, 5), new Interval1D(6, 10));
        assertEquals(25, interval.size());
    }

    @Test
    public void testSizeOfIntersection()
    {
        assertEquals(0, new Interval2D(new Interval1D(0, 1), new Interval1D(2, 3)).sizeOfIntersectionWith(new Interval2D(new Interval1D(2, 3),new Interval1D(0, 1))));
        assertEquals(1, new Interval2D(new Interval1D(0, 1), new Interval1D(2, 3)).sizeOfIntersectionWith(new Interval2D(new Interval1D(1, 2),new Interval1D(3, 4))));
        assertEquals(4, new Interval2D(new Interval1D(0, 1), new Interval1D(2, 3)).sizeOfIntersectionWith(new Interval2D(new Interval1D(0, 1),new Interval1D(2, 3))));
    }

    @Test
    public void testBoundingUnion()
    {
        final Interval1D interval1A = new Interval1D(1, 4);
        final Interval1D interval1B = new Interval1D(5, 6);
        final Interval1D interval2A = new Interval1D(2, 3);
        final Interval1D interval2B = new Interval1D(2, 8);

        final Interval2D intervalA = new Interval2D(interval1A,interval2A);
        final Interval2D intervalB = new Interval2D(interval1B,interval2B);

        final Interval2D expected = new Interval2D(new Interval1D(1, 6), new Interval1D(2, 8));

        assertEquals(expected, intervalA.boundingUnion(intervalB));
        assertEquals(expected, intervalB.boundingUnion(intervalA));

    }


    @Test (expected = IllegalArgumentException.class)
    public void testUnionIllegalArgument()
    {
        final Interval2D interval1 = new Interval2D(new Interval1D(0, 2), new Interval1D(1,2));
        final Interval2D interval2 = new Interval2D(new Interval1D(5, 7), new Interval1D(0,1));

        interval1.union(interval2);
    }

    @Test
    public void testUnion()
    {
        final Interval2D interval1 = new Interval2D(new Interval1D(0, 5), new Interval1D(1, 3));
        final Interval2D interval2 = new Interval2D(new Interval1D(0, 5), new Interval1D(1, 3));

        final Interval2D expected = new Interval2D(new Interval1D(0, 5), new Interval1D(1, 3));
        assertEquals(expected, interval1.union(interval2));
    }


    @Test
    public void testToString()
    {
        final Interval2D interval = new Interval2D(new Interval1D(3, 14), new Interval1D(15,92));
        assertEquals("[3..14]×[15..92]", interval.toString());
    }
}
