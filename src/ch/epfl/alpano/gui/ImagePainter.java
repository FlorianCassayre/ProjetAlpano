package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;

/**
 * Represents an image painter that supports HSB & gray modes.
 */
@FunctionalInterface
public interface ImagePainter
{
    /**
     * Returns the {@link Color} at the given point.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the color at that point
     */
    Color colorAt(int x, int y);

    /**
     * Creates an image painter from HSB channels.
     * @param hue the hue channel
     * @param saturation the saturation channel
     * @param lightness the lightness channel
     * @param opacity the opacity channel
     * @return the new image painter
     */
    static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation, ChannelPainter lightness, ChannelPainter opacity)
    {
        return (x, y) -> Color.hsb(hue.valueAt(x, y), saturation.valueAt(x, y), lightness.valueAt(x, y), opacity.valueAt(x, y));
    }

    /**
     * Creates an image painter from gray channels.
     * @param gray the gray channel
     * @param opacity the opacity channel
     * @return the new image painter
     */
    static ImagePainter gray(ChannelPainter gray, ChannelPainter opacity)
    {
        return (x, y) -> Color.gray(gray.valueAt(x, y), opacity.valueAt(x, y));
    }
}
