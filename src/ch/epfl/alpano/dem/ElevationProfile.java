package ch.epfl.alpano.dem;

import ch.epfl.alpano.GeoPoint;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public final class ElevationProfile
{
    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length)
    {
        // TODO: IllegalArgumentException
        // TODO: NullPointerException
        throw new NotImplementedException();
    }

    public double elevationAt(double x)
    {
        // TODO: IllegalArgumentException
        throw new NotImplementedException();
    }

    public GeoPoint positionAt(double x)
    {
        // TODO: IllegalArgumentException
        throw new NotImplementedException();
    }

    public double slopeAt(double x)
    {
        // TODO: IllegalArgumentException
        throw new NotImplementedException();
    }
}
