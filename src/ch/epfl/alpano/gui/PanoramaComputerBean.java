package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.util.List;

public class PanoramaComputerBean
{
    private final PanoramaComputer computer;
    private final Labelizer labelizer;
    private final ObjectProperty<Panorama> panorama = new SimpleObjectProperty<>();
    private final ObjectProperty<PanoramaUserParameters> parameters = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private final ObjectProperty<ObservableList<Node>> labels;

    public PanoramaComputerBean(ContinuousElevationModel cDEM, PanoramaUserParameters parameters, List<Summit> summits)
    {
        this.computer = new PanoramaComputer(cDEM);
        this.labelizer = new Labelizer(cDEM, summits);

        final ObservableList<Node> list = FXCollections.observableArrayList();
        this.labels = new SimpleObjectProperty<>(FXCollections.unmodifiableObservableList(list));

        this.parameters.addListener((observable, oldValue, newValue) ->
        {
            final Panorama p = computer.computePanorama(newValue.panoramaParameters());
            panorama.set(p);

            final ImagePainter painter;
            switch(newValue.painter())
            {
                case 0:
                    painter = PanoramaRenderer.coloredImagePainter(p);
                    break;
                case 1:
                    painter = PanoramaRenderer.blackWhiteBorderedImagePainter(p);
                    break;
                default:
                    painter = PanoramaRenderer.borderedImagePainter(p);
            }

            final Image i = PanoramaRenderer.renderPanorama(p, painter);
            image.set(i);

            final List<Node> l = labelizer.labels(newValue.panoramaDisplayParameters());
            list.setAll(l);
        });

        this.parameters.set(parameters);
    }

    public ObjectProperty<PanoramaUserParameters> parametersProperty()
    {
        return parameters;
    }

    public PanoramaUserParameters getParameters()
    {
        return parameters.get();
    }

    public void setParameters(PanoramaUserParameters newParameters)
    {
        parameters.set(newParameters);
    }

    public ReadOnlyObjectProperty<Panorama> panoramaProperty()
    {
        return panorama;
    }

    public Panorama getPanorama()
    {
        return panorama.get();
    }

    public ReadOnlyObjectProperty<Image> imageProperty()
    {
        return image;
    }

    public Image getImage()
    {
        return image.get();
    }

    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProperty()
    {
        return labels;
    }

    public ObservableList<Node> getLabels()
    {
        return labels.get();
    }
}
