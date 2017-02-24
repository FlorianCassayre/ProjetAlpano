package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeoPointTest
{
    private static final GeoPoint LAUSANNE = new GeoPoint(Math.toRadians(6.631), Math.toRadians(46.521));
    private static final GeoPoint MOSCOW = new GeoPoint(Math.toRadians(37.623), Math.toRadians(55.753));

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalArgumentLongitude()
    {
        final GeoPoint point = new GeoPoint(-Math.PI - 0.1, 0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testIllegalArgumentLatitude()
    {
        final GeoPoint point = new GeoPoint(0, Math.PI / 2.0 + 0.1);
    }

    @Test
    public void testLongitudeLatitude()
    {
        final GeoPoint point = new GeoPoint(Math.toRadians(2.333), Math.toRadians(48.866));
        assertEquals(Math.toRadians(2.333), point.longitude(), 1E-3);
        assertEquals(Math.toRadians(48.866), point.latitude(), 1E-3);
    }

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
        final GeoPoint point = new GeoPoint(Math.toRadians(-7.6543), Math.toRadians(54.3210));
        final String expected = "(-7.6543,54.3210)";
        final String actual = point.toString();
        assertEquals(expected, actual);
    }
}
