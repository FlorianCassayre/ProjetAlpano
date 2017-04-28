package ch.epfl.alpano.gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class FixedPointStringConverterTest
{
    @Test
    public void testFromString()
    {
        final FixedPointStringConverter converter = new FixedPointStringConverter(1);

        assertEquals((Integer) 120, converter.fromString("12"));
        assertEquals((Integer) 123, converter.fromString("12.3"));
        assertEquals((Integer) 123, converter.fromString("12.34"));
        assertEquals((Integer) 124, converter.fromString("12.35"));
        assertEquals((Integer) 124, converter.fromString("12.36789"));
    }

    @Test
    public void testToString()
    {
        final FixedPointStringConverter converter = new FixedPointStringConverter(1);

        assertEquals("67.8", converter.toString(678));
    }
}
