package ch.epfl.test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisplayDifferencesPanorama
{
    public static void main(String[] args) throws IOException
    {
        final BufferedImage expected = ImageIO.read(new File("res/expected/niesen-shaded.png"));
        final BufferedImage actual = ImageIO.read(new File("res/actual/niesen-shaded.png"));

        for(int y = 0; y < expected.getHeight(); y++)
        {
            for(int x = 0; x < expected.getWidth(); x++)
            {
                final Color a = new Color(expected.getRGB(x, y));
                final Color b = new Color(actual.getRGB(x, y));

                if(!(a.getRed() == b.getRed() && a.getGreen() == b.getGreen() && a.getBlue() == b.getBlue()))
                {
                    actual.setRGB(x, y, new Color(255, 83, 211).getRGB());
                }
            }
        }

        ImageIO.write(actual, "png", new File("res/actual/niesen1.png"));
    }
}
