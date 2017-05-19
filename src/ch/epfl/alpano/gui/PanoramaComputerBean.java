package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Objects;

/**
 * A bean for the panorama computer.
 */
public class PanoramaComputerBean
{
    private final PanoramaComputer computer;
    private final Labelizer labelizer;
    private final ReadOnlyObjectWrapper<Panorama> panorama = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<PanoramaUserParameters> parameters = new SimpleObjectProperty<>();
    private final ReadOnlyObjectWrapper<Image> image = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<ObservableList<Node>> labels;

    /**
     * Creates a new bean from a continuous elevation model, a list of summits and some initial panorama parameters.
     * @param cDEM the continuous elevation model
     * @param parameters the panorama user parameters
     * @param summits the list of summits
     */
    public PanoramaComputerBean(ContinuousElevationModel cDEM, PanoramaUserParameters parameters, List<Summit> summits)
    {
        this.computer = new PanoramaComputer(cDEM);
        this.labelizer = new Labelizer(cDEM, summits);

        final ObservableList<Node> list = FXCollections.observableArrayList();
        this.labels = new ReadOnlyObjectWrapper<>(FXCollections.unmodifiableObservableList(list));

        this.parameters.addListener((observable, oldValue, newValue) ->
        {
            final Panorama p = computer.computePanorama(newValue.panoramaParameters());
            panorama.set(p);

            final ChannelPainter distance = p::distanceAt, slope = p::slopeAt;

            ChannelPainter h = distance.div(100_000).cycling().mul(360);
            ChannelPainter s = distance.div(200_000).clamped().inverted();
            ChannelPainter b = slope.mul(2).div((float) Math.PI).inverted().mul(0.7f).add(0.3f);

            ChannelPainter opacity = distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

            ImagePainter painter = ImagePainter.hsb(h, s, b, opacity);

            final Image i = PanoramaRenderer.renderPanorama(p, painter);
            image.set(i);

            final List<Node> l = labelizer.labels(newValue.panoramaDisplayParameters());
            list.setAll(l);
        });

        this.parameters.set(Objects.requireNonNull(parameters));
    }

    /**
     * The panorama user parameters property.
     * When the value is modified, the panorama will be re-computed.
     * @return the panorama user parameters property
     */
    public ObjectProperty<PanoramaUserParameters> parametersProperty()
    {
        return parameters;
    }

    /**
     * Shortened call to {@link #parametersProperty() parametersProperty()}.{@link ObjectProperty#get() get()}.
     * @return the value of the panorama user parameters property
     */
    public PanoramaUserParameters getParameters()
    {
        return parameters.get();
    }

    /**
     * Shortened call to {@link #parametersProperty() parametersProperty()}.{@link SimpleObjectProperty#set(Object) set(PanoramaUserParameters)}.
     * @param newParameters the value of the new panorama user proeprties
     */
    public void setParameters(PanoramaUserParameters newParameters)
    {
        parameters.set(newParameters);
    }

    /**
     * Read-only panorama property.
     * @return the panorama property
     */
    public ReadOnlyObjectProperty<Panorama> panoramaProperty()
    {
        return panorama.getReadOnlyProperty();
    }

    /**
     * Shortened call to {@link #panoramaProperty() panoramaProperty()}.{@link ObjectProperty#get() get()}.
     * @return the value of the panorama property
     */
    public Panorama getPanorama()
    {
        return panorama.get();
    }

    /**
     * Read-only image property.
     * @return the image property
     */
    public ReadOnlyObjectProperty<Image> imageProperty()
    {
        return image.getReadOnlyProperty();
    }

    /**
     * Shortened call to {@link #imageProperty() imageProperty()}.{@link ObjectProperty#get() get()}.
     * @return the value of the image property
     */
    public Image getImage()
    {
        return image.get();
    }

    /**
     * Read-only observable list of nodes property.
     * @return the observable list property
     */
    public ReadOnlyObjectProperty<ObservableList<Node>> labelsProperty()
    {
        return labels.getReadOnlyProperty();
    }

    /**
     * Shortened call to {@link #labelsProperty() labelsProperty()}.{@link ObjectProperty#get() get()}.
     * @return the value of the labels list property
     */
    public ObservableList<Node> getLabels()
    {
        return labels.get();
    }
}
