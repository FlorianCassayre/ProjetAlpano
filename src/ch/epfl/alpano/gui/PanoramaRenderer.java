package ch.epfl.alpano.gui;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Utility class to render an image from a panorama.
 */
public interface PanoramaRenderer
{
    /**
     * Renders an image from a panorama using the given parameters.
     * @param panorama the panorama
     * @param painter the image painter
     * @return the rendered image
     */
    static Image renderPanorama(Panorama panorama, ImagePainter painter)
    {
        final WritableImage image = new WritableImage(panorama.parameters().width(), panorama.parameters().height());
        final PixelWriter writer = image.getPixelWriter();

        for(int x = 0; x < image.getWidth(); x++)
        {
            for(int y = 0; y < image.getHeight(); y++)
            {
                final Color color = painter.colorAt(x, y);
                writer.setColor(x, y, color);
            }
        }

        return image;
    }

    static ImagePainter coloredImagePainter(Panorama panorama)
    {
        final ChannelPainter distance = panorama::distanceAt, slope = panorama::slopeAt;

        ChannelPainter h = distance.div(100_000).cycling().mul(360);
        ChannelPainter s = distance.div(200_000).clamped().inverted();
        ChannelPainter b = slope.mul(2).div((float) Math.PI).inverted().mul(0.7f).add(0.3f);

        ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

        return ImagePainter.hsb(h, s, b, opacity);
    }

    static ImagePainter blackWhiteBorderedImagePainter(Panorama panorama)
    {
        final ChannelPainter distance = panorama::distanceAt, slope = panorama::slopeAt;

        ChannelPainter h = distance.div(100_000).add(0.5f).clamped();
        ChannelPainter s = slope.mul(2).div((float) Math.PI).inverted().mul(0.7f).add(0.3f);
        ChannelPainter b = ChannelPainter.maxDistanceToNeighbors(panorama).sub(500).div(4500).clamped().inverted().add(0.6f).clamped();

        ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

        ChannelPainter p = (x, y) -> Math.min(Math.min(h.valueAt(x, y), s.valueAt(x, y)), b.valueAt(x, y));

        return ImagePainter.gray(p, opacity);
    }

    static ImagePainter borderedImagePainter(Panorama panorama)
    {
        ChannelPainter gray = ChannelPainter.maxDistanceToNeighbors(panorama).sub(500).div(4500).clamped().inverted();

        ChannelPainter distance = panorama::distanceAt;
        ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

        return ImagePainter.gray(gray, opacity);
    }
}
