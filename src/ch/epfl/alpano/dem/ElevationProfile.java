package ch.epfl.alpano.dem;

import ch.epfl.alpano.*;

import java.util.Objects;

public final class ElevationProfile
{
    private final ContinuousElevationModel elevationModel;
    private final GeoPoint origin;
    private final double azimuth, length;

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
     * Checks if the distance in in the bounds of the profile
     * @param x
     * @return
     */
    private boolean isInBounds(double x)
    {
        return x >= 0 && x <= length;
    }

    public double elevationAt(double x)
    {
        // TODO: IllegalArgumentException
        throw new UnsupportedOperationException();
    }

    public GeoPoint positionAt(double x)
    {
        // TODO: IllegalArgumentException
        throw new UnsupportedOperationException();
    }

    public double slopeAt(double x)
    {
        // TODO: IllegalArgumentException
        throw new UnsupportedOperationException();
    }
}
