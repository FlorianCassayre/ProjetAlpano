package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

import java.util.Objects;

/**
 * Represents a composite discrete elevation model.
 */
final class CompositeDiscreteElevationModel implements DiscreteElevationModel
{
    private final DiscreteElevationModel dem1, dem2;
    private final Interval2D union;

    /**
     * Creates a composite elevation model from two discrete models.
     * @param dem1 the first model
     * @param dem2 the second model
     * @throws IllegalArgumentException if the models are not unionable
     */
    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2)
    {
        Preconditions.checkArgument(dem1.extent().isUnionableWith(dem2.extent()), "The two models must be unionable.");

        this.dem1 = Objects.requireNonNull(dem1);
        this.dem2 = Objects.requireNonNull(dem2);

        this.union = dem1.extent().union(dem2.extent());
    }

    @Override
    public Interval2D extent()
    {
        return union;
    }

    @Override
    public double elevationSample(int x, int y)
    {
        if(dem1.extent().contains(x, y))
            return dem1.elevationSample(x, y);
        else if(dem2.extent().contains(x, y))
            return dem2.elevationSample(x, y);

        throw new IllegalArgumentException("(" + x + "," + y + ")");
    }

    @Override
    public void close() throws Exception
    {
        dem1.close();
        dem2.close();
    }
}
