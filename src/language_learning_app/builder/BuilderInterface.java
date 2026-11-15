package language_learning_app.builder;
import language_learning_app.models.Word;

public interface BuilderInterface {
    BuilderInterface start(int id, String title);
    BuilderInterface addWord(Word word);
    LessonProduct build();
}
