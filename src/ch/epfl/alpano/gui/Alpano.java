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
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class Alpano extends Application
{
    private static final String FILE_SUMMITS = "alps.txt";

    private static final String OSM_PROTOCOL = "http", OSM_DOMAIN = "www.openstreetmap.org";
    private static final String TEXT_PARAMETERS_MODIFIED = "Les paramètres du panorama ont changé.\nCliquez ici pour mettre le dessin à jour.";
    private static final String TEXT_PANORAMA_COMPUTING = "Calcul du panorama en cours...\nVeuillez patienter.";

    private static final PanoramaUserParameters INITIAL_PANORAMA = PredefinedPanoramas.ALPES_JURA;

    private List<Summit> summits;
    private ContinuousElevationModel cdem;

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        loadData();

        final PanoramaParametersBean parametersBean = new PanoramaParametersBean(INITIAL_PANORAMA);
        final PanoramaComputerBean computerBean = new PanoramaComputerBean(cdem, INITIAL_PANORAMA, summits);


        final BorderPane root = new BorderPane();

        final Scene scene = new Scene(root);

        final StackPane panoPane = new StackPane();
        final GridPane paramsGrid = new GridPane();
        final BorderPane splitPane = new BorderPane();
        final ProgressBar progressBar = new ProgressBar();


        root.setCenter(panoPane);
        root.setBottom(splitPane);

        splitPane.setTop(progressBar);
        splitPane.setBottom(paramsGrid);

        final TextArea textArea = new TextArea();


        final ImageView panoView = new ImageView();
        panoView.preserveRatioProperty().setValue(true);
        panoView.smoothProperty().setValue(true);
        panoView.fitWidthProperty().bind(parametersBean.widthProperty());
        panoView.imageProperty().bind(computerBean.imageProperty());


        panoView.setOnMouseMoved(e ->
        {
            final int supersampling = computerBean.getParameters().supersamplingExponent();

            final int x = ((int) Math.round(e.getX()) << supersampling), y = ((int) Math.round(e.getY()) << supersampling);
            final Panorama panorama = computerBean.getPanorama();
            final double longitude = Math.toDegrees(panorama.longitudeAt(x, y)), latitude = Math.toDegrees(panorama.latitudeAt(x, y));
            final double distance = panorama.distanceAt(x, y) / 1000.0;
            final int elevation = Math.round(panorama.elevationAt(x, y));
            final double azimuth = Math.toDegrees(Azimuth.toMath(computerBean.getParameters().panoramaParameters().azimuthForX(x))), verticalAngle = Math.toDegrees(computerBean.getParameters().panoramaParameters().altitudeForY(y));

            final String octantAzimuth = Azimuth.toOctantString(Math.toRadians(azimuth), "N", "E", "S", "O");

            final StringBuilder builder = new StringBuilder();
            builder.append("Position : ").append(String.format((Locale) null, "%.4f", longitude)).append("°N ").append(String.format((Locale) null, "%.4f", latitude)).append("°E\n");
            builder.append("Distance : ").append(String.format((Locale) null, "%.1f", distance)).append(" km\n");
            builder.append("Altitude : ").append(elevation).append(" m\n");
            builder.append("Azimuth : ").append(String.format((Locale) null, "%.1f", azimuth)).append("° (").append(octantAzimuth).append(") Elévation : ").append(String.format((Locale) null, "%.1f", verticalAngle)).append("°");

            textArea.setText(builder.toString());
        });

        panoView.setOnMouseClicked(e ->
        {
            final int supersampling = computerBean.getParameters().supersamplingExponent();

            final int x = ((int) Math.round(e.getX()) << supersampling), y = ((int) Math.round(e.getY()) << supersampling);
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
        Bindings.bindContent(labelsPane.getChildren(), computerBean.getLabels());


        final StackPane updateNotice = new StackPane();
        updateNotice.setBackground(new Background(new BackgroundFill(Color.gray(1.0, 0.9), null, null)));

        final Font font = new Font(40.0);

        final Text updateText = new Text(TEXT_PARAMETERS_MODIFIED);
        updateText.setFont(font);
        updateText.setTextAlignment(TextAlignment.CENTER);
        updateNotice.getChildren().add(updateText);

        final Text computingText = new Text(TEXT_PANORAMA_COMPUTING);
        computingText.setFont(font);
        computingText.setTextAlignment(TextAlignment.CENTER);
        updateNotice.getChildren().add(computingText);

        updateText.visibleProperty().bind(computerBean.parametersProperty().isNotEqualTo(parametersBean.parametersProperty()).and(computerBean.computingProperty().not()));
        computingText.visibleProperty().bind(computerBean.computingProperty());

        updateNotice.visibleProperty().bind(updateText.visibleProperty().or(computingText.visibleProperty()));


        updateNotice.setOnMouseClicked(event ->
        {
            if(!computerBean.computingProperty().getValue())
                computerBean.setParameters(parametersBean.parametersProperty().get());
        });

        final StackPane panoGroup = new StackPane();
        panoGroup.getChildren().addAll(panoView, labelsPane);

        final ScrollPane panoScrollPane = new ScrollPane(panoGroup);

        panoPane.getChildren().addAll(panoScrollPane, updateNotice);



        addParameterToGrid(paramsGrid, "Latitude (°) :", createTextField(new FixedPointStringConverter(4), parametersBean.observerLatitudeProperty(), 7), 0, 0);
        addParameterToGrid(paramsGrid, "Longitude (°) :", createTextField(new FixedPointStringConverter(4), parametersBean.observerLongitudeProperty(), 7), 1, 0);
        addParameterToGrid(paramsGrid, "Altitude (m) :", createTextField(new FixedPointStringConverter(0), parametersBean.observerElevationProperty(), 4), 2, 0);
        addParameterToGrid(paramsGrid, "Azimuth (°) :", createTextField(new FixedPointStringConverter(0), parametersBean.centerAzimuthProperty(), 3), 0, 1);
        addParameterToGrid(paramsGrid, "Angle de vue (°) :", createTextField(new FixedPointStringConverter(0), parametersBean.horizontalFieldOfViewProperty(), 3), 1, 1);
        addParameterToGrid(paramsGrid, "Visibilité (km) :", createTextField(new FixedPointStringConverter(0), parametersBean.maxDistanceProperty(), 3), 2, 1);
        addParameterToGrid(paramsGrid, "Largeur (px) :", createTextField(new FixedPointStringConverter(0), parametersBean.widthProperty(), 4), 0, 2);
        addParameterToGrid(paramsGrid, "Hauteur (px) :", createTextField(new FixedPointStringConverter(0), parametersBean.heightProperty(), 4), 1, 2);


        final ChoiceBox<Integer> choiceBox = new ChoiceBox<>();
        choiceBox.setConverter(new LabeledListStringConverter("non", "2×", "4×"));
        choiceBox.setItems(FXCollections.observableArrayList(0, 1, 2));
        choiceBox.valueProperty().bindBidirectional(parametersBean.superSamplingExponentProperty());
        addParameterToGrid(paramsGrid, "Suréchantillonnage :", choiceBox, 2, 2);

        final Label predefinedTitle = new Label("Panoramas prédéfinis");
        GridPane.setHalignment(predefinedTitle, HPos.CENTER);
        paramsGrid.add(predefinedTitle, 8, 0);

        final ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(PredefinedPanoramas.LIST.keySet())));
        listView.maxWidthProperty().bind(predefinedTitle.widthProperty());
        listView.setPrefHeight(0);
        listView.setOnMouseClicked(event ->
        {
            final String key = listView.getSelectionModel().getSelectedItem();
            parametersBean.setAll(PredefinedPanoramas.LIST.get(key));
        });
        paramsGrid.add(listView, 8, 1, 8, 2);


        textArea.setEditable(false);
        textArea.setPrefRowCount(2);
        paramsGrid.add(textArea, 9, 0, 9, 3);


        final ChoiceBox<Integer> choiceShowLabels = new ChoiceBox<>();
        choiceShowLabels.setConverter(new LabeledListStringConverter("non", "oui"));
        choiceShowLabels.setItems(FXCollections.observableArrayList(0, 1));
        choiceShowLabels.setValue(1);
        labelsPane.visibleProperty().bind(choiceShowLabels.valueProperty().isEqualTo(1));
        addParameterToGrid(paramsGrid, "Étiquettes :", choiceShowLabels, 3, 0);

        final ChoiceBox<Integer> choicePainter = new ChoiceBox<>();
        choicePainter.setConverter(new LabeledListStringConverter("couleur", "noir et blanc", "bordure uniquement"));
        choicePainter.valueProperty().bindBidirectional(parametersBean.painterProperty());
        choicePainter.setItems(FXCollections.observableArrayList(0, 1, 2));

        addParameterToGrid(paramsGrid, "Style :", choicePainter, 3, 1);

        final Button saveImage = new Button("...");
        saveImage.setOnMouseClicked(event ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir le répertoire de destination de l'image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image PNG (*.png)", "*.png"));
            final File file = fileChooser.showSaveDialog(stage);

            if(file != null)
            {
                try
                {
                    final WritableImage image = panoGroup.snapshot(new SnapshotParameters(), null);
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                }
                catch(IOException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setContentText("Impossible d'écrire le fichier sur le disque.");
                    alert.showAndWait();

                    e.printStackTrace();
                }
            }
        });
        addParameterToGrid(paramsGrid, "Sauvegarder :", saveImage, 3, 2);


        paramsGrid.setHgap(10);
        paramsGrid.setVgap(3);
        paramsGrid.setPadding(new Insets(3, 3, 3, 3));
        paramsGrid.setAlignment(Pos.CENTER);


        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.progressProperty().bind(computerBean.progressProperty());


        stage.setTitle("Alpano");
        stage.setScene(scene);
        stage.show();
    }

    private void addParameterToGrid(GridPane gridPane, String name, Node node, int column, int row)
    {
        final Label label = new Label(name);
        GridPane.setHalignment(label, HPos.RIGHT);
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
