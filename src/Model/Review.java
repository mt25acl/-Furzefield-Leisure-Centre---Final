package model;

import java.time.LocalDateTime;

public class Review {
    private static int nextId = 1;
    private int reviewId;
    private String memberId;
    private String lessonId;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;
    
    public Review(String memberId, String lessonId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be 1-5");
        }
        this.reviewId = nextId++;
        this.memberId = memberId;
        this.lessonId = lessonId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDateTime.now();
    }
    
    
    public int getReviewId() { return reviewId; }
    public String getMemberId() { return memberId; }
    public String getLessonId() { return lessonId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getReviewDate() { return reviewDate; }
}