package ch.epfl.alpano.dem;

import ch.epfl.alpano.GeoPoint;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Objects;

/**
 * Represents a continuous elevation model.
 */
public final class ContinuousElevationModel
{
    private final DiscreteElevationModel dem;

    /**
     * Creates a continuous elevation model from a discrete elevation model.
     * @param dem the source model
     */
    public ContinuousElevationModel(DiscreteElevationModel dem)
    {
        Objects.requireNonNull(dem);

        this.dem = dem;
    }

    /**
     * Returns the value of the elevation at the given {@link GeoPoint}.
     * @param p the point
     * @return the value of the elevation
     */
    public double elevationAt(GeoPoint p)
    {
        // TODO implement using bilerp().
        throw new NotImplementedException();
    }

    /**
     * Returns the value of the slope at the given {@link GeoPoint}.
     * @param p the point
     * @return the value of the slope
     */
    public double slopeAt(GeoPoint p)
    {
        // TODO implement using bilerp().
        throw new NotImplementedException();
    }
}
