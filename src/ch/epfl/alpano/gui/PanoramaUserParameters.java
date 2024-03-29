package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.Preconditions;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Represents user parameters. Provides methods to sanitize the user entered values.
 */
public final class PanoramaUserParameters
{
    private final Map<UserParameter, Integer> userParameters;

    /**
     * Creates a new instance using an associative map.
     * @param userParameters the map
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> userParameters)
    {
        Preconditions.checkArgument(userParameters.size() == UserParameter.values().length, "The map is missing values.");

        final Map<UserParameter, Integer> map = new EnumMap<>(userParameters);

        map.replaceAll(UserParameter::sanitize);

        final int maxHorizontalFieldOfView = 170 * (map.get(UserParameter.WIDTH) - 1) / map.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW) + 1;

        map.put(UserParameter.HEIGHT, Math.min(map.get(UserParameter.HEIGHT), maxHorizontalFieldOfView));

        this.userParameters = Collections.unmodifiableMap(map);
    }

    /**
     * Creates a new instance using explicit parameters.
     * @param observerLongitude the observer's longitude
     * @param observerLatitude the observer's latitude
     * @param observerElevation the observer's elevation
     * @param centerAzimuth the horizontal azimuth (in radians)
     * @param horizontalFieldOfView the horizontal field of view (in radians)
     * @param maxDistance the maximal view distance
     * @param width the width of the panorama
     * @param height the height of the panorama
     * @param supersamplingExponent the supersampling exponent
     */
    public PanoramaUserParameters(int observerLongitude, int observerLatitude, int observerElevation, int centerAzimuth, int horizontalFieldOfView, int maxDistance, int width, int height, int supersamplingExponent, int painter)
    {
        this(new EnumMap<UserParameter, Integer>(UserParameter.class)
        {
            {
                put(UserParameter.OBSERVER_LONGITUDE, observerLongitude);
                put(UserParameter.OBSERVER_LATITUDE, observerLatitude);
                put(UserParameter.OBSERVER_ELEVATION, observerElevation);
                put(UserParameter.CENTER_AZIMUTH, centerAzimuth);
                put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, horizontalFieldOfView);
                put(UserParameter.MAX_DISTANCE, maxDistance);
                put(UserParameter.WIDTH, width);
                put(UserParameter.HEIGHT, height);
                put(UserParameter.SUPER_SAMPLING_EXPONENT, supersamplingExponent);
                put(UserParameter.PAINTER, painter);
            }
        });
    }

    /**
     * Returns the value of the specified parameter.
     * @param parameter the parameter
     * @return the value of this parameter
     */
    public Integer get(UserParameter parameter)
    {
        return userParameters.get(parameter);
    }

    /**
     * Creates a new instance of {@link PanoramaParameters} from these parameters, with the supersampling exponent rescaling.
     * @return a new instance
     */
    public PanoramaParameters panoramaParameters()
    {
        return new PanoramaParameters(
                new GeoPoint(Math.toRadians(observerLongitude() / 10_000.0), Math.toRadians(observerLatitude() / 10_000.0)),
                observerElevation(),
                Math.toRadians(centerAzimuth()),
                Math.toRadians(horizontalFieldOfView()),
                maxDistance() * 1_000,
                width() << supersamplingExponent(),
                height() << supersamplingExponent()
        );
    }

    /**
     * Creates a new instance of {@link PanoramaParameters} from these parameters, without the supersampling exponent rescaling (so as the parameters are stored).
     * @return a new instance
     */
    public PanoramaParameters panoramaDisplayParameters()
    {
        return new PanoramaParameters(
                new GeoPoint(Math.toRadians(observerLongitude() / 10_000.0), Math.toRadians(observerLatitude() / 10_000.0)),
                observerElevation(),
                Math.toRadians(centerAzimuth()),
                Math.toRadians(horizontalFieldOfView()),
                maxDistance() * 1_000,
                width(),
                height()
        );
    }

    /**
     * Returns a copy of the parameters and their value.
     * @return a copy of the parameters
     */
    public Map<UserParameter, Integer> userParameters()
    {
        return new EnumMap<>(userParameters);
    }

    /**
     * The observer longitude.
     * @return the observer longitude
     */
    public int observerLongitude()
    {
        return get(UserParameter.OBSERVER_LONGITUDE);
    }

    /**
     * The observer latitude.
     * @return the observer latitude
     */
    public int observerLatitude()
    {
        return get(UserParameter.OBSERVER_LATITUDE);
    }

    /**
     * The observer elevation.
     * @return the observer elevation
     */
    public int observerElevation()
    {
        return get(UserParameter.OBSERVER_ELEVATION);
    }

    /**
     * The center azimuth.
     * @return the center azimuth
     */
    public int centerAzimuth()
    {
        return get(UserParameter.CENTER_AZIMUTH);
    }

    /**
     * The horizontal field of view.
     * @return the horizontal field of view
     */
    public int horizontalFieldOfView()
    {
        return get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }

    /**
     * The maximum distance.
     * @return the maximum distance
     */
    public int maxDistance()
    {
        return get(UserParameter.MAX_DISTANCE);
    }

    /**
     * The width.
     * @return the width
     */
    public int width()
    {
        return get(UserParameter.WIDTH);
    }

    /**
     * The height.
     * @return the height
     */
    public int height()
    {
        return get(UserParameter.HEIGHT);
    }

    /**
     * The supersampling exponent.
     * @return the supersampling exponent
     */
    public int supersamplingExponent()
    {
        return get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }

    public int painter()
    {
        return get(UserParameter.PAINTER);
    }

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof PanoramaUserParameters))
            return false;

        final PanoramaUserParameters that = (PanoramaUserParameters) o;

        return this.userParameters.equals(that.userParameters);
    }

    @Override
    public int hashCode()
    {
        return userParameters.hashCode();
    }
}
