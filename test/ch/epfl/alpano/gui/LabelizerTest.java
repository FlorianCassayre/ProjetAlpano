package ch.epfl.alpano.gui;

import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import ch.epfl.test.ReflexionUtil;
import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.Node;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JfxRunner.class)
public class LabelizerTest
{
    private static final File HGT_FILE = new File("N46E007.hgt");
    private static final File SUMMIT_FILE = new File("alps.txt");
    private static final PanoramaUserParameters PANORAMA = PredefinedPanoramas.NIESEN;

    private static ContinuousElevationModel cDEM;
    private static List<Summit> summits;

    @BeforeClass
    public static void loadModel() throws IOException
    {
        cDEM = new ContinuousElevationModel(new HgtDiscreteElevationModel(HGT_FILE));
        summits = GazetteerParser.readSummitsFrom(SUMMIT_FILE);
    }

    @Test
    public void testNiesenVisibleSummits() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException
    {
        final Labelizer labelizer = new Labelizer(cDEM, summits);

        final Method method = Labelizer.class.getDeclaredMethod("getVisibleSummits", PanoramaParameters.class);
        method.setAccessible(true);
        final List list = (List) method.invoke(labelizer, PANORAMA.panoramaDisplayParameters());

        final String expected = "NIESEN (1223, 157)\n" +
                "FROMBERGHORE (1437, 190)\n" +
                "DREISPITZ (595, 258)\n" +
                "JUNGFRAU (157, 259)\n" +
                "SCHWALMERE (341, 259)\n" +
                "FIRST (519, 262)\n" +
                "BLUEMLISALP (811, 263)\n" +
                "WYSSI FRAU (767, 263)\n" +
                "MORGENHORN (735, 263)\n" +
                "MOENCH (14, 264)";

        final int count = 10;
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < count; i++)
        {
            final Object o = list.get(i);

            final Field fieldSummit = o.getClass().getDeclaredField("summit"), fieldX = o.getClass().getDeclaredField("x"), fieldY = o.getClass().getDeclaredField("y");
            fieldSummit.setAccessible(true);
            fieldX.setAccessible(true);
            fieldY.setAccessible(true);
            final Summit summit = (Summit) fieldSummit.get(o);
            final int x = (int) fieldX.get(o);
            final int y = (int) fieldY.get(o);

            builder.append(summit.name()).append(" (").append(x).append(", ").append(y).append(")");
            if(i != count - 1)
                builder.append("\n");
        }

        assertEquals(expected, builder.toString());
    }

    @Test
    public void testNiesenLabels()
    {
        final Labelizer labelizer = new Labelizer(cDEM, summits);
        final List<Node> nodes = labelizer.labels(PANORAMA.panoramaDisplayParameters());

        final String expected = "Text[text=\"FROMBERGHORE (2394 m)\", x=0.0, y=0.0,\n" +
                "Line[startX=1437.0, startY=170.0, endX=1437.0, endY=190.0,\n" +
                "Text[text=\"DREISPITZ (2520 m)\", x=0.0, y=0.0,\n" +
                "Line[startX=595.0, startY=170.0, endX=595.0, endY=258.0,\n" +
                "Text[text=\"JUNGFRAU (4158 m)\", x=0.0, y=0.0,\n" +
                "Line[startX=157.0, startY=170.0, endX=157.0, endY=259.0,\n" +
                "Text[text=\"SCHWALMERE (2777 m)\", x=0.0, y=0.0,\n" +
                "Line[startX=341.0, startY=170.0, endX=341.0, endY=259.0,\n" +
                "Text[text=\"FIRST (2440 m)\", x=0.0, y=0.0,\n" +
                "Line[startX=519.0, startY=170.0, endX=519.0, endY=262.0, ";

        final String[] lines = expected.split("\n");

        assertTrue(nodes.size() >= 10);

        System.out.println("Size: " + nodes.size());

        for(int i = 0; i < lines.length; i++)
            System.out.println(nodes.get(i));

        for(int i = 0; i < lines.length; i++)
        {
            assertTrue(nodes.get(i).toString().startsWith(lines[i]));
        }
    }
}
