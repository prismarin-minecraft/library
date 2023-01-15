package in.prismar.library.common.text;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Progress {

    private final String template;
    private final int spacers;
    private final String templateChar;
    private final String replaceChar;
    private final boolean showPercentage;

    public Progress(String template, int spacers, String templateChar, String replaceChar, boolean showPercentage) {
        this.template = template;
        this.spacers = spacers;
        this.templateChar = templateChar;
        this.replaceChar = replaceChar;
        this.showPercentage = showPercentage;
    }

    public String show(long current, long max) {
        String pattern = this.template;
        long spaceReplaces = (current * spacers) / max;

        for (int i = 0; i < spaceReplaces; i++) {
            pattern = pattern.replaceFirst(templateChar, replaceChar);
        }
        if(showPercentage) {
            pattern = pattern.replaceFirst("0", Long.toString((current * 100) / max));
        }
        return pattern;
    }
}
