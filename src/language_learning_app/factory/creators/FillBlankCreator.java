package language_learning_app.factory.creators;

import language_learning_app.factory.products.ExerciseInterface;
import language_learning_app.factory.products.FillBlankExercise;
import language_learning_app.models.Word;
import java.util.List;
import java.util.Objects;

public final class FillBlankCreator extends CreatorAbstract {
    @Override
    public ExerciseInterface createExercise(Word word, List<Word> vocabulary) {
        Objects.requireNonNull(vocabulary, "Vocabulary must not be null.");
        return new FillBlankExercise(word);
    }
}
