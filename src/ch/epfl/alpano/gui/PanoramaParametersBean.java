package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;

import java.util.EnumMap;
import java.util.Map;

import static javafx.application.Platform.runLater;

public class PanoramaParametersBean
{
    private final ReadOnlyObjectWrapper<PanoramaUserParameters> parameters;

    private final Map<UserParameter, ObjectProperty<Integer>> properties = new EnumMap<>(UserParameter.class);

    public PanoramaParametersBean(PanoramaUserParameters parameters)
    {
        this.parameters = new ReadOnlyObjectWrapper<>(parameters);

        for(UserParameter parameter : UserParameter.values())
        {
            final ObjectProperty<Integer> property = new SimpleObjectProperty<>(parameters.get(parameter));
            properties.put(parameter, property);

            property.addListener((b, o, n) -> runLater(this::synchronizeParameters));
        }
    }

    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty()
    {
        return parameters;
    }

    public void setAll(PanoramaUserParameters parameters)
    {
        for(UserParameter p : UserParameter.values())
        {
            this.properties.get(p).set(parameters.get(p));
        }
    }

    public ObjectProperty<Integer> observerLongitudeProperty()
    {
        return properties.get(UserParameter.OBSERVER_LONGITUDE);
    }

    public ObjectProperty<Integer> observerLatitudeProperty()
    {
        return properties.get(UserParameter.OBSERVER_LATITUDE);
    }

    public ObjectProperty<Integer> observerElevationProperty()
    {
        return properties.get(UserParameter.OBSERVER_ELEVATION);
    }

    public ObjectProperty<Integer> centerAzimuthProperty()
    {
        return properties.get(UserParameter.CENTER_AZIMUTH);
    }

    public ObjectProperty<Integer> horizontalFieldOfViewProperty()
    {
        return properties.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }

    public ObjectProperty<Integer> maxDistanceProperty()
    {
        return properties.get(UserParameter.MAX_DISTANCE);
    }

    public ObjectProperty<Integer> widthProperty()
    {
        return properties.get(UserParameter.WIDTH);
    }

    public ObjectProperty<Integer> heightProperty()
    {
        return properties.get(UserParameter.HEIGHT);
    }

    public ObjectProperty<Integer> superSamplingExponentProperty()
    {
        return properties.get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }

    public ObjectProperty<Integer> painterProperty()
    {
        return properties.get(UserParameter.PAINTER);
    }

    private void synchronizeParameters()
    {
        final Map<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
        for(UserParameter parameter : UserParameter.values())
        {
            map.put(parameter, properties.get(parameter).getValue());
        }

        parameters.set(new PanoramaUserParameters(map));

        for(UserParameter parameter : UserParameter.values())
        {
            properties.get(parameter).set(parameters.getValue().get(parameter));
        }
    }
}
