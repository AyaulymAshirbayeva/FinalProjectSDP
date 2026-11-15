package language_learning_app.factory.products;

import language_learning_app.models.Word;

public final class FillBlankExercise extends ExerciseAbstract {
    private final String sentence;

    public FillBlankExercise(Word word) {
        super(word);
        sentence = ExerciseText.requireText(word.getExampleSentence(), "Example sentence");
        int gap = sentence.indexOf("___");
        if (gap < 0 || sentence.indexOf("___", gap + 1) >= 0) {
            throw new IllegalArgumentException("Example sentence must contain exactly one ___ gap.");
        }
    }

    @Override
    public String getPrompt() {
        return "Fill in the blank in " + getWord().getLanguage() + ": " + sentence;
    }
}
