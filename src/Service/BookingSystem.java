package service;

import model.*;
import java.util.*;

    public class BookingSystem {
    private Timetable timetable;
    private Map<String, Member> members;
    
   

    public boolean bookLesson(String memberId, String lessonId) {
    Member member = members.get(memberId);
    Lesson lesson = timetable.getLesson(lessonId);
    
    if (member == null || lesson == null) {
        return false;
    }
    
    return member.bookLesson(lessonId, lesson, timetable.getAllLessons());
}

   public boolean cancelBooking(String memberId, String lessonId) {
    Member member = members.get(memberId);
    Lesson lesson = timetable.getLesson(lessonId);
    
    if (member == null || lesson == null) {
        return false;
    }
    
    return member.cancelBooking(lessonId, lesson);
}

    public boolean changeBooking(String memberId, String oldLessonId, String newLessonId) {
    if (cancelBooking(memberId, oldLessonId)) {
        if (bookLesson(memberId, newLessonId)) {
            return true;
        }
        // Rollback
        bookLesson(memberId, oldLessonId);
        return false;
    }
    return false;
}
    
    
    
    
    
    
    
    
    public BookingSystem(Timetable timetable) {
        this.timetable = timetable;
        this.members = new HashMap<>();
    }
    
    public void addMember(Member member) {
        members.put(member.getMemberId(), member);
    }
    
    public Member getMember(String memberId) {
        return members.get(memberId);
    }
    
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }
}