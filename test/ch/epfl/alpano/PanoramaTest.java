package ch.epfl.alpano;

import org.junit.Test;

import static org.junit.Assert.*;

public class PanoramaTest
{
    private PanoramaParameters createPanoramaParameters()
    {
        return new PanoramaParameters(new GeoPoint(Math.toRadians(6.8087), Math.toRadians(47.0085)), 1380, Math.toRadians(162), Math.toRadians(27), 300, 2500, 800);
    }

    private Panorama.Builder createPanoramaBuilder()
    {
        return new Panorama.Builder(createPanoramaParameters());
    }

    @Test
    public void a()
    {
        final Panorama.Builder builder = createPanoramaBuilder();

        assertEquals(0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotBeBuiltTwice()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.build();
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotBeModifiedAfterBuild1()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.build();
        builder.setDistanceAt(0, 0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotBeModifiedAfterBuild2()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.build();
        builder.setLongitudeAt(0, 0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotBeModifiedAfterBuild3()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.build();
        builder.setLatitudeAt(0, 0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotBeModifiedAfterBuild4()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.build();
        builder.setElevationAt(0, 0, 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotBeModifiedAfterBuild5()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.build();
        builder.setSlopeAt(0, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBound1()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.setDistanceAt(-1, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBound2()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.setDistanceAt(0, -1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBound3()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.setDistanceAt(2500, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBound4()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.setDistanceAt(0, 800, 0);
    }

    @Test
    public void testValidIndexes()
    {
        final Panorama.Builder builder = createPanoramaBuilder();
        builder.setDistanceAt(0, 0, 0);
        builder.setDistanceAt(100, 0, 0);
        builder.setDistanceAt(0, 100, 0);
        builder.setDistanceAt(2499, 799, 0);
    }
}
