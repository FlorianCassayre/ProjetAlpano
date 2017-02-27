package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Math2;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Represents a discrete DTM (Digital Terrain Model).
 */
public interface DiscreteElevationModel extends AutoCloseable
{
    /**
     * Number of samples per degree.
     */
    int SAMPLES_PER_DEGREE = 3600;

    /**
     * Number of samples per radian.
     */
    double SAMPLES_PER_RADIAN = 360 * SAMPLES_PER_DEGREE / Math2.PI2;

    static double sampleIndex(double angle)
    {
        throw new NotImplementedException();
    }

    Interval2D extent();

    double elevationSample(int x, int y);

    default DiscreteElevationModel union(DiscreteElevationModel that)
    {
        throw new NotImplementedException();
    }
}
