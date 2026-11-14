package LanguageLearningApp.models;

import LanguageLearningApp.builder.LessonProduct;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private final String languageName; // or Course name
    private final List<Word> vocabulary = new ArrayList<>();
    private final List<LessonProduct> lessons = new ArrayList<>();

    public Course(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void addWord(Word word) {
        vocabulary.add(word);
    }

    public List<Word> getVocabulary() {
        return vocabulary;
    }

    public void addLesson(LessonProduct lesson) {
        lessons.add(lesson);
    }

    public List<LessonProduct> getLessons() {
        return lessons;
    }
}