import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the functionality and input validation of AssignmentTracker.
 *
 * @author Zachary Vest
 * @version 2026.09.21
 */
class AssignmentTrackerTest
{
    /**
     * Tests that a new AssignmentTracker starts with no courses.
     */
    @Test
    void testConstructor()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        assertNotNull(tracker);
        assertTrue(tracker.getCourses().isEmpty());
        assertTrue(tracker.getAssignmentsSorted().isEmpty());
    }

    /**
     * Tests that a course can be added successfully.
     */
    @Test
    void testAddCourse()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        tracker.addCourse("CS 2114");

        assertTrue(tracker.getCourses().contains("CS 2114"));
    }

    /**
     * Tests that course names are trimmed when added.
     */
    @Test
    void testAddCourseTrimsName()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        tracker.addCourse("  CS 2114  ");

        assertTrue(tracker.getCourses().contains("CS 2114"));
    }

    /**
     * Tests that duplicate courses are rejected regardless of case.
     */
    @Test
    void testDuplicateCourse()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        tracker.addCourse("CS 2114");

        assertThrows(
            IllegalArgumentException.class,
            () -> tracker.addCourse("cs 2114"));
    }

    /**
     * Tests that a null course name is rejected.
     */
    @Test
    void testNullCourse()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        assertThrows(
            IllegalArgumentException.class,
            () -> tracker.addCourse(null));
    }

    /**
     * Tests that a blank course name is rejected.
     */
    @Test
    void testBlankCourse()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        assertThrows(
            IllegalArgumentException.class,
            () -> tracker.addCourse("   "));
    }

    /**
     * Tests that a course containing a tab is rejected.
     */
    @Test
    void testCourseWithTab()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        assertThrows(
            IllegalArgumentException.class,
            () -> tracker.addCourse("CS\t2114"));
    }

    /**
     * Tests that an assignment can be added to an existing course.
     */
    @Test
    void testAddAssignment()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        tracker.addCourse("CS 2114");

        Assignment assignment = new Assignment(
            "Project 1",
            "CS 2114",
            LocalDate.of(2026, 10, 1));

        tracker.addAssignment(assignment);

        assertEquals(
            1,
            tracker.getAssignmentsSorted().size());

        assertEquals(
            "Project 1",
            tracker.getAssignmentsSorted().get(0).getName());
    }

    /**
     * Tests that a null assignment is rejected.
     */
    @Test
    void testNullAssignment()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        tracker.addCourse("CS 2114");

        assertThrows(
            IllegalArgumentException.class,
            () -> tracker.addAssignment(null));
    }

    /**
     * Tests that an assignment cannot be added to a course
     * that does not exist.
     */
    @Test
    void testAssignmentWithUnknownCourse()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        Assignment assignment = new Assignment(
            "Project 1",
            "CS 2114",
            LocalDate.of(2026, 10, 1));

        assertThrows(
            IllegalArgumentException.class,
            () -> tracker.addAssignment(assignment));
    }

    /**
     * Tests that assignments are returned in due-date order.
     */
    @Test
    void testAssignmentsSorted()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        tracker.addCourse("CS 2114");

        Assignment later = new Assignment(
            "Project 2",
            "CS 2114",
            LocalDate.of(2026, 11, 1));

        Assignment earlier = new Assignment(
            "Project 1",
            "CS 2114",
            LocalDate.of(2026, 10, 1));

        tracker.addAssignment(later);
        tracker.addAssignment(earlier);

        List<Assignment> sorted =
            tracker.getAssignmentsSorted();

        assertEquals("Project 1", sorted.get(0).getName());
        assertEquals("Project 2", sorted.get(1).getName());
    }

    /**
     * Tests that assignments for a selected course are returned
     * in due-date order.
     */
    @Test
    void testAssignmentsForCourse()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        tracker.addCourse("CS 2114");
        tracker.addCourse("MATH 2114");

        tracker.addAssignment(new Assignment(
            "Math Homework",
            "MATH 2114",
            LocalDate.of(2026, 10, 5)));

        tracker.addAssignment(new Assignment(
            "CS Project",
            "CS 2114",
            LocalDate.of(2026, 10, 1)));

        List<Assignment> result =
            tracker.getAssignmentsForCourse("CS 2114");

        assertEquals(1, result.size());
        assertEquals(
            "CS Project",
            result.get(0).getName());
    }

    /**
     * Tests that course selection is case-insensitive.
     */
    @Test
    void testAssignmentsForCourseCaseInsensitive()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        tracker.addCourse("CS 2114");

        tracker.addAssignment(new Assignment(
            "Project 1",
            "CS 2114",
            LocalDate.of(2026, 10, 1)));

        List<Assignment> result =
            tracker.getAssignmentsForCourse("cs 2114");

        assertEquals(1, result.size());
    }

    /**
     * Tests that an unknown course is rejected when retrieving
     * assignments.
     */
    @Test
    void testAssignmentsForUnknownCourse()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        tracker.addCourse("CS 2114");

        assertThrows(
            IllegalArgumentException.class,
            () -> tracker.getAssignmentsForCourse("MATH 2114"));
    }

    /**
     * Tests that a null course is rejected when retrieving
     * assignments.
     */
    @Test
    void testAssignmentsForNullCourse()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        assertThrows(
            IllegalArgumentException.class,
            () -> tracker.getAssignmentsForCourse(null));
    }

    /**
     * Tests that a blank course is rejected when retrieving
     * assignments.
     */
    @Test
    void testAssignmentsForBlankCourse()
    {
        AssignmentTracker tracker = new AssignmentTracker();

        assertThrows(
            IllegalArgumentException.class,
            () -> tracker.getAssignmentsForCourse("   "));
    }
}