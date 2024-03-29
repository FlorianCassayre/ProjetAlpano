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

/**
 * A class used to get the visible labels for a given panorama, from a {@link Summit} list.
 */
public final class Labelizer
{
    private static final int MAX_Y = 170;
    private static final double DISTANCE_THRESHOLD = 200;
    private static final int HORIZONTAL_LINE_SPACE = 22;
    private static final int LABEL_LINE_SPACE = 2;
    private static final int RAY_INTERVAL = 64;
    private static final int MIN_DISTANCE = 20;
    private static final int ROTATION_ANGLE = -60;

    private final ContinuousElevationModel cDEM;
    private final List<Summit> summits;

    /**
     * Creates a labelizer from a continuous elevation model and a summit list.
     * @param cDEM the continuous elevation model
     * @param summits the summits list
     */
    public Labelizer(ContinuousElevationModel cDEM, List<Summit> summits)
    {
        this.cDEM = Objects.requireNonNull(cDEM);
        this.summits = Collections.unmodifiableList(new ArrayList<>(summits));
    }

    /**
     * Labelizes the visible summits
     * @param parameters the panorama parameters
     * @return the list of nodes representing the labels
     */
    public List<Node> labels(PanoramaParameters parameters)
    {
        final List<PositionalSummit> points = getVisibleSummits(parameters);

        final Set<Integer> horizontalCoordinates = new HashSet<>();

        final List<PositionalSummit> labelled = new ArrayList<>();

        int maxY = Integer.MAX_VALUE;

        for(PositionalSummit point : points)
        {
            if(point.y >= MAX_Y
                    && point.x >= MIN_DISTANCE && point.x < parameters.width() - MIN_DISTANCE
                    && doesNotContainSimilar(horizontalCoordinates, point.x, MIN_DISTANCE))
            {
                if(point.y < maxY)
                    maxY = point.y;

                horizontalCoordinates.add(point.x);
                labelled.add(point);
            }
        }

        final List<Node> nodes = new ArrayList<>();

        final int labelsY = maxY - HORIZONTAL_LINE_SPACE;


        for(PositionalSummit point : labelled)
        {
            String builder = point.summit.name() + " (" + point.summit.elevation() + " m)";

            Text text = new Text(builder);
            text.getTransforms().addAll(new Translate(point.x, labelsY), new Rotate(ROTATION_ANGLE, 0, 0));
            nodes.add(text);

            Line line = new Line(point.x, labelsY + LABEL_LINE_SPACE, point.x, point.y);
            nodes.add(line);
        }

        return nodes;
    }

    /**
     * Gives the visible summits based on panorama parameters.
     * @param parameters the panorama parameters
     * @return the list of the visible summits
     */
    private List<PositionalSummit> getVisibleSummits(PanoramaParameters parameters)
    {
        final List<PositionalSummit> visible = new ArrayList<>();

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

                final double angle = Math.atan2(-distanceCurve, distance);

                final DoubleUnaryOperator function = PanoramaComputer.rayToGroundDistance(profile, parameters.observerElevation(), angle);

                final double firstInterval = Math2.firstIntervalContainingRoot(function, 0, parameters.maxDistance(), RAY_INTERVAL);

                if(firstInterval == Double.POSITIVE_INFINITY
                        || firstInterval >= distance - DISTANCE_THRESHOLD)
                {
                    visible.add(new PositionalSummit(summit, (int) Math.round(parameters.xForAzimuth(azimuth)), (int) Math.round(parameters.yForAltitude(angle))));
                }
            }
        }

        visible.sort((a, b) -> a.y == b.y ? Integer.compare(b.summit.elevation(), a.summit.elevation()) : Integer.compare(a.y, b.y));

        return visible;
    }

    /**
     * Checks if a set contains a specified value or similar.
     * @param collection the collection to check
     * @param value the value to find
     * @param distance the maximum distance between the value
     * @return <code>false</code> no similar value was found, <code>true</code> else
     */
    private static boolean doesNotContainSimilar(Iterable<Integer> collection, double value, double distance)
    {
        for(Integer i : collection)
            if(Math.abs(i - value) < distance)
                return false;

        return true;
    }

    /**
     * Represents a summit and its coordinates on the screen.
     */
    private static final class PositionalSummit
    {
        private final Summit summit;
        private final int x, y;

        /**
         * Creates a new instance.
         * @param summit the summit
         * @param x the x coordinate
         * @param y the y coordinate
         */
        public PositionalSummit(Summit summit, int x, int y)
        {
            this.summit = summit;
            this.x = x;
            this.y = y;
        }
    }
}