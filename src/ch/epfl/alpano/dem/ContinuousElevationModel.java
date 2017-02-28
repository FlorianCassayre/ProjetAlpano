package ch.epfl.alpano.dem;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

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
        final double longitudeIndex = DiscreteElevationModel.sampleIndex(p.longitude()), latitudeIndex = DiscreteElevationModel.sampleIndex(p.latitude());
        final int longitudeIndexMin = (int) Math.floor(longitudeIndex), longitudeIndexMax = longitudeIndexMin + 1;
        final int latitudeIndexMin = (int) Math.floor(latitudeIndex), latitudeIndexMax = latitudeIndexMin + 1;

        return Math2.bilerp(dem.elevationSample(longitudeIndexMin, latitudeIndexMin), dem.elevationSample(longitudeIndexMax, latitudeIndexMin), dem.elevationSample(longitudeIndexMin, latitudeIndexMax), dem.elevationSample(longitudeIndexMax, latitudeIndexMax), longitudeIndex - longitudeIndexMin, latitudeIndex - latitudeIndexMin);
    }

    /**
     * Returns the value of the slope at the given {@link GeoPoint}.
     * @param p the point
     * @return the value of the slope
     */
    public double slopeAt(GeoPoint p)
    {
        final double longitudeIndex = DiscreteElevationModel.sampleIndex(p.longitude()), latitudeIndex = DiscreteElevationModel.sampleIndex(p.latitude());
        final int longitudeIndexMin = (int) Math.floor(longitudeIndex), longitudeIndexMax = longitudeIndexMin + 1;
        final int latitudeIndexMin = (int) Math.floor(latitudeIndex), latitudeIndexMax = latitudeIndexMin + 1;

        return Math2.bilerp(slopeAt(longitudeIndexMin, latitudeIndexMin), slopeAt(longitudeIndexMax, latitudeIndexMin), slopeAt(longitudeIndexMin, latitudeIndexMax), slopeAt(longitudeIndexMax, latitudeIndexMax), longitudeIndex - longitudeIndexMin, latitudeIndex - latitudeIndexMin);
    }

    /**
     * Returns the value of the slope at the given integer index coordinates
     * @param longitudeIndex the longitude index
     * @param latitudeIndex the latitude index
     * @return the value of the slope
     */
    private double slopeAt(int longitudeIndex, int latitudeIndex)
    {
        // FIXME: the result isn't quite exactly as expected (see slope.png)
        final int d = 1; // The derivative step

        final double z = dem.elevationSample(longitudeIndex, latitudeIndex);
        final double za = dem.elevationSample(longitudeIndex + d, latitudeIndex) - z;
        final double zb = dem.elevationSample(longitudeIndex, latitudeIndex + d) - z;

        return Math.acos(d / (Math.sqrt(Math2.sq(za) + Math2.sq(zb) + Math2.sq(d))));
    }
}
