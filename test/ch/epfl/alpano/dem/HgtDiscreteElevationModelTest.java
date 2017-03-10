package ch.epfl.alpano.dem;

import ch.epfl.test.Utils;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HgtDiscreteElevationModelTest
{
    @BeforeClass
    public static void drawHgtDEM()
    {
        try
        {
            DrawHgtDEM.main(null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFile()
    {
        final HgtDiscreteElevationModel hgt = new HgtDiscreteElevationModel(new File(""));
    }

    @Test
    public void testCompareHgtDEM() throws IOException
    {
        final BufferedImage expected = ImageIO.read(new File("res/expected/dem.png"));
        final BufferedImage actual = ImageIO.read(new File("res/actual/dem.png"));

        Utils.testAreSame(expected, actual);
    }
}
