package ch.epfl.alpano.gui;

import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;

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
        throw new UnsupportedOperationException();
    }

    private List<Summit> getVisibleSummits()
    {
        throw new UnsupportedOperationException();
    }
}
