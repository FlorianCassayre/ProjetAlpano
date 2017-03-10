package ch.epfl.alpano.dem;

import ch.epfl.alpano.GeoPoint;
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

    @Test
    public void testCompareElevation() throws IOException
    {
        final BufferedImage expected = ImageIO.read(new File("res/expected/profile.png"));
        final BufferedImage actual = ImageIO.read(new File("res/actual/profile.png"));

        Utils.testAreSame(expected, actual);
    }

    @Test
    public void testPositionAt(){
        final ElevationProfile profile = new ElevationProfile(new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("res/data/N46E006.hgt"))), new GeoPoint(Math.toRadians(6),Math.toRadians(46)),Math.toRadians(45),9999);
        final double expectedLongitude = Math.toRadians(6.09385);
        final double expectedLatitude = Math.toRadians(46.06508);
        assertEquals(expectedLongitude,profile.positionAt(10240).longitude(),1e-2);
        assertEquals(expectedLatitude,profile.positionAt(10240).latitude(),1e-2);
    }
}
