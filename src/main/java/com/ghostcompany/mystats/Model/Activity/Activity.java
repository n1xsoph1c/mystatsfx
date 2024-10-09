package com.ghostcompany.mystats.Model.Activity;

import java.util.Date;

public class Activity {
    private int id;
    private String name;
    private String description;
    private Date timeSpent;

    public Activity(int id, String name, String description, Date timeSpent) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.timeSpent = timeSpent;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getTimeSpent() { return timeSpent; }
    public void setTimeSpent(Date timeSpent) { this.timeSpent = timeSpent; }


}
