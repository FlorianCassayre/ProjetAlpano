package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


public interface MiniMapRenderer
{
    int DIAMETER = 200;
    int MARGIN_X = 20, MARGIN_Y = 20;
    double MIN_ELEVATION = 200;
    double MAX_ELEVATION = 4000;
    double WIDTH = Math.toRadians(1);

    static Canvas render(ContinuousElevationModel cdem, PanoramaParameters parameters)
    {
        final Canvas canvas = new Canvas(MARGIN_X * 2 + DIAMETER, MARGIN_Y * 3 + DIAMETER);
        GraphicsContext g = canvas.getGraphicsContext2D();

        g.setFill(Color.WHITE);
        g.fillOval(MARGIN_X, MARGIN_Y, DIAMETER, DIAMETER);

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
                    g.fillRect(x + MARGIN_X, DIAMETER - 1 - y + MARGIN_Y, 1, 1);
                }
            }
        }

        g.setFill(Color.rgb(255, 0, 0, 0.5));
        g.fillArc(MARGIN_X, MARGIN_Y, DIAMETER, DIAMETER, Math.toDegrees(-parameters.centerAzimuth() + (Math.PI - parameters.horizontalFieldOfView()) / 2.0), Math.toDegrees(parameters.horizontalFieldOfView()), ArcType.ROUND);

        g.setStroke(Color.BLACK);
        g.setLineWidth(4.0);
        g.strokeOval(MARGIN_X, MARGIN_Y, DIAMETER, DIAMETER);
        canvas.getTransforms().addAll(new Rotate(Math.toDegrees(-parameters.centerAzimuth()), MARGIN_X + (DIAMETER >> 1), MARGIN_Y + (DIAMETER >> 1)));

        return canvas;
    }
}
