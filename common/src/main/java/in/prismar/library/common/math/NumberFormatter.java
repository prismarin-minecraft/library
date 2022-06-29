package in.prismar.library.common.math;

public final class NumberFormatter {

    /**
     * Format number value to thousands
     * Example: 1,000
     *
     * @param value
     * @return
     */
    public static String formatNumberToThousands(long value) {
        return String.format("%,d", new Object[]{Long.valueOf(value)});
    }

    /**
     * Format decimal value to thousands
     *
     * @param value
     * @return
     */
    public static String formatDoubleToThousands(double value) {
        return formatNumberToThousands((long)value);
    }
}