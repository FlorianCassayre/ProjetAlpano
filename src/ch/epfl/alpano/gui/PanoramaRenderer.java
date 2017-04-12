package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Utility class to render a panorama to an image.
 */
public interface PanoramaRenderer
{
    /**
     * Renders a panorama to an image using the given parameters.
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
}
