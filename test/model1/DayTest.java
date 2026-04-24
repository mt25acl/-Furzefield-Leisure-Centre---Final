package model1;

import model.Lesson;
import model.Day;
import model.ExerciseType;
import model.TimeSlot;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DayTest {

    private Day day;
    private static final LocalDate SAT_DATE = LocalDate.of(2026, 4, 25);
    private static final LocalDate SUN_DATE = LocalDate.of(2026, 4, 26);

    @BeforeAll
    public static void setUpClass() {
        System.out.println("=== DayTest Suite Starting ===");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("=== DayTest Suite Finished ===");
    }

    @BeforeEach
    public void setUp() {
        day = Day.SATURDAY;
        day.clearLessons();
        Day.SUNDAY.clearLessons();
    }

    @AfterEach
    public void tearDown() {
        day.clearLessons();
        Day.SUNDAY.clearLessons();
        day = null;
    }

    // ── Name tests ──────────────────────────────────────────

    @Test
    public void testSaturdayDayName() {
        assertEquals("SATURDAY", day.getDayName());
    }

    @Test
    public void testSundayDayName() {
        assertEquals("SUNDAY", Day.SUNDAY.getDayName());
    }

    @Test
    public void testDayNameNotNull() {
        assertNotNull(day.getDayName());
    }

    @Test
    public void testDayNameNotEmpty() {
        assertFalse(day.getDayName().isEmpty());
    }

    // ── Enum tests ──────────────────────────────────────────

    @Test
    public void testEnumHasTwoDays() {
        assertEquals(2, Day.values().length);
    }

    @Test
    public void testEnumValueOfSaturday() {
        assertEquals(Day.SATURDAY, Day.valueOf("SATURDAY"));
    }

    @Test
    public void testEnumValueOfSunday() {
        assertEquals(Day.SUNDAY, Day.valueOf("SUNDAY"));
    }

    @Test
    public void testToStringNotNull() {
        assertNotNull(day.toString());
    }

    @Test
    public void testSetDayNameThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> day.setDayName("Monday"));
    }

    // ── Lesson list tests ───────────────────────────────────

    @Test
    public void testGetLessonsNotNull() {
        assertNotNull(day.getLessons());
    }

    @Test
    public void testDayInitiallyHasNoLessons() {
        assertTrue(day.getLessons().isEmpty());
    }

    @Test
    public void testAddOneLessonIncreasesSize() {
        Lesson lesson = new Lesson("L001", ExerciseType.YOGA, Day.SATURDAY, TimeSlot.MORNING, SAT_DATE);
        day.addLesson(lesson);
        assertEquals(1, day.getLessons().size());
    }

    @Test
    public void testAddMultipleLessonsCorrectSize() {
        day.addLesson(new Lesson("L001", ExerciseType.YOGA,     Day.SATURDAY, TimeSlot.MORNING,   SAT_DATE));
        day.addLesson(new Lesson("L002", ExerciseType.ZUMBA,    Day.SATURDAY, TimeSlot.AFTERNOON, SAT_DATE));
        day.addLesson(new Lesson("L003", ExerciseType.AQUACISE, Day.SATURDAY, TimeSlot.EVENING,   SAT_DATE));
        assertEquals(3, day.getLessons().size());
    }

    @Test
    public void testAddedLessonIsInList() {
        Lesson lesson = new Lesson("L001", ExerciseType.YOGA, Day.SATURDAY, TimeSlot.MORNING, SAT_DATE);
        day.addLesson(lesson);
        assertTrue(day.getLessons().contains(lesson));
    }

    @Test
    public void testLessonsOrderPreserved() {
        Lesson l1 = new Lesson("L001", ExerciseType.YOGA,  Day.SATURDAY, TimeSlot.MORNING,   SAT_DATE);
        Lesson l2 = new Lesson("L002", ExerciseType.ZUMBA, Day.SATURDAY, TimeSlot.AFTERNOON, SAT_DATE);
        day.addLesson(l1);
        day.addLesson(l2);
        List<Lesson> lessons = day.getLessons();
        assertEquals(l1, lessons.get(0));
        assertEquals(l2, lessons.get(1));
    }

    @Test
    public void testClearLessonsEmptiesList() {
        day.addLesson(new Lesson("L001", ExerciseType.YOGA, Day.SATURDAY, TimeSlot.MORNING, SAT_DATE));
        day.clearLessons();
        assertTrue(day.getLessons().isEmpty());
    }

    // ── Sunday specific tests ───────────────────────────────

    @Test
    public void testSundayStartsEmpty() {
        assertTrue(Day.SUNDAY.getLessons().isEmpty());
    }

    @Test
    public void testSundayCanAddLesson() {
        Lesson lesson = new Lesson("L001", ExerciseType.BOX_FIT, Day.SUNDAY, TimeSlot.EVENING, SUN_DATE);
        Day.SUNDAY.addLesson(lesson);
        assertEquals(1, Day.SUNDAY.getLessons().size());
    }
}