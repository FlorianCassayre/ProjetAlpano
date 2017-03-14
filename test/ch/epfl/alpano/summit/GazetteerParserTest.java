package ch.epfl.alpano.summit;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class GazetteerParserTest
{
    private static List<Summit> parsed;

    @BeforeClass
    public static void parseFile() throws IOException
    {
        parsed = GazetteerParser.readSummitsFrom(new File("res/data/alps.txt"));
    }

    @Test
    public void testParsedNotNull()
    {
        assertNotNull(parsed);
    }

    @Test
    public void testParsedNoNullValues()
    {
        for(Summit summit : parsed)
        {
            assertNotNull(summit);
            assertNotNull(summit.name());
            assertNotNull(summit.position());
        }
    }

    @Test
    public void testParsedSize()
    {
        assertEquals(21069, parsed.size());
    }

    @Test
    public void testParsedForOneArbitraryValue() throws IOException
    {
        final Summit summit = parsed.get(6026);

        assertEquals("POINTE DES SEMELEYS", summit.name());
        assertEquals(2328, summit.elevation());
        assertEquals(Math.toRadians(7.12361111), summit.position().longitude(), 1.0 / DiscreteElevationModel.SAMPLES_PER_RADIAN);
        assertEquals(Math.toRadians(46.3777778), summit.position().latitude(), 1.0 / DiscreteElevationModel.SAMPLES_PER_RADIAN);
    }

    @Test
    public void testParsedToString() throws IOException
    {
        assertEquals("EIGER (8.0053,46.5775) 3970", parsed.get(7646).toString());
    }
}
