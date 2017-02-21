package ch.epfl.alpano;

/**
 * An utility interface containing useful mathematical functions.
 */
public interface Math2
{
    /**
     * The value of <code>2 * PI</code>.
     */
    double PI2 = 2 * Math.PI;

    /**
     * Squares a number.
     * @param x the number
     * @return the result
     */
    static double sq(double x)
    {
        return x * x;
    }

    /**
     * Calculate the floor modulo.
     * @param x the number
     * @param y the divisor
     * @return the result
     */
    static double floorMod(double x, double y)
    {
        return x - y * Math.floor(x / y);
    }

    /**
     * Calculates the haversin of a number.
     * @param x the number
     * @return the result
     */
    static double haversin(double x)
    {
        return sq(Math.sin(x / 2));
    }

    /**
     * Calculates an angular distance.
     * @param a1 the first angle
     * @param a2 the second angle
     * @return the distance
     */
    static double angularDistance(double a1, double a2)
    {
        return floorMod(a2 - a1 + Math.PI, PI2) - Math.PI;
    }

    /**
     * Calculates a linear interpolation at a specific position.
     * @param y0 the value at 0
     * @param y1 the value at 1
     * @param x the x value between 0 and 1
     * @return the interpolated value
     */
    static double lerp(double y0, double y1, double x)
    {
        return y0 + x * (y1 - y0);
    }

    /**
     * Calculates the bilinear interpolation at a specific position, knowing four points.
     * @param z00 the value at coordinates (0, 0)
     * @param z10 the value at coordinates (1, 0)
     * @param z01 the value at coordinates (0, 1)
     * @param z11 the value at coordinates (1, 1)
     * @param x the x value between 0 and 1
     * @param y the y value between 0 and 1
     * @return the interpolated value
     */
    static double bilerp(double z00, double z10, double z01, double z11, double x, double y)
    {
        final double d1 = lerp(z00, z10, x);
        final double d2 = lerp(z01, z11, x);

        return lerp(d1, d2, y);
    }

    static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX)
    {
        if(f.applyAsDouble(minX) * f.applyAsDouble(maxX) > 0)
            return Double.POSITIVE_INFINITY;

        while(maxX - minX > dX)
        {
            final double m = (minX + maxX) / 2.0;
            final double fa = f.applyAsDouble(minX);
            final double fm = f.applyAsDouble(m);

            if(fa * fm > 0)
            {
                minX = m;
            }
            else
            {
                maxX = m;
            }
        }

        //System.out.println(minX + " " + maxX);
        return (minX + maxX) / 2.0 - dX;
    }

    static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon)
    {
        if(f.applyAsDouble(x1) * f.applyAsDouble(x2) > 0)
            throw new IllegalArgumentException();

        while(x2 - x1 > epsilon)
        {
            final double m = (x1 + x2) / 2.0;
            final double fa = f.applyAsDouble(x1);
            final double fm = f.applyAsDouble(m);

            if(fa * fm > 0)
            {
                x1 = m;
            }
            else
            {
                x2 = m;
            }
        }

        return x1;
    }
}
