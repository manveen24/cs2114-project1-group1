import java.time.LocalDate;
import student.TestCase;

/**
 * Tests the Assignment class.
 *
 * @author Manveen Sidhu
 * @version 2026.09.20
 */
public class AssignmentTest
    extends TestCase
{
    private Assignment assignment;
    private LocalDate dueDate;

    /**
     * Creates the objects used by each test.
     */
    public void setUp()
    {
        dueDate = LocalDate.of(2026, 9, 30);
        assignment = new Assignment("Project 1", "CS 2114", dueDate);
    }


    /**
     * Tests all three getter methods.
     */
    public void testGetters()
    {
        assertEquals("Project 1", assignment.getName());
        assertEquals("CS 2114", assignment.getCourseName());
        assertEquals(dueDate, assignment.getDueDate());
    }


    /**
     * Tests that spaces at the ends of names are removed.
     */
    public void testNamesAreTrimmed()
    {
        Assignment trimmed =
            new Assignment("  Exam Review  ", "  MATH 2114  ", dueDate);

        assertEquals("Exam Review", trimmed.getName());
        assertEquals("MATH 2114", trimmed.getCourseName());
    }


    /**
     * Tests a null assignment name.
     */
    public void testNullAssignmentName()
    {
        Exception thrown = null;
        try
        {
            new Assignment(null, "CS 2114", dueDate);
        }
        catch (IllegalArgumentException exception)
        {
            thrown = exception;
        }
        assertNotNull(thrown);
    }


    /**
     * Tests invalid assignment names.
     */
    public void testInvalidAssignmentNames()
    {
        assertInvalidAssignmentName("");
        assertInvalidAssignmentName("   ");
        assertInvalidAssignmentName("Project\t1");
        assertInvalidAssignmentName("Project\n1");
        assertInvalidAssignmentName("Project\r1");
    }


    /**
     * Tests a null course name.
     */
    public void testNullCourseName()
    {
        Exception thrown = null;
        try
        {
            new Assignment("Project 1", null, dueDate);
        }
        catch (IllegalArgumentException exception)
        {
            thrown = exception;
        }
        assertNotNull(thrown);
    }


    /**
     * Tests invalid course names.
     */
    public void testInvalidCourseNames()
    {
        assertInvalidCourseName("");
        assertInvalidCourseName("   ");
        assertInvalidCourseName("CS\t2114");
        assertInvalidCourseName("CS\n2114");
        assertInvalidCourseName("CS\r2114");
    }


    /**
     * Tests a null due date.
     */
    public void testNullDueDate()
    {
        Exception thrown = null;
        try
        {
            new Assignment("Project 1", "CS 2114", null);
        }
        catch (IllegalArgumentException exception)
        {
            thrown = exception;
        }
        assertNotNull(thrown);
    }


    /**
     * Confirms that a past due date is allowed.
     */
    public void testPastDueDateAllowed()
    {
        LocalDate oldDate = LocalDate.of(2025, 1, 1);
        Assignment oldAssignment =
            new Assignment("Old Homework", "CS 2114", oldDate);

        assertEquals(oldDate, oldAssignment.getDueDate());
    }


    /**
     * Checks one invalid assignment name.
     *
     * @param invalidName
     *            the invalid name to test
     */
    private void assertInvalidAssignmentName(String invalidName)
    {
        Exception thrown = null;
        try
        {
            new Assignment(invalidName, "CS 2114", dueDate);
        }
        catch (IllegalArgumentException exception)
        {
            thrown = exception;
        }
        assertNotNull(thrown);
    }


    /**
     * Checks one invalid course name.
     *
     * @param invalidName
     *            the invalid name to test
     */
    private void assertInvalidCourseName(String invalidName)
    {
        Exception thrown = null;
        try
        {
            new Assignment("Project 1", invalidName, dueDate);
        }
        catch (IllegalArgumentException exception)
        {
            thrown = exception;
        }
        assertNotNull(thrown);
    }
}
