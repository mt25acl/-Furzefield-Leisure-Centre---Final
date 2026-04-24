package model;

import java.util.ArrayList;
import java.util.List;

public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    private List<Lesson> lessons = new ArrayList<>();

    public String getDayName() {
        return this.name();
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public void setDayName(String name) {
        // Enums can't rename — this is intentionally unsupported
        throw new UnsupportedOperationException("Cannot rename an enum constant.");
    }

    public void clearLessons() {
        lessons.clear();
    }
}