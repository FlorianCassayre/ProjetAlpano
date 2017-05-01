package ch.epfl.alpano.gui;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.Node;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JfxRunner.class)
public class LabelizerTest
{
    private static final File HGT_FILE = new File("res/data/N46E007.hgt");
    private static final File SUMMIT_FILE = new File("res/data/alps.txt");

    @Test
    public void testForNiesen() throws IOException
    {
        final ContinuousElevationModel cDEM = new ContinuousElevationModel(new HgtDiscreteElevationModel(HGT_FILE));

        final List<Summit> summits = GazetteerParser.readSummitsFrom(SUMMIT_FILE);

        final Labelizer labelizer = new Labelizer(cDEM, summits);

        final List<Node> nodes = labelizer.labels(PredefinedPanoramas.NIESEN.panoramaDisplayParameters());

        //nodes.forEach(System.out::println);

        final StringBuilder builder = new StringBuilder();

        final String expected = "Text[text=\"FROMBERGHORE (2394 m)\", x=0.0, y=0.0,\n" +
                "Line[startX=1437.0, startY=170.0, endX=1437.0,endY=190.0,\n" +
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

        nodes.forEach(System.out::println);
        System.out.println();

        System.out.println("Size: " + nodes.size());

        for(int i = 0; i < lines.length; i++)
            System.out.println(nodes.get(i));

        for(int i = 0; i < lines.length; i++)
        {
            assertTrue(nodes.get(i).toString().startsWith(lines[i]));
        }
    }
}
