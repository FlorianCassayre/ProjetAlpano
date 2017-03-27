package ch.epfl.alpano;

/**
 * An utility interface to help working with azimuth.
 */
public interface Azimuth
{
    /**
     * Checks if an azimuth is in a canonical form.
     * @param azimuth the angle
     * @return true if the angle is canonical, false else
     */
    static boolean isCanonical(double azimuth)
    {
        return azimuth >= 0 && azimuth < Math2.PI2;
    }

    /**
     * Canonicalize an azimuth.
     * @param azimuth the angle
     * @return the angle in a canonicalized form
     */
    static double canonicalize(double azimuth)
    {
        return Math2.floorMod(azimuth, Math2.PI2);
    }

    /**
     * Converts an azimuth into a mathematical form.
     * @param azimuth the angle
     * @return the angle in a mathematical form
     */
    static double toMath(double azimuth)
    {
        Preconditions.checkArgument(isCanonical(azimuth), "The azimuth must be canonical: " + azimuth);

        return canonicalize(-azimuth);
    }

    /**
     * Converts an azimuth into a cardinal form.
     * @param azimuth the angle
     * @return the angle in a cardinal form
     */
    static double fromMath(double azimuth)
    {
        Preconditions.checkArgument(isCanonical(azimuth), "The azimuth must be canonical: " + azimuth);

        return canonicalize(-azimuth);
    }

    /**
     * Converts the azimuth into a human-readable format.
     * @param azimuth the azimuth to convert
     * @param n the north
     * @param e the east
     * @param s the south
     * @param w the west
     * @return a string representing this angle
     */
    static String toOctantString(double azimuth, String n, String e, String s, String w)
    {
        Preconditions.checkArgument(isCanonical(azimuth), "The specified azimuth is not canonical: " + azimuth);

        final String[] cardinals = {n, e, s, w};

        final int i = ((int) Math.floor(8 * azimuth / Math2.PI2 + 1.0 / 2.0)) % 8;

        if(i % 2 == 0)
        {
            return cardinals[Math.floorDiv(i, 2)];
        }
        else
        {
            final String pre = cardinals[i >= 3 && i <= 5 ? 2 : 0];
            final String suffix = cardinals[Math.floorDiv(i, 4) * 2 + 1];
            return pre + suffix;
        }
    }
}
