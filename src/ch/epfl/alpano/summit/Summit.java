package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;

public final class Summit
{
    private final String name;
    private final GeoPoint position;
    private final int elevation;

    public Summit(String name, GeoPoint position, int elevation)
    {
        this.name = name;
        this.position = position;
        this.elevation = elevation;
    }

    public String name()
    {
        return name;
    }

    public GeoPoint position()
    {
        return position;
    }

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
