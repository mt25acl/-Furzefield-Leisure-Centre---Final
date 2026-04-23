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
}