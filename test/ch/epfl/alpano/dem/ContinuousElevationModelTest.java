package ch.epfl.alpano.dem;

import ch.epfl.alpano.dem.draw.DrawDEM;
import ch.epfl.test.Utils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ContinuousElevationModelTest
{
    @BeforeClass
    public static void drawDEM()
    {
        try
        {
            DrawDEM.main(null);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testCompareElevation() throws IOException
    {
        final BufferedImage expected = ImageIO.read(new File("res/expected/elevation.png"));
        final BufferedImage actual = ImageIO.read(new File("res/actual/elevation.png"));

        Utils.testAreSame(expected, actual);
    }

    @Test
    public void testCompareSlope() throws IOException
    {
        final BufferedImage expected = ImageIO.read(new File("res/expected/slope.png"));
        final BufferedImage actual = ImageIO.read(new File("res/actual/slope.png"));

        Utils.testAreSame(expected, actual);
    }
}
