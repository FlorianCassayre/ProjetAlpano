package ch.epfl.alpano;

/**
 * An utility interface to simplify arguments checking.
 */
public interface Preconditions
{
    /**
     * Throws an IllegalArgumentException if the condition is not verified.
     * @param condition the condition to check
     */
    static void checkArgument(boolean condition)
    {
        if(!condition)
            throw new IllegalArgumentException();
    }

    /**
     * Throws an IllegalArgumentException with a message if the condition is not verified.
     * @param condition the condition to check
     * @param message the message
     */
    static void checkArgument(boolean condition, String message)
    {
        if(!condition)
            throw new IllegalArgumentException(message);
    }
}
