import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

// -------------------------------------------------------------------------
/**
 *  Manage courses and assignments and provide sorted assignment
*   lists.
 * 
 *  @author will
 *  @version Sep 21, 2026
 */
public class AssignmentTracker
{
    private ArrayList<String> courses;
    private ArrayList<Assignment> assignments;

    /**
    *   Constructor for the class. 
    */
    public AssignmentTracker() {
        courses = new ArrayList<String>();
        assignments = new ArrayList<Assignment>(); 
        
    }

    /**
    *   Getter for the courses in the list. 
    *   @return a string of courses. 
    */
    public List<String> getCourses(){
        return new ArrayList<String>(courses);
    }
    
    /**
    *   Method to ensure that when a course is being added, 
    *   it has proper input and the course name gets trimmed. 
     * @param courseName the course being added
    */
    public void addCourse(String courseName) {
        if (courseName == null) {
            throw new IllegalArgumentException(
                "Course name cannot be null");
        }
        if (courseName.contains("\t") || courseName.contains("\n") ||
            courseName.contains("\r")) {
            throw new IllegalArgumentException(
                "Course name cannot contain tabs or line breaks");
        }
        courseName = courseName.trim();
        
        if (courseName.isEmpty()) {
            throw new IllegalArgumentException(
                "Course name cannot be empty");
        }
        
        for (String existingCourse : courses) {
            if (existingCourse.equalsIgnoreCase(courseName)) {
                throw new IllegalArgumentException(
                    "Course already exists");
            }
        }
        courses.add(courseName);
    }
    
    /**
    *   Method to add an assignment. Ensures that the course exists 
    *   and the method also is not case sensitive. 
     * @param assignment the assignment being added
    */
    public void addAssignment(Assignment assignment) {
        if (assignment == null) {
            throw new IllegalArgumentException(
                "Assignment cannot be null");
        }
        
        for (String existingCourse : courses) {
            if (existingCourse.equalsIgnoreCase(assignment.getCourseName())) {
                assignments.add(assignment);
                return; 
            }
        }
        
        throw new IllegalArgumentException("Course does not exist"); 
           
    }

    /**
    *   Sorts the assignments using a method reference. Uses the method
    *   on each created assignment. 
    *   @return the sorted list of assignments. 
    */
    public List<Assignment> getAssignmentsSorted() {
        List<Assignment> sorted = new ArrayList<Assignment>(assignments);
        sorted.sort(Comparator.comparing(Assignment::getDueDate));
        return sorted; 
    }

    /**
    *   Method that gets all the assignments for a specific course. 
    *   Uses a method reference to sort them by due date. 
    *   @return filtered list of assignments in the given course. 
    */
    public List<Assignment> getAssignmentsForCourse(String courseName) {
        if (courseName == null) {
            throw new IllegalArgumentException(
                "Course name cannot be null");
        }
        
        if (courseName.contains("\t") || courseName.contains("\n") ||
            courseName.contains("\r")) {
            throw new IllegalArgumentException(
                "Course name cannot contain tabs or line breaks");
        }
        
        courseName = courseName.trim();
        
        if (courseName.isEmpty()) {
            throw new IllegalArgumentException(
                "Course name cannot be empty"); 
        }
        
        boolean courseFound = false;
        for (String existingCourse : courses) {
            if (existingCourse.equalsIgnoreCase(courseName)) {
                courseFound = true;     
            }
        }
        
        if (!courseFound) {
            throw new IllegalArgumentException(
                "Course not found"); 
        }
        
        List<Assignment> filtered = new ArrayList<Assignment>();
        for (int i = 0; i < assignments.size(); i++) {
            Assignment assignment = assignments.get(i);
            if (assignment.getCourseName().equalsIgnoreCase(courseName)) {
                filtered.add(assignment);     
            }
        }
        filtered.sort(Comparator.comparing(Assignment::getDueDate));
        return filtered; 
    }
    
}
