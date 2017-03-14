package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Preconditions;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class GazetteerParser
{
    private GazetteerParser() // Not instantiable
    {
    }

    public static List<Summit> readSummitsFrom(File file) throws IOException
    {
        final List<Summit> list = new ArrayList<>();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        String line;
        while((line = reader.readLine()) != null)
        {
            try
            {
                list.add(readSummit(line));
            }
            catch(IllegalArgumentException e)
            {
                throw new IOException(e);
            }
        }

        return Collections.unmodifiableList(list);
    }

    private static Summit readSummit(String str)
    {
        final String[] parts = str.trim().split(" +");

        Preconditions.checkArgument(parts.length >= 7);

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

    private static double readAngle(String str, boolean base180)
    {
        final String[] parts = str.split(":");

        Preconditions.checkArgument(parts.length == 3);

        final int degrees = readDigits(parts[0], base180 ? 180 : 90);
        final int minutes = readDigits(parts[1], 60);
        final int seconds = readDigits(parts[2], 60);

        return Math.toRadians(degrees + minutes / 60.0 + seconds / 3600.0);
    }

    private static int readDigits(String str, int max)
    {
        Preconditions.checkArgument(str.length() < 10); // Checks for overflow

        int exponent = 1;
        int sum = 0;

        for(int i = str.length() - 1; i >= 0; i--)
        {
            sum += digitToInt(str.charAt(i)) * exponent;
            exponent *= 10;
        }

        Preconditions.checkArgument(sum < max);

        return sum;
    }

    private static int digitToInt(char c)
    {
        Preconditions.checkArgument(c >= '0' && c <= '9');

        return c - '0';
    }
}
