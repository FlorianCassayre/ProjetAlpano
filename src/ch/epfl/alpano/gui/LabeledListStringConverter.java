package ch.epfl.alpano.gui;

import ch.epfl.alpano.Preconditions;
import javafx.util.StringConverter;

import java.util.Arrays;

public final class LabeledListStringConverter extends StringConverter<Integer>
{
    private final String[] strings;

    public LabeledListStringConverter(String... strings)
    {
        this.strings = Arrays.copyOf(strings, strings.length);
    }

    @Override
    public String toString(Integer i)
    {
        Preconditions.checkArgument(i >= 0 && i < strings.length, "Illegal index.");

        return strings[i];
    }

    @Override
    public Integer fromString(String string)
    {
        for(int i = 0; i < strings.length; i++)
            if(strings[i].equals(string))
                return i;

        throw new IllegalArgumentException("The provided string can't be converted to an integer.");
    }
}
