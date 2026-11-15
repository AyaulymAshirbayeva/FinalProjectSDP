package language_learning_app.factory.products;

import language_learning_app.models.Word;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class MultipleChoiceExercise extends ExerciseAbstract {
    private final List<String> options;

    public MultipleChoiceExercise(Word word, List<String> options) {
        super(word);
        Objects.requireNonNull(options, "Options must not be null.");
        if (options.size() < 2) {
            throw new IllegalArgumentException("Multiple choice needs at least two options.");
        }
        this.options = options.stream().map(option -> ExerciseText.requireText(option, "Option")).toList();
        Set<String> unique = new HashSet<>();
        for (String option : this.options) {
            if (!unique.add(ExerciseText.normalize(option))) {
                throw new IllegalArgumentException("Options must be distinct.");
            }
        }
        if (!unique.contains(ExerciseText.normalize(word.getTerm()))) {
            throw new IllegalArgumentException("Options must include the correct term.");
        }
    }

    @Override
    public String getPrompt() {
        return "Translate \"" + getWord().getTranslation() + "\" into " + getWord().getLanguage() + ".";
    }

    @Override
    public List<String> getOptions() {
        return options;
    }

    /** Digits select a one-based option; other input is checked as a term. */
    @Override
    public boolean checkAnswer(String answer) {
        String value = answer == null ? "" : answer.strip();
        if (value.matches("[0-9]+")) {
            try {
                int index = Integer.parseInt(value) - 1;
                return index >= 0 && index < options.size() && super.checkAnswer(options.get(index));
            } catch (NumberFormatException ignored) {
                return false;
            }
        }
        return super.checkAnswer(value);
    }
}
