package ch.epfl.alpano;

/**
 * An utility interface to help working with earth distances.
 */
public interface Distance
{
    /**
     * The Earth radius.
     */
    int EARTH_RADIUS = 6371000;

    /**
     * The Earth perimeter.
     */
    double EARTH_PERIMETER = EARTH_RADIUS * 2 * Math.PI;

    /**
     * Converts an earth distance into an angle.
     * @param distanceInMeters the distance
     * @return the angle
     */
    static double toRadians(double distanceInMeters)
    {
        Preconditions.checkArgument(distanceInMeters >= 0 && distanceInMeters < EARTH_PERIMETER + 1E3, "The distance must belong to [0,EARTH_PERIMETER].");

        return distanceInMeters / EARTH_RADIUS;
    }

    /**
     * Converts an angle into an earth distance.
     * @param distanceInRadians the angle
     * @return the distance
     */
    static double toMeters(double distanceInRadians)
    {
        Preconditions.checkArgument(0 <= distanceInRadians && distanceInRadians < 2 * Math.PI + 1E-3, "The angle must belong to [0,2π].");

        return EARTH_RADIUS * distanceInRadians;
    }
}
