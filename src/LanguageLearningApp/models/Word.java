package LanguageLearningApp.models;

public class Word {
    private final String language;
    private final String term;            // studying word Wasser
    private final String translation;
    private final String hint;            // for HintDecorator
    private final String exampleSentence; // Sentence with a gap for FillBlankExercise

    // fields for memorize stategies (SM2Strategy, BasicIntervalStrategy)
    private int repetitions = 0;
    private int intervalDays = 1;
    private double easinessFactor = 2.5;

    public Word(String language, String term, String translation, String hint, String exampleSentence) {
        this.language = language;
        this.term = term;
        this.translation = translation;
        this.hint = hint;
        this.exampleSentence = exampleSentence;
    }

    public String getLanguage() {
        return language;
    }

    public String getTerm() {
        return term;
    }

    public String getTranslation() {
        return translation;
    }

    public String getHint() {
        return hint;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(int intervalDays) {
        this.intervalDays = intervalDays;
    }

    public double getEasinessFactor() {
        return easinessFactor;
    }

    public void setEasinessFactor(double easinessFactor) {
        this.easinessFactor = Math.max(1.3, easinessFactor); // По стандарту SM-2 минимум 1.3
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (Next review in %d days)", language, term, translation, intervalDays);
    }
}