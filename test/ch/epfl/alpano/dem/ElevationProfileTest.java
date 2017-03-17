package ch.epfl.alpano.dem;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.dem.draw.DrawElevationProfile;
import ch.epfl.test.Utils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ElevationProfileTest
{
    @BeforeClass
    public static void drawDEM()
    {
        try
        {
            DrawElevationProfile.main(null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private ElevationProfile createElevationProfile()
    {
        return new ElevationProfile(new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("res/data/N46E006.hgt"))), new GeoPoint(Math.toRadians(6), Math.toRadians(46)), Math.toRadians(45), 102400);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalConstructorValues1()
    {
        new ElevationProfile(new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("res/data/N46E006.hgt"))), new GeoPoint(Math.toRadians(6), Math.toRadians(46)), Math.toRadians(45), 0);
    }

    @Test(expected = NullPointerException.class)
    public void testIllegalConstructorValues2()
    {
        new ElevationProfile(new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("res/data/N46E006.hgt"))), null, Math.toRadians(45), 102400);
    }

    @Test(expected = NullPointerException.class)
    public void testIllegalConstructorValues3()
    {
        new ElevationProfile(new ContinuousElevationModel(new HgtDiscreteElevationModel(null)), new GeoPoint(Math.toRadians(6), Math.toRadians(46)), Math.toRadians(45), 102400);
    }

    @Test
    public void testCompareElevation() throws IOException
    {
        final BufferedImage expected = ImageIO.read(new File("res/expected/profile.png"));
        final BufferedImage actual = ImageIO.read(new File("res/actual/profile.png"));

        Utils.testAreSame(expected, actual);
    }

    @Test
    public void testPositionAtWithExactValue()
    {
        final ElevationProfile profile = createElevationProfile();
        final double expectedLongitude = Math.toRadians(6.09385);
        final double expectedLatitude = Math.toRadians(46.06508);

        final GeoPoint actual = profile.positionAt(10240);

        assertEquals(expectedLongitude, actual.longitude(), 1.0 / DiscreteElevationModel.SAMPLES_PER_RADIAN);
        assertEquals(expectedLatitude, actual.latitude(), 1.0 / DiscreteElevationModel.SAMPLES_PER_RADIAN);
    }

    @Test
    public void testPositionAtWithOtherValues()
    {
        final double[] distances = new double[] {0, 4096, 8192, 12288, 102400};
        final double[] expectedLongitudes = new double[] {6.00000, 6.03751, 6.07506, 6.11265, 6.94857};
        final double[] expectedLatitudes = new double[] {46.00000, 46.02604, 46.05207, 46.07809, 46.64729};

        final ElevationProfile profile = createElevationProfile();
        for(int i = 0; i < distances.length; i++)
        {
            final double distance = distances[i];
            final double expectedLongitude = Math.toRadians(expectedLongitudes[i]);
            final double expectedLatitude = Math.toRadians(expectedLatitudes[i]);

            final GeoPoint actual = profile.positionAt(distance);

            assertEquals(expectedLongitude, actual.longitude(), 1.0 / DiscreteElevationModel.SAMPLES_PER_RADIAN);
            assertEquals(expectedLatitude, actual.latitude(), 1.0 / DiscreteElevationModel.SAMPLES_PER_RADIAN);
        }
    }

    @Test
    public void testElevationAtForIllegalValues()
    {
        final ElevationProfile profile = createElevationProfile();
        final double[] values = {-1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 102401};

        for(double value : values)
        {
            try
            {
                profile.elevationAt(value);
                fail();
            }
            catch(IllegalArgumentException ex)
            {}
        }
    }

    @Test
    public void testPositionAtForIllegalValues()
    {
        final ElevationProfile profile = createElevationProfile();
        final double[] values = {-1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 102401};

        for(double value : values)
        {
            try
            {
                profile.positionAt(value);
                fail();
            }
            catch(IllegalArgumentException ex)
            {}
        }
    }

    @Test
    public void testSlopeAtForIllegalValues()
    {
        final ElevationProfile profile = createElevationProfile();
        final double[] values = {-1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 102401};

        for(double value : values)
        {
            try
            {
                profile.slopeAt(value);
                fail();
            }
            catch(IllegalArgumentException ex)
            {}
        }
    }
}
