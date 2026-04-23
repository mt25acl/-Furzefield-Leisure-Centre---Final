
import model.*;
import service.*;
import java.time.LocalDate;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static BookingSystem bookingSystem;
    private static Timetable timetable;
    private static Member currentMember = null;
    
    public static void main(String[] args) {
        System.out.println("================================================");
        System.out.println("     FURZEFIELD LEISURE CENTRE");
        System.out.println("     GROUP EXERCISE BOOKING SYSTEM");
        System.out.println("================================================");
        
        // Initialize system
        initializeSystem();
        
        // Main menu loop
        while (true) {
            displayMainMenu();
            int choice = getIntInput("Enter choice: ");
            
            switch (choice) {
                case 1:
                    viewTimetable();
                    break;
                case 2:
                    bookLesson();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    changeBooking();
                    break;
                case 5:
                    addReview();
                    break;
                case 6:
                    generateReports();
                    break;
                case 7:
                    viewMyBookings();
                    break;
                case 8:
                    System.out.println("\nThank you for using FLC Booking System!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private static void initializeSystem() {
        System.out.println("\n📅 Initializing System...");
        
        // Create timetable
        timetable = new Timetable();
        List<ExerciseType> exerciseTypes = Arrays.asList(
            ExerciseType.YOGA, ExerciseType.ZUMBA,
            ExerciseType.AQUACISE, ExerciseType.BOX_FIT
        );
        
        timetable.generateTimetable(8, exerciseTypes);
        System.out.println("✓ Generated " + timetable.getAllLessons().size() + " lessons (8 weekends)");
        
        // Create booking system
        bookingSystem = new BookingSystem(timetable);
        
        // Create 10 members
        String[] memberNames = {
            "Alice Johnson", "Bob Smith", "Carol Davis", "David Brown",
            "Emma Wilson", "Frank Miller", "Grace Lee", "Henry Taylor",
            "Ivy Moore", "Jack White"
        };
        
        for (int i = 0; i < 10; i++) {
            String id = "MEM" + String.format("%03d", i + 1);
            Member member = new Member(id, memberNames[i]);
            bookingSystem.addMember(member);
        }
        System.out.println("✓ Created 10 members");
        
        // Create sample bookings
        createSampleBookings();
        
        // Create sample reviews
        createSampleReviews();
        
        System.out.println("\n✓ System Ready!\n");
    }
    
    private static void createSampleBookings() {
        Random random = new Random(42);
        List<Member> members = bookingSystem.getAllMembers();
        List<Lesson> allLessons = timetable.getAllLessons();
        
        int bookingCount = 0;
        for (Member member : members) {
            int numBookings = random.nextInt(3) + 2;
            List<Lesson> shuffled = new ArrayList<>(allLessons);
            Collections.shuffle(shuffled, random);
            
            for (Lesson lesson : shuffled) {
                if (member.getBookedLessonIds().size() >= numBookings) break;
                if (bookingSystem.bookLesson(member.getMemberId(), lesson.getLessonId())) {
                    bookingCount++;
                }
            }
        }
        System.out.println("✓ Created " + bookingCount + " sample bookings");
    }
    
    private static void createSampleReviews() {
        Random random = new Random(42);
        List<Member> members = bookingSystem.getAllMembers();
        String[] comments = {
            "Great class!", "Loved the instructor", "Very energetic",
            "Will come again", "Excellent workout", "Fantastic session"
        };
        
        int reviewCount = 0;
        for (Member member : members) {
            for (String lessonId : member.getBookedLessonIds()) {
                if (reviewCount >= 25) break;
                if (random.nextDouble() < 0.8) {
                    bookingSystem.markAttendance(member.getMemberId(), lessonId);
                    int rating = random.nextInt(5) + 1;
                    String comment = comments[random.nextInt(comments.length)];
                    bookingSystem.addReview(member.getMemberId(), lessonId, rating, comment);
                    reviewCount++;
                }
            }
        }
        System.out.println("✓ Created " + reviewCount + " sample reviews");
    }
    
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. View Timetable (by Day or Exercise)");
        System.out.println("2. Book a Lesson");
        System.out.println("3. Cancel a Booking");
        System.out.println("4. Change a Booking");
        System.out.println("5. Write a Review");
        System.out.println("6. Generate Reports");
        System.out.println("7. View My Bookings");
        System.out.println("8. Exit");
        System.out.println("-".repeat(50));
    }
    
    private static void viewTimetable() {
        System.out.println("\n1. View by Day (Saturday/Sunday)");
        System.out.println("2. View by Exercise Name");
        int choice = getIntInput("Enter choice: ");
        
        if (choice == 1) {
            System.out.println("\n1. Saturday");
            System.out.println("2. Sunday");
            int dayChoice = getIntInput("Choose day (1 or 2): ");
            
            Day day = (dayChoice == 1) ? Day.SATURDAY : Day.SUNDAY;
            List<Lesson> lessons = timetable.getLessonsByDay(day);
            
            System.out.println("\n📅 " + day + " LESSONS:");
            System.out.println("-".repeat(60));
            for (int i = 0; i < Math.min(20, lessons.size()); i++) {
                Lesson l = lessons.get(i);
                System.out.printf("  %s | %s | %s | Booked: %d/4 | Rating: %.1f\n",
                    l.getDate(), l.getTimeSlot().getDisplayName(),
                    l.getExerciseType().getDisplayName(),
                    l.getBookingsCount(), l.getAverageRating());
            }
        } else if (choice == 2) {
            System.out.println("\nAvailable Exercises:");
            System.out.println("  1. YOGA");
            System.out.println("  2. ZUMBA");
            System.out.println("  3. AQUACISE");
            System.out.println("  4. BOX_FIT");
            int exChoice = getIntInput("Choose exercise (1-4): ");
            
            ExerciseType exercise = null;
            switch (exChoice) {
                case 1: exercise = ExerciseType.YOGA; break;
                case 2: exercise = ExerciseType.ZUMBA; break;
                case 3: exercise = ExerciseType.AQUACISE; break;
                case 4: exercise = ExerciseType.BOX_FIT; break;
                default: System.out.println("Invalid choice"); return;
            }
            
            List<Lesson> lessons = timetable.getLessonsByExercise(exercise);
            System.out.println("\n🏋️ " + exercise.getDisplayName() + " LESSONS:");
            System.out.println("-".repeat(60));
            for (int i = 0; i < Math.min(20, lessons.size()); i++) {
                Lesson l = lessons.get(i);
                System.out.printf("  %s | %s | Booked: %d/4 | Rating: %.1f | Price: £%.2f\n",
                    l.getDate(), l.getTimeSlot().getDisplayName(),
                    l.getBookingsCount(), l.getAverageRating(),
                    exercise.getPrice());
            }
        }
    }
    
    private static void bookLesson() {
        selectMember();
        if (currentMember == null) return;
        
        System.out.println("\n📋 AVAILABLE LESSONS (with space):");
        System.out.println("-".repeat(60));
        List<Lesson> available = new ArrayList<>();
        for (Lesson lesson : timetable.getAllLessons()) {
            if (lesson.hasSpace()) {
                available.add(lesson);
                System.out.printf("%2d. %s | %s | %-12s | Available: %d\n", 
                    available.size(), lesson.getDate(), 
                    lesson.getTimeSlot().getDisplayName(),
                    lesson.getExerciseType().getDisplayName(),
                    lesson.getMaxCapacity() - lesson.getBookingsCount());
            }
            if (available.size() >= 20) break;
        }
        
        int choice = getIntInput("Select lesson number (0 to cancel): ");
        if (choice > 0 && choice <= available.size()) {
            Lesson selected = available.get(choice - 1);
            if (bookingSystem.bookLesson(currentMember.getMemberId(), selected.getLessonId())) {
                System.out.println("✓ Booked successfully: " + selected.getExerciseType().getDisplayName());
            } else {
                System.out.println("✗ Booking failed! Time conflict or already booked.");
            }
        }
    }
    
    private static void cancelBooking() {
        selectMember();
        if (currentMember == null) return;
        
        List<String> bookings = currentMember.getBookedLessonIds();
        if (bookings.isEmpty()) {
            System.out.println("No bookings to cancel.");
            return;
        }
        
        System.out.println("\n📋 YOUR BOOKINGS:");
        System.out.println("-".repeat(50));
        List<Lesson> bookedLessons = new ArrayList<>();
        for (String id : bookings) {
            Lesson l = timetable.getLesson(id);
            if (l != null) {
                bookedLessons.add(l);
                System.out.printf("%2d. %s | %s | %s\n", 
                    bookedLessons.size(), l.getDate(), 
                    l.getTimeSlot().getDisplayName(),
                    l.getExerciseType().getDisplayName());
            }
        }
        
        int choice = getIntInput("Select booking to cancel (0 to cancel): ");
        if (choice > 0 && choice <= bookedLessons.size()) {
            Lesson selected = bookedLessons.get(choice - 1);
            if (bookingSystem.cancelBooking(currentMember.getMemberId(), selected.getLessonId())) {
                System.out.println("✓ Cancelled successfully!");
            } else {
                System.out.println("✗ Cancel failed!");
            }
        }
    }
    
    private static void changeBooking() {
        selectMember();
        if (currentMember == null) return;
        
        List<String> bookings = currentMember.getBookedLessonIds();
        if (bookings.isEmpty()) {
            System.out.println("No bookings to change.");
            return;
        }
        
        System.out.println("\n📋 YOUR BOOKINGS:");
        System.out.println("-".repeat(50));
        List<Lesson> currentBookings = new ArrayList<>();
        for (String id : bookings) {
            Lesson l = timetable.getLesson(id);
            if (l != null) {
                currentBookings.add(l);
                System.out.printf("%2d. %s | %s | %s\n", 
                    currentBookings.size(), l.getDate(),
                    l.getTimeSlot().getDisplayName(),
                    l.getExerciseType().getDisplayName());
            }
        }
        
        int oldChoice = getIntInput("Select booking to change (0 to cancel): ");
        if (oldChoice == 0) return;
        Lesson oldLesson = currentBookings.get(oldChoice - 1);
        
        System.out.println("\n📋 AVAILABLE LESSONS TO CHANGE TO:");
        System.out.println("-".repeat(60));
        List<Lesson> available = new ArrayList<>();
        for (Lesson lesson : timetable.getAllLessons()) {
            if (lesson.hasSpace() && !lesson.getLessonId().equals(oldLesson.getLessonId())) {
                available.add(lesson);
                System.out.printf("%2d. %s | %s | %-12s\n", 
                    available.size(), lesson.getDate(),
                    lesson.getTimeSlot().getDisplayName(),
                    lesson.getExerciseType().getDisplayName());
            }
            if (available.size() >= 20) break;
        }
        
        int newChoice = getIntInput("Select new lesson (0 to cancel): ");
        if (newChoice == 0) return;
        Lesson newLesson = available.get(newChoice - 1);
        
        if (bookingSystem.changeBooking(currentMember.getMemberId(), 
            oldLesson.getLessonId(), newLesson.getLessonId())) {
            System.out.println("✓ Booking changed successfully!");
            System.out.println("  From: " + oldLesson.getExerciseType().getDisplayName());
            System.out.println("  To:   " + newLesson.getExerciseType().getDisplayName());
        } else {
            System.out.println("✗ Change failed! Time conflict.");
        }
    }
    
    private static void addReview() {
        selectMember();
        if (currentMember == null) return;
        
        List<String> attended = currentMember.getAttendedLessonIds();
        if (attended.isEmpty()) {
            System.out.println("No attended lessons to review. Attend a lesson first!");
            return;
        }
        
        System.out.println("\n📋 ATTENDED LESSONS:");
        System.out.println("-".repeat(50));
        List<Lesson> attendedLessons = new ArrayList<>();
        for (String id : attended) {
            Lesson l = timetable.getLesson(id);
            if (l != null) {
                attendedLessons.add(l);
                System.out.printf("%2d. %s | %s | %s\n", 
                    attendedLessons.size(), l.getDate(),
                    l.getTimeSlot().getDisplayName(),
                    l.getExerciseType().getDisplayName());
            }
        }
        
        int choice = getIntInput("Select lesson to review (0 to cancel): ");
        if (choice == 0) return;
        Lesson selected = attendedLessons.get(choice - 1);
        
        System.out.println("\nRating Guide:");
        System.out.println("  1 = Very dissatisfied");
        System.out.println("  2 = Dissatisfied");
        System.out.println("  3 = OK");
        System.out.println("  4 = Satisfied");
        System.out.println("  5 = Very Satisfied");
        
        int rating = getIntInput("Enter rating (1-5): ");
        if (rating < 1 || rating > 5) {
            System.out.println("Invalid rating! Must be 1-5.");
            return;
        }
        
        scanner.nextLine(); // consume newline
        System.out.print("Enter your review comment: ");
        String comment = scanner.nextLine();
        
        bookingSystem.addReview(currentMember.getMemberId(), selected.getLessonId(), rating, comment);
        System.out.println("✓ Thank you! Your review has been added.");
    }
    
    private static void generateReports() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("GENERATING REPORTS");
        System.out.println("=".repeat(60));
        
        ReportGenerator reporter = new ReportGenerator(timetable);
        reporter.printLessonReport();
        reporter.printIncomeReport();
        
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }
    
    private static void viewMyBookings() {
        selectMember();
        if (currentMember == null) return;
        
        System.out.println("\n📋 CURRENT BOOKINGS:");
        System.out.println("-".repeat(50));
        List<String> bookings = currentMember.getBookedLessonIds();
        if (bookings.isEmpty()) {
            System.out.println("  No current bookings.");
        } else {
            for (String id : bookings) {
                Lesson l = timetable.getLesson(id);
                if (l != null) {
                    System.out.printf("  • %s | %s | %s\n",
                        l.getDate(), l.getTimeSlot().getDisplayName(),
                        l.getExerciseType().getDisplayName());
                }
            }
        }
        
        System.out.println("\n📋 ATTENDED LESSONS:");
        System.out.println("-".repeat(50));
        List<String> attended = currentMember.getAttendedLessonIds();
        if (attended.isEmpty()) {
            System.out.println("  No attended lessons yet.");
        } else {
            for (String id : attended) {
                Lesson l = timetable.getLesson(id);
                if (l != null) {
                    System.out.printf("  • %s | %s | %s\n",
                        l.getDate(), l.getTimeSlot().getDisplayName(),
                        l.getExerciseType().getDisplayName());
                }
            }
        }
    }
    
    private static void selectMember() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("SELECT MEMBER");
        System.out.println("=".repeat(40));
        List<Member> members = bookingSystem.getAllMembers();
        for (int i = 0; i < members.size(); i++) {
            System.out.printf("  %d. %s (%s)\n", i+1, members.get(i).getName(), 
                members.get(i).getMemberId());
        }
        int choice = getIntInput("Enter member number: ");
        if (choice >= 1 && choice <= members.size()) {
            currentMember = members.get(choice - 1);
            System.out.println("✓ Logged in as: " + currentMember.getName());
        } else {
            System.out.println("Invalid selection!");
            currentMember = null;
        }
    }
    
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int result = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return result;
    }
}