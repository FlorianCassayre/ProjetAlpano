package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;

import java.util.Objects;

/**
 * Represents a point of interest (a peak for instance).
 */
public final class Summit
{
    private final String name;
    private final GeoPoint position;
    private final int elevation;

    public Summit(String name, GeoPoint position, int elevation)
    {
        this.name = Objects.requireNonNull(name);
        this.position = Objects.requireNonNull(position);
        this.elevation = elevation;
    }

    /**
     * Returns the name of this point.
     * @return the name
     */
    public String name()
    {
        return name;
    }

    /**
     * Returns the position of this point.
     * @return the position
     */
    public GeoPoint position()
    {
        return position;
    }

    /**
     * Returns the elevation of this point.
     * @return the elevation
     */
    public int elevation()
    {
        return elevation;
    }

    @Override
    public String toString()
    {
        return name + " " + position.toString() + " " + elevation;
    }
}
