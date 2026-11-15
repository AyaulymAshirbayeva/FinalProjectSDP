package language_learning_app.factory.products;

import java.text.Normalizer;
import java.util.Locale;

public final class ExerciseText {
    private ExerciseText() {
    }

    public static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank.");
        }
        return value.strip();
    }

    public static String normalize(String value) {
        return value == null ? "" : Normalizer.normalize(value.strip(), Normalizer.Form.NFC)
                .toLowerCase(Locale.ROOT);
    }
}
