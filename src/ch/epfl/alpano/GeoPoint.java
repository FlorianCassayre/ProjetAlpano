package ch.epfl.alpano;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static ch.epfl.alpano.Math2.*;
import static java.lang.Math.*;

/**
 * Represents a point on earth.
 */
public final class GeoPoint
{
    /**
     * Locale for number formatting.
     */
    private static final Locale LOCALE = null;

    private final double longitude, latitude;

    /**
     * @param longitude the longitude
     * @param latitude the latitude
     */
    public GeoPoint(double longitude, double latitude)
    {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double longitude()
    {
        return longitude;
    }

    /**
     * @return the latitude
     */
    public double latitude()
    {
        return latitude;
    }

    /**
     * Calculates the distance between this geopoint and another one.
     * @param that the other geopoint
     * @return the distance between the two
     */
    public double distanceTo(GeoPoint that)
    {
        return Azimuth.canonicalize(2 * asin(sqrt(haversin(toRadians(this.latitude - that.latitude)) + cos(toRadians(this.latitude)) * cos(toRadians(that.latitude)) * haversin(toRadians(this.longitude - that.longitude))))) * Distance.EARTH_RADIUS;
    }

    /**
     * Calculates the azimuth between two points.
     * @param that the other geopoint
     * @return the angle between the two
     */
    public double azimuthTo(GeoPoint that)
    {
        return Azimuth.canonicalize(atan(sin(toRadians(this.longitude - that.longitude)) * cos(toRadians(that.latitude)) / (cos(toRadians(this.latitude)) * sin(toRadians(that.latitude)) - sin(toRadians(this.latitude)) * cos(toRadians(that.latitude)) * cos(toRadians(this.longitude - that.longitude)))));
    }

    @Override
    public String toString()
    {
        return String.format(LOCALE, "(%.4f,%.4f)", longitude, latitude);
    }
}
