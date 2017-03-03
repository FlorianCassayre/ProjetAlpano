package ch.epfl.alpano.dem;

import static org.junit.Assert.*;

import ch.epfl.alpano.Math2;
import org.junit.Test;

public class TestSampleIndex
{
    @Test
    public void testSampleIndex()
    {
        assertEquals(0, DiscreteElevationModel.sampleIndex(0), 1E-10);
        assertEquals(3600 * 360, DiscreteElevationModel.sampleIndex(Math2.PI2), 1E-10);
        assertEquals(3600 * 180, DiscreteElevationModel.sampleIndex(Math.PI), 1E-10);
    }
}
