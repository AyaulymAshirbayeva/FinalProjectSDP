package LanguageLearningApp.tests;

import java.util.Objects;

final class Checks {
    private static int count;

    private Checks() {
    }

    static void that(boolean condition, String message) {
        count++;
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    static void equal(Object expected, Object actual, String message) {
        that(Objects.equals(expected, actual), message + " (expected " + expected + ", got " + actual + ")");
    }

    static void throwsType(Class<? extends Throwable> type, Runnable action, String message) {
        count++;
        try {
            action.run();
        } catch (Throwable error) {
            if (type.isInstance(error)) {
                return;
            }
            throw new AssertionError(message + ": unexpected exception", error);
        }
        throw new AssertionError(message + ": expected " + type.getSimpleName());
    }

    static void report(String suite) {
        System.out.println(suite + ": " + count + " checks passed.");
    }
}
