package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

public final class HgtDiscreteElevationModel implements DiscreteElevationModel
{
    private static final int SAMPLES_COUNT = 12967201;
    private static final int HGT_FILE_LENGTH = SAMPLES_COUNT * 2;

    private final int latitudeIndex, longitudeIndex;
    private final FileChannel channel;

    /**
     * Creates a new instance.
     * @param file the source file
     * @throws IllegalArgumentException if the file name is not correct or if the file does not exist
     */
    public HgtDiscreteElevationModel(File file)
    {
        if(!file.exists())
            throw new IllegalArgumentException();

        final String name = file.getName();

        Preconditions.checkArgument(name.length() == 11); // The length must be equal to 11
        Preconditions.checkArgument(name.endsWith(".hgt")); // The extension must be exactly ".hgt"

        final char ns = name.charAt(0), ew = name.charAt(3);

        Preconditions.checkArgument((ns == 'N' || ns == 'S') && (ew == 'E' || ew == 'W')); // The letters must be N or S, and E or W

        final boolean signLatitude = ns == 'N', signLongitude = ew == 'E';

        final int latitude = getAsPositiveInteger(name.substring(1, 3)), longitude = getAsPositiveInteger(name.substring(4, 6)); // There must be 2 and 3 digits

        this.latitudeIndex = (signLatitude ? 1 : -1) * latitude * DiscreteElevationModel.SAMPLES_PER_DEGREE;
        this.longitudeIndex = (signLongitude ? 1 : -1) * longitude * DiscreteElevationModel.SAMPLES_PER_DEGREE;

        Preconditions.checkArgument(file.length() == HGT_FILE_LENGTH); // The file length must be exactly 25 934 402 bytes

        try
        {
            this.channel = new FileInputStream(file).getChannel();
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Return the positive integer representation of a string.
     * @param str the positive integer as string
     * @throws IllegalArgumentException if the string is not a positive integer
     * @return the integer
     */
    private static int getAsPositiveInteger(String str)
    {
        for(char c : str.toCharArray())
            if(!(c >= '0' && c <= '9'))
                throw new IllegalArgumentException();

        try
        {
            return Integer.parseInt(str);
        }
        catch(NumberFormatException e) // Catched if the integer is too big
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Interval2D extent()
    {
        return new Interval2D(new Interval1D(0, SAMPLES_PER_DEGREE - 1), new Interval1D(0, SAMPLES_PER_DEGREE - 1));
    }

    @Override
    public double elevationSample(int x, int y)
    {
        final int i = 2 * (x + y * SAMPLES_PER_DEGREE);

        try
        {
            final ShortBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, i, 2).asShortBuffer();
            final short data = buffer.get();
            return (double) data;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    @Override
    public void close() throws Exception
    {
        channel.close();
    }
}
