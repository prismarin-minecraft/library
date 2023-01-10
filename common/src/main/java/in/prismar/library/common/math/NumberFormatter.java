package in.prismar.library.common.math;

import java.text.DecimalFormat;

public final class NumberFormatter {

    private static final DecimalFormat TWO_DIGITS_FORMAT = new DecimalFormat("0.00");
    private static final DecimalFormat THREE_DIGITS_FORMAT = new DecimalFormat("0.000");
    private static final DecimalFormat ONE_DIGIT_FORMAT = new DecimalFormat("0.0");

    public static String formatDoubleToThreeDigits(double value) {
        return THREE_DIGITS_FORMAT.format(value);
    }

    public static String formatDoubleToTwoDigits(double value) {
        return TWO_DIGITS_FORMAT.format(value);
    }

    public static String formatDoubleToOneDigits(double value) {
        return ONE_DIGIT_FORMAT.format(value);
    }

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