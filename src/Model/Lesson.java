package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lesson {
    private String lessonId;
    private ExerciseType exerciseType;
    private Day day;
    private TimeSlot timeSlot;
    private LocalDate date;
    private final int MAX_CAPACITY = 4;
    private Set<String> memberIds; 
    private List<Review> reviews;
    
    public Lesson(String lessonId, ExerciseType exerciseType, Day day, 
                  TimeSlot timeSlot, LocalDate date) {
        this.lessonId = lessonId;
        this.exerciseType = exerciseType;
        this.day = day;
        this.timeSlot = timeSlot;
        this.date = date;
        this.memberIds = new HashSet<>();
        this.reviews = new ArrayList<>();
    }
    
    public boolean addBooking(String memberId) {
    if (memberIds.size() < MAX_CAPACITY && !memberIds.contains(memberId)) {
        memberIds.add(memberId);
        return true;
    }
    return false;
}

    public boolean removeBooking(String memberId) {
        return memberIds.remove(memberId);
    }
    
     public void addReview(Review review) {
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return new ArrayList<>(reviews);
    }

    public double getAverageRating() {
        if (reviews.isEmpty()) return 0.0;
        int sum = 0;
        for (Review r : reviews) {
            sum += r.getRating();
        }
        return (double) sum / reviews.size();
    }

    public int getBookingsCount() {
        return memberIds.size();
    }

    public boolean hasSpace() {
        return memberIds.size() < MAX_CAPACITY;
    }
    
    
    public int getMaxCapacity() { return MAX_CAPACITY; }
    public Set<String> getMemberIds() { return memberIds; }
    
    
    public String getLessonId() { return lessonId; }
    public ExerciseType getExerciseType() { return exerciseType; }
    public Day getDay() { return day; }
    public TimeSlot getTimeSlot() { return timeSlot; }
    public LocalDate getDate() { return date; }
}
