package model;

import java.util.*;

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
    
    public boolean bookLesson(String lessonId, Lesson newLesson, List<Lesson> allLessons) {
        if (bookedLessonIds.contains(lessonId)) {
            return false;
        }
        
        // Check time conflict
        for (String bookedId : bookedLessonIds) {
            for (Lesson lesson : allLessons) {
                if (lesson != null && lesson.getLessonId().equals(bookedId)) {
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
    
    public boolean cancelBooking(String lessonId, Lesson lesson) {
        if (lesson.removeBooking(this.memberId)) {
            bookedLessonIds.remove(lessonId);
            return true;
        }
        return false;
    }
    
    public boolean changeBooking(String oldLessonId, String newLessonId, 
                                  Lesson oldLesson, Lesson newLesson, List<Lesson> allLessons) {
        if (cancelBooking(oldLessonId, oldLesson)) {
            if (bookLesson(newLessonId, newLesson, allLessons)) {
                return true;
            }
            // Rollback
            oldLesson.addBooking(this.memberId);
            bookedLessonIds.add(oldLessonId);
            return false;
        }
        return false;
    }
    
    public void attendLesson(String lessonId) {
        if (bookedLessonIds.contains(lessonId) && !attendedLessonIds.contains(lessonId)) {
            attendedLessonIds.add(lessonId);
        }
    }
    
    public void addReview(String lessonId, int rating, String comment, Lesson lesson) {
        if (attendedLessonIds.contains(lessonId)) {
            Review review = new Review(this.memberId, lessonId, rating, comment);
            lesson.addReview(review);
        }
    }
    
    // ========== GETTER METHODS ==========
    public String getMemberId() { 
        return memberId; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public List<String> getBookedLessonIds() { 
        return new ArrayList<>(bookedLessonIds); 
    }
    
    public List<String> getAttendedLessonIds() { 
        return new ArrayList<>(attendedLessonIds); 
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s", memberId, name);
    }
}