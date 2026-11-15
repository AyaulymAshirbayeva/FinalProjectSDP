//package LanguageLearningApp.cli;
//
//import LanguageLearningApp.builder.LessonDirector;
//import LanguageLearningApp.builder.LessonProduct;
//import LanguageLearningApp.builder.StandardLessonBuilder;
//import LanguageLearningApp.models.Course;
//import LanguageLearningApp.models.Word;
//import LanguageLearningApp.strategy.BasicIntervalStrategy;
//import LanguageLearningApp.strategy.ReviewSchedulerContext;
//import LanguageLearningApp.strategy.SM2Strategy;
//import java.nio.charset.StandardCharsets;
//import java.util.Scanner;
//
//public class BuilderStrategyDemo {
//    public static void main(String[] args) {
//        LessonDirector director = new LessonDirector(new StandardLessonBuilder());
//        Course course = new Course("English");
//        course.addLesson(director.greetings());
//        course.addLesson(director.travel());
//        for (LessonProduct lesson : course.getLessons()) {
//            lesson.getVocabulary().forEach(course::addWord);
//        }
//        ReviewSchedulerContext scheduler = new ReviewSchedulerContext(new BasicIntervalStrategy());
//        try (Scanner input = new Scanner(System.in, StandardCharsets.UTF_8)) {
//            while (true) {
//                System.out.println("Words due for review: " + scheduler.dueWords().size());
//                System.out.println("1. Lessons  2. Review  3. Basic  4. SM-2  0. Exit");
//                if (!input.hasNextLine()) {
//                    return;
//                }
//                switch (input.nextLine().strip()) {
//                    case "1" -> runLesson(course, scheduler, input);
//                    case "2" -> runReview(scheduler, input);
//                    case "3" -> {
//                        scheduler.setStrategy(new BasicIntervalStrategy());
//                        System.out.println("Review strategy: Basic");
//                    }
//                    case "4" -> {
//                        scheduler.setStrategy(new SM2Strategy());
//                        System.out.println("Review strategy: SM-2");
//                    }
//                    case "0" -> { return; }
//                    default -> System.out.println("Choose an option from 0 to 4.");
//                }
//            }
//        }
//    }
//
//    private static void runLesson(Course course, ReviewSchedulerContext scheduler, Scanner input) {
//        for (LessonProduct lesson : course.getLessons()) {
//            System.out.println(lesson.getId() + ". " + lesson.getTitle());
//        }
//        if (!input.hasNextLine()) {
//            return;
//        }
//        String selection = input.nextLine().strip();
//        for (LessonProduct lesson : course.getLessons()) {
//            if (Integer.toString(lesson.getId()).equals(selection)) {
//                playLesson(lesson, scheduler, input);
//                return;
//            }
//        }
//        System.out.println("Unknown lesson.");
//    }
//
//    private static void playLesson(LessonProduct lesson, ReviewSchedulerContext scheduler, Scanner input) {
//        for (Word word : lesson.getVocabulary()) {
//            System.out.println(word.getTerm() + " = " + word.getTranslation());
//        }
//        int correct = 0;
//        for (Word word : lesson.getVocabulary()) {
//            System.out.println("Translate " + word.getTranslation() + " into English, or type ? for a hint.");
//            while (true) {
//                if (!input.hasNextLine()) {
//                    return;
//                }
//                String answer = input.nextLine().strip();
//                if (answer.equals("?")) {
//                    System.out.println(word.getHint());
//                    continue;
//                }
//                if (word.getTerm().equalsIgnoreCase(answer)) {
//                    correct++;
//                    System.out.println("Correct!");
//                } else {
//                    System.out.println("Incorrect.");
//                }
//                break;
//            }
//        }
//        lesson.getVocabulary().forEach(scheduler::addWord);
//        System.out.println("Lesson completed. Correct answers: " + correct + "/" + lesson.getVocabulary().size());
//    }
//
//    private static void runReview(ReviewSchedulerContext scheduler, Scanner input) {
//        var due = scheduler.dueWords();
//        if (due.isEmpty()) {
//            System.out.println("No words due.");
//        }
//        for (Word word : due) {
//            System.out.println(word.getTerm() + " (press Enter to reveal, or type back)");
//            if (!input.hasNextLine() || input.nextLine().strip().equalsIgnoreCase("back")) {
//                return;
//            }
//            System.out.println(word.getTranslation());
//            while (true) {
//                System.out.println("Rate recall from 0 to 5, or type back.");
//                if (!input.hasNextLine()) {
//                    return;
//                }
//                String rating = input.nextLine().strip();
//                if (rating.equalsIgnoreCase("back")) {
//                    return;
//                }
//                if (rating.matches("[0-5]")) {
//                    System.out.println("Next review: " + scheduler.review(word, Integer.parseInt(rating)));
//                    break;
//                }
//            }
//        }
//    }
//}
