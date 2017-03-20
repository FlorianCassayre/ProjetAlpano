package ch.epfl.alpano;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

public final class PanoramaComputer
{
    private final ContinuousElevationModel dem;

    public PanoramaComputer(ContinuousElevationModel dem)
    {
        this.dem = Objects.requireNonNull(dem);
    }

    public Panorama computePanorama(PanoramaParameters parameters)
    {
        final Panorama.Builder builder = new Panorama.Builder(parameters);

        // TODO

        return builder.build();
    }

    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope)
    {
        throw new UnsupportedOperationException();
    }
}
