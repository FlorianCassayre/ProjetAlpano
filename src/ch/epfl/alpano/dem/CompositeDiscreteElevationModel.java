package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;

import java.util.Objects;

/**
 * Represents a composite discrete elevation model.
 */
final class CompositeDiscreteElevationModel implements DiscreteElevationModel
{
    private final DiscreteElevationModel dem1, dem2;

    /**
     * Creates a composite elevation model from two discrete models.
     * @param dem1 the first model
     * @param dem2 the second model
     */
    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2)
    {
        Objects.requireNonNull(dem1);
        Objects.requireNonNull(dem2);

        if(!dem1.extent().isUnionableWith(dem2.extent()))
            throw new IllegalArgumentException();

        this.dem1 = dem1;
        this.dem2 = dem2;
    }

    @Override
    public Interval2D extent()
    {
        return dem1.extent().union(dem2.extent());
    }

    @Override
    public double elevationSample(int x, int y)
    {
        if(dem1.extent().contains(x, y))
            return dem1.elevationSample(x, y);
        else
            return dem2.elevationSample(x, y);
    }

    @Override
    public void close() throws Exception
    {
        dem1.close();
        dem2.close();
    }
}
