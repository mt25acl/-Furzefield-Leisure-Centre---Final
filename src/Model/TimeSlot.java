package model;

public enum TimeSlot {
    MORNING("Morning", 1),
    AFTERNOON("Afternoon", 2),
    EVENING("Evening", 3);
    
    private final String displayName;
    private final int order;
    
    TimeSlot(String displayName, int order) {
        this.displayName = displayName;
        this.order = order;
    }
    
    public String getDisplayName() { 
        return displayName; 
    }
    
    public int getOrder() { 
        return order; 
    }
}