package ch.epfl.alpano.gui;

import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class FixedPointStringConverter extends StringConverter<Integer>
{
    private final int i;

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
