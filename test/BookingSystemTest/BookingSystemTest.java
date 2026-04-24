package Service;

import model.*;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BookingSystemTest {
    
    private BookingSystem bookingSystem;
    private Timetable timetable;
    private Member testMember;
    private Lesson testLesson1;
    private Lesson testLesson2;
    private Lesson testLesson3;
    
    @BeforeEach
    void setUp() {
        // Create timetable
        timetable = new Timetable();
        List<ExerciseType> exercises = Arrays.asList(
            ExerciseType.YOGA, ExerciseType.ZUMBA
        );
        timetable.generateTimetable(1, exercises);
        
        // Create booking system
        bookingSystem = new BookingSystem(timetable);
        
        // Create test member
        testMember = new Member("TEST001", "Test User");
        bookingSystem.addMember(testMember);
        
        // Get test lessons
        List<Lesson> allLessons = timetable.getAllLessons();
        testLesson1 = allLessons.get(0);
        testLesson2 = allLessons.get(1);
        testLesson3 = allLessons.get(2);
    }
    
    // ========== CONSTRUCTOR AND INITIALIZATION TESTS ==========
    
    @Test
    void testConstructor() {
        assertNotNull(bookingSystem);
        assertNotNull(bookingSystem.getAllMembers());
        assertTrue(bookingSystem.getAllMembers().isEmpty() == false);
    }
    
    @Test
    void testAddMember() {
        Member newMember = new Member("TEST002", "New User");
        bookingSystem.addMember(newMember);
        
        assertEquals(2, bookingSystem.getAllMembers().size());
        assertNotNull(bookingSystem.getMember("TEST002"));
        assertEquals("New User", bookingSystem.getMember("TEST002").getName());
    }
    
    @Test
    void testGetMemberExists() {
        Member found = bookingSystem.getMember("TEST001");
        assertNotNull(found);
        assertEquals("Test User", found.getName());
    }
    
    @Test
    void testGetMemberNotExists() {
        Member found = bookingSystem.getMember("NONEXISTENT");
        assertNull(found);
    }
    
    @Test
    void testGetAllMembers() {
        List<Member> members = bookingSystem.getAllMembers();
        assertEquals(1, members.size());
        assertEquals("TEST001", members.get(0).getMemberId());
    }
    
    @Test
    void testGetAllMembersReturnsCopy() {
        List<Member> members1 = bookingSystem.getAllMembers();
        List<Member> members2 = bookingSystem.getAllMembers();
        
        // Should be different objects but same content
        assertNotSame(members1, members2);
        assertEquals(members1.size(), members2.size());
    }
    
    // ========== BOOK LESSON TESTS ==========
    
    @Test
    void testBookLessonSuccess() {
        boolean result = bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        assertTrue(result);
        assertEquals(1, testMember.getBookedLessonIds().size());
        assertTrue(testMember.getBookedLessonIds().contains(testLesson1.getLessonId()));
        assertEquals(1, testLesson1.getBookingsCount());
    }
    
    @Test
    void testBookLessonWithInvalidMemberId() {
        boolean result = bookingSystem.bookLesson("INVALID_ID", testLesson1.getLessonId());
        
        assertFalse(result);
        assertEquals(0, testLesson1.getBookingsCount());
    }
    
    @Test
    void testBookLessonWithInvalidLessonId() {
        boolean result = bookingSystem.bookLesson("TEST001", "INVALID_LESSON_ID");
        
        assertFalse(result);
        assertEquals(0, testMember.getBookedLessonIds().size());
    }
    
    @Test
    void testBookLessonWithNullMemberId() {
        boolean result = bookingSystem.bookLesson(null, testLesson1.getLessonId());
        
        assertFalse(result);
    }
    
    @Test
    void testBookLessonWithNullLessonId() {
        boolean result = bookingSystem.bookLesson("TEST001", null);
        
        assertFalse(result);
    }
    
    @Test
    void testBookSameLessonTwice() {
        // First booking
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        // Second booking (should fail)
        boolean result = bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        assertFalse(result);
        assertEquals(1, testLesson1.getBookingsCount());
    }
    
    @Test
    void testBookLessonWhenFull() {
        // Fill lesson to capacity (max 4)
        for (int i = 0; i < 4; i++) {
            Member tempMember = new Member("TEMP" + i, "Temp User " + i);
            bookingSystem.addMember(tempMember);
            bookingSystem.bookLesson("TEMP" + i, testLesson1.getLessonId());
        }
        
        // Try to book with test member
        boolean result = bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        assertFalse(result);
        assertEquals(4, testLesson1.getBookingsCount());
    }
    
    @Test
    void testBookLessonWithTimeConflict() {
        // Get two lessons at same time on same day
        List<Lesson> satMorningLessons = new ArrayList<>();
        for (Lesson lesson : timetable.getAllLessons()) {
            if (lesson.getDay() == Day.SATURDAY && 
                lesson.getTimeSlot() == TimeSlot.MORNING) {
                satMorningLessons.add(lesson);
            }
        }
        
        if (satMorningLessons.size() >= 2) {
            // Book first morning lesson
            bookingSystem.bookLesson("TEST001", satMorningLessons.get(0).getLessonId());
            
            // Try to book second morning lesson (same time) - should fail
            boolean result = bookingSystem.bookLesson("TEST001", satMorningLessons.get(1).getLessonId());
            
            assertFalse(result);
            assertEquals(1, testMember.getBookedLessonIds().size());
        }
    }
    
    @Test
    void testBookMultipleLessonsDifferentTimes() {
        // Book Saturday morning
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        // Book Saturday afternoon
        bookingSystem.bookLesson("TEST001", testLesson2.getLessonId());
        
        // Book Sunday morning
        bookingSystem.bookLesson("TEST001", testLesson3.getLessonId());
        
        assertEquals(3, testMember.getBookedLessonIds().size());
    }
    
    // ========== CANCEL BOOKING TESTS ==========
    
    @Test
    void testCancelBookingSuccess() {
        // First book a lesson
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        assertEquals(1, testLesson1.getBookingsCount());
        
        // Then cancel it
        boolean result = bookingSystem.cancelBooking("TEST001", testLesson1.getLessonId());
        
        assertTrue(result);
        assertEquals(0, testMember.getBookedLessonIds().size());
        assertEquals(0, testLesson1.getBookingsCount());
    }
    
    @Test
    void testCancelBookingWithInvalidMemberId() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        boolean result = bookingSystem.cancelBooking("INVALID_ID", testLesson1.getLessonId());
        
        assertFalse(result);
        assertEquals(1, testLesson1.getBookingsCount());
    }
    
    @Test
    void testCancelBookingWithInvalidLessonId() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        boolean result = bookingSystem.cancelBooking("TEST001", "INVALID_ID");
        
        assertFalse(result);
        assertEquals(1, testMember.getBookedLessonIds().size());
    }
    
    @Test
    void testCancelBookingNotBooked() {
        boolean result = bookingSystem.cancelBooking("1", testLesson1.getLessonId());
        
        assertFalse(result);
        assertEquals(0, testLesson1.getBookingsCount());
    }
    
    @Test
    void testCancelBookingWithNullMemberId() {
        boolean result = bookingSystem.cancelBooking(null, testLesson1.getLessonId());
        
        assertFalse(result);
    }
    
    @Test
    void testCancelBookingWithNullLessonId() {
        boolean result = bookingSystem.cancelBooking("TEST001", null);
        
        assertFalse(result);
    }
    
    @Test
    void testCancelBookingAfterCancelling() {
        // Book
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        // Cancel
        bookingSystem.cancelBooking("TEST001", testLesson1.getLessonId());
        
        // Try to cancel again
        boolean result = bookingSystem.cancelBooking("TEST001", testLesson1.getLessonId());
        
        assertFalse(result);
    }
    
    // ========== CHANGE BOOKING TESTS ==========
    
    @Test
    void testChangeBookingSuccess() {
        // Book original lesson
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        assertEquals(1, testLesson1.getBookingsCount());
        
        // Change to different lesson
        boolean result = bookingSystem.changeBooking("TEST001", 
            testLesson1.getLessonId(), testLesson2.getLessonId());
        
        assertTrue(result);
        assertEquals(0, testLesson1.getBookingsCount());
        assertEquals(1, testLesson2.getBookingsCount());
        assertTrue(testMember.getBookedLessonIds().contains(testLesson2.getLessonId()));
        assertFalse(testMember.getBookedLessonIds().contains(testLesson1.getLessonId()));
    }
    
    @Test
    void testChangeBookingWithInvalidMemberId() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        boolean result = bookingSystem.changeBooking("INVALID_ID", 
            testLesson1.getLessonId(), testLesson2.getLessonId());
        
        assertFalse(result);
        assertEquals(1, testLesson1.getBookingsCount());
        assertEquals(0, testLesson2.getBookingsCount());
    }
    
    @Test
    void testChangeBookingWithInvalidOldLessonId() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        boolean result = bookingSystem.changeBooking("TEST001", 
            "INVALID_ID", testLesson2.getLessonId());
        
        assertFalse(result);
        assertEquals(1, testLesson1.getBookingsCount());
    }
    
    @Test
    void testChangeBookingWithInvalidNewLessonId() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        boolean result = bookingSystem.changeBooking("TEST001", 
            testLesson1.getLessonId(), "INVALID_ID");
        
        assertFalse(result);
        assertEquals(1, testLesson1.getBookingsCount());
    }
    
    @Test
    void testChangeBookingWhenNewLessonFull() {
        // Fill new lesson to capacity
        for (int i = 0; i < 4; i++) {
            Member tempMember = new Member("TEMP" + i, "Temp User " + i);
            bookingSystem.addMember(tempMember);
            bookingSystem.bookLesson("TEMP" + i, testLesson2.getLessonId());
        }
        
        // Book original lesson
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        // Try to change to full lesson
        boolean result = bookingSystem.changeBooking("TEST001", 
            testLesson1.getLessonId(), testLesson2.getLessonId());
        
        assertFalse(result);
        assertEquals(1, testLesson1.getBookingsCount());
        assertEquals(4, testLesson2.getBookingsCount());
    }
    
    @Test
    void testChangeBookingWithTimeConflict() {
        // Get conflicting lessons (same day, same time)
        List<Lesson> satMorningLessons = new ArrayList<>();
        for (Lesson lesson : timetable.getAllLessons()) {
            if (lesson.getDay() == Day.SATURDAY && 
                lesson.getTimeSlot() == TimeSlot.MORNING) {
                satMorningLessons.add(lesson);
            }
        }
        
        if (satMorningLessons.size() >= 2) {
            // Book first morning lesson
            bookingSystem.bookLesson("TEST001", satMorningLessons.get(0).getLessonId());
            
            // Try to change to second morning lesson (same time) - should fail
            boolean result = bookingSystem.changeBooking("TEST001", 
                satMorningLessons.get(0).getLessonId(), 
                satMorningLessons.get(1).getLessonId());
            
            assertFalse(result);
        }
    }
    
    @Test
    void testChangeBookingWhenNoExistingBooking() {
        boolean result = bookingSystem.changeBooking("TEST001", 
            testLesson1.getLessonId(), testLesson2.getLessonId());
        
        assertFalse(result);
    }
    
    @Test
    void testChangeBookingToSameLesson() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        boolean result = bookingSystem.changeBooking("TEST001", 
            testLesson1.getLessonId(), testLesson1.getLessonId());
        
        assertFalse(result);
    }
    
    // ========== MARK ATTENDANCE TESTS ==========
    
    @Test
    void testMarkAttendanceSuccess() {
        // Book lesson first
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        // Mark attendance
        bookingSystem.markAttendance("TEST001", testLesson1.getLessonId());
        
        assertEquals(1, testMember.getAttendedLessonIds().size());
        assertTrue(testMember.getAttendedLessonIds().contains(testLesson1.getLessonId()));
    }
    
    @Test
    void testMarkAttendanceWithoutBooking() {
        bookingSystem.markAttendance("TEST001", testLesson1.getLessonId());
        
        assertEquals(0, testMember.getAttendedLessonIds().size());
    }
    
    @Test
    void testMarkAttendanceWithInvalidMemberId() {
        bookingSystem.markAttendance("INVALID_ID", testLesson1.getLessonId());
        
        // Should not throw exception
        assertTrue(true);
    }
    
    @Test
    void testMarkAttendanceWithInvalidLessonId() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        bookingSystem.markAttendance("TEST001", "INVALID_ID");
        
        assertEquals(0, testMember.getAttendedLessonIds().size());
    }
    
    @Test
    void testMarkAttendanceWithNullMemberId() {
        bookingSystem.markAttendance(null, testLesson1.getLessonId());
        
        assertTrue(true); // Should not throw exception
    }
    
    @Test
    void testMultipleAttendanceMarks() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        // Mark attendance twice
        bookingSystem.markAttendance("TEST001", testLesson1.getLessonId());
        bookingSystem.markAttendance("TEST001", testLesson1.getLessonId());
        
        // Should only be added once
        assertEquals(1, testMember.getAttendedLessonIds().size());
    }
    
    // ========== ADD REVIEW TESTS ==========
    
    @Test
    void testAddReviewSuccess() {
        // Book, attend, then review
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        bookingSystem.markAttendance("TEST001", testLesson1.getLessonId());
        bookingSystem.addReview("TEST001", testLesson1.getLessonId(), 5, "Excellent!");
        
        assertEquals(1, testLesson1.getReviews().size());
        assertEquals(5.0, testLesson1.getAverageRating());
    }
    
    @Test
    void testAddReviewWithoutAttendance() {
        // Book but don't attend
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        bookingSystem.addReview("TEST001", testLesson1.getLessonId(), 5, "Great!");
        
        assertEquals(0, testLesson1.getReviews().size());
        assertEquals(0.0, testLesson1.getAverageRating());
    }
    
    @Test
    void testAddReviewWithInvalidMemberId() {
        bookingSystem.addReview("INVALID_ID", testLesson1.getLessonId(), 4, "Good");
        
        assertEquals(0, testLesson1.getReviews().size());
    }
    
    @Test
    void testAddReviewWithInvalidLessonId() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        bookingSystem.markAttendance("TEST001", testLesson1.getLessonId());
        
        bookingSystem.addReview("TEST001", "INVALID_ID", 4, "Good");
        
        assertEquals(0, testLesson1.getReviews().size());
    }
    
    @Test
    void testAddReviewWithInvalidRating() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        bookingSystem.markAttendance("TEST001", testLesson1.getLessonId());
        
        // Rating 0 is invalid (should be 1-5)
        // This might throw exception or handle gracefully
        try {
            bookingSystem.addReview("TEST001", testLesson1.getLessonId(), 0, "Bad");
            // If no exception, review should not be added
            assertEquals(0, testLesson1.getReviews().size());
        } catch (IllegalArgumentException e) {
            assertEquals("Rating must be 1-5", e.getMessage());
        }
    }
    
    @Test
    void testAddReviewWithNullComment() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        bookingSystem.markAttendance("TEST001", testLesson1.getLessonId());
        
        bookingSystem.addReview("TEST001", testLesson1.getLessonId(), 3, null);
        
        assertNotNull(testLesson1.getReviews().get(0));
    }
    
    @Test
    void testMultipleReviewsSameLesson() {
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        bookingSystem.markAttendance("TEST001", testLesson1.getLessonId());
        
        bookingSystem.addReview("TEST001", testLesson1.getLessonId(), 4, "Good");
        bookingSystem.addReview("TEST001", testLesson1.getLessonId(), 5, "Excellent");
        
        // Should allow multiple reviews
        assertEquals(2, testLesson1.getReviews().size());
        assertEquals(4.5, testLesson1.getAverageRating());
    }
    
    // ========== EDGE CASE TESTS ==========
    
    @Test
    void testConcurrentBookings() {
        // Simulate multiple members booking same lesson
        Member member1 = new Member("M001", "User1");
        Member member2 = new Member("M002", "User2");
        Member member3 = new Member("M003", "User3");
        Member member4 = new Member("M004", "User4");
        Member member5 = new Member("M005", "User5");
        
        bookingSystem.addMember(member1);
        bookingSystem.addMember(member2);
        bookingSystem.addMember(member3);
        bookingSystem.addMember(member4);
        bookingSystem.addMember(member5);
        
        // All try to book same lesson
        boolean r1 = bookingSystem.bookLesson("M001", testLesson1.getLessonId());
        boolean r2 = bookingSystem.bookLesson("M002", testLesson1.getLessonId());
        boolean r3 = bookingSystem.bookLesson("M003", testLesson1.getLessonId());
        boolean r4 = bookingSystem.bookLesson("M004", testLesson1.getLessonId());
        boolean r5 = bookingSystem.bookLesson("M005", testLesson1.getLessonId());
        
        // First 4 should succeed, 5th should fail (max capacity 4)
        assertTrue(r1);
        assertTrue(r2);
        assertTrue(r3);
        assertTrue(r4);
        assertFalse(r5);
        assertEquals(4, testLesson1.getBookingsCount());
    }
    
    @Test
    void testMaximumLessonsPerMember() {
        // A member can book max 3 lessons per day (morning, afternoon, evening)
        // Try to book 4 lessons on same day
        
        List<Lesson> saturdayLessons = new ArrayList<>();
        for (Lesson lesson : timetable.getAllLessons()) {
            if (lesson.getDay() == Day.SATURDAY) {
                saturdayLessons.add(lesson);
            }
        }
        
        int booked = 0;
        for (Lesson lesson : saturdayLessons) {
            if (bookingSystem.bookLesson("TEST001", lesson.getLessonId())) {
                booked++;
            }
        }
        
        // Should only be able to book 3 lessons (one per time slot)
        assertEquals(3, booked);
    }
    
    @Test
    void testBookingHistoryAfterCancellation() {
        // Book
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        // Cancel
        bookingSystem.cancelBooking("TEST001", testLesson1.getLessonId());
        
        // Should have no bookings
        assertEquals(0, testMember.getBookedLessonIds().size());
        
        // Lesson should have 0 bookings
        assertEquals(0, testLesson1.getBookingsCount());
    }
    
    @Test
    void testGetTimetable() {
        Timetable retrieved = bookingSystem.getTimetable();
        assertNotNull(retrieved);
        assertEquals(timetable, retrieved);
    }
    
    @Test
    void testMultipleMembersDifferentBookings() {
        Member member2 = new Member("TEST002", "Second User");
        bookingSystem.addMember(member2);
        
        // Member1 books lesson1
        bookingSystem.bookLesson("TEST001", testLesson1.getLessonId());
        
        // Member2 books lesson2
        bookingSystem.bookLesson("TEST002", testLesson2.getLessonId());
        
        assertEquals(1, testMember.getBookedLessonIds().size());
        assertEquals(1, member2.getBookedLessonIds().size());
        assertEquals(1, testLesson1.getBookingsCount());
        assertEquals(1, testLesson2.getBookingsCount());
    }
}