package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Preconditions;

import java.io.*;
import java.util.ArrayList;
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
        final String[] parts = str.split("[^ ]( )"); // FIXME

        final double longitude = readAngle(parts[0], true);
        final double latitude = readAngle(parts[1], false);

        final GeoPoint position = new GeoPoint(longitude, latitude);

        final int elevation = Integer.parseInt(parts[2].trim());

        final String name = parts[6];

        return new Summit(name, position, elevation);
    }

    private static double readAngle(String str, boolean base180)
    {
        final String[] parts = str.split(":");

        final int degrees = base180 ? readThreeDigits(parts[0], 180, true) : readTwoDigits(parts[0], 90, true);
        final int minutes = readTwoDigits(parts[1], 60, false);
        final int seconds = readTwoDigits(parts[2], 60, false);

        return Math.toRadians(degrees / (base180 ? 180.0 : 90.0) + minutes / 60.0 + seconds / 3600.0);
    }

    private static int readThreeDigits(String str, int max, boolean leadingSpaceAllowed)
    {
        Preconditions.checkArgument(str.length() == 3);
        final int d1;
        final int d2;
        final int d3 = digitToInt(str.charAt(2));

        if(str.charAt(0) == ' ' && leadingSpaceAllowed)
        {
            d1 = 0;
            if(str.charAt(1) == ' ')
            {
                d2 = 0;
            }
            else
            {
                d2 = digitToInt(str.charAt(1));
            }
        }
        else
        {
            d1 = digitToInt(str.charAt(0));
            d2 = digitToInt(str.charAt(1));
        }

        final int value = d1 * 100 + d2 * 10 + d3;

        Preconditions.checkArgument(value >= 0 && value < max);

        return value;
    }

    private static int readTwoDigits(String str, int max, boolean leadingSpaceAllowed)
    {
        Preconditions.checkArgument(str.length() == 2);
        final int d1;
        final int d2 = digitToInt(str.charAt(1));

        if(str.charAt(0) == ' ' && leadingSpaceAllowed)
            d1 = 0;
        else
            d1 = digitToInt(str.charAt(0));

        final int value = d1 * 10 + d2;

        Preconditions.checkArgument(value >= 0 && value < max);

        return value;
    }

    private static int digitToInt(char c)
    {
        Preconditions.checkArgument(c >= '0' && c <= '9');

        return c - '0';
    }
}
