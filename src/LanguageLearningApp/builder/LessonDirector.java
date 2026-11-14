package LanguageLearningApp.builder;

import LanguageLearningApp.models.Word;
import java.util.Objects;

public class LessonDirector {
    private final BuilderInterface builder;

    public LessonDirector(BuilderInterface builder) {
        this.builder = Objects.requireNonNull(builder);
    }

    public LessonProduct greetings() {
        return greetings("english");
    }

    /** Builds the greetings lesson for a supported target language; English is the prompt language. */
    public LessonProduct greetings(String targetLanguage) {
        String language = normalizeLanguage(targetLanguage);

        if (language.equals("english")) {
            return builder.start(1, "Greetings")
                    .addWord(new Word("English", "hello", "привет", "It starts with h.", "Say ___ to your friend."))
                    .addWord(new Word("English", "thanks", "спасибо", "It starts with th.", "Many ___ for your help."))
                    .build();
        } else if (language.equals("russian")) {
            return builder.start(1, "Приветствия")
                    .addWord(new Word("Russian", "привет", "hello", "A common informal greeting.", "Скажи ___ другу."))
                    .addWord(new Word("Russian", "спасибо", "thank you", "A polite expression of gratitude.", "Скажи ___ за помощь."))
                    .build();
        } else if (language.equals("german")) {
            return builder.start(1, "Greetings")
                    .addWord(new Word("German", "Hallo", "hello", "A common greeting beginning with H.", "Sag ___ zu deinem Freund."))
                    .addWord(new Word("German", "Danke", "thank you", "A polite expression of gratitude.", "Ich sage ___."))
                    .build();
        } else if (language.equals("chinese")) {
            return builder.start(1, "问候")
                    .addWord(new Word("Chinese", "你好", "hello", "A common greeting; pronounced nǐ hǎo.", "我对你说___。"))
                    .addWord(new Word("Chinese", "谢谢", "thank you", "A polite expression of gratitude; pronounced xièxie.", "我说___。"))
                    .build();
        } else {
            throw new IllegalArgumentException("Unsupported language: " + targetLanguage);
        }
    }

    public LessonProduct travel() {
        return builder.start(2, "Travel")
                .addWord(new Word("English", "station", "станция", "Trains stop there.", "Meet me at the ___."))
                .addWord(new Word("English", "ticket", "билет", "You buy one before a trip.", "I bought a train ___."))
                .build();
    }

    private String normalizeLanguage(String language) {
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("Target language must not be blank.");
        }
        return language.strip().toLowerCase(java.util.Locale.ROOT);
    }
}
