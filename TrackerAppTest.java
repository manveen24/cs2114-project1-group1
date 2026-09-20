import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the main functions and input validation of TrackerApp.
 *
 * @author Zachary Vest
 * @version 2026.09.20
 */
class TrackerAppTest
{
    private PrintStream originalOut;
    private java.io.InputStream originalIn;

    /**
     * Sets up each test by saving the original input and output
     * streams and removing any previous save file.
     */
    @BeforeEach
    void setUp()
    {
        originalOut = System.out;
        originalIn = System.in;

        try
        {
            Files.deleteIfExists(Paths.get("assignments.txt"));
        }
        catch (Exception e)
        {
            fail("Could not clean up assignments.txt");
        }
    }

    /**
     * Restores the original input and output streams after each test
     * and removes the test save file.
     */
    @AfterEach
    void tearDown()
    {
        System.setIn(originalIn);
        System.setOut(originalOut);

        try
        {
            Files.deleteIfExists(Paths.get("assignments.txt"));
        }
        catch (Exception e)
        {
            // Nothing needed here
        }
    }

    /**
     * Tests that a valid course can be added.
     */
    @Test
    void testAddCourse()
    {
        String input = "1\nCS 2114\n6\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        TrackerApp.main(new String[0]);

        assertNotNull(TrackerApp.tracker);
        assertTrue(
            TrackerApp.tracker.getCourses().contains("CS 2114"));
    }

    /**
     * Tests that non-numeric menu input is rejected.
     */
    @Test
    void testInvalidMenuInput()
    {
        String input = "abc\n6\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        TrackerApp.main(new String[0]);

        String result = output.toString();

        assertTrue(
            result.contains("Please enter a number."));
    }

    /**
     * Tests that a menu number outside the valid range is rejected.
     */
    @Test
    void testInvalidMenuNumber()
    {
        String input = "7\n6\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        TrackerApp.main(new String[0]);

        String result = output.toString();

        assertTrue(
            result.contains("Invalid choice. Please try again."));
    }

    /**
     * Tests that an assignment can be added to an existing course.
     */
    @Test
    void testAddAssignment()
    {
        String input =
            "1\nCS 2114\n"
            + "2\n1\nProject 1\n2026-10-01\n"
            + "6\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        TrackerApp.main(new String[0]);

        assertEquals(
            1,
            TrackerApp.tracker.getAssignmentsSorted().size());

        Assignment assignment =
            TrackerApp.tracker.getAssignmentsSorted().get(0);

        assertEquals("Project 1", assignment.getName());
        assertEquals("CS 2114", assignment.getCourseName());
    }

    /**
     * Tests that an invalid assignment date is rejected.
     */
    @Test
    void testInvalidAssignmentDate()
    {
        String input =
            "1\nCS 2114\n"
            + "2\n1\nProject 1\n"
            + "tomorrow\n2026-10-01\n"
            + "6\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        TrackerApp.main(new String[0]);

        String result = output.toString();

        assertTrue(
            result.contains("Please enter date in correct format"));
    }

    /**
     * Tests that duplicate course names are rejected.
     */
    @Test
    void testDuplicateCourse()
    {
        String input =
            "1\nCS 2114\n"
            + "1\nCS 2114\n"
            + "CS 2204\n"
            + "6\n";

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        TrackerApp.main(new String[0]);

        String result = output.toString();

        assertTrue(
            result.contains("Invalid course name."));

        assertTrue(
            TrackerApp.tracker.getCourses().contains("CS 2204"));
    }

    /**
     * Tests that the user cannot add an assignment before adding
     * a course.
     */
    @Test
    void testAddAssignmentWithoutCourse()
    {
        String input = "2\n6\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        TrackerApp.main(new String[0]);

        String result = output.toString();

        assertTrue(
            result.contains("Please add a course first"));
    }
}