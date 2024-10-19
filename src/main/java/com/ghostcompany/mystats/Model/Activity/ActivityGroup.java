package com.ghostcompany.mystats.Model.Activity;

import com.ghostcompany.mystats.Service.ActivityGroupDAO;
import com.ghostcompany.mystats.Service.ActivityDAO;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ActivityGroup {
    private int id;
    private String name;
    private final ActivityGroupDAO activityGroupDAO = new ActivityGroupDAO();
    private final ActivityDAO activityDAO = new ActivityDAO();

    public ActivityGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ActivityGroup() {}

    public int getId() { return id; }
    public String getName() { return name; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    public ObservableList<ActivityGroup> getActivityGroups() throws SQLException {
        return activityGroupDAO.getAllActivityGroups();
    }

    /**
     * Get the total time spent for all activities in this group.
     *
     * @return A human-readable string of total time spent like "4h 10m 5s"
     * @throws SQLException If database access fails.
     */
    public String getTotalTimeSpent() throws SQLException {
        // Fetch all activities that belong to this group
        ObservableList<Activity> activities = activityDAO.getActivitiesForGroup(this.name);
        Duration totalDuration = Duration.ZERO;

        // Loop through each activity and sum up the total duration
        for (Activity activity : activities) {
            ObservableList<ActivityEntry> entries = activity.getEntries();

            for (ActivityEntry entry : entries) {
                LocalDateTime startTime = entry.getStartTime();
                LocalDateTime endTime = entry.getEndTime();
                Duration entryDuration = Duration.between(startTime, endTime);
                totalDuration = totalDuration.plus(entryDuration);
            }
        }

        // Return the total duration in human-readable format
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
