package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeoPointTest
{
    private static final GeoPoint LAUSANNE = new GeoPoint(6.631, 46.521);
    private static final GeoPoint MOSCOW = new GeoPoint(37.623, 55.753);

    @Test
    public void testAzimuthToLausanneMoscow()
    {
        final double expected = Azimuth.canonicalize(Math.toRadians(-52.95));
        final double actual = LAUSANNE.azimuthTo(MOSCOW);
        assertEquals(expected, actual, 1E-4);
    }

    @Test
    public void testDistanceToLausanneMoscow()
    {
        final double expected = 2370E3;
        final double actual = LAUSANNE.distanceTo(MOSCOW);
        assertEquals(expected, actual, 1E4);
    }

    @Test
    public void testToString()
    {
        final GeoPoint point = new GeoPoint(-7.6543, 54.3210);
        final String expected = "(-7.6543,54.3210)";
        final String actual = point.toString();
        assertEquals(expected, actual);
    }
}
