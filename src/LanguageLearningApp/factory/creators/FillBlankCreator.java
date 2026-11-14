package LanguageLearningApp.factory.creators;

import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.factory.products.FillBlankExercise;
import LanguageLearningApp.models.Word;
import java.util.List;
import java.util.Objects;

public final class FillBlankCreator extends CreatorAbstract {
    @Override
    public ExerciseInterface createExercise(Word word, List<Word> vocabulary) {
        Objects.requireNonNull(vocabulary, "Vocabulary must not be null.");
        return new FillBlankExercise(word);
    }
}
