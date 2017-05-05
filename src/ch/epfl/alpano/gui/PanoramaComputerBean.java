package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.util.List;

public class PanoramaComputerBean
{
    private Panorama panorama;
    private PanoramaUserParameters parameters;
    private Image image;
    private List<Node> labels;

    public ObjectProperty<PanoramaUserParameters> parametersProperty()
    {
        return new SimpleObjectProperty<>(parameters);
    }

    public PanoramaUserParameters getParameters()
    {
        return parameters;
    }

    public void setParameters(PanoramaUserParameters newParameters)
    {
        this.parameters = newParameters;
    }

    public ReadOnlyObjectProperty<Panorama> panoramaProperty()
    {
        return new ReadOnlyObjectWrapper<>(panorama);
    }

    public Panorama getPanorama()
    {
        return panorama;
    }

    public ReadOnlyObjectProperty<Image> imageProperty()
    {
        return new ReadOnlyObjectWrapper<>(image);
    }

    public Image getImage()
    {
        return image;
    }

    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProperty()
    {
        return new ReadOnlyObjectWrapper<>(FXCollections.observableArrayList(labels));
    }

    public ObservableList<Node> getLabels()
    {
        return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(labels));
    }
}
