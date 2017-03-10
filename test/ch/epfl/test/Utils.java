package ch.epfl.test;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.junit.Test;
import static org.junit.Assert.*;

public interface Utils
{
    static void testAreSame(BufferedImage expected, BufferedImage actual)
    {
        testHaveSameDimensions(expected, actual);

        for(int y = 0; y < expected.getHeight(); y++)
        {
            for(int x = 0; x < expected.getWidth(); x++)
            {
                assertEquals(new Color(expected.getRGB(x, y)), new Color(actual.getRGB(x, y)));
            }
        }
    }

    static void testHaveSameDimensions(BufferedImage expected, BufferedImage actual)
    {
        assertTrue(expected.getWidth() == actual.getWidth() && expected.getHeight() == actual.getHeight());
    }
}
