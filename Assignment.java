import java.time.LocalDate;

/**
 * Represents one assignment in the ClassMate program.
 *
 * @author Manveen Sidhu
 * @version 2026.09.20
 */
public class Assignment
{
    private String name;
    private String courseName;
    private LocalDate dueDate;

    /**
     * Creates a new assignment.
     *
     * @param assignmentName
     *            the name of the assignment
     * @param assignmentCourse
     *            the course for the assignment
     * @param assignmentDueDate
     *            the assignment's due date
     * @throws IllegalArgumentException
     *             if a name or date is invalid
     */
    public Assignment(
        String assignmentName,
        String assignmentCourse,
        LocalDate assignmentDueDate)
    {
        checkName(assignmentName, "Assignment name");
        checkName(assignmentCourse, "Course name");

        if (assignmentDueDate == null)
        {
            throw new IllegalArgumentException("Due date cannot be null");
        }

        name = assignmentName.trim();
        courseName = assignmentCourse.trim();
        dueDate = assignmentDueDate;
    }


    /**
     * Checks whether a name is valid.
     *
     * @param value
     *            the name to check
     * @param fieldName
     *            the name of the field being checked
     * @throws IllegalArgumentException
     *             if the name is invalid
     */
    private void checkName(String value, String fieldName)
    {
        if (value == null)
        {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }

        if (value.contains("\t") || value.contains("\n")
            || value.contains("\r"))
        {
            throw new IllegalArgumentException(
                fieldName + " cannot contain tabs or line breaks");
        }

        if (value.trim().isEmpty())
        {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
    }


    /**
     * Gets the assignment name.
     *
     * @return the assignment name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Gets the course name.
     *
     * @return the course name
     */
    public String getCourseName()
    {
        return courseName;
    }


    /**
     * Gets the due date.
     *
     * @return the due date
     */
    public LocalDate getDueDate()
    {
        return dueDate;
    }
}
