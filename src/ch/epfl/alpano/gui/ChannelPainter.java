package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;

import java.util.function.DoubleUnaryOperator;

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

    default ChannelPainter add(float v)
    {
        return (x, y) -> valueAt(x, y) + v;
    }

    default ChannelPainter sub(float v)
    {
        return (x, y) -> valueAt(x, y) - v;
    }

    default ChannelPainter mul(float v)
    {
        return (x, y) -> valueAt(x, y) * v;
    }

    default ChannelPainter div(float v)
    {
        return (x, y) -> valueAt(x, y) / v;
    }

    default ChannelPainter map(DoubleUnaryOperator operator)
    {
        return (x, y) -> (float) operator.applyAsDouble(valueAt(x, y));
    }

    default ChannelPainter inverted()
    {
        return (x, y) -> 1 - valueAt(x, y);
    }

    default ChannelPainter clamped()
    {
        return (x, y) -> Math.max(0, Math.min(valueAt(x, y), 1));
    }

    default ChannelPainter cycling()
    {
        return (x, y) -> valueAt(x, y) % 1;
    }

    static ChannelPainter maxDistanceToNeighbors(Panorama panorama)
    {
        return (x, y) -> Math.max(Math.max(panorama.distanceAt(x, y - 1, 0), panorama.distanceAt(x + 1, y, 0)), Math.max(panorama.distanceAt(x, y + 1, 0), panorama.distanceAt(x - 1, y, 0))) - panorama.distanceAt(x, y);
    }

    static ChannelPainter distanceAt(Panorama panorama)
    {
        return panorama::distanceAt;
    }

    static ChannelPainter slopeAt(Panorama panorama)
    {
        return panorama::slopeAt;
    }
}
