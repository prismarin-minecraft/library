package in.prismar.library.common.time;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class TimeUtil {

    public static String convertToThreeDigits(long secs) {
        long hours = secs / 3600;
        long minutes = (secs % 3600) / 60;
        long seconds = (secs % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String convertToTwoDigits(long secs) {
        long minutes = (secs % 3600) / 60;
        long seconds = (secs % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }
}
