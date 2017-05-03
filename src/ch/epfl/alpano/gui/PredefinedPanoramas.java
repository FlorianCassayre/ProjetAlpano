package ch.epfl.alpano.gui;

/**
 * A class containing preset panorama parameters examples.
 * The presets are namely:
 * <ul>
 *     <li>Niesen</li>
 *     <li>Alpes du Jura</li>
 *     <li>Mont Racine</li>
 *     <li>Finsteraarhorn</li>
 *     <li>Tour de Sauvabelin</li>
 *     <li>Plage du Pélican</li>
 * </ul>
 */
public interface PredefinedPanoramas
{
    PanoramaUserParameters NIESEN = new PanoramaUserParameters(7_6500, 46_7300, 600, 180, 110, 300, 2500, 800, 0);
    PanoramaUserParameters ALPES_JURA = new PanoramaUserParameters(6_8087, 47_0085, 1380, 162, 27, 300, 2500, 800, 0);
    PanoramaUserParameters MONT_RACINE = new PanoramaUserParameters(6_8200, 47_0200, 1500, 135, 45, 300, 2500, 800, 0);
    PanoramaUserParameters FINSTERAARHORN = new PanoramaUserParameters(8_1260, 46_5374, 4300, 205, 20, 300, 2500, 800, 0);
    PanoramaUserParameters TOUR_DE_SAUVABELIN = new PanoramaUserParameters(6_6385, 46_5353, 700, 135, 100, 300, 2500, 800, 0);
    PanoramaUserParameters PLAGE_DU_PELICAN = new PanoramaUserParameters(6_5728, 46_5132, 380, 135, 60, 300, 2500, 800, 0);
}
