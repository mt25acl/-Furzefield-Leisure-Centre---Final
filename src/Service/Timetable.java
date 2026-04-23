package service;

import model.*;
import java.util.*;
import java.time.LocalDate;


    public class Timetable {
    private Map<String, Lesson> lessonMap;
    private List<Lesson> allLessons;
    
    
    
    public void generateTimetable(int numberOfWeekends, List<ExerciseType> exerciseTypes) {
    LocalDate startDate = LocalDate.of(2026, 4, 25); // First Saturday
    
    for (int w = 0; w < numberOfWeekends; w++) {
        LocalDate saturday = startDate.plusWeeks(w);
        LocalDate sunday = saturday.plusDays(1);
        
        generateDayLessons(saturday, Day.SATURDAY, exerciseTypes);
        generateDayLessons(sunday, Day.SUNDAY, exerciseTypes);
    }
}

private void generateDayLessons(LocalDate date, Day day, List<ExerciseType> exerciseTypes) {
    int counter = 1;
    for (TimeSlot slot : TimeSlot.values()) {
        for (ExerciseType exercise : exerciseTypes) {
            String lessonId = String.format("%s_%s_%s_%d", date, day, slot, counter++);
            Lesson lesson = new Lesson(lessonId, exercise, day, slot, date);
            addLesson(lesson);
        }
    }
}
    
    public Timetable() {
        this.lessonMap = new HashMap<>();
        this.allLessons = new ArrayList<>();
    }
    
    public void addLesson(Lesson lesson) {
        allLessons.add(lesson);
        lessonMap.put(lesson.getLessonId(), lesson);
    }
    
    public Lesson getLesson(String lessonId) {
        return lessonMap.get(lessonId);
    }
    
    public List<Lesson> getAllLessons() {
        return new ArrayList<>(allLessons);
    }
}