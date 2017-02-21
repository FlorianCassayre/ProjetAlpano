package ch.epfl.alpano;

/**
 * Represents a mathematical function.
 */
public interface DoubleUnaryOperator
{
    /**
     * Calculate an operation with this function.
     * @param operand the operand
     * @return the result
     */
    double applyAsDouble(double operand);
}
