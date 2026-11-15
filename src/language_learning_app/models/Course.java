package language_learning_app.models;

import language_learning_app.builder.LessonProduct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private final String languageName;
    private final List<LessonProduct> lessons = new ArrayList<>();

    public Course(String languageName) {
        if (languageName == null || languageName.isBlank()) {
            throw new IllegalArgumentException("Course language must not be blank.");
        }
        this.languageName = languageName.strip();
    }

    public String getLanguageName() {
        return languageName;
    }

    public List<Word> getVocabulary() {
        return lessons.stream().flatMap(lesson -> lesson.getVocabulary().stream()).distinct().toList();
    }

    public void addLesson(LessonProduct lesson) {
        Objects.requireNonNull(lesson);
        if (lessons.stream().anyMatch(existing -> existing.getId() == lesson.getId())) {
            throw new IllegalArgumentException("Lesson id is already present in this course.");
        }
        if (lesson.getVocabulary().stream().anyMatch(word -> !languageName.equalsIgnoreCase(word.getLanguage()))) {
            throw new IllegalArgumentException("Lesson vocabulary must match the course language.");
        }
        lessons.add(lesson);
    }

    public List<LessonProduct> getLessons() {
        return List.copyOf(lessons);
    }
}
