package ch.epfl.alpano;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a panorama.
 */
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
    }

    /**
     * Returns the parameters of this panorama.
     * @return the parameters
     */
    public PanoramaParameters parameters()
    {
        return parameters;
    }

    /**
     * Returns the distance from the projected point to the viewer position.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the distance to the point
     */
    public float distanceAt(int x, int y)
    {
        return distance[linearSampleIndex(x, y)];
    }

    /**
     * Returns the distance from the projected point to the viewer position.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param d the default value
     * @return the distance to the point if it exists, the default value else
     */
    public float distanceAt(int x, int y, float d)
    {
        if(parameters.isValidSampleIndex(x, y))
            return distance[parameters.linearSampleIndex(x, y)];
        return d;
    }

    /**
     * Returns the longitude at the specified projected point.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the longitude
     */
    public float longitudeAt(int x, int y)
    {
        return longitudes[linearSampleIndex(x, y)];
    }

    /**
     * Returns the latitude at the specified projected point.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the latitude
     */
    public float latitudeAt(int x, int y)
    {
        return latitudes[linearSampleIndex(x, y)];
    }

    /**
     * Returns the elevation at the specified projected point.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the elevation
     */
    public float elevationAt(int x, int y)
    {
        return elevations[linearSampleIndex(x, y)];
    }

    /**
     * Returns the slope at the specified projected point.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the slope
     */
    public float slopeAt(int x, int y)
    {
        return slopes[linearSampleIndex(x, y)];
    }

    /**
     * Calculates the linear index of two coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the linear index
     */
    private int linearSampleIndex(int x, int y)
    {
        if(!parameters.isValidSampleIndex(x, y))
            throw new IndexOutOfBoundsException();

        return parameters.linearSampleIndex(x, y);
    }

    /**
     * A panorama builder.
     */
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

        /**
         * Sets the distance from the viewer position to the specified projected point.
         * @param x the x coordinate
         * @param y the y coordinate
         * @param distance the distance
         * @return this instance
         * @throws IllegalStateException if built has already been called
         */
        public Builder setDistanceAt(int x, int y, float distance)
        {
            checkBuilt();
            distances[linearSampleIndex(x, y)] = distance;
            return this;
        }

        /**
         * Sets the longitude at the specified projected point.
         * @param x the x coordinate
         * @param y the y coordinate
         * @param longitude the longitude
         * @return this instance
         * @throws IllegalStateException if built has already been called
         */
        public Builder setLongitudeAt(int x, int y, float longitude)
        {
            checkBuilt();
            longitudes[linearSampleIndex(x, y)] = longitude;
            return this;
        }

        /**
         * Sets the latitude at the specified projected point.
         * @param x the x coordinate
         * @param y the y coordinate
         * @param latitude the latitude
         * @return this instance
         * @throws IllegalStateException if built has already been called
         */
        public Builder setLatitudeAt(int x, int y, float latitude)
        {
            checkBuilt();
            latitudes[linearSampleIndex(x, y)] = latitude;
            return this;
        }

        /**
         * Sets the elevation at the specified projected point.
         * @param x the x coordinate
         * @param y the y coordinate
         * @param elevation the elevation
         * @return this instance
         * @throws IllegalStateException if built has already been called
         */
        public Builder setElevationAt(int x, int y, float elevation)
        {
            checkBuilt();
            elevations[linearSampleIndex(x, y)] = elevation;
            return this;
        }

        /**
         * Sets the slope at the specified projected point.
         * @param x the x coordinate
         * @param y the y coordinate
         * @param slope the slope
         * @return this instance
         * @throws IllegalStateException if built has already been called
         */
        public Builder setSlopeAt(int x, int y, float slope)
        {
            checkBuilt();
            slopes[linearSampleIndex(x, y)] = slope;
            return this;
        }

        /**
         * Builds the panorama.
         * This method can only be called once.
         * @return an instance of {@link Panorama}
         * @throws IllegalStateException if built has already been called
         */
        public Panorama build()
        {
            checkBuilt();
            built = true;
            return new Panorama(parameters, distances, longitudes, latitudes, elevations, slopes);
        }

        /**
         * Check if build has been called
         * @throws IllegalStateException if built has already been called
         */
        private void checkBuilt()
        {
            if(built)
                throw new IllegalStateException();
        }

        /**
         * Calculates the linear index of two coordinates.
         * @param x the x coordinate
         * @param y the y coordinate
         * @return the linear index
         */
        private int linearSampleIndex(int x, int y)
        {
            if(!parameters.isValidSampleIndex(x, y))
                throw new IndexOutOfBoundsException();

            return parameters.linearSampleIndex(x, y);
        }
    }
}
