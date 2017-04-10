package ch.epfl.alpano.gui;

/**
 * Represents user parameters. Provides a method to sanitize the values.
 */
public enum UserParameter
{
    OBSERVER_LONGITUDE(60_000, 120_000),
    OBSERVER_LATITUDE(450_000, 480_000),
    OBSERVER_ELEVATION(300, 10_000),
    CENTER_AZIMUTH(0, 359),
    HORIZONTAL_FIELD_OF_VIEW(1, 360),
    MAX_DISTANCE(10, 600),
    WIDTH(30, 16_000),
    HEIGHT(10, 4_000),
    SUPER_SAMPLING_EXPONENT(0, 2);

    private final int min, max;

    UserParameter(int min, int max)
    {
        this.min = min;
        this.max = max;
    }

    public int sanitize(int value)
    {
        return Math.max(Math.min(value, max), min);
    }
}
