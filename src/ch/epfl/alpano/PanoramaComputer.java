package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleUnaryOperator;

/**
 * A panorama calculator.
 */
public final class PanoramaComputer
{
    private static final int SEARCH_INTERVAL = 64;
    private static final int DICHOTOMY_STEP = 4;

    private final ContinuousElevationModel dem;

    /**
     * Creates a new instance from a continuous elevation model (DEM).
     * @param dem the continuous elevation model
     */
    public PanoramaComputer(ContinuousElevationModel dem)
    {
        this.dem = Objects.requireNonNull(dem);
    }

    /**
     * Computes a panorama for the given parameters.
     * @param parameters the parameters
     * @return a panorama
     */
    public Panorama computePanorama(PanoramaParameters parameters)
    {
        final Panorama.Builder builder = new Panorama.Builder(parameters);

        final ExecutorService executor = Executors.newFixedThreadPool(8);

        for(int x = 0; x < parameters.width(); x++)
        {
            final double azimuth = parameters.azimuthForX(x);

            final ElevationProfile profile = new ElevationProfile(dem, parameters.observerPosition(), azimuth, parameters.maxDistance());

            final int x1 = x;

            executor.execute(() ->
            {
                double lastRoot = 0;

                for(int y = parameters.height() - 1; y >= 0; y--)
                {
                    final double altitude = parameters.altitudeForY(y);

                    final DoubleUnaryOperator function = rayToGroundDistance(profile, parameters.observerElevation(), Math.tan(altitude));

                    final double firstInterval = Math2.firstIntervalContainingRoot(function, lastRoot, parameters.maxDistance() - SEARCH_INTERVAL, SEARCH_INTERVAL);

                    if(firstInterval != Double.POSITIVE_INFINITY)
                    {
                        final double root = Math2.improveRoot(function, firstInterval, firstInterval + SEARCH_INTERVAL, DICHOTOMY_STEP);

                        final GeoPoint position = profile.positionAt(root);

                        builder.setDistanceAt(x1, y, (float) (root / Math.cos(altitude)));
                        builder.setLongitudeAt(x1, y, (float) position.longitude());
                        builder.setLatitudeAt(x1, y, (float) position.latitude());
                        builder.setElevationAt(x1, y, (float) profile.elevationAt(root));
                        builder.setSlopeAt(x1, y, (float) profile.slopeAt(root));

                        lastRoot = root;
                    }
                    else
                    {
                        break;
                    }
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

        return builder.build();
    }

    /**
     * Returns the function used to determine the distance travelled by a ray to the ground.
     * @param profile the elevation profile
     * @param ray0 the elevation of the viewer
     * @param raySlope the slope of the ray (radians)
     * @return the function
     */
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope)
    {
        final double refraction = 0.13;

        final double d = (1 - refraction) / (2 * Distance.EARTH_RADIUS);

        return x -> ray0 + x * raySlope - profile.elevationAt(x) + d * Math2.sq(x);
    }
}
