package ch.epfl.alpano;

import java.util.Objects;

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

        Objects.requireNonNull(observerPosition);
        this.observerPosition = observerPosition;

        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
        this.centerAzimuth = centerAzimuth;

        Preconditions.checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= Math2.PI2);
        this.horizontalFieldOfView = horizontalFieldOfView;

        Preconditions.checkArgument(maxDistance > 0 && width > 0 && height > 0);
        this.width = width;
        this.height = height;
        this.maxDistance = maxDistance;
    }

    public GeoPoint observerPosition()
    {
        return observerPosition;
    }

    public int observerElevation()
    {
        return observerElevation;
    }

    public double centerAzimuth()
    {
        return centerAzimuth;
    }

    public double horizontalFieldOfView()
    {
        return horizontalFieldOfView;
    }

    public double verticalFieldOfView()
    {
        return horizontalFieldOfView*(height - 1)/(width - 1);
    }

    public int maxDistance()
    {
        return maxDistance;
    }

    public int width()
    {
        return width;
    }

    public int height()
    {
        return height;
    }

    /*
    public double azimuthForX(double x)
    {
        Preconditions.checkArgument(x >=0 | x < width );
        //TODO
    }


    public double xForAzimuth(double a)
    {
        throw new UnsupportedOperationException();
    }

    public double altitudeForY(double y)
    {
        Preconditions.checkArgument(y >=0 | y < height );
        //TODO
    }
*/

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
