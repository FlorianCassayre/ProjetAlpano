package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

public class PanoramaComputerBean
{
    private final ObjectProperty<Panorama> panorama;
    private final ObjectProperty<PanoramaUserParameters> parameters;
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private final ObjectProperty<ObservableList<Node>> labels = new SimpleObjectProperty<>();

    public PanoramaComputerBean(Panorama panorama, PanoramaUserParameters parameters)
    {
        this.panorama = new SimpleObjectProperty<>(panorama);
        this.parameters = new SimpleObjectProperty<>(parameters);
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
        return FXCollections.unmodifiableObservableList(labels.get());
    }
}
