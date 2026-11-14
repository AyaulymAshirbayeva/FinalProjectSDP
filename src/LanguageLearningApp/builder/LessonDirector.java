package LanguageLearningApp.builder;

import LanguageLearningApp.models.Word;
import java.util.Objects;

public class LessonDirector {
    private final BuilderInterface builder;

    public LessonDirector(BuilderInterface builder) {
        this.builder = Objects.requireNonNull(builder);
    }

    public LessonProduct greetings() {
        return builder.start(1, "Greetings")
                .addWord(new Word("English", "hello", "привет", "It starts with h.", "Say ___ to your friend."))
                .addWord(new Word("English", "thanks", "спасибо", "It starts with th.", "Many ___ for your help."))
                .build();
    }

    public LessonProduct travel() {
        return builder.start(2, "Travel")
                .addWord(new Word("English", "station", "станция", "Trains stop there.", "Meet me at the ___."))
                .addWord(new Word("English", "ticket", "билет", "You buy one before a trip.", "I bought a train ___."))
                .build();
    }
}
