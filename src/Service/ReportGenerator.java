package service;

import java.time.LocalDate;
import model.*;
import java.util.*;

public class ReportGenerator {
    private Timetable timetable;
    
    public ReportGenerator(Timetable timetable) {
        this.timetable = timetable;
    }
    
    public void printLessonReport() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("REPORT 1: LESSON ATTENDANCE & AVERAGE RATING");
        System.out.println("=".repeat(70));
        
        List<Lesson> allLessons = timetable.getAllLessons();
        LocalDate currentDate = null;
        
        for (Lesson lesson : allLessons) {
            if (!lesson.getDate().equals(currentDate)) {
                currentDate = lesson.getDate();
                System.out.println("\n📅 " + currentDate + " (" + lesson.getDay() + ")");
                System.out.println("-".repeat(60));
            }
            
            System.out.printf("  %-8s | %-12s | Members: %d/4 | Avg Rating: %.2f/5\n",
                lesson.getTimeSlot().getDisplayName(),
                lesson.getExerciseType().getDisplayName(),
                lesson.getBookingsCount(),
                lesson.getAverageRating());
        }
    }
    
    public void printIncomeReport() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("REPORT 2: HIGHEST INCOME BY EXERCISE TYPE");
        System.out.println("=".repeat(70));
        
        Map<ExerciseType, Double> incomeMap = new HashMap<>();
        
        for (Lesson lesson : timetable.getAllLessons()) {
            ExerciseType type = lesson.getExerciseType();
            double lessonIncome = lesson.getBookingsCount() * type.getPrice();
            incomeMap.put(type, incomeMap.getOrDefault(type, 0.0) + lessonIncome);
        }
        
        ExerciseType topType = null;
        double topIncome = 0;
        
        System.out.println("\n📊 All Exercises Total Income:");
        System.out.println("-".repeat(40));
        
        for (Map.Entry<ExerciseType, Double> entry : incomeMap.entrySet()) {
            System.out.printf("  %-12s: £%.2f\n", 
                entry.getKey().getDisplayName(), entry.getValue());
            if (entry.getValue() > topIncome) {
                topIncome = entry.getValue();
                topType = entry.getKey();
            }
        }
        
        if (topType != null) {
            System.out.println("\n🏆 WINNER: " + topType.getDisplayName());
            System.out.println("   Total Income: £" + String.format("%.2f", topIncome));
            System.out.println("   Price per lesson: £" + String.format("%.2f", topType.getPrice()));
        }
    }
}