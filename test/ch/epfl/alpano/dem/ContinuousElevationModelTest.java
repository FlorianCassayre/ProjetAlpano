package ch.epfl.alpano.dem;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

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

        testAreSame(expected, actual);
    }

    @Test
    public void testCompareSlope() throws IOException
    {
        final BufferedImage expected = ImageIO.read(new File("res/expected/slope.png"));
        final BufferedImage actual = ImageIO.read(new File("res/actual/slope.png"));

        testAreSame(expected, actual);
    }

    private void testAreSame(BufferedImage expected, BufferedImage actual)
    {
        testHaveSameDimensions(expected, actual);

        for(int y = 0; y < expected.getHeight(); y++)
        {
            for(int x = 0; x < expected.getWidth(); x++)
            {
                assertEquals(expected.getRGB(x, y), actual.getRGB(x, y));
            }
        }
    }

    private void testHaveSameDimensions(BufferedImage expected, BufferedImage actual)
    {
        assertTrue(expected.getWidth() == actual.getWidth() && expected.getHeight() == actual.getHeight());
    }
}
