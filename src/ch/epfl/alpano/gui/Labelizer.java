package ch.epfl.alpano.gui;

import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
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

        for(Summit summit : getVisibleSummits(parameters))
        {
            

            final double azimuth = 0.0; // TODO
            final double verticalAngle = 0.0; // TODO

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

        }

        return visible;
    }
}
