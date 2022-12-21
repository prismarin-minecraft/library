package in.prismar.library.spigot.text;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ColoredMessage {

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    public static String rgbGradient(String str, Color from, Color to, Interpolator interpolator) {
        final double[] red = interpolator.interpolate(from.getRed(), to.getRed(), str.length());
        final double[] green = interpolator.interpolate(from.getGreen(), to.getGreen(), str.length());
        final double[] blue = interpolator.interpolate(from.getBlue(), to.getBlue(), str.length());

        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            builder.append(ChatColor.of(new Color(
                            (int) Math.round(red[i]),
                            (int) Math.round(green[i]),
                            (int) Math.round(blue[i]))))
                    .append(str.charAt(i));
        }
        return builder.toString();
    }

    public static String rgbGradientLinear(String str, Color from, Color to) {
        return rgbGradient(str, from, to, LINEAR_INTERPOLATOR);
    }

    public static class LinearInterpolator implements Interpolator {

        @Override
        public double[] interpolate(double from, double to, int max) {
            final double[] res = new double[max];
            for (int i = 0; i < max; i++) {
                res[i] = from + i * ((to - from) / (max - 1));
            }
            return res;
        }
    }

    public interface Interpolator {

        double[] interpolate(double from, double to, int max);

    }
}
