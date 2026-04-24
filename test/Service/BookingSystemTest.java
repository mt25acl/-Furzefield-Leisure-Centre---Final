package Service;

import java.time.LocalDate;
import java.util.List;
import model.Day;
import model.ExerciseType;
import model.Lesson;
import model.Member;
import model.TimeSlot;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import service.BookingSystem;
import service.Timetable;

public class BookingSystemTest {

    private BookingSystem instance;
    private Timetable timetable;

    private static final String MEMBER_ID  = "M001";
    private static final String LESSON_ID  = "L001";
    private static final String LESSON_ID2 = "L002";

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        timetable = new Timetable();

        Lesson lesson1 = new Lesson(LESSON_ID, ExerciseType.YOGA, Day.SATURDAY,
                TimeSlot.MORNING, LocalDate.of(2026, 4, 25));
        Lesson lesson2 = new Lesson(LESSON_ID2, ExerciseType.AQUACISE, Day.SUNDAY,
                TimeSlot.AFTERNOON, LocalDate.of(2026, 4, 26));

        timetable.addLesson(lesson1);
        timetable.addLesson(lesson2);

        instance = new BookingSystem(timetable);

        Member member = new Member(MEMBER_ID, "Test User");
        instance.addMember(member);
    }

    @After
    public void tearDown() {
        instance  = null;
        timetable = null;
    }

    // ------------------------------------------------------------------ //
    //  bookLesson
    // ------------------------------------------------------------------ //

    @Test
    public void testBookLesson_success() {
        boolean result = instance.bookLesson(MEMBER_ID, LESSON_ID);
        assertTrue("Expected bookLesson to return true for a valid booking", result);
    }

    @Test
    public void testBookLesson_duplicateBooking_returnsFalse() {
        instance.bookLesson(MEMBER_ID, LESSON_ID);
        boolean result = instance.bookLesson(MEMBER_ID, LESSON_ID);
        assertFalse("Expected bookLesson to return false for a duplicate booking", result);
    }

    @Test
    public void testBookLesson_invalidMember_returnsFalse() {
        boolean result = instance.bookLesson("INVALID_ID", LESSON_ID);
        assertFalse("Expected bookLesson to return false for an unknown member", result);
    }

    @Test
    public void testBookLesson_invalidLesson_returnsFalse() {
        boolean result = instance.bookLesson(MEMBER_ID, "INVALID_LESSON");
        assertFalse("Expected bookLesson to return false for an unknown lesson", result);
    }

    // ------------------------------------------------------------------ //
    //  cancelBooking
    // ------------------------------------------------------------------ //

    @Test
    public void testCancelBooking_success() {
        instance.bookLesson(MEMBER_ID, LESSON_ID);
        boolean result = instance.cancelBooking(MEMBER_ID, LESSON_ID);
        assertTrue("Expected cancelBooking to return true for an existing booking", result);
    }

    @Test
    public void testCancelBooking_nonExistentBooking_returnsFalse() {
        boolean result = instance.cancelBooking(MEMBER_ID, LESSON_ID);
        assertFalse("Expected cancelBooking to return false when no booking exists", result);
    }

    @Test
    public void testCancelBooking_invalidMember_returnsFalse() {
        boolean result = instance.cancelBooking("INVALID_ID", LESSON_ID);
        assertFalse("Expected cancelBooking to return false for an unknown member", result);
    }

    // ------------------------------------------------------------------ //
    //  changeBooking
    // ------------------------------------------------------------------ //

    @Test
    public void testChangeBooking_success() {
        instance.bookLesson(MEMBER_ID, LESSON_ID);
        boolean result = instance.changeBooking(MEMBER_ID, LESSON_ID, LESSON_ID2);
        assertTrue("Expected changeBooking to return true when moving to a new lesson", result);
    }

    @Test
    public void testChangeBooking_noOriginalBooking_returnsFalse() {
        // Cannot change a booking that was never made
        boolean result = instance.changeBooking(MEMBER_ID, LESSON_ID, LESSON_ID2);
        assertFalse("Expected changeBooking to return false when original booking does not exist", result);
    }

    @Test
    public void testChangeBooking_invalidNewLesson_returnsFalse() {
        instance.bookLesson(MEMBER_ID, LESSON_ID);
        boolean result = instance.changeBooking(MEMBER_ID, LESSON_ID, "INVALID_LESSON");
        assertFalse("Expected changeBooking to return false when new lesson does not exist", result);
    }

    // ------------------------------------------------------------------ //
    //  markAttendance
    // ------------------------------------------------------------------ //

    @Test
    public void testMarkAttendance_doesNotThrow() {
        instance.bookLesson(MEMBER_ID, LESSON_ID);
        try {
            instance.markAttendance(MEMBER_ID, LESSON_ID);
        } catch (Exception e) {
            fail("markAttendance threw an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMarkAttendance_unknownMember_doesNotThrow() {
        // BookingSystem silently ignores unknown members — must not crash
        try {
            instance.markAttendance("INVALID_ID", LESSON_ID);
        } catch (Exception e) {
            fail("markAttendance should not throw for an unknown member: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------ //
    //  addReview
    // ------------------------------------------------------------------ //

    @Test
    public void testAddReview_doesNotThrow() {
        instance.bookLesson(MEMBER_ID, LESSON_ID);
        instance.markAttendance(MEMBER_ID, LESSON_ID);
        try {
            instance.addReview(MEMBER_ID, LESSON_ID, 5, "Great lesson!");
        } catch (Exception e) {
            fail("addReview threw an unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAddReview_invalidMemberOrLesson_doesNotThrow() {
        // BookingSystem guards with null checks — must not crash
        try {
            instance.addReview("INVALID_ID", LESSON_ID, 4, "Good");
            instance.addReview(MEMBER_ID, "INVALID_LESSON", 4, "Good");
        } catch (Exception e) {
            fail("addReview should not throw when member or lesson is unknown: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------ //
    //  addMember / getMember / getAllMembers
    // ------------------------------------------------------------------ //

    @Test
    public void testAddMember_thenGetMember_returnsCorrectMember() {
        Member newMember = new Member("M002", "Alice");
        instance.addMember(newMember);

        Member result = instance.getMember("M002");
        assertNotNull("Expected getMember to return a non-null member", result);
        assertEquals("M002", result.getMemberId());
    }

    @Test
    public void testGetMember_unknownId_returnsNull() {
        Member result = instance.getMember("DOES_NOT_EXIST");
        assertNull("Expected getMember to return null for an unknown ID", result);
    }

    @Test
    public void testGetAllMembers_returnsNonEmptyList() {
        List<Member> result = instance.getAllMembers();
        assertNotNull("Expected getAllMembers to return a non-null list", result);
        assertFalse("Expected getAllMembers to contain at least one member", result.isEmpty());
    }

    @Test
    public void testGetAllMembers_containsAddedMember() {
        Member newMember = new Member("M003", "Bob");
        instance.addMember(newMember);

        List<Member> result = instance.getAllMembers();
        boolean found = result.stream().anyMatch(m -> "M003".equals(m.getMemberId()));
        assertTrue("Expected getAllMembers to contain the newly added member", found);
    }

    @Test
    public void testGetAllMembers_countIncreasesAfterAdd() {
        int before = instance.getAllMembers().size();
        instance.addMember(new Member("M004", "Charlie"));
        int after = instance.getAllMembers().size();
        assertEquals("Expected member count to increase by 1 after addMember", before + 1, after);
    }
}