package ch.epfl.alpano.gui;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.test.Utils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.toRadians;
import static org.junit.Assert.*;

public class PanoramaRendererTest
{
    final static File HGT_FILE = new File("res/data/N46E007.hgt");

    final static int IMAGE_WIDTH = 500;
    final static int IMAGE_HEIGHT = 200;

    final static double ORIGIN_LON = toRadians(7.65);
    final static double ORIGIN_LAT = toRadians(46.73);
    final static int ELEVATION = 600;
    final static double CENTER_AZIMUTH = toRadians(180);
    final static double HORIZONTAL_FOV = toRadians(60);
    final static int MAX_DISTANCE = 100_000;

    final static PanoramaParameters PARAMS =
            new PanoramaParameters(new GeoPoint(ORIGIN_LON,
                    ORIGIN_LAT),
                    ELEVATION,
                    CENTER_AZIMUTH,
                    HORIZONTAL_FOV,
                    MAX_DISTANCE,
                    IMAGE_WIDTH,
                    IMAGE_HEIGHT);


    /*@BeforeClass
    public static void createImage1() throws IOException
    {
        DiscreteElevationModel dDEM =
                new HgtDiscreteElevationModel(HGT_FILE);
        ContinuousElevationModel cDEM =
                new ContinuousElevationModel(dDEM);
        Panorama p = new PanoramaComputer(cDEM)
                .computePanorama(PARAMS);

        ChannelPainter gray = ChannelPainter.maxDistanceToNeighbors(p)
                .sub(500)
                .div(4500)
                .clamped()
                .inverted();

        ChannelPainter distance = p::distanceAt;
        ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

        ImagePainter l = ImagePainter.gray(gray, opacity);

        Image i = PanoramaRenderer.renderPanorama(p, l);
        ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png", new File("res/actual/niesen-profile.png"));
    }*/

    @BeforeClass
    public static void createImage2() throws IOException
    {
        DiscreteElevationModel dDEM =
                new HgtDiscreteElevationModel(HGT_FILE);
        ContinuousElevationModel cDEM =
                new ContinuousElevationModel(dDEM);
        Panorama p = new PanoramaComputer(cDEM)
                .computePanorama(PARAMS);

        ChannelPainter h = ChannelPainter.distanceAt(p).div(100_000).cycling().mul(360);
        ChannelPainter s = ChannelPainter.distanceAt(p).div(200_000).clamped().inverted();
        ChannelPainter b = ChannelPainter.slopeAt(p).mul(2).div((float) Math.PI).inverted().mul(0.7f).add(0.3f);

        ChannelPainter distance = p::distanceAt;
        ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

        ImagePainter l = ImagePainter.hsb(h, s, b, opacity);

        Image i = PanoramaRenderer.renderPanorama(p, l);
        ImageIO.write(SwingFXUtils.fromFXImage(i, null), "png", new File("res/actual/niesen-shaded.png"));
    }

    @Test
    public void testAreSame1() throws IOException
    {
        final BufferedImage expected = ImageIO.read(new File("res/expected/niesen-profile.png"));
        final BufferedImage actual = ImageIO.read(new File("res/actual/niesen-profile.png"));

        Utils.testAreSame(expected, actual);
    }

    @Test
    public void testAreSame2() throws IOException
    {
        final BufferedImage expected = ImageIO.read(new File("res/expected/niesen-shaded.png"));
        final BufferedImage actual = ImageIO.read(new File("res/actual/niesen-shaded.png"));

        Utils.testAreSame(expected, actual);
    }
}
