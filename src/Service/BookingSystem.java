package service;

import model.*;
import java.util.*;

public class BookingSystem {
    private Timetable timetable;
    private Map<String, Member> members;
    
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