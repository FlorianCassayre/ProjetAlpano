package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

/**
 * Represents a discrete elevation model.
 */
public final class HgtDiscreteElevationModel implements DiscreteElevationModel
{
    private static final int SAMPLES_COUNT = 12967201;
    private static final int HGT_FILE_LENGTH = SAMPLES_COUNT * 2;

    private final int latitudeIndex, longitudeIndex;
    private final ShortBuffer buffer;

    /**
     * Creates a new instance.
     * @param file the source file
     * @throws IllegalArgumentException if the file name is not correct or if the file does not exist
     */
    public HgtDiscreteElevationModel(File file)
    {
        Preconditions.checkArgument(file.exists(), "The specified file does not exist");

        final String name = file.getName();

        Preconditions.checkArgument(name.length() == 11, "The file name must match the required length of 11: " + name.length()); // The length must be equal to 11
        Preconditions.checkArgument(name.endsWith(".hgt"), "The file extension must match the required extension .hgt"); // The extension must be exactly ".hgt"

        final char ns = name.charAt(0), ew = name.charAt(3);

        Preconditions.checkArgument((ns == 'N' || ns == 'S') && (ew == 'E' || ew == 'W'), "The directions must be N/S and E/W: " + ns + ", " + ew); // The letters must be N or S, and E or W

        final boolean signLatitude = ns == 'N', signLongitude = ew == 'E';

        final int latitude = getAsPositiveInteger(name.substring(1, 3)), longitude = getAsPositiveInteger(name.substring(4, 7)); // There must be 2 and 3 digits

        Preconditions.checkArgument(latitude >= 0 && latitude < 90, "The latitude must be in [0,90[: " + latitude);
        Preconditions.checkArgument(longitude >= 0 && longitude < 180, "The longitude must be in [0,180[: " + longitude);

        this.latitudeIndex = (signLatitude ? 1 : -1) * latitude * DiscreteElevationModel.SAMPLES_PER_DEGREE;
        this.longitudeIndex = (signLongitude ? 1 : -1) * longitude * DiscreteElevationModel.SAMPLES_PER_DEGREE;

        Preconditions.checkArgument(file.length() == HGT_FILE_LENGTH, "The file length must be exactly 25 934 402 bytes: " + file.length()); // The file length must be exactly 25 934 402 bytes

        try(FileInputStream stream = new FileInputStream(file))
        {
            this.buffer = stream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, HGT_FILE_LENGTH).asShortBuffer();
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException("Exception while reading the file");
        }
    }

    /**
     * Return the positive integer representation of a string.
     * @param str the positive integer as string
     * @return the integer
     * @throws IllegalArgumentException if the string is not a positive integer
     */
    private static int getAsPositiveInteger(String str)
    {
        for(char c : str.toCharArray())
            if(!(c >= '0' && c <= '9'))
                throw new IllegalArgumentException("Character is not a digit: " + c);

        try
        {
            return Integer.parseInt(str);
        }
        catch(NumberFormatException e) // Catched if the integer is too big
        {
            throw new IllegalArgumentException("The specified integer is too big.");
        }
    }

    @Override
    public Interval2D extent()
    {
        return new Interval2D(new Interval1D(longitudeIndex, longitudeIndex + SAMPLES_PER_DEGREE), new Interval1D(latitudeIndex, latitudeIndex + SAMPLES_PER_DEGREE));
    }

    @Override
    public double elevationSample(int x, int y)
    {
        Preconditions.checkArgument(extent().contains(x, y), "The sample is out of the bound: " + x + ", " + y);

        final int i = (x - longitudeIndex) + (SAMPLES_PER_DEGREE - y + latitudeIndex) * (SAMPLES_PER_DEGREE + 1);

        return (double) buffer.get(i);
    }

    @Override
    public void close() throws IOException
    {
    }
}
