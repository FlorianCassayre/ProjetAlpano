package ch.epfl.alpano.gui;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

public final class Alpano extends Application
{
    private static final String FILE_SUMMITS = "alps.txt";

    private static final String OSM_PROTOCOL = "http", OSM_DOMAIN = "www.openstreetmap.org";
    private static final String TEXT_PARAMETERS_MODIFIED = "Les paramètres du panorama ont changé.\nCliquez ici pour mettre le dessin à jour.";

    private static final PanoramaUserParameters INITIAL_PANORAMA = PredefinedPanoramas.ALPES_JURA;

    private List<Summit> summits;
    private ContinuousElevationModel cdem;

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        loadData();

        final PanoramaParametersBean parametersBean = new PanoramaParametersBean(INITIAL_PANORAMA);
        final PanoramaComputerBean computerBean = new PanoramaComputerBean(cdem, INITIAL_PANORAMA, summits); // FIXME



        final BorderPane root = new BorderPane();

        final StackPane panoPane = new StackPane();
        final GridPane paramsGrid = new GridPane();

        root.setCenter(panoPane);
        root.setBottom(paramsGrid);


        final TextArea textArea = new TextArea();


        {

            final ImageView panoView = new ImageView();
            panoView.preserveRatioProperty().setValue(true);
            panoView.smoothProperty().setValue(true);
            panoView.fitWidthProperty().bind(parametersBean.widthProperty());
            panoView.imageProperty().bind(computerBean.imageProperty());


            panoView.setOnMouseMoved(e ->
            {
                final int x = (int) Math.round(e.getX()), y = (int) Math.round(e.getY());
                final Panorama panorama = computerBean.getPanorama();
                final double longitude = Math.toDegrees(panorama.longitudeAt(x, y)), latitude = Math.toDegrees(panorama.latitudeAt(x, y));
                final double distance = panorama.distanceAt(x, y) / 1000.0;
                final int elevation = Math.round(panorama.elevationAt(x, y));
                final double azimuth = Math.toDegrees(Azimuth.toMath(computerBean.getParameters().panoramaDisplayParameters().azimuthForX(x))), verticalAngle = Math.toDegrees(computerBean.getParameters().panoramaDisplayParameters().altitudeForY(y));

                final StringBuilder builder = new StringBuilder();
                builder.append("Position : ").append(String.format((Locale) null, "%.4f", longitude)).append("°N ").append(String.format((Locale) null, "%.4f", latitude)).append("°E\n");
                builder.append("Distance : ").append(String.format((Locale) null, "%.1f", distance)).append(" km\n");
                builder.append("Altitude : ").append(elevation).append(" m\n");
                builder.append("Azimuth : ").append(String.format((Locale) null, "%.1f", azimuth)).append("° (S) Elévation : ").append(String.format((Locale) null, "%.1f", verticalAngle)).append("°");

                textArea.setText(builder.toString());
            });

            panoView.setOnMouseClicked(e ->
            {
                final int x = (int) Math.round(e.getX()), y = (int) Math.round(e.getY());
                final double longitude = Math.toDegrees(computerBean.getPanorama().longitudeAt(x, y)), latitude = Math.toDegrees(computerBean.getPanorama().latitudeAt(x, y));

                final String qy = "mlat=" + latitude + "&mlon=" + longitude;
                final String fg = "map=15/" + latitude + "/" + longitude;

                try
                {
                    final URI osmURI = new URI(OSM_PROTOCOL, OSM_DOMAIN, "/", qy, fg);
                    java.awt.Desktop.getDesktop().browse(osmURI);
                }
                catch(URISyntaxException | IOException e1)
                {
                    e1.printStackTrace();
                }
            });

            final Pane labelsPane = new Pane();
            labelsPane.prefWidthProperty().bind(parametersBean.widthProperty());
            labelsPane.prefHeightProperty().bind(parametersBean.heightProperty());
            labelsPane.setMouseTransparent(true);
            Bindings.bindContent(labelsPane.getChildren(), computerBean.getLabels()); // FIXME (NPE)


            final StackPane updateNotice = new StackPane();
            updateNotice.setBackground(new Background(new BackgroundFill(Color.gray(1.0, 0.9), null, null)));
            final Text text = new Text(TEXT_PARAMETERS_MODIFIED);
            text.setFont(new Font(40.0));
            text.setTextAlignment(TextAlignment.CENTER);
            updateNotice.getChildren().add(text);

            updateNotice.visibleProperty().bind(parametersBean.parametersProperty().isNotEqualTo(computerBean.getParameters()));

            updateNotice.setOnMouseClicked(event -> computerBean.setParameters(parametersBean.parametersProperty().get()));

            final StackPane panoGroup = new StackPane();
            panoGroup.getChildren().addAll(panoView, labelsPane);

            final ScrollPane panoScrollPane = new ScrollPane(panoGroup);

            panoPane.getChildren().addAll(panoScrollPane, updateNotice);
        }


        {
            final TextField f = createTextField(new FixedPointStringConverter(4), parametersBean.observerLatitudeProperty(), 7);
            addParameterToGrid(paramsGrid, "Latitude (°) :", f, 0, 0);
            addParameterToGrid(paramsGrid, "Longitude (°) :", createTextField(new FixedPointStringConverter(4), parametersBean.observerLongitudeProperty(), 7), 1, 0);
            addParameterToGrid(paramsGrid, "Altitude (m) :", createTextField(new FixedPointStringConverter(0), parametersBean.observerElevationProperty(), 4), 2, 0);
            addParameterToGrid(paramsGrid, "Azimuth (°) :", createTextField(new FixedPointStringConverter(0), parametersBean.centerAzimuthProperty(), 3), 0, 1);
            addParameterToGrid(paramsGrid, "Angle de vue (°) :", createTextField(new FixedPointStringConverter(0), parametersBean.horizontalFieldOfViewProperty(), 3), 1, 1);
            addParameterToGrid(paramsGrid, "Visibilité (km) :", createTextField(new FixedPointStringConverter(0), parametersBean.maxDistanceProperty(), 3), 2, 1);
            addParameterToGrid(paramsGrid, "Largeur (px) :", createTextField(new FixedPointStringConverter(0), parametersBean.widthProperty(), 4), 0, 2);
            addParameterToGrid(paramsGrid, "Hauteur (px) :", createTextField(new FixedPointStringConverter(0), parametersBean.heightProperty(), 4), 1, 2);


            final ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
            final StringConverter stringConverter = new LabeledListStringConverter("non", "2×", "4×");
            choiceBox.setConverter(stringConverter);
            choiceBox.valueProperty().bindBidirectional(parametersBean.superSamplingExponentProperty());
            addParameterToGrid(paramsGrid, "Suréchantillonnage :", choiceBox, 2, 2);

            textArea.setEditable(false);
            textArea.setPrefRowCount(2);
            paramsGrid.add(textArea, 6, 0);
        }




        final Scene scene = new Scene(root);

        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addParameterToGrid(GridPane gridPane, String name, Node node, int column, int row)
    {
        final Label label = new Label(name);
        gridPane.add(label, column * 2, row);
        gridPane.add(node, column * 2 + 1, row);
    }

    private TextField createTextField(StringConverter<Integer> converter, ObjectProperty<Integer> property, int columns)
    {
        final TextField field = new TextField("0");
        field.setAlignment(Pos.CENTER_RIGHT);
        field.setPrefColumnCount(columns);
        field.setText("0"); // FIXME

        field.setText(converter.toString(property.get()));

        final TextFormatter<Integer> formatter = new TextFormatter<>(converter);
        field.setTextFormatter(formatter);

        formatter.valueProperty().bindBidirectional(property);

        return field;
    }

    private void loadData() throws IOException
    {
        summits = GazetteerParser.readSummitsFrom(new File(FILE_SUMMITS));

        DiscreteElevationModel composition1 = null;

        for(int i = 45; i < 47; i++)
        {
            DiscreteElevationModel composition2 = null;

            for(int j = 6; j < 10; j++)
            {
                final DiscreteElevationModel dem = new HgtDiscreteElevationModel(new File("N" + i + "E00" + j + ".hgt"));

                if(composition2 == null)
                    composition2 = dem;
                else
                    composition2 = composition2.union(dem);
            }

            if(composition1 == null)
                composition1 = composition2;
            else
                composition1 = composition2.union(composition2);
        }

        cdem = new ContinuousElevationModel(composition1);
    }
}