package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;


public class FXPanoramaDraw extends Application
{
    private static final File HGT_FILE_1 = new File("N46E006.hgt");
    private static final File HGT_FILE_2 = new File("N46E007.hgt");

    private static final File SUMMIT_FILE = new File("alps.txt");
    private static final PanoramaUserParameters PARAMETERS = PredefinedPanoramas.NIESEN;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        final ContinuousElevationModel cDEM = new ContinuousElevationModel(new HgtDiscreteElevationModel(HGT_FILE_2));
        final List<Summit> summits = GazetteerParser.readSummitsFrom(SUMMIT_FILE);

        final Labelizer labelizer = new Labelizer(cDEM, summits);

        final PanoramaComputer computer = new PanoramaComputer(cDEM);
        final Panorama panorama = computer.computePanorama(PARAMETERS.panoramaDisplayParameters());


        final ChannelPainter distance = panorama::distanceAt, slope = panorama::slopeAt;

        ChannelPainter h = distance.div(100_000).cycling().mul(360);
        ChannelPainter s = distance.div(200_000).clamped().inverted();
        ChannelPainter b = slope.mul(2).div((float) Math.PI).inverted().mul(0.7f).add(0.3f);

        ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

        ImagePainter l = ImagePainter.hsb(h, s, b, opacity);

        final Image background = PanoramaRenderer.renderPanorama(panorama, l);
        final Group group = new Group();
        group.getChildren().add(new ImageView(background));
        group.getChildren().addAll(labelizer.labels(PARAMETERS.panoramaDisplayParameters()));
        final Scene scene = new Scene(group, PARAMETERS.width(), PARAMETERS.height());

        stage.setScene(scene);
        stage.show();

        //ImageIO.write(SwingFXUtils.fromFXImage(scene.snapshot(img), null), "png", new File("res/actual/temp.png"));

        //Platform.exit();
    }
}
