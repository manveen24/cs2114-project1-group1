import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author zach1
 *  @version Sep 14, 2026
 */
public class TrackerApp
{
    /**
     * 
     */
    static Scanner input;
    /**
     * 
     */
    static AssignmentTracker tracker;
    /**
     * 
     */
    static TrackerStorage storage;
    
    // ----------------------------------------------------------
    /**
     * Place a description of your method here.
     * @param args
     */
    //~ Fields ................................................................

    //~ Constructors ..........................................................
    public static void main(String[] args)
    {
        
        System.out.println("Welcome to ClassMate!");
        input = new Scanner(System.in);
        storage = new TrackerStorage(Paths.get("assignments.txt"));
        
        try 
        {
            tracker = storage.load();
        }
        catch (IOException e)
        {
            System.out.println("Could not load saved data.");
            return;
        }
        
        boolean running = true;
        
        while (running)
        {
            System.out.println("1. Add Course");
            System.out.println("2. Add Assignment");
            System.out.println("3. View All Assignments");
            System.out.println("4. View Assignments by Course");
            System.out.println("5. Save");
            System.out.println("6. Save and Exit");
            
            int choice = getNumber(1,6);
            
            switch (choice)
            {
                case 1:
                    addCourse();
                    break;
                
                case 2:
                    addAssignment();
                    break;

                case 3:
                    viewAllAssignments();
                    break;

                case 4:
                    viewAssignmentsByCourse();
                    break;

                case 5:
                    try
                    {
                        storage.save(tracker);
                        System.out.println("Saved.");
                    }
                    catch (IOException e)
                    {
                        System.out.println("Could not save data.");
                    }
                    break;

                case 6:
                    try
                    {
                        storage.save(tracker);
                        System.out.println("Saved. Goodbye!");
                        running = false;
                    }
                    catch (IOException e)
                    {
                        System.out.println("Could not save data.");
                    }
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    //~Public  Methods ........................................................
    
    private static void addCourse()
    {
       boolean valid = false;
        
       while(!valid)
        {
            System.out.print("Enter course name:");
            String courseName = input.nextLine();
            
            try
            {
                tracker.addCourse(courseName);
                System.out.println("Course added.");
                valid = true;
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("Invalid course name.");
            }
        }
    }
    
    private static void addAssignment()
    {
        if(tracker.getCourses().isEmpty())
        {
            System.out.println("Please add a course first");
            return;
        }
        
        List<String> courses = tracker.getCourses();
        
        System.out.println("Select a course:");
        int num = 1;
        
        for (int i = 0; i < courses.size() ; i++)
        {
            System.out.println(num + ". " + courses.get(i));
            num++;
        }
        
        boolean valid = false;
        String courseName = "";

        while (!valid)
        {
            System.out.print("Enter course number: ");
            int courseChoice = getNumber(1, courses.size());
            
            courseName = courses.get(courseChoice - 1);
            System.out.println("Course Selected.");
            valid = true;
        }
        
        System.out.print("Enter Assignment Name: ");
        String assignmentName = input.nextLine();
        
        LocalDate assignmentDueDate = null;
        
        valid = false;
        
        while(!valid)
        {
            
            System.out.print("Enter Assignment Due Date (YYYY-MM-DD): ");
            
            String assignmentDDChoice = input.nextLine();
            
            try
            {
                assignmentDueDate = LocalDate.parse(assignmentDDChoice);
                valid = true;
            }
            catch(DateTimeParseException e)
            {
                System.out.println("Please enter date in correct format");
            }
        }

        
        Assignment assignment = new Assignment(
            assignmentName,
            courseName,
            assignmentDueDate);

        tracker.addAssignment(assignment);

        
        System.out.println("Assignment added.");
    }
    
    private static void viewAllAssignments()
    {
        List<Assignment> assignments = tracker.getAssignmentsSorted();
        
        if (assignments.isEmpty())
        {
            System.out.println("No assignments found.");
            return;
            
        }
        
        for (Assignment assignment : assignments)
        {
            System.out.println(
                assignment.getName() + " | "
                + assignment.getCourseName() + " | "
                + assignment.getDueDate());
        }
    }
    
    private static void viewAssignmentsByCourse()
    {
        List<String> courses = tracker.getCourses();
        
        if (courses.isEmpty())
        {
            System.out.println("Please add a course first");
            return;
            
        }
        
        System.out.println("Select a course:");

        for (int i = 0; i < courses.size(); i++)
        {
            System.out.println((i + 1) + ". " + courses.get(i));
        }

        System.out.print("Enter course number: ");
        int courseChoice = getNumber(1, courses.size());

        String courseName = courses.get(courseChoice - 1);

        List<Assignment> assignments =
            tracker.getAssignmentsForCourse(courseName);

        if (assignments.isEmpty())
        {
            System.out.println("No assignments found.");
            return;
        }

        for (Assignment assignment : assignments)
        {
            System.out.println(
                assignment.getName() + " | "
                + assignment.getCourseName() + " | "
                + assignment.getDueDate());
        }
    }
    
    private static int getNumber(int min, int max)
    {
        while (true)
        {
            try
            {
                int number = Integer.parseInt(input.nextLine());
                
                if (number >= min && number <= max)
                {
                    return number;
                }
                
                System.out.println("Invalid choice. Please try again.");
        }
            catch (NumberFormatException e)
            {
                System.out.println("Please enter a number.");
                
            }
        }
    }
}
