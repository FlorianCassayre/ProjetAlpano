package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;

import java.util.EnumMap;
import java.util.Map;

import static javafx.application.Platform.runLater;

/**
 * A bean for the panorama parameters.
 */
public class PanoramaParametersBean
{
    private final ReadOnlyObjectWrapper<PanoramaUserParameters> parameters;

    private final Map<UserParameter, ObjectProperty<Integer>> properties = new EnumMap<>(UserParameter.class);

    /**
     * Creates a new bean with some initial parameters.
     * @param parameters the parameters for this bean
     */
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

    /**
     * The panorama parameters property.
     * @return the read-only panorama parameters property
     */
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty()
    {
        return parameters.getReadOnlyProperty();
    }

    /**
     * The observer longitude property.
     * @return the longitude property
     */
    public ObjectProperty<Integer> observerLongitudeProperty()
    {
        return properties.get(UserParameter.OBSERVER_LONGITUDE);
    }

    /**
     * The observer latitude property.
     * @return the latitude property
     */
    public ObjectProperty<Integer> observerLatitudeProperty()
    {
        return properties.get(UserParameter.OBSERVER_LATITUDE);
    }

    /**
     * The observer elevation property.
     * @return the elevation property
     */
    public ObjectProperty<Integer> observerElevationProperty()
    {
        return properties.get(UserParameter.OBSERVER_ELEVATION);
    }

    /**
     * The center azimuth property.
     * @return the center azimuth property
     */
    public ObjectProperty<Integer> centerAzimuthProperty()
    {
        return properties.get(UserParameter.CENTER_AZIMUTH);
    }

    /**
     * The horizontal field of view property.
     * @return the horizontal field of view property
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty()
    {
        return properties.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }

    /**
     * The maximum distance property.
     * @return the maximum distance property.
     */
    public ObjectProperty<Integer> maxDistanceProperty()
    {
        return properties.get(UserParameter.MAX_DISTANCE);
    }

    /**
     * The width property.
     * @return the width property
     */
    public ObjectProperty<Integer> widthProperty()
    {
        return properties.get(UserParameter.WIDTH);
    }

    /**
     * The height property.
     * @return the height property
     */
    public ObjectProperty<Integer> heightProperty()
    {
        return properties.get(UserParameter.HEIGHT);
    }

    /**
     * The supersampling property.
     * @return the supersampling property
     */
    public ObjectProperty<Integer> superSamplingExponentProperty()
    {
        return properties.get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }

    /**
     * Replaces the instance of the parameters property.
     */
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
