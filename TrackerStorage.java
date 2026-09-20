import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves and loads courses and assignments for the program.
 * Course records stored first in a txt followed by the assignment
 * @author colin
 * @version 2026.09.20
 * 
 */
public class TrackerStorage
{
    private static final String Course1 = "COURSE";
    private static final String Assignment1 = "ASSIGNMENT";
    private Path filePath;


    /**
     * Creates storage object for save file
     * 
     * @param path location of the save file
     * @throws IllegalArgumentException if the path is null
     * 
     */

    public TrackerStorage(Path path)
    {
        if(path == null)
        {
            throw new IllegalArgumentException("Path cannot be null");
        }
        filePath = path;
    }


    /**
     * Saves all courses and assignments to save file
     * 
     * @param tracker the tracker to save
     * @throws IOException if the file cannot be written
     * @throws IllegalArgumentException if the tracker is null.
     */
    public void save(AssignmentTracker tracker) throws IOException{
        if(tracker==null)
        {
            throw new IllegalArgumentException("Tracker cannot be null");
        }

        List<String> lines = new ArrayList<String>();
        for(String course : tracker.getCourses())
        {
            lines.add(Course1 + "\t" + course);
        }
        for(Assignment assignment : tracker.getAssignmentsSorted())
        {
            lines.add(Assignment1 + "\t" + assignment.getCourseName() + "\t" + assignment.getName()+"\t"+assignment.getDueDate());
        }

        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }

    /**
     * Loads a tracker from the save file. 
     * If it doesn't exist, empty tracker is returned. 
     * If any record is invalid, 
     * the load fails and nothing partial is returned.
     * 
     * @return the loaded tracker
     * @throws IOException if the file cannot be read or contains improper data
     */

    public AssignmentTracker load() throws IOException
    {
        AssignmentTracker tracker = new AssignmentTracker();
        if(!Files.exists(filePath))
        {
            return tracker;
        }

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        boolean seenAssignment = false;
        for(int i = 0; i < lines.size(); i++)
        {
            int lineNumber = i + 1;
            String[] fields = lines.get(i).split("\t", -1);
            String type = fields[0];

            if(type.equals(Course1))
            {
                if(seenAssignment)
                {
                    throw invalid(lineNumber, "course record after an assignment record");
                }
                if (fields.length != 2)
                {
                    throw invalid(lineNumber, "course record needs 2 fields");
                }
                try
                {
                    tracker.addCourse(fields[1]);
                }
                catch(IllegalArgumentException e)
                {
                    throw invalid(lineNumber, e.getMessage());
                }
            }
            else if(type.equals(Assignment1))
            {
                seenAssignment = true;
                if(fields.length!=4)
                {
                    throw invalid(lineNumber, "assignment record needs 4 fields");
                }
                try
                {
                    LocalDate dueDate = LocalDate.parse(fields[3]);
                    Assignment assignment = new Assignment(fields[2], fields[1], dueDate);
                    tracker.addAssignment(assignment);
                }
                catch (DateTimeParseException e)
                {
                    throw invalid(lineNumber, "bad date " + fields[3]);
                }
                catch (IllegalArgumentException e)
                {
                    throw invalid(lineNumber, e.getMessage());
                }
            }
            else
            {
                throw invalid(lineNumber, "unknown record type");
            }
        }
        return tracker;
    }

    /**
     * Builds an IOException that tells you that there's a bad line in the save file.
     * 
     * @param lineNumber the line where the problem was found
     * @param reason what is wrong with the line
     * @return the exception to throw
     */

    private IOException invalid(int lineNumber, String reason)
    {
        return new IOException("Invalid save data at line " + lineNumber + ": " + reason);
    }


}