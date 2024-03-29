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
    private final double verticalFieldOfView;
    private final double delta;

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

        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth), "centerAzimuth must be canonical.");
        this.centerAzimuth = centerAzimuth;

        Preconditions.checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= Math2.PI2, "horizontalFieldOfView must be in ]0,2π].");
        this.horizontalFieldOfView = horizontalFieldOfView;

        Preconditions.checkArgument(maxDistance > 0 && width > 0 && height > 0, "maxDistance, width and height must be stricly positive.");
        this.width = width;
        this.height = height;
        this.maxDistance = maxDistance;

        this.verticalFieldOfView = horizontalFieldOfView * (height - 1) / (width - 1);
        this.delta = horizontalFieldOfView / (width - 1);
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
     * @return the vertical field of view
     */
    public double verticalFieldOfView()
    {
        return verticalFieldOfView;
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

    /**
     * Returns the azimuth for a given x coordinate.
     * @param x the x coordinate
     * @return the azimuth
     */
    public double azimuthForX(double x)
    {
        Preconditions.checkArgument(x >= 0 && x <= width - 1, "Illegal x coordinate.");

        return Azimuth.canonicalize(centerAzimuth + (x - (width - 1) / 2.0) * delta);
    }

    /**
     * Returns the x coordinate for a given azimuth.
     * @param a the azimuth
     * @return the x coordinate
     */
    public double xForAzimuth(double a)
    {
        Preconditions.checkArgument(Azimuth.isCanonical(a), "The azimuth must be canonical.");
        Preconditions.checkArgument(Math.abs(Math2.angularDistance(centerAzimuth, a)) * 2 <= horizontalFieldOfView, "The azimuth is out of the bounds.");

        return (a - centerAzimuth) / delta + (width - 1) / 2.0;
    }

    /**
     * Returns the altitude for the given y coordinate.
     * @param y the y coordinate
     * @return the altitude
     */
    public double altitudeForY(double y)
    {
        final double yMax = height - 1;

        Preconditions.checkArgument(y >= 0 && y <= yMax, "Illegal y coordinate.");

        final double delta = verticalFieldOfView() / yMax;
        return 0 + (yMax / 2.0 - y) * delta;
    }

    /**
     * Returns the y coordinate for a given altitude.
     * @param a the altitude
     * @return the y coordinate
     */
    public double yForAltitude(double a)
    {
        Preconditions.checkArgument(Math.abs(Math2.angularDistance(0, a)) * 2 <= verticalFieldOfView(), "The azimuth is out of the bounds.");

        final double delta = verticalFieldOfView() / (height - 1);
        return (0 - a) / delta + (height - 1) / 2.0;
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
