import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests saving and loading assignment tracker data.
 *
 * @author Zachary Vest
 * @version 2026.09.21
 */
class TrackerStorageTest
{
    /**
     * Tests that a tracker can be saved and loaded successfully.
     *
     * @param tempDir temporary directory provided by JUnit
     */
    @Test
    void testSaveAndLoad(@TempDir Path tempDir)
        throws Exception
    {
        Path file = tempDir.resolve("assignments.txt");

        AssignmentTracker tracker = new AssignmentTracker();
        tracker.addCourse("CS 2114");

        tracker.addAssignment(new Assignment(
            "Project 1",
            "CS 2114",
            LocalDate.of(2026, 10, 1)));

        TrackerStorage storage =
            new TrackerStorage(file);

        storage.save(tracker);

        AssignmentTracker loaded = storage.load();

        assertTrue(
            loaded.getCourses().contains("CS 2114"));

        assertEquals(
            1,
            loaded.getAssignmentsSorted().size());

        assertEquals(
            "Project 1",
            loaded.getAssignmentsSorted()
                .get(0).getName());
    }

    /**
     * Tests that loading a file that does not exist returns
     * an empty tracker.
     *
     * @param tempDir temporary directory provided by JUnit
     */
    @Test
    void testLoadMissingFile(@TempDir Path tempDir)
        throws Exception
    {
        Path file = tempDir.resolve("missing.txt");

        TrackerStorage storage =
            new TrackerStorage(file);

        AssignmentTracker tracker = storage.load();

        assertTrue(tracker.getCourses().isEmpty());
        assertTrue(tracker.getAssignmentsSorted().isEmpty());
    }

    /**
     * Tests that a null path is rejected by the constructor.
     */
    @Test
    void testNullPath()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new TrackerStorage(null));
    }

    /**
     * Tests that a null tracker cannot be saved.
     *
     * @param tempDir temporary directory provided by JUnit
     */
    @Test
    void testSaveNullTracker(@TempDir Path tempDir)
    {
        Path file = tempDir.resolve("assignments.txt");

        TrackerStorage storage =
            new TrackerStorage(file);

        assertThrows(
            IllegalArgumentException.class,
            () -> storage.save(null));
    }

    /**
     * Tests that multiple courses, including a course with no
     * assignments, are saved and loaded correctly.
     *
     * @param tempDir temporary directory provided by JUnit
     */
    @Test
    void testSaveMultipleCourses(@TempDir Path tempDir)
        throws Exception
    {
        Path file = tempDir.resolve("assignments.txt");

        AssignmentTracker tracker = new AssignmentTracker();

        tracker.addCourse("CS 2114");
        tracker.addCourse("MATH 2114");

        tracker.addAssignment(new Assignment(
            "Project 1",
            "CS 2114",
            LocalDate.of(2026, 10, 1)));

        TrackerStorage storage =
            new TrackerStorage(file);

        storage.save(tracker);

        AssignmentTracker loaded = storage.load();

        assertEquals(2, loaded.getCourses().size());
        assertTrue(
            loaded.getCourses().contains("CS 2114"));
        assertTrue(
            loaded.getCourses().contains("MATH 2114"));

        assertEquals(
            1,
            loaded.getAssignmentsSorted().size());
    }

    /**
     * Tests that malformed save data causes an IOException.
     *
     * @param tempDir temporary directory provided by JUnit
     */
    @Test
    void testInvalidSaveData(@TempDir Path tempDir)
        throws Exception
    {
        Path file = tempDir.resolve("assignments.txt");

        Files.writeString(
            file,
            "INVALID\tBad Data\n");

        TrackerStorage storage =
            new TrackerStorage(file);

        assertThrows(
            java.io.IOException.class,
            () -> storage.load());
    }
}
