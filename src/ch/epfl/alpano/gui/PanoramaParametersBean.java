package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PanoramaParametersBean
{
    private final PanoramaUserParameters parameters;

    public PanoramaParametersBean(PanoramaUserParameters parameters)
    {
        this.parameters = parameters;
    }

    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty()
    {
        throw new UnsupportedOperationException();
    }

    public ObjectProperty<Integer> observerLongitudeProperty()
    {
        return new SimpleObjectProperty<>(parameters.observerLongitude());
    }

    public ObjectProperty<Integer> observerLatitudeProperty()
    {
        return new SimpleObjectProperty<>(parameters.observerLatitude());
    }

    public ObjectProperty<Integer> observerElevationProperty()
    {
        return new SimpleObjectProperty<>(parameters.observerElevation());
    }

    public ObjectProperty<Integer> centerAzimuthProperty()
    {
        return new SimpleObjectProperty<>(parameters.centerAzimuth());
    }

    public ObjectProperty<Integer> horizontalFieldOfViewProperty()
    {
        return new SimpleObjectProperty<>(parameters.horizontalFieldOfView());
    }

    public ObjectProperty<Integer> maxDistanceProperty()
    {
        return new SimpleObjectProperty<>(parameters.maxDistance());
    }

    public ObjectProperty<Integer> widthProperty()
    {
        return new SimpleObjectProperty<>(parameters.width());
    }

    public ObjectProperty<Integer> heightProperty()
    {
        return new SimpleObjectProperty<>(parameters.height());
    }

    public ObjectProperty<Integer> superSamplingExponentProperty()
    {
        return new SimpleObjectProperty<>(parameters.supersamplingExponent());
    }
}
