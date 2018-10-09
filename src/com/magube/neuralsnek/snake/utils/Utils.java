package com.magube.neuralsnek.snake.utils;

public class Utils {

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}
