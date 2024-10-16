package com.ghostcompany.mystats.Model.Activity;

import java.time.LocalDateTime;
import java.util.Date;

public class ActivityEntry {
    private int id;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int activityId;

    public ActivityEntry(int id, String description, LocalDateTime startTime, LocalDateTime endTime, int activityId) {
        this.id = id;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.activityId = activityId;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setId(int id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setActivityId(int activityId) { this.activityId = activityId; }

}