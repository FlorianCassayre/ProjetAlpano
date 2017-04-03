package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;

@FunctionalInterface
public interface ImagePainter
{
    Color colorAt(int x, int y);

    static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation, ChannelPainter lightness, ChannelPainter opacity)
    {
        return (x, y) -> Color.hsb(hue.valueAt(x, y), saturation.valueAt(x, y), lightness.valueAt(x, y), opacity.valueAt(x, y));
    }

    static ImagePainter gray(ChannelPainter gray, ChannelPainter opacity)
    {
        return (x, y) -> Color.gray(gray.valueAt(x, y), opacity.valueAt(x, y));
    }
}
