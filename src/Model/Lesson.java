package model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Lesson {
    private String lessonId;
    private ExerciseType exerciseType;
    private Day day;
    private TimeSlot timeSlot;
    private LocalDate date;
    private final int MAX_CAPACITY = 4;
    private Set<String> memberIds;  // Store member IDs who booked
    
    public Lesson(String lessonId, ExerciseType exerciseType, Day day, 
                  TimeSlot timeSlot, LocalDate date) {
        this.lessonId = lessonId;
        this.exerciseType = exerciseType;
        this.day = day;
        this.timeSlot = timeSlot;
        this.date = date;
        this.memberIds = new HashSet<>();
    }
    
    public int getMaxCapacity() { return MAX_CAPACITY; }
    public Set<String> getMemberIds() { return memberIds; }
    
    // Getters
    public String getLessonId() { return lessonId; }
    public ExerciseType getExerciseType() { return exerciseType; }
    public Day getDay() { return day; }
    public TimeSlot getTimeSlot() { return timeSlot; }
    public LocalDate getDate() { return date; }
}