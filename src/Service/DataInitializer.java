
package service;

import model.*;
import java.util.*;

public class DataInitializer {
    private BookingSystem bookingSystem;
    
    public DataInitializer(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
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