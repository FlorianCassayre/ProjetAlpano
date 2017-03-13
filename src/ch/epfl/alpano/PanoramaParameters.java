package ch.epfl.alpano;

public final class PanoramaParameters
{
    private final GeoPoint observerPosition;
    private final int observerElevation;
    private final double centerAzimuth, horizontalFieldOfView;
    private final int maxDistance;
    private final int width, height;

    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth, double horizontalFieldOfView, int maxDistance, int width, int height)
    {
        // TODO NullPointerException

        // TODO IllegalArgumentException

        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
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

    public double azimuthForX(double x)
    {
        throw new UnsupportedOperationException();
    }

    public double xForAzimuth(double a)
    {
        throw new UnsupportedOperationException();
    }

    public double altitudeForY(double y)
    {
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
