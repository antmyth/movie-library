package com.apn.fixtures;

import java.util.Random;

@SuppressWarnings("UnusedDeclaration")
public class TestDataFixtures {
    private static Random random = new Random();

    public static String someString() {
        return someStringOfLength(someNumberBetween(8, 12));
    }

    public static int someNumberBetween(int inclusiveStart, int inclusiveEnd) {
        return random.nextInt(inclusiveEnd - inclusiveStart + 1) + inclusiveStart;
    }

    public static String someStringOfLength(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int index = 0; index < length; index++) {
            sb.append(someCapitalLetter());
        }
        return sb.toString();
    }

    public static String someCapitalLetter() {
        return String.valueOf((char) (random.nextInt(26) + 'A'));
    }

}
