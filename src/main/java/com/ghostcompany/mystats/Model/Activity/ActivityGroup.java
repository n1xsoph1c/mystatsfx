package com.ghostcompany.mystats.Model.Activity;

import java.util.HashMap;
import java.util.Map;

public class ActivityGroup {
    private int id;
    private String name;
    private Map<String, Activity> activities = new HashMap<>();

    public ActivityGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    public void addActivity(Activity activity) {
        activities.put(activity.getName(), activity);
    }

    public Map<String, Activity> getActivities() {
        return activities;
    }
}
