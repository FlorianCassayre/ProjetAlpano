package ch.epfl.alpano;

import static org.junit.Assert.*;

import ch.epfl.alpano.dem.draw.DrawPanorama;
import ch.epfl.test.Utils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PanoramaComputerTest
{
    @BeforeClass
    public static void drawHgtDEM()
    {
        try
        {
            DrawPanorama.main(null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testComparePanoramas() throws IOException
    {
        final BufferedImage expected = ImageIO.read(new File("res/expected/niesen.png"));
        final BufferedImage actual = ImageIO.read(new File("res/actual/niesen.png"));

        Utils.testAreSame(expected, actual);
    }
}
