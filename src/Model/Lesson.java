// src/model/Lesson.java
package model;

import java.time.LocalDate;

public class Lesson {
    private String lessonId;
    private ExerciseType exerciseType;
    private Day day;
    private TimeSlot timeSlot;
    private LocalDate date;
    
    public Lesson(String lessonId, ExerciseType exerciseType, Day day, 
                  TimeSlot timeSlot, LocalDate date) {
        this.lessonId = lessonId;
        this.exerciseType = exerciseType;
        this.day = day;
        this.timeSlot = timeSlot;
        this.date = date;
    }
    
    // Getters
    public String getLessonId() { return lessonId; }
    public ExerciseType getExerciseType() { return exerciseType; }
    public Day getDay() { return day; }
    public TimeSlot getTimeSlot() { return timeSlot; }
    public LocalDate getDate() { return date; }
}