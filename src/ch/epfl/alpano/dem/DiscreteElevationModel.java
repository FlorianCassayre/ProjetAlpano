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

    /**
     * Returns the sample index at the given coordinate. The angle can be a longitude or a latitude, expressed in radians.
     * @param angle an angle in radians
     * @return the sample index
     */
    static double sampleIndex(double angle)
    {
        return angle * SAMPLES_PER_RADIAN;
    }

    /**
     * Returns the DTM extent.
     * @return a bi-dimensional interval
     */
    Interval2D extent();

    /**
     * Return the altitude sample at the given coordinates.
     * Throws {@link IllegalArgumentException} if the index if out of the DTM bounds.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the elevation
     */
    double elevationSample(int x, int y);

    /**
     * Returns the union of the two models.
     * Throws {@link IllegalArgumentException} if the models are not unionable.
     * @param that the other DTM
     * @return an instance of {@link CompositeDiscreteElevationModel}
     */
    default DiscreteElevationModel union(DiscreteElevationModel that)
    {
        return new CompositeDiscreteElevationModel(this, that);
    }
}
