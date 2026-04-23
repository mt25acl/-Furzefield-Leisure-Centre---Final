package service;

import java.time.LocalDate;
import model.*;
import java.util.*;

public class ReportGenerator {
    private Timetable timetable;
    
    public ReportGenerator(Timetable timetable) {
        this.timetable = timetable;
    }
    
    
    public void printIncomeReport() {
    System.out.println("\n=== HIGHEST INCOME BY EXERCISE TYPE ===");
     Map<ExerciseType, Double> incomeMap = new HashMap<>();
    
    for (Lesson lesson : timetable.getAllLessons()) {
        ExerciseType type = lesson.getExerciseType();
        double lessonIncome = lesson.getBookingsCount() * getPriceForExercise(type);
        incomeMap.put(type, incomeMap.getOrDefault(type, 0.0) + lessonIncome);
    }
    }
    
    
    
    
    public void printLessonReport() {
        System.out.println("\n=== LESSON ATTENDANCE & RATING REPORT ===");
        
        List<Lesson> allLessons = timetable.getAllLessons();
        LocalDate currentDate = null;
        
        for (Lesson lesson : allLessons) {
            if (!lesson.getDate().equals(currentDate)) {
                currentDate = lesson.getDate();
                System.out.println("\nDate: " + currentDate + " (" + lesson.getDay() + ")");
                System.out.println("----------------------------------------");
            }
            
            System.out.printf("  %s %s: %d/%d members, Avg Rating: %.1f\n",
                lesson.getTimeSlot(), lesson.getExerciseType(),
                lesson.getBookingsCount(), lesson.getMaxCapacity(),
                lesson.getAverageRating());
        }
    }

   private double getPriceForExercise(ExerciseType type) {
    switch(type) {
        case YOGA: return 12.0;
        case ZUMBA: return 10.0;
        case AQUACISE: return 15.0;
        case BOX_FIT: return 14.0;
        case BODY_BLITZ: return 13.0;
        default: return 0;
    }
    
   }
    
}