package ch.epfl.alpano.gui;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Labelizer
{
    private final ContinuousElevationModel cDEM;
    private final List<Summit> summits;

    public Labelizer(ContinuousElevationModel cDEM, List<Summit> summits)
    {
        this.cDEM = Objects.requireNonNull(cDEM);
        this.summits = Collections.unmodifiableList(new ArrayList<>(summits));
    }

    public List<Node> labels(PanoramaParameters parameters)
    {
        final List<Node> nodes = new ArrayList<>();

        final int yMax = 170;

        //final ElevationProfile profile = new ElevationProfile(cDEM, parameters.observerPosition(), parameters.observerElevation(), )
        //PanoramaComputer.rayToGroundDistance()

        for(Summit summit : getVisibleSummits(parameters))
        {
            final double azimuth = parameters.observerPosition().azimuthTo(summit.position());
            final double verticalAngle = Math.atan2(summit.elevation() - parameters.observerElevation(), parameters.observerPosition().distanceTo(summit.position())); // TODO


            final double x = parameters.xForAzimuth(azimuth), y = parameters.yForAltitude(verticalAngle);

            Text text = new Text(summit.name());
            text.getTransforms().addAll(new Translate(x, yMax), new Rotate(45, 0, 0));
            nodes.add(text);

            Line line = new Line(x, y, x, yMax);
            nodes.add(line);
        }

        return nodes;
    }

    private List<Summit> getVisibleSummits(PanoramaParameters parameters)
    {
        final List<Summit> visible = new ArrayList<>();

        for(Summit summit : summits)
        {
            final double azimuth = parameters.observerPosition().azimuthTo(summit.position());
            final double verticalAngle = Math.atan2(summit.elevation() - parameters.observerElevation(), parameters.observerPosition().distanceTo(summit.position())); // TODO

            if(Math.abs(Math2.angularDistance(parameters.centerAzimuth(), azimuth)) * 2 <= parameters.horizontalFieldOfView()
                    && Math.abs(Math2.angularDistance(0, verticalAngle)) * 2 <= parameters.verticalFieldOfView())
            {
                visible.add(summit);
            }
        }

        return visible;
    }
}
