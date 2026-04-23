
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
    
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public List<String> getBookedLessonIds() { return bookedLessonIds; }
}