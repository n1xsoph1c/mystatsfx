package com.ghostcompany.mystats.Model.Activity;

import com.ghostcompany.mystats.Service.ActivityDAO;
import com.ghostcompany.mystats.Service.ActivityEntryDAO;
import com.ghostcompany.mystats.Service.InvalidActivityException;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Activity {
    private int id;
    private String name;
    private String groupName; // Changed from int groupId to String groupName
    private final ActivityEntryDAO activityEntryDAO = new ActivityEntryDAO();
    private final ActivityDAO activityDAO = new ActivityDAO();

    public Activity(int id, String name, String groupName) { // Updated constructor
        this.id = id;
        this.name = name;
        this.groupName = groupName;
    }

    public Activity() {}

    public int getId() { return id; }
    public String getName() { return name; }
    public String getGroupName() { return groupName; } // Updated getter

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGroupName(String groupName) { this.groupName = groupName; } // Updated setter

    public ObservableList<ActivityEntry> getEntries() throws SQLException {
        return activityEntryDAO.getAllEntries(this.id);
    }

    public ObservableList<Activity> getAllActivities() throws SQLException {
        return activityDAO.getActivities();
    }

    public void addActivity(Activity entry) throws SQLException, InvalidActivityException {
        activityDAO.addActivity(entry);
    }

    public void deleteActivity(Activity entry) throws SQLException {
        // Implement delete activity logic if needed
    }

    public void addActivityEntry(ActivityEntry entry) throws SQLException {
        activityEntryDAO.addActivityEntry(entry);
    }

    /**
     * Calculate the total time spent on this activity by summing up the durations of all entries.
     *
     * @return A human-readable string of total time spent like "4h 10m 5s"
     * @throws SQLException If database access fails.
     */
    public String getTotalTimeSpent() throws SQLException {
        ObservableList<ActivityEntry> entries = getEntries();
        Duration totalDuration = Duration.ZERO;

        // Sum the duration of each entry
        for (ActivityEntry entry : entries) {
            LocalDateTime startTime = entry.getStartTime();
            LocalDateTime endTime = entry.getEndTime();
            Duration entryDuration = Duration.between(startTime, endTime);
            totalDuration = totalDuration.plus(entryDuration);
        }

        // Format the total duration as a human-readable string
        return formatDuration(totalDuration);
    }

    /**
     * Helper method to format a Duration object into a human-readable string.
     *
     * @param duration The duration to format.
     * @return A string representing the duration in the format "4h 10m 5s".
     */
    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        StringBuilder result = new StringBuilder();
        if (hours > 0) {
            result.append(hours).append("h ");
        }
        if (minutes > 0) {
            result.append(minutes).append("m ");
        }
        if (seconds > 0 || result.length() == 0) {  // Always show seconds if there are no hours or minutes
            result.append(seconds).append("s");
        }

        return result.toString().trim();  // Remove any trailing spaces
    }
}
