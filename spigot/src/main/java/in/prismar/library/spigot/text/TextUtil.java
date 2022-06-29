package in.prismar.library.spigot.text;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class TextUtil {

    public static String getArgsFrom(String[] args, int from) {
        StringBuilder builder = new StringBuilder();
        for (int i = from; i < args.length; i++) {
            if(i >= args.length-1) {
                builder.append(args[i]);
            } else {
                builder.append(args[i]).append(" ");
            }
        }
        return builder.toString();
    }
}