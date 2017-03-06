package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;

public final class HgtDiscreteElevationModel implements DiscreteElevationModel
{
    public HgtDiscreteElevationModel(File file)
    {
        // TODO: IllegalArgumentException
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
