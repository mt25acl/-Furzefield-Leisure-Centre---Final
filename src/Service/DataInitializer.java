// src/service/DataInitializer.java
package service;

import model.*;
import java.util.*;

public class DataInitializer {
    private BookingSystem bookingSystem;
    
    public DataInitializer(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
    }
    
   
    

public void createSampleReviews() {
    String[] comments = {
        "Great class!", "Loved it", "Good workout",
        "Excellent", "Not bad", "Fantastic instructor",
        "Will come again", "Best class ever", "So-so",
        "Amazing energy", "Very professional"
    };
    
    Random random = new Random();
    List<Member> members = bookingSystem.getAllMembers();
    int reviewCount = 0;
    
    for (Member member : members) {
        for (String lessonId : member.getBookedLessonIds()) {
            if (reviewCount >= 20) break;
            
          
            if (random.nextDouble() < 0.7) {
                bookingSystem.markAttendance(member.getMemberId(), lessonId);
                int rating = random.nextInt(5) + 1;
                String comment = comments[random.nextInt(comments.length)];
                bookingSystem.addReview(member.getMemberId(), lessonId, rating, comment);
                reviewCount++;
            }
        }
        if (reviewCount >= 20) break;
    }
    
    System.out.println("Created " + reviewCount + " reviews");
}
    
    public void createMembers() {
        String[] names = {
            "Alice Johnson", "Bob Smith", "Carol Davis", "David Brown",
            "Emma Wilson", "Frank Miller", "Grace Lee", "Henry Taylor",
            "Ivy Moore", "Jack White"
        };
        
        for (int i = 1; i <= 10; i++) {
            String id = "MEM" + String.format("%03d", i);
            Member member = new Member(id, names[i-1]);
            bookingSystem.addMember(member);
        }
        
        System.out.println("Created " + 10 + " members");
    }
}