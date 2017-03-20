package ch.epfl.alpano;

import java.util.Objects;

public final class Panorama
{
    private final PanoramaParameters parameters;

    private Panorama(PanoramaParameters parameters, float[] distances, float[] longitudes, float[] latitudes, float[] elevations, float[] slopes)
    {
        this.parameters = Objects.requireNonNull(parameters);

        final int size = parameters.width() * parameters.height();

        throw new UnsupportedOperationException();
    }

    public PanoramaParameters parameters()
    {
        return parameters;
    }

    public float distanceAt(int x, int y)
    {
        throw new UnsupportedOperationException();
    }

    public float distanceAt(int x, int y, float d)
    {
        throw new UnsupportedOperationException();
    }

    public float longitudeAt(int x, int y)
    {
        throw new UnsupportedOperationException();
    }

    public float latitudeAt(int x, int y)
    {
        throw new UnsupportedOperationException();
    }

    public float elevationAt(int x, int y)
    {
        throw new UnsupportedOperationException();
    }

    public float slopeAt(int x, int y)
    {
        throw new UnsupportedOperationException();
    }

    public static final class Builder
    {
        private PanoramaParameters parameters;

        public Builder(PanoramaParameters parameters)
        {
            this.parameters = Objects.requireNonNull(parameters);
        }

        public Builder setDistanceAt(int x, int y, float distance)
        {
            throw new UnsupportedOperationException();
        }

        public Builder setLongitudeAt(int x, int y, float longitude)
        {
            throw new UnsupportedOperationException();
        }

        public Builder setLatitudeAt(int x, int y, float latitude)
        {
            throw new UnsupportedOperationException();
        }

        public Builder setElevationAt(int x, int y, float elevation)
        {
            throw new UnsupportedOperationException();
        }

        public Builder setSlopeAt(int x, int y, float slope)
        {
            throw new UnsupportedOperationException();
        }

        public Panorama build()
        {
            throw new UnsupportedOperationException();
        }
    }
}
