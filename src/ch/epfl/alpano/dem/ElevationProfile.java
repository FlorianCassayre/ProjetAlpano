package ch.epfl.alpano.dem;

import ch.epfl.alpano.*;

import static java.lang.Math.*;

import java.util.Objects;

/**
 * Represents an elevation profile.
 */
public final class ElevationProfile
{
    private final ContinuousElevationModel elevationModel;
    private final GeoPoint origin;
    private final double azimuth, length;

    private static final int INTERVAL = 4096;

    private final GeoPoint[] points;

    /**
     * Creates an elevation profile following a circle arc
     * @param elevationModel the continuous MNT
     * @param origin the origin
     * @param azimuth the direction
     * @param length the length of the profile
     */
    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length)
    {
        this.elevationModel = Objects.requireNonNull(elevationModel);
        this.origin = Objects.requireNonNull(origin);

        Preconditions.checkArgument(Azimuth.isCanonical(azimuth));
        this.azimuth = azimuth;

        Preconditions.checkArgument(length > 0);
        this.length = length;


        final int size = (int) Math.ceil(length / INTERVAL + 1);
        this.points = new GeoPoint[size];

        for(int i = 0; i < size; i++)
        {
            final double angle = Distance.toRadians(i * INTERVAL);
            final double latitude = asin(sin(origin.latitude()) * cos(angle) + cos(origin.latitude()) * sin(angle) * cos(Azimuth.toMath(azimuth)));
            final double longitude = ((origin.longitude() - asin(sin(Azimuth.toMath(azimuth)) * sin(angle) / cos(latitude)) + PI) % Math2.PI2) - PI;

            points[i] = new GeoPoint(longitude, latitude);
        }
    }

    /**
     * Checks if the position is in the bounds of the profile
     * @param x the position to test
     * @return true if the position is in the bounds, false else
     */
    private boolean isInBounds(double x)
    {
        return x >= 0 && x <= length;
    }

    /**
     * Gives the altitude of the field at a given position
     * @param x the position given
     * @return the altitude
     * @throws IllegalArgumentException if the position is out of the bounds
     */
    public double elevationAt(double x)
    {
        return elevationModel.elevationAt(positionAt(x));
    }

    /**
     * Returns the position of a point located at the specified distance from the origin.
     * @param x the distance from the origin
     * @return the position
     * @throws IllegalArgumentException if the position is out of the bounds
     */
    public GeoPoint positionAt(double x)
    {
        Preconditions.checkArgument(isInBounds(x));

        final GeoPoint low = points[(int) Math.floor(x / INTERVAL)];
        final GeoPoint up = points[(int) Math.ceil(x / INTERVAL)];
        final double g = (x % INTERVAL) / INTERVAL;

        return new GeoPoint(Math2.lerp(low.longitude(), up.longitude(), g), Math2.lerp(low.latitude(), up.latitude(), g));
    }

    /**
     * Returns the slope at a point located at the specified distance from the origin.
     * @param x the distance from the origin
     * @return the slope at this position
     * @throws IllegalArgumentException if the position is out of the bounds
     */
    public double slopeAt(double x)
    {
        return elevationModel.slopeAt(positionAt(x));
    }
}
