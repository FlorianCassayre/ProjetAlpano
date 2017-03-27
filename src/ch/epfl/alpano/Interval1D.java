package ch.epfl.alpano;

import java.util.Objects;

/**
 * Represents a one dimensional integer interval.
 */
public final class Interval1D
{
    private final int includedFrom, includedTo;

    /**
     * @param includedFrom the smallest element of the interval
     * @param includedTo the largest element of the interval
     */
    public Interval1D(int includedFrom, int includedTo)
    {
        Preconditions.checkArgument(includedTo >= includedFrom, "includedTo must be greater or equal to includedFrom: " + includedFrom + ", " + includedTo);

        this.includedFrom = includedFrom;
        this.includedTo = includedTo;
    }

    /**
     * Returns the smallest element of the interval
     * @return includedFrom
     */
    public int includedFrom()
    {
        return includedFrom;
    }

    /**
     * Returns the largest element of the interval
     * @return includedTo
     */
    public int includedTo()
    {
        return includedTo;
    }

    /**
     * Returns the size of the interval.
     * @return the size
     */
    public int size()
    {
        return includedTo - includedFrom + 1;
    }

    /**
     * Checks if a number is in the interval.
     * @param v the number to check
     * @return true if it is in the interval, false else
     */
    public boolean contains(int v)
    {
        return v >= includedFrom && v <= includedTo;
    }

    /**
     * Calculates the size of the intersection between this interval and another one.
     * @param that the other interval
     * @return the size of the intersection
     */
    public int sizeOfIntersectionWith(Interval1D that)
    {
        return Math.max(Math.min(this.includedTo, that.includedTo) - Math.max(this.includedFrom, that.includedFrom) + 1, 0);
    }

    /**
     * Returns the bounding union of two intervals.
     * @param that the other interval
     * @return the bouding union
     */
    public Interval1D boundingUnion(Interval1D that)
    {
        return new Interval1D(Math.min(this.includedFrom, that.includedFrom), Math.max(this.includedTo, that.includedTo));
    }

    /**
     * Checks if two intervals are unionable (their union create another interval).
     * @param that the other interval
     * @return true if they are unionable, false else
     */
    public boolean isUnionableWith(Interval1D that)
    {
        return this.size() + that.size() - this.sizeOfIntersectionWith(that) == this.boundingUnion(that).size();
    }

    /**
     * Returns the union of two intervals.
     * @param that the other interval
     * @return the union
     * @throws IllegalArgumentException if the intervals are not unionable
     */
    public Interval1D union(Interval1D that)
    {
        Preconditions.checkArgument(isUnionableWith(that), "The provided interval must be unionable with this one: " + that + ", " + this);

        return boundingUnion(that);
    }

    @Override
    public boolean equals(Object thatO)
    {
        if(thatO == null || thatO.getClass() != this.getClass())
            return false;

        final Interval1D that = (Interval1D) thatO;
        return this.includedFrom == that.includedFrom && this.includedTo == that.includedTo;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(includedFrom, includedTo);
    }

    @Override
    public String toString()
    {
        return "[" + includedFrom + ".." + includedTo + "]";
    }
}
