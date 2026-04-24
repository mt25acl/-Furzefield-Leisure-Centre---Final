/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Model;

import model.Day;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Devar
 */
public class DayTest {
    
    public DayTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    
    @Test
    
     public void testDayNameSetCorrectly() {
        // Valid day name should be stored and returned correctly
        assertEquals("Monday", Day.getDayName());
    }
     
         @Test
    public void testDayNameNotNull() {
        // Day name should never be null after construction
        assertNotNull(Day.getDayName());
    }

    @Test
    public void testDayNameNotEmpty() {
        // Day name should not be an empty string
        assertFalse(Day.getDayName().isEmpty());
    }

    @Test
    public void testGetLessonsReturnsNotNull() {
        // getLessons() should return a list, not null
        assertNotNull(Day.getLessons());
    }

    @Test
    public void testDayInitiallyHasNoLessons() {
        // A new Day should start with an empty lesson list
        assertTrue(Day.getLessons().isEmpty());
    }
    
    @Test
    public void testAddLessonIncreasesSize() {
        // Adding one lesson should increase list size to 1
        Lesson lesson = new Lesson("L001", "Yoga", null, null, 10);
        Day.addLesson(lesson);
        assertEquals(1, Day.getLessons().size());
    }

    @Test
    public void testAddMultipleLessonsCorrectSize() {
       
        Day.addLesson(new Lesson("L001", "Yoga", null, null, 10));
        Day.addLesson(new Lesson("L002", "Pilates", null, null, 12));
        Day.addLesson(new Lesson("L003", "Spin", null, null, 8));
        assertEquals(3, Day.getLessons().size());
    }

     @Test
    public void testAddedLessonIsInList() {
        // The exact lesson added should appear in the returned list
        Lesson lesson = new Lesson("L001", "Yoga", null, null, 10);
        Day.addLesson(lesson);
        assertTrue(Day.getLessons().contains(lesson));
    }

    @Test
    public void testDayNameAllValidDays() {
        // All 7 valid day names should be accepted
        String[] validDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (String dayName : validDays) {
            Day d = new Day(DayName);
            assertEquals(dayName, d.getDayName());
        }
    }
    
    
    @Test
    public void testSetDayName() {
        // Setting a new day name should update correctly
        day.setDayName("Friday");
        assertEquals("Friday", day.getDayName());
    }

    @Test
    public void testLessonsListOrderPreserved() {
        // Lessons should be returned in the order they were added
        Lesson l1 = new Lesson("L001", "Yoga", null, null, 10);
        Lesson l2 = new Lesson("L002", "Zumba", null, null, 12);
        day.addLesson(l1);
        day.addLesson(l2);
        List<Lesson> lessons = day.getLessons();
        assertEquals(l1, lessons.get(0));
        assertEquals(l2, lessons.get(1));
    }
    
    
    
    
    
    @Test
    public void testValues() {
        System.out.println("values");
        Day[] expResult = null;
        Day[] result = Day.values();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("");
    }

    /**
     * Test of valueOf method, of class Day.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "";
        Day expResult = null;
        Day result = Day.valueOf(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
