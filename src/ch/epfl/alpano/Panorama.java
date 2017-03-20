package ch.epfl.alpano;

import java.util.Arrays;
import java.util.Objects;

public final class Panorama
{
    private final PanoramaParameters parameters;
    private final float[] distance, longitudes, latitudes, elevations, slopes;

    private Panorama(PanoramaParameters parameters, float[] distances, float[] longitudes, float[] latitudes, float[] elevations, float[] slopes)
    {
        this.parameters = Objects.requireNonNull(parameters);

        final int size = parameters.width() * parameters.height();

        Preconditions.checkArgument(distances.length == size);
        Preconditions.checkArgument(longitudes.length == size);
        Preconditions.checkArgument(latitudes.length == size);
        Preconditions.checkArgument(elevations.length == size);
        Preconditions.checkArgument(slopes.length == size);

        this.distance = distances;
        this.longitudes = longitudes;
        this.latitudes = latitudes;
        this.elevations = elevations;
        this.slopes = slopes;

        throw new UnsupportedOperationException();
    }

    public PanoramaParameters parameters()
    {
        return parameters;
    }

    public float distanceAt(int x, int y)
    {
        return distance[linearSampleIndex(x, y)];
    }

    public float distanceAt(int x, int y, float d)
    {
        if(parameters.isValidSampleIndex(x, y))
            return distance[parameters.linearSampleIndex(x, y)];
        return d;
    }

    public float longitudeAt(int x, int y)
    {
        return longitudes[linearSampleIndex(x, y)];
    }

    public float latitudeAt(int x, int y)
    {
        return latitudes[linearSampleIndex(x, y)];
    }

    public float elevationAt(int x, int y)
    {
        return elevations[linearSampleIndex(x, y)];
    }

    public float slopeAt(int x, int y)
    {
        return slopes[linearSampleIndex(x, y)];
    }

    private int linearSampleIndex(int x, int y)
    {
        if(!parameters.isValidSampleIndex(x, y))
            throw new IndexOutOfBoundsException();

        return parameters.linearSampleIndex(x, y);
    }

    public static final class Builder
    {
        private final PanoramaParameters parameters;
        private final float[] distances, longitudes, latitudes, elevations, slopes;

        private boolean built = false;

        public Builder(PanoramaParameters parameters)
        {
            this.parameters = Objects.requireNonNull(parameters);

            final int size = parameters.width() * parameters.height();

            this.distances = new float[size];
            this.longitudes = new float[size];
            this.latitudes = new float[size];
            this.elevations = new float[size];
            this.slopes = new float[size];

            Arrays.fill(distances, Float.POSITIVE_INFINITY);
        }

        public Builder setDistanceAt(int x, int y, float distance)
        {
            distances[linearSampleIndex(x, y)] = distance;
            return this;
        }

        public Builder setLongitudeAt(int x, int y, float longitude)
        {
            longitudes[linearSampleIndex(x, y)] = longitude;
            return this;
        }

        public Builder setLatitudeAt(int x, int y, float latitude)
        {
            latitudes[linearSampleIndex(x, y)] = latitude;
            return this;
        }

        public Builder setElevationAt(int x, int y, float elevation)
        {
            elevations[linearSampleIndex(x, y)] = elevation;
            return this;
        }

        public Builder setSlopeAt(int x, int y, float slope)
        {
            slopes[linearSampleIndex(x, y)] = slope;
            return this;
        }

        public Panorama build()
        {
            if(built)
                throw new IllegalStateException();

            built = true;
            return new Panorama(parameters, distances, longitudes, latitudes, elevations, slopes);
        }

        private int linearSampleIndex(int x, int y)
        {
            if(!parameters.isValidSampleIndex(x, y))
                throw new IndexOutOfBoundsException();

            return parameters.linearSampleIndex(x, y);
        }
    }
}
