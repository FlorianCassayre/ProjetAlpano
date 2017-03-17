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
        final PanoramaParameters p = createPanorama();

        assertEquals(p.azimuthForX(0), 0, 1E-10);

        assertEquals(0, Math.toDegrees(p.azimuthForX(1250)), 1E-10);
        assertTrue(Math.abs(Math.toDegrees(p.azimuthForX(2500)) + Math.toDegrees(p.azimuthForX(0))) < 1E-10);
        System.out.println(Math.toDegrees(p.azimuthForX(2500)) + "°");


    }

}
