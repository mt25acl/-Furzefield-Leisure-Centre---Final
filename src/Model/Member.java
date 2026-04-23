
package model;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private String memberId;
    private String name;
    private List<String> bookedLessonIds;
    private List<String> attendedLessonIds;
    
    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.bookedLessonIds = new ArrayList<>();
        this.attendedLessonIds = new ArrayList<>();
    }
    
    public void attendLesson(String lessonId) {
    if (bookedLessonIds.contains(lessonId) && !attendedLessonIds.contains(lessonId)) {
        attendedLessonIds.add(lessonId);
    }
}

public boolean canReview(String lessonId) {
    return attendedLessonIds.contains(lessonId);
}

public void addReview(String lessonId, int rating, String comment, Lesson lesson) {
    if (canReview(lessonId)) {
        Review review = new Review(this.memberId, lessonId, rating, comment);
        lesson.addReview(review);
    }
}
  

    public boolean bookLesson(String lessonId, Lesson lesson) {
        
        if (bookedLessonIds.contains(lessonId)) {
            return false;
        }

        
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
    
    
    public boolean bookLesson(String lessonId, Lesson newLesson, List<Lesson> allLessons) {
    if (bookedLessonIds.contains(lessonId)) {
        return false;
    }
    
    
    for (String bookedId : bookedLessonIds) {
        for (Lesson lesson : allLessons) {
            if (lesson.getLessonId().equals(bookedId)) {
                if (lesson.getDate().equals(newLesson.getDate()) && 
                    lesson.getTimeSlot() == newLesson.getTimeSlot()) {
                    return false;
                }
            }
        }
    }
    
    if (newLesson.addBooking(this.memberId)) {
        bookedLessonIds.add(lessonId);
        return true;
    }
    return false;
    }
    
    
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public List<String> getBookedLessonIds() { return bookedLessonIds; }
}

