package ch.epfl.alpano.gui;

import ch.epfl.alpano.*;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.*;
import java.util.function.DoubleUnaryOperator;

public final class Labelizer
{
    private static final int MAX_Y = 170;
    private static final double DISTANCE_THRESHOLD = 200;
    private static final int HORIZONTAL_LINE_SPACE = 22;
    private static final int RAY_INTERVAL = 64;
    private static final int MIN_DISTANCE = 20;
    private static final int ROTATION_ANGLE = 45;

    private final ContinuousElevationModel cDEM;
    private final List<Summit> summits;

    public Labelizer(ContinuousElevationModel cDEM, List<Summit> summits)
    {
        this.cDEM = Objects.requireNonNull(cDEM);
        this.summits = Collections.unmodifiableList(new ArrayList<>(summits));
    }

    public List<Node> labels(PanoramaParameters parameters)
    {
        final List<Summit> summits = getVisibleSummits(parameters);

        final Set<Double> horizontalCoordinates = new HashSet<>();

        final List<Summit> labelled = new ArrayList<>();

        double maxY = Double.POSITIVE_INFINITY;

        for(Summit summit : summits)
        {
            final double azimuth = parameters.observerPosition().azimuthTo(summit.position());
            final double verticalAngle = Math.atan2(summit.elevation() - parameters.observerElevation(), parameters.observerPosition().distanceTo(summit.position()));

            final double x = parameters.xForAzimuth(azimuth), y = parameters.yForAltitude(verticalAngle);

            if(y >= MAX_Y
                    && x >= MIN_DISTANCE && x < parameters.width() - MIN_DISTANCE
                    && doesNotContainSimilar(horizontalCoordinates, x, MIN_DISTANCE))
            {
                if(y < maxY)
                    maxY = y;

                horizontalCoordinates.add(x);
                labelled.add(summit);
            }
        }

        final List<Node> nodes = new ArrayList<>();

        final double labelsY = Math.round(maxY - HORIZONTAL_LINE_SPACE);

        for(Summit summit : labelled)
        {
            final double azimuth = parameters.observerPosition().azimuthTo(summit.position());
            final double verticalAngle = Math.atan2(summit.elevation() - parameters.observerElevation(), parameters.observerPosition().distanceTo(summit.position()));
            final double x = Math.round(parameters.xForAzimuth(azimuth)), y = Math.round(parameters.yForAltitude(verticalAngle));

            String builder = summit.name() + " (" + summit.elevation() + " m)";

            Text text = new Text(builder);
            text.getTransforms().addAll(new Translate(x, labelsY), new Rotate(ROTATION_ANGLE, 0, 0));
            nodes.add(text);

            Line line = new Line(x, labelsY, x, y);
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
            final double verticalAngle = Math.atan2(summit.elevation() - parameters.observerElevation(), parameters.observerPosition().distanceTo(summit.position()));

            final double distance = parameters.observerPosition().distanceTo(summit.position());

            if(Math.abs(Math2.angularDistance(parameters.centerAzimuth(), azimuth)) * 2 <= parameters.horizontalFieldOfView()
                    && Math.abs(verticalAngle) * 2 <= parameters.verticalFieldOfView()
                    && distance <= parameters.maxDistance())
            {
                final ElevationProfile profile = new ElevationProfile(cDEM, parameters.observerPosition(), azimuth, parameters.maxDistance());

                final DoubleUnaryOperator functionDistance = PanoramaComputer.rayToGroundDistance(profile, parameters.observerElevation(), 0);
                final double distanceCurve = functionDistance.applyAsDouble(distance);

                double angle = Math.atan2(-distanceCurve, distance);

                final DoubleUnaryOperator function = PanoramaComputer.rayToGroundDistance(profile, parameters.observerElevation(), angle);

                final double firstInterval = Math2.firstIntervalContainingRoot(function, 0, parameters.maxDistance(), RAY_INTERVAL);

                if(firstInterval == Double.POSITIVE_INFINITY
                        || (firstInterval < parameters.maxDistance() && profile.positionAt(firstInterval).distanceTo(summit.position()) <= DISTANCE_THRESHOLD))
                {
                    visible.add(summit);
                }
            }
        }

        visible.sort((Comparator.comparingInt(summit -> (int) parameters.yForAltitude(Math.atan2(summit.elevation() - parameters.observerElevation(), parameters.observerPosition().distanceTo(summit.position()))))));

        return visible;
    }

    private static boolean doesNotContainSimilar(Set<Double> set, double value, double distance)
    {
        for(Double d : set)
            if(Math.abs(d - value) < distance)
                return false;

        return true;
    }
}