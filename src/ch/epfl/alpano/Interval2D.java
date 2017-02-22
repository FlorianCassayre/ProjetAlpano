package ch.epfl.alpano;

import java.util.Objects;

/**
 * Represents a two dimensional integer interval.
 */
public final class Interval2D
{
    private final Interval1D iX, iY;

    /**
     * Creates a new instance.
     * @param iX the first interval
     * @param iY the second interval
     * @throws NullPointerException if any interval is null
     */
    public Interval2D(Interval1D iX, Interval1D iY)
    {
        if(iX == null || iY == null)
            throw new NullPointerException();

        this.iX = iX;
        this.iY = iY;
    }

    /**
     * Returns the first interval.
     * @return the first interval
     */
    public Interval1D iX()
    {
        return iX;
    }

    /**
     * Returns the second interval.
     * @return the second interval
     */
    public Interval1D iY()
    {
        return iY;
    }

    /**
     * Checks if the (x, y) pair is contained in the two-dimensional interval
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the pair is contained, false else
     */
    public boolean contains(int x, int y)
    {
        return iX.contains(x) && iY.contains(y);
    }

    /**
     * Returns the size of the two-dimensional interval;
     * @return the size of the two-dimensional interval
     */
    public int size(){ return iX.size() * iY.size(); }

    /**
     * Calculates the size of the intersection.
     * @param that the other interval
     * @return the size of the intersection
     */
    public int sizeOfIntersectionWith(Interval2D that)
    {
        return this.iX.sizeOfIntersectionWith(that.iX) * this.iY.sizeOfIntersectionWith(that.iY);
    }

    /**
     * Returns the bounding union of two intervals.
     * @param that the other interval
     * @return the bounding union
     */
    public Interval2D boundingUnion(Interval2D that)
    {
        return new Interval2D(this.iX.boundingUnion(that.iX), this.iY.boundingUnion(that.iY));
    }

    /**
     * Checks if two intervals are "unionable" (their union create another interval).
     * @param that the other interval
     * @return the union
     */
    public boolean isUnionableWith(Interval2D that)
    {
        return this.iX.isUnionableWith(that.iX) && this.iY.isUnionableWith(that.iY);
    }

    /**
     * Returns the union of two intervals.
     * @param that the other interval
     * @return the union
     * @throws IllegalArgumentException if the intervals are not unionable
     */
    public Interval2D union(Interval2D that)
    {
        if(!isUnionableWith(that))
            throw new NullPointerException();

        return boundingUnion(that);
    }

    @Override
    public boolean equals(Object thatO)
    {
        if(thatO == null || thatO.getClass() != this.getClass())
            return false;

        final Interval2D that = (Interval2D) thatO;
        return this.iX.equals(that.iX) && this.iY.equals(that.iY);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(iX, iY);
    }

    @Override
    public String toString()
    {
        return iX + "×" + iY;
    }
}
