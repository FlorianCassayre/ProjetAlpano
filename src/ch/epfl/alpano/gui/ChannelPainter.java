package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;

import java.util.function.DoubleUnaryOperator;

/**
 * Represents a channel painter used to calculate a color value at a specified 2D point.
 */
@FunctionalInterface
public interface ChannelPainter
{
    /**
     * Returns the value of point a given point.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the value
     */
    float valueAt(int x, int y);

    /**
     * Creates a new channel that adds a constant.
     * @param v the constant
     * @return the new channel
     */
    default ChannelPainter add(float v)
    {
        return (x, y) -> valueAt(x, y) + v;
    }

    /**
     * Creates a new channel that subtracts a constant.
     * @param v the constant
     * @return the new channel
     */
    default ChannelPainter sub(float v)
    {
        return (x, y) -> valueAt(x, y) - v;
    }

    /**
     * Creates a new channel that multiplies by a constant.
     * @param v the constant
     * @return the new channel
     */
    default ChannelPainter mul(float v)
    {
        return (x, y) -> valueAt(x, y) * v;
    }

    /**
     * Creates a new channel that divides by a constant.
     * @param v the constant
     * @return the new channel
     */
    default ChannelPainter div(float v)
    {
        return (x, y) -> valueAt(x, y) / v;
    }

    /**
     * Creates a new channel that maps a custom function.
     * @param operator the custom function
     * @return the new channel
     */
    default ChannelPainter map(DoubleUnaryOperator operator)
    {
        return (x, y) -> (float) operator.applyAsDouble(valueAt(x, y));
    }

    /**
     * Creates a new channel that inverts the current one.
     * @return the new channel
     */
    default ChannelPainter inverted()
    {
        return (x, y) -> 1 - valueAt(x, y);
    }

    /**
     * Creates a new channel that represents a clamped version of the current one.
     * @return the new channel
     */
    default ChannelPainter clamped()
    {
        return (x, y) -> Math.max(0, Math.min(valueAt(x, y), 1));
    }

    /**
     * Creates a new channel that cycles according to the current one.
     * @return the new channel
     */
    default ChannelPainter cycling()
    {
        return (x, y) -> valueAt(x, y) % 1;
    }

    /**
     * Creates a new channel that calculates the maximal distance between 4 neighbors.
     * @param panorama the source panorama
     * @return the new channel painter
     */
    static ChannelPainter maxDistanceToNeighbors(Panorama panorama)
    {
        return (x, y) -> Math.max(Math.max(panorama.distanceAt(x, y - 1, 0), panorama.distanceAt(x + 1, y, 0)), Math.max(panorama.distanceAt(x, y + 1, 0), panorama.distanceAt(x - 1, y, 0))) - panorama.distanceAt(x, y);
    }
}
