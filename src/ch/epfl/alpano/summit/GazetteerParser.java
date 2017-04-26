package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Preconditions;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Util class to parse summits from a file.
 */
public final class GazetteerParser
{
    private GazetteerParser() // Not instantiable
    {
    }

    /**
     * Returns a {@link List} of {@link Summit} read from a file.
     * @param file the file to read
     * @return a list of summits
     * @throws IOException if an IO exception occurs or if the lines format is incorrect
     */
    public static List<Summit> readSummitsFrom(File file) throws IOException
    {
        final List<Summit> list = new ArrayList<>();

        String line;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file))))
        {
            while((line = reader.readLine()) != null)
            {
                list.add(readSummit(line));
            }
        }
        catch(IllegalArgumentException e)
        {
            throw new IOException(e);
        }

        return Collections.unmodifiableList(list);
    }

    /**
     * Reads a line and converts it into a {@link Summit}.
     * @param str the line
     * @return the summit
     * @throws IllegalArgumentException if the format is incorrect
     */
    private static Summit readSummit(String str)
    {
        final String[] parts = str.trim().split(" +");

        Preconditions.checkArgument(parts.length >= 7, "The line does not match the summit format.");

        final double longitude = readAngle(parts[0], true);
        final double latitude = readAngle(parts[1], false);

        final GeoPoint position = new GeoPoint(longitude, latitude);

        final int elevation = Integer.parseInt(parts[2].trim());

        StringBuilder builder = new StringBuilder();
        for(int i = 6; i < parts.length; i++)
        {
            builder.append(parts[i]);
            if(i < parts.length - 1)
                builder.append(" ");
        }

        return new Summit(builder.toString(), position, elevation);
    }

    /**
     * Reads an angle encoded in the format <code>degrees:minutes:seconds</code>.
     * @param str the string
     * @param base180 <code>true</code> if the degrees are expressed in base 180, <code>false</code> if in base 90
     * @return the angle in radians
     * @throws IllegalArgumentException if the format is incorrect
     */
    private static double readAngle(String str, boolean base180)
    {
        final String[] parts = str.split(":");

        Preconditions.checkArgument(parts.length == 3, "The angle must be 3 characters long.");

        final int degrees = readDigits(parts[0], base180 ? 180 : 90);
        final int minutes = readDigits(parts[1], 60);
        final int seconds = readDigits(parts[2], 60);

        return Math.toRadians(degrees + minutes / 60.0 + seconds / 3600.0);
    }

    /**
     * Reads a positive integer from a string.
     * @param str the string
     * @param max the maximum value
     * @return an integer
     * @throws IllegalArgumentException if the format is incorrect
     */
    private static int readDigits(String str, int max)
    {
        Preconditions.checkArgument(str.length() < 10, "The specified number is too big."); // Checks for overflow

        int exponent = 1;
        int sum = 0;

        for(int i = str.length() - 1; i >= 0; i--)
        {
            sum += digitToInt(str.charAt(i)) * exponent;
            exponent *= 10;
        }

        Preconditions.checkArgument(sum < max, "The integer is too big.");

        return sum;
    }

    /**
     * Converts a character to the representing integer.
     * @param c the character
     * @return an integer
     * @throws IllegalArgumentException if the character is not a digit
     */
    private static int digitToInt(char c)
    {
        Preconditions.checkArgument(c >= '0' && c <= '9', "The character must be a digit.");

        return c - '0';
    }
}
