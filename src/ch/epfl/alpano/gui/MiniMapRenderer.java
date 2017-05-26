package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;


public interface MiniMapRenderer
{
    int DIAMETER = 200;
    int CENTER_X = 20, CENTER_Y = 20;
    double MIN_ELEVATION = 200;
    double MAX_ELEVATION = 4000;
    double WIDTH = Math.toRadians(1);

    static Canvas render(ContinuousElevationModel cdem, PanoramaParameters parameters)
    {
        final Canvas canvas = new Canvas(parameters.width(), parameters.height());
        GraphicsContext g = canvas.getGraphicsContext2D();

        g.setFill(Color.WHITE);
        g.fillOval(0, 0, DIAMETER, DIAMETER);

        final int radius = DIAMETER >> 1;
        final int distance = radius * radius;
        final int mid = (DIAMETER >> 1);

        double step = WIDTH / (DIAMETER - 1);

        for(int x = 0; x < DIAMETER; ++x)
        {
            final int rX = x - mid;
            double lon = parameters.observerPosition().longitude() + rX * step;
            for(int y = 0; y < DIAMETER; ++y)
            {
                final int rY = y - mid;
                if(Math2.sq(rX) + Math2.sq(rY) < distance)
                {
                    final double latitude = parameters.observerPosition().latitude() + rY * step;
                    final GeoPoint point = new GeoPoint(lon, latitude);
                    final double elevation = (cdem.elevationAt(point) - MIN_ELEVATION) / (MAX_ELEVATION - MIN_ELEVATION);
                    g.setFill(Color.gray(Math.max(Math.min(elevation, 1.0), 0)));
                    g.fillRect(x, DIAMETER - 1 - y, 1, 1);
                }
            }
        }

        g.setFill(Color.rgb(255, 0, 0, 0.5));
        g.fillArc(0, 0, DIAMETER, DIAMETER, Math.toDegrees(-parameters.centerAzimuth() + (Math.PI - parameters.horizontalFieldOfView()) / 2.0), Math.toDegrees(parameters.horizontalFieldOfView()), ArcType.ROUND);

        g.setStroke(Color.BLACK);
        g.setLineWidth(1.0);
        g.strokeOval(0, 0, DIAMETER, DIAMETER);
        canvas.getTransforms().addAll(new Translate(CENTER_X, CENTER_Y), new Rotate(Math.toDegrees(-parameters.centerAzimuth()), DIAMETER >> 1, DIAMETER >> 1));

        return canvas;
    }
}
