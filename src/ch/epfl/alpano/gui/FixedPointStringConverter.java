package ch.epfl.alpano.gui;

import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * A class used to translate an integer into a fixed point string and vice versa.
 */
public final class FixedPointStringConverter extends StringConverter<Integer>
{
    private final int i;

    /**
     * Creates a new translator, according to the fixed point rule.
     * @param i the number of decimal places to the left that the point should move
     */
    public FixedPointStringConverter(int i)
    {
        this.i = i;
    }

    @Override
    public String toString(Integer n)
    {
        return new BigDecimal(n).movePointLeft(i).toPlainString();
    }

    @Override
    public Integer fromString(String string)
    {
        return new BigDecimal(string).movePointRight(i).setScale(0, RoundingMode.HALF_UP).intValueExact();
    }
}
