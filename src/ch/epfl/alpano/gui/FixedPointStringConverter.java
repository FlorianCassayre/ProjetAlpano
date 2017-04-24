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
        BigDecimal decimal = new BigDecimal(n);

        if(i > 0)
            decimal = decimal.movePointRight(i);
        else if(i < 0)
            decimal = decimal.movePointLeft(-i);

        return decimal.setScale(0, RoundingMode.HALF_UP).toPlainString();
    }

    @Override
    public Integer fromString(String string)
    {
        BigDecimal decimal = new BigDecimal(string);

        if(i > 0)
            decimal = decimal.movePointRight(i);
        else if(i < 0)
            decimal = decimal.movePointLeft(-i);

        return decimal.intValueExact();
    }
}
