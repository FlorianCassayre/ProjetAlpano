package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public final class CompositeDiscreteElevationModel implements DiscreteElevationModel
{
    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2)
    {
        throw new NotImplementedException();
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
        throw new NotImplementedException();
    }
}
