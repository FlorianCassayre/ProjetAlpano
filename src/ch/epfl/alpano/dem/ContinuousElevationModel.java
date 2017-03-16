package ch.epfl.alpano.dem;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

import java.util.Objects;

/**
 * Represents a continuous elevation model.
 */
public final class ContinuousElevationModel
{
    /**
     * The derivative step
     */
    private static final double D = Distance.toMeters(1 / DiscreteElevationModel.SAMPLES_PER_RADIAN);

    private final DiscreteElevationModel dem;

    /**
     * Creates a continuous elevation model from a discrete elevation model.
     * @param dem the source model
     */
    public ContinuousElevationModel(DiscreteElevationModel dem)
    {
        this.dem = Objects.requireNonNull(dem);
    }

    /**
     * Returns the value of the elevation at the given {@link GeoPoint}.
     * @param p the point
     * @return the value of the elevation, or 0 if the point is out of the bounds
     */
    public double elevationAt(GeoPoint p)
    {
        final double longitudeIndex = DiscreteElevationModel.sampleIndex(p.longitude()), latitudeIndex = DiscreteElevationModel.sampleIndex(p.latitude());
        final int longitudeIndexMin = (int) Math.floor(longitudeIndex), longitudeIndexMax = longitudeIndexMin + 1;
        final int latitudeIndexMin = (int) Math.floor(latitudeIndex), latitudeIndexMax = latitudeIndexMin + 1;

        return Math2.bilerp(elevationAt(longitudeIndexMin, latitudeIndexMin), elevationAt(longitudeIndexMax, latitudeIndexMin), elevationAt(longitudeIndexMin, latitudeIndexMax), elevationAt(longitudeIndexMax, latitudeIndexMax), longitudeIndex - longitudeIndexMin, latitudeIndex - latitudeIndexMin);
    }

    /**
     * Returns the value of the elevation at the given point.
     * @param longitudeIndex the longitude index
     * @param latitudeIndex the latitude index
     * @return the value of the elevation, or 0 if the point is out of the bounds
     */
    private double elevationAt(int longitudeIndex, int latitudeIndex)
    {
        if(dem.extent().contains(longitudeIndex, latitudeIndex))
            return dem.elevationSample(longitudeIndex, latitudeIndex);
        return 0.0;
    }

    /**
     * Returns the value of the slope at the given {@link GeoPoint}.
     * @param p the point
     * @return the value of the slope, or 0 if the point is out of the bounds
     */
    public double slopeAt(GeoPoint p)
    {
        final double longitudeIndex = DiscreteElevationModel.sampleIndex(p.longitude()), latitudeIndex = DiscreteElevationModel.sampleIndex(p.latitude());
        final int longitudeIndexMin = (int) Math.floor(longitudeIndex), longitudeIndexMax = longitudeIndexMin + 1;
        final int latitudeIndexMin = (int) Math.floor(latitudeIndex), latitudeIndexMax = latitudeIndexMin + 1;

        return Math2.bilerp(slopeAt(longitudeIndexMin, latitudeIndexMin), slopeAt(longitudeIndexMax, latitudeIndexMin), slopeAt(longitudeIndexMin, latitudeIndexMax), slopeAt(longitudeIndexMax, latitudeIndexMax), longitudeIndex - longitudeIndexMin, latitudeIndex - latitudeIndexMin);
    }

    /**
     * Returns the value of the slope at the given integer index coordinates.
     * @param longitudeIndex the longitude index
     * @param latitudeIndex the latitude index
     * @return the value of the slope, or 0 if the point is out of the bounds
     */
    private double slopeAt(int longitudeIndex, int latitudeIndex)
    {
        final double z = elevationAt(longitudeIndex, latitudeIndex);
        final double za = elevationAt(longitudeIndex + 1, latitudeIndex) - z;
        final double zb = elevationAt(longitudeIndex, latitudeIndex + 1) - z;

        return Math.acos(D / (Math.sqrt(Math2.sq(za) + Math2.sq(zb) + Math2.sq(D))));
    }
}
