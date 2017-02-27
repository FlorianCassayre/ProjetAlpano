package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
        throw new NotImplementedException();
    }

    @Override
    public double elevationSample(int x, int y)
    {
        throw new NotImplementedException();
    }

    @Override
    public void close() throws Exception
    {
        dem1.close();
        dem2.close();
    }
}
