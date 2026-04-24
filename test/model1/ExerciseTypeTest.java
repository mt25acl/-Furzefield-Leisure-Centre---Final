package model1;

import model.ExerciseType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Devar
 */
public class ExerciseTypeTest {

    public ExerciseTypeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        System.out.println("=== ExerciseTypeTest Suite Starting ===");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("=== ExerciseTypeTest Suite Finished ===");
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    // =====================================================
    // POSITIVE TEST CASES
    // =====================================================

    @Test
    public void testValuesReturnsAllFiveTypes() {
        // Enum must have exactly 5 constants
        assertEquals(5, ExerciseType.values().length);
    }

    @Test
    public void testValuesArrayNotNull() {
        assertNotNull(ExerciseType.values());
    }

    // --- YOGA ---
    @Test
    public void testYogaEnumExists() {
        assertEquals(ExerciseType.YOGA, ExerciseType.valueOf("YOGA"));
    }

    @Test
    public void testYogaDisplayName() {
        assertEquals("Yoga", ExerciseType.YOGA.getDisplayName());
    }

    @Test
    public void testYogaPrice() {
        assertEquals(12.0, ExerciseType.YOGA.getPrice());
    }

    // --- ZUMBA ---
    @Test
    public void testZumbaEnumExists() {
        assertEquals(ExerciseType.ZUMBA, ExerciseType.valueOf("ZUMBA"));
    }

    @Test
    public void testZumbaDisplayName() {
        assertEquals("Zumba", ExerciseType.ZUMBA.getDisplayName());
    }

    @Test
    public void testZumbaPrice() {
        assertEquals(10.0, ExerciseType.ZUMBA.getPrice());
    }

    // --- AQUACISE ---
    @Test
    public void testAquaciseEnumExists() {
        assertEquals(ExerciseType.AQUACISE, ExerciseType.valueOf("AQUACISE"));
    }

    @Test
    public void testAquaciseDisplayName() {
        assertEquals("Aquacise", ExerciseType.AQUACISE.getDisplayName());
    }

    @Test
    public void testAquacisePrice() {
        assertEquals(15.0, ExerciseType.AQUACISE.getPrice());
    }

    // --- BOX_FIT ---
    @Test
    public void testBoxFitEnumExists() {
        assertEquals(ExerciseType.BOX_FIT, ExerciseType.valueOf("BOX_FIT"));
    }

    @Test
    public void testBoxFitDisplayName() {
        assertEquals("Box Fit", ExerciseType.BOX_FIT.getDisplayName());
    }

    @Test
    public void testBoxFitPrice() {
        assertEquals(14.0, ExerciseType.BOX_FIT.getPrice());
    }

    // --- BODY_BLITZ ---
    @Test
    public void testBodyBlitzEnumExists() {
        assertEquals(ExerciseType.BODY_BLITZ, ExerciseType.valueOf("BODY_BLITZ"));
    }

    @Test
    public void testBodyBlitzDisplayName() {
        assertEquals("Body Blitz", ExerciseType.BODY_BLITZ.getDisplayName());
    }

    @Test
    public void testBodyBlitzPrice() {
        assertEquals(13.0, ExerciseType.BODY_BLITZ.getPrice());
    }

    // --- General Enum Behaviour ---
    @Test
    public void testAllDisplayNamesNotNull() {
        for (ExerciseType type : ExerciseType.values()) {
            assertNotNull(type.getDisplayName());
        }
    }

    @Test
    public void testAllDisplayNamesNotEmpty() {
        for (ExerciseType type : ExerciseType.values()) {
            assertFalse(type.getDisplayName().isEmpty());
        }
    }

    @Test
    public void testAllPricesArePositive() {
        for (ExerciseType type : ExerciseType.values()) {
            assertTrue(type.getPrice() > 0);
        }
    }

    @Test
    public void testEnumOrdinalsAreUnique() {
        ExerciseType[] types = ExerciseType.values();
        long unique = java.util.Arrays.stream(types)
                .mapToInt(ExerciseType::ordinal)
                .distinct()
                .count();
        assertEquals(types.length, unique);
    }

    @Test
    public void testEnumNamesAreUnique() {
        ExerciseType[] types = ExerciseType.values();
        long unique = java.util.Arrays.stream(types)
                .map(ExerciseType::name)
                .distinct()
                .count();
        assertEquals(types.length, unique);
    }

    @Test
    public void testEnumComparisonWithEquals() {
        ExerciseType t1 = ExerciseType.YOGA;
        ExerciseType t2 = ExerciseType.YOGA;
        assertEquals(t1, t2);
    }

    @Test
    public void testEnumSingletonDoubleEquals() {
        assertTrue(ExerciseType.YOGA == ExerciseType.YOGA);
    }

    @Test
    public void testEnumCanBeUsedInSwitch() {
        ExerciseType type = ExerciseType.ZUMBA;
        String result = "";
        switch (type) {
            case YOGA -> result = "yoga";
            case ZUMBA -> result = "zumba";
            case AQUACISE -> result = "aquacise";
            case BOX_FIT -> result = "box_fit";
            case BODY_BLITZ -> result = "body_blitz";
        }
        assertEquals("zumba", result);
    }

    @Test
    public void testFirstOrdinalIsZero() {
        assertEquals(0, ExerciseType.values()[0].ordinal());
    }

    @Test
    public void testYogaOrdinalIsZero() {
        assertEquals(0, ExerciseType.YOGA.ordinal());
    }

    @Test
    public void testBodyBlitzOrdinalIsFour() {
        assertEquals(4, ExerciseType.BODY_BLITZ.ordinal());
    }

   
    @Test
    public void testValueOfInvalidNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            ExerciseType.valueOf("PILATES")
        );
    }

    @Test
    public void testValueOfLowercaseThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            ExerciseType.valueOf("yoga")
        );
    }

    @Test
    public void testValueOfEmptyStringThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            ExerciseType.valueOf("")
        );
    }

    @Test
    public void testValueOfNullThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            ExerciseType.valueOf(null)
        );
    }

    @Test
    public void testValueOfWithSpacesThrowsException() {
        // "Box Fit" uses space — enum name is BOX_FIT with underscore
        assertThrows(IllegalArgumentException.class, () ->
            ExerciseType.valueOf("Box Fit")
        );
    }

    @Test
    public void testValueOfMixedCaseThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            ExerciseType.valueOf("Zumba")
        );
    }

    @Test
    public void testValueOfNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            ExerciseType.valueOf("123")
        );
    }

    @Test
    public void testTwoDifferentEnumsNotEqual() {
        assertNotEquals(ExerciseType.YOGA, ExerciseType.ZUMBA);
    }

    @Test
    public void testDifferentEnumsHaveDifferentOrdinals() {
        assertNotEquals(ExerciseType.YOGA.ordinal(), ExerciseType.ZUMBA.ordinal());
    }

    @Test
    public void testDifferentEnumsHaveDifferentDisplayNames() {
        assertNotEquals(ExerciseType.YOGA.getDisplayName(),
                        ExerciseType.ZUMBA.getDisplayName());
    }

    @Test
    public void testDifferentEnumsHaveDifferentPrices() {
        assertNotEquals(ExerciseType.YOGA.getPrice(),
                        ExerciseType.AQUACISE.getPrice());
    }

    @Test
    public void testEnumDoesNotEqualNull() {
        assertNotEquals(null, ExerciseType.YOGA);
    }

    @Test
    public void testEnumDoesNotEqualString() {
        assertNotEquals("YOGA", ExerciseType.YOGA);
    }

    @Test
    public void testPriceIsNotZero() {
        assertNotEquals(0.0, ExerciseType.YOGA.getPrice());
    }

    @Test
    public void testPriceIsNotNegative() {
        for (ExerciseType type : ExerciseType.values()) {
            assertFalse(type.getPrice() < 0);
        }
    }

    @Test
    public void testDisplayNameDoesNotMatchEnumName() {
        // "Box Fit" (display) != "BOX_FIT" (enum name)
        assertNotEquals(ExerciseType.BOX_FIT.name(),
                        ExerciseType.BOX_FIT.getDisplayName());
    }

    @Test
    public void testBodyBlitzDisplayNameNotEmpty() {
        assertNotEquals("", ExerciseType.BODY_BLITZ.getDisplayName());
    }
}