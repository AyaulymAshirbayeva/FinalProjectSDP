package LanguageLearningApp.builder;
import LanguageLearningApp.models.Word;

public interface BuilderInterface {
    BuilderInterface start(int id, String title);
    BuilderInterface addWord(Word word);
    LessonProduct build();
}
