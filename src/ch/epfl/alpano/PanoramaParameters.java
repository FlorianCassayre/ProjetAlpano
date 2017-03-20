package ch.epfl.alpano;

import java.util.Objects;

/**
 * Represents a panorama view.
 */
public final class PanoramaParameters
{
    private final GeoPoint observerPosition;
    private final int observerElevation;
    private final double centerAzimuth, horizontalFieldOfView;
    private final int maxDistance;
    private final int width, height;

    /**
     * Constructs a new instance of a panorama view.
     * See <a href="https://i.cassayre.me/20170315135322.png">this schematic</a> for full details.
     * @param observerPosition the observer's position
     * @param observerElevation the observer's elevation
     * @param centerAzimuth the horizontal azimuth (in radians)
     * @param horizontalFieldOfView the horizontal field of view (in radians)
     * @param maxDistance the maximal view distance
     * @param width the width of the panorama
     * @param height the height of the panorama
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth, double horizontalFieldOfView, int maxDistance, int width, int height)
    {
        this.observerElevation = observerElevation;

        this.observerPosition = Objects.requireNonNull(observerPosition);

        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
        this.centerAzimuth = centerAzimuth;

        Preconditions.checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= Math2.PI2);
        this.horizontalFieldOfView = horizontalFieldOfView;

        Preconditions.checkArgument(maxDistance > 0 && width > 0 && height > 0);
        this.width = width;
        this.height = height;
        this.maxDistance = maxDistance;
    }

    /**
     * Returns the observer's position.
     * @return the position
     */
    public GeoPoint observerPosition()
    {
        return observerPosition;
    }

    /**
     * Returns the elevation at the observer's position.
     * @return the elevation
     */
    public int observerElevation()
    {
        return observerElevation;
    }

    /**
     * Returns the azimuth at the center fo the panorama.
     * @return the azimuth
     */
    public double centerAzimuth()
    {
        return centerAzimuth;
    }

    /**
     * Returns the horizontal field of view.
     * @return the horizontal field of view
     */
    public double horizontalFieldOfView()
    {
        return horizontalFieldOfView;
    }

    /**
     * Returns the vertical field of view.
     * @return the vertidal field of view
     */
    public double verticalFieldOfView()
    {
        return horizontalFieldOfView * (height - 1) / (width - 1);
    }

    /**
     * Returns the maximum distance of the panorama.
     * @return the maximum distance
     */
    public int maxDistance()
    {
        return maxDistance;
    }

    /**
     * Returns the width of the panorama.
     * @return the width
     */
    public int width()
    {
        return width;
    }

    /**
     * Returns the height of the panorama.
     * @return the height
     */
    public int height()
    {
        return height;
    }

    public double azimuthForX(double x)
    {
        Preconditions.checkArgument(x >= 0 | x <= width - 1);

        final double delta = horizontalFieldOfView / (width - 1);
        return Azimuth.canonicalize(centerAzimuth + (x - width / 2.0) * delta);
    }

    public double xForAzimuth(double a)
    {
        Preconditions.checkArgument(Azimuth.isCanonical(a));
        Preconditions.checkArgument(Math.abs(Math2.angularDistance(centerAzimuth, a)) * 2 <= horizontalFieldOfView);

        final double delta = horizontalFieldOfView / (width - 1);
        return (a - centerAzimuth) / delta + width / 2.0;
    }

    public double altitudeForY(double y)
    {
        Preconditions.checkArgument(y >= 0 | y <= height - 1);

        final double delta = horizontalFieldOfView / (height - 1);
        return Azimuth.canonicalize(centerAzimuth + (y - height / 2.0) * delta);
    }

    public double yForAltitude(double a)
    {
        Preconditions.checkArgument(Azimuth.isCanonical(a));
        Preconditions.checkArgument(Math.abs(Math2.angularDistance(centerAzimuth, a)) * 2 <= verticalFieldOfView());
        final double delta = horizontalFieldOfView / (height - 1);
        return (a - centerAzimuth) / delta + height / 2.0;
    }

    /**
     * Checks if the coordinates are located in the interval.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return <code>true</code> if the coordinates are valid, <code>false</code> else
     */
    boolean isValidSampleIndex(int x, int y)
    {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Returns the linear representation of these coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the linear representation of these coordinates
     */
    int linearSampleIndex(int x, int y)
    {
        return x + (y * width);
    }
}
