package ch.epfl.alpano.gui;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

        final ExecutorService executor = Executors.newFixedThreadPool(Math.max(Runtime.getRuntime().availableProcessors() - 1, 1));

        for(int x = 0; x < image.getWidth(); x++)
        {
            final int x1 = x;

            executor.execute(() ->
            {
                for(int y = 0; y < image.getHeight(); y++)
                {
                    final Color color = painter.colorAt(x1, y);
                    writer.setColor(x1, y, color);
                }
            });
        }

        executor.shutdown();

        try
        {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
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
