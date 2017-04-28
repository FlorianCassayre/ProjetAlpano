package ch.epfl.alpano.gui;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LabeledListStringConverterTest
{
    @Test
    public void testToString()
    {
        final LabeledListStringConverter converter = new LabeledListStringConverter("zéro", "un", "deux");

        assertEquals("zéro", converter.toString(0));
        assertEquals("un", converter.toString(1));
        assertEquals("deux", converter.toString(2));
    }

    @Test
    public void testFromString()
    {
        final LabeledListStringConverter converter = new LabeledListStringConverter("zéro", "un", "deux");

        assertEquals((Integer) 0, converter.fromString("zéro"));
        assertEquals((Integer) 1, converter.fromString("un"));
        assertEquals((Integer) 2, converter.fromString("deux"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testIllegalIndex1()
    {
        final LabeledListStringConverter converter = new LabeledListStringConverter("zéro", "un", "deux");

        converter.toString(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalIndex2()
    {
        final LabeledListStringConverter converter = new LabeledListStringConverter("zéro", "un", "deux");

        converter.toString(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalString()
    {
        final LabeledListStringConverter converter = new LabeledListStringConverter("zéro", "un", "deux");

        converter.fromString("trois");
    }
}
