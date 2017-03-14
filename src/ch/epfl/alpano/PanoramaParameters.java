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

    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth, double horizontalFieldOfView, int maxDistance, int width, int height)
    {
        this.observerElevation = observerElevation;

        this.observerPosition = Objects.requireNonNull(observerPosition);;

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
        Preconditions.checkArgument(x >= 0 | x < width); // TODO check "height minus one" => x < width OR x <= width - 1 ?

        // TODO

        throw new UnsupportedOperationException();
    }


    public double xForAzimuth(double a)
    {
        throw new UnsupportedOperationException();
    }

    public double altitudeForY(double y)
    {
        Preconditions.checkArgument(y >= 0 | y < height); // TODO same as azimuthForX(...)

        // TODO

        throw new UnsupportedOperationException();
    }

    public double yForAltitude(double a)
    {
        throw new UnsupportedOperationException();
    }

    boolean isValidSampleIndex(int x, int y)
    {
        throw new UnsupportedOperationException();
    }

    int linearSampleIndex(int x, int y)
    {
        throw new UnsupportedOperationException();
    }
}
