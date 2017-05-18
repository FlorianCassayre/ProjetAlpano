package ch.epfl.alpano.gui;

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

        final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
}
