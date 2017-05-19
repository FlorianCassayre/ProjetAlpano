package ch.epfl.alpano.gui;

import ch.epfl.alpano.Preconditions;
import javafx.util.StringConverter;

import java.util.Arrays;

/**
 * Represents a labeled list of string, and offers a conversion between the index and the string.
 */
public final class LabeledListStringConverter extends StringConverter<Integer>
{
    private final String[] strings;

    /**
     * Creates a list of convertible strings, with a variable number of arguments.
     * @param strings the list of strings
     */
    public LabeledListStringConverter(String... strings)
    {
        this.strings = Arrays.copyOf(strings, strings.length);
    }

    /**
     * Gives us the string at the index provided.
     * @param i the index to be converted
     * @return the corresponding string
     */
    @Override
    public String toString(Integer i)
    {
        Preconditions.checkArgument(i >= 0 && i < strings.length, "Illegal index.");

        return strings[i];
    }

    /**
     * Gives us the index of the string provided.
     * @param string the string to be converted
     * @return the corresponding integer
     */
    @Override
    public Integer fromString(String string)
    {
        for(int i = 0; i < strings.length; i++)
            if(strings[i].equals(string))
                return i;

        throw new IllegalArgumentException("The provided string can't be converted to an integer.");
    }
}
