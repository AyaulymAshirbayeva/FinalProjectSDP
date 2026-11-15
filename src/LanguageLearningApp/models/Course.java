package LanguageLearningApp.models;

import LanguageLearningApp.builder.LessonProduct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private final String languageName;
    private final List<Word> vocabulary = new ArrayList<>();
    private final List<LessonProduct> lessons = new ArrayList<>();

    public Course(String languageName) {
        this.languageName = Objects.requireNonNull(languageName);
    }
    public String getLanguageName() {
        return languageName;
    }
    public void addWord(Word word) {
        vocabulary.add(Objects.requireNonNull(word));
    }
    public List<Word> getVocabulary() {
        return List.copyOf(vocabulary);
    }
    public void addLesson(LessonProduct lesson) {
        lessons.add(Objects.requireNonNull(lesson));
    }
    public List<LessonProduct> getLessons() {
        return List.copyOf(lessons);
    }
}
