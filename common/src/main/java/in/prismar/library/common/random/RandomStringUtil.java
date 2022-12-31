package in.prismar.library.common.random;

import in.prismar.library.common.math.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class RandomStringUtil {

    private static final char[] ALPHABET_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUFWXYZabcdefghijklmnopqrstufwxyz".toCharArray();
    private static final char[] NUMBER_CHARACTERS = "1234567890".toCharArray();
    private static final char[] SYMBOL_CHARACTERS = "@#-*+'<>/&$§%=}{[]()?!;:,._~".toCharArray();

    public static String generateString(int length, boolean numbers, boolean symbols) {
        StringBuilder builder = new StringBuilder();
        List<char[]> chars = new ArrayList<>();
        /** Register **/
        chars.add(ALPHABET_CHARACTERS);
        if(numbers) {
            chars.add(NUMBER_CHARACTERS);
        }
        if(symbols) {
            chars.add(SYMBOL_CHARACTERS);
        }
        for (int i = 0; i < length; i++) {
            char[] characters = chars.get(MathUtil.random(chars.size()-1));
            builder.append(characters[MathUtil.random(characters.length-1)]);
        }
        return builder.toString();
    }


}