package ch.epfl.alpano.dem;

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
}
