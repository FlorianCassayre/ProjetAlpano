package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class PanoramaParametersTest
{
    private PanoramaParameters createPanorama()
    {
        return new PanoramaParameters(new GeoPoint(Math.toRadians(6.8087), Math.toRadians(47.0085)), 1380, Math.toRadians(162), Math.toRadians(27), 300, 2500, 800);
    }

    @Test
    public void testAzimuthForX()
    {
        final PanoramaParameters parameters = createPanorama();

        assertEquals(Math.toRadians(162 - 27 / 2.0), parameters.azimuthForX(0), 1E-10);
        assertEquals(Math.toRadians(162), parameters.azimuthForX(1249.5), 1E-10);
        assertEquals(Math.toRadians(162 + 27 / 2.0), parameters.azimuthForX(2499), 1E-10);

        assertEquals(Math.toRadians(162 + 0.3 * 27 / 2.0), parameters.azimuthForX(1.3 * 2499 / 2.0), 1E-10);
    }

    @Test
    public void testXForAzimuth()
    {
        final PanoramaParameters parameters = createPanorama();

        assertEquals(0, parameters.xForAzimuth(Math.toRadians(162 - 27 / 2.0)), 1E-10);
        assertEquals(1249.5, parameters.xForAzimuth(Math.toRadians(162)), 1E-10);
        assertEquals(2499, parameters.xForAzimuth(Math.toRadians(162 + 27 / 2.0)), 1E-10);

        assertEquals(1.3 * 2499 / 2.0, parameters.xForAzimuth(Math.toRadians(162 + 0.3 * 27 / 2.0)), 1E-10);
    }

    @Test
    public void testAltitudeForY()
    {
        final PanoramaParameters parameters = createPanorama();

        assertEquals(0 + parameters.verticalFieldOfView() / 2.0, parameters.altitudeForY(0), 1E-10);
        assertEquals(Math.toRadians(0), parameters.altitudeForY(399.5), 1E-10);
        assertEquals(0 - parameters.verticalFieldOfView() / 2.0, parameters.altitudeForY(799), 1E-10);

        assertEquals(0.7 * parameters.verticalFieldOfView() / 2.0, parameters.altitudeForY(119.85), 1E-10);
    }

    @Test
    public void testYForAltitude()
    {
        final PanoramaParameters parameters = createPanorama();

        assertEquals(0, parameters.yForAltitude(parameters.verticalFieldOfView() / 2.0 - 1E-12), 1E-8);
        assertEquals(399.5, parameters.yForAltitude(Math.toRadians(0)), 1E-10);
        assertEquals(799, parameters.yForAltitude(-parameters.verticalFieldOfView() / 2.0 + 1E-12), 1E-8);

        assertEquals(119.85, parameters.yForAltitude(0.7 * parameters.verticalFieldOfView() / 2.0), 1E-8);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAzimuthForXThrows1()
    {
        createPanorama().azimuthForX(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAzimuthForXThrows2()
    {
        createPanorama().azimuthForX(2500);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXForAzimuthThrows1()
    {
        createPanorama().xForAzimuth(Math.toRadians(162 - 27 / 2.0 - 0.1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testXForAzimuthThrows2()
    {
        createPanorama().xForAzimuth(Math.toRadians(162 + 27 / 2.0 + 0.1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltitudeForYThrows1()
    {
        createPanorama().altitudeForY(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltitudeForYThrows2()
    {
        createPanorama().altitudeForY(800);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testYForAltitudeThrows1()
    {
        createPanorama().yForAltitude(createPanorama().verticalFieldOfView() / 2.0 + 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testYForAltitudeThrows2()
    {
        createPanorama().yForAltitude(-createPanorama().verticalFieldOfView() / 2.0 - 0.1);
    }
}
