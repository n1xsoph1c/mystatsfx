package com.ghostcompany.mystats.Model.Activity;


import java.util.HashMap;
import java.util.Map;

public class ActivityGroup {
    private int id;
    private String name;
    private Map<String, Activity> activities;

    public ActivityGroup (int id, String name) {
        this.id = id;
        this.name = name;
        this.activities = new HashMap<String, Activity>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void addActivity(Activity activity) { this.activities.put(activity.getName(), activity); }
    public Map<String, Activity> getActivities() { return activities; }

}