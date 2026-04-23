
package model;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private String memberId;
    private String name;
    private List<String> bookedLessonIds;
    
    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.bookedLessonIds = new ArrayList<>();
    }
    
    // src/model/Member.java (add these methods)

    public boolean bookLesson(String lessonId, Lesson lesson) {
        // Check if already booked this specific lesson
        if (bookedLessonIds.contains(lessonId)) {
            return false;
        }

        // Check time conflict with existing bookings
        // This needs Lesson objects - will implement later
        // For now just add if lesson has space
        if (lesson.addBooking(this.memberId)) {
            bookedLessonIds.add(lessonId);
            return true;
        }
        return false;
    }

    public boolean cancelBooking(String lessonId, Lesson lesson) {
        if (lesson.removeBooking(this.memberId)) {
            bookedLessonIds.remove(lessonId);
            return true;
        }
        return false;
    }
    
    
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public List<String> getBookedLessonIds() { return bookedLessonIds; }
}