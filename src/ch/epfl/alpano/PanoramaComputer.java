package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

/**
 * A panorama calculator.
 */
public final class PanoramaComputer
{
    private final ContinuousElevationModel dem;

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

        final int searchInterval = 64, dichotomyStep = 4;

        for(int x = 0; x < parameters.width(); x++)
        {
            final double azimuth = parameters.azimuthForX(x);

            final ElevationProfile profile = new ElevationProfile(dem, parameters.observerPosition(), azimuth, parameters.maxDistance());

            double lastRoot = 0;

            for(int y = parameters.height() - 1; y >= 0; y--)
            {
                final double altitude = parameters.altitudeForY(y);

                final DoubleUnaryOperator function = rayToGroundDistance(profile, parameters.observerElevation(), altitude);

                final double firstInterval = Math2.firstIntervalContainingRoot(function, lastRoot, parameters.maxDistance() - searchInterval, searchInterval);

                if(firstInterval != Double.POSITIVE_INFINITY)
                {
                    final double root = Math2.improveRoot(function, firstInterval, firstInterval + searchInterval, dichotomyStep);

                    final GeoPoint position = profile.positionAt(root);

                    builder.setDistanceAt(x, y, (float) root);
                    builder.setLongitudeAt(x, y, (float) position.longitude());
                    builder.setLatitudeAt(x, y, (float) position.latitude());
                    builder.setElevationAt(x, y, (float) profile.elevationAt(root));
                    builder.setSlopeAt(x, y, (float) profile.slopeAt(root));

                    lastRoot = root;
                }
                else
                {
                    break;
                }
            }
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

        return x -> ray0 + x * Math.tan(raySlope) - profile.elevationAt(x) + (1 - refraction) / (2 * Distance.EARTH_RADIUS) * Math2.sq(x);
    }
}
