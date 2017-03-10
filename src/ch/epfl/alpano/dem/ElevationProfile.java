package ch.epfl.alpano.dem;

import ch.epfl.alpano.*;
import static java.lang.Math.*;

import java.util.Objects;

public final class ElevationProfile
{
    private final ContinuousElevationModel elevationModel;
    private final GeoPoint origin;
    private final double azimuth, length;

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
    }

    /**
     * Checks if the position is in the bounds of the profile
     * @param x the position to test
     * @return true if the position is in the bounds, false else
     * @throws IllegalArgumentException if the position is not in the bounds
     */
    private boolean isInBounds(double x)
    {
        return x >= 0 && x <= length;
    }

    /**
     * Gives the altitude of the field at a given position
     * @param x the position given
     * @return the altitude
     * @throws IllegalArgumentException if position is not in the bounds
     */
    public double elevationAt(double x)
    {
        return elevationModel.elevationAt(positionAt(x));
    }

    public GeoPoint positionAt(double x)
    {
        isInBounds(x);

        final double angle = Distance.toRadians(x);
        final double longitude = asin(sin(origin.longitude()) * cos(angle) + cos(origin.longitude()) * sin(angle) * cos(Azimuth.toMath(azimuth)));
        final double latitude = ((origin.latitude() - asin(sin(Azimuth.toMath(azimuth)) * sin(angle) / cos(longitude)) + PI) % Math2.PI2) - PI;

        return new GeoPoint(longitude, latitude);
    }

    public double slopeAt(double x)
    {
        return elevationModel.slopeAt(positionAt(x));
    }
}
