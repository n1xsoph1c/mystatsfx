package com.ghostcompany.mystats.Model.Activity;

import com.ghostcompany.mystats.Service.ActivityEntryDAO;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

public class ActivityEntry {
    private int id;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int activityId;
    private final ActivityEntryDAO entryDAO = new ActivityEntryDAO();

    public ActivityEntry(int id, String description,
                         LocalDateTime startTime, LocalDateTime endTime, int activityId) {
        this.id = id;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityId = activityId;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getActivityId() { return activityId; }

    public void setId(int id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setActivityId(int activityId) { this.activityId = activityId; }

    public void addEntry(ActivityEntry entry) throws SQLException {
        entryDAO.addActivityEntry(entry);
    }

    public String getDuration() {
        // Calculate the duration between startTime and endTime
        Duration duration = Duration.between(startTime, endTime);

        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();  // Use toMinutesPart() in Java 9+
        long seconds = duration.toSecondsPart();  // Use toSecondsPart() in Java 9+

        StringBuilder result = new StringBuilder();

        if (hours > 0) {
            result.append(hours).append("h ");
        }
        if (minutes > 0) {
            result.append(minutes).append("m ");
        }
        if (seconds > 0 || result.length() == 0) {  // Always show seconds if there’s no hours or minutes
            result.append(seconds).append("s");
        }

        return result.toString().trim();  // Remove any extra spaces at the end
    }
}
