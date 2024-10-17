package com.ghostcompany.mystats.Model.Activity;

import com.ghostcompany.mystats.Service.ActivityEntryDAO;

import java.sql.SQLException;
import java.util.List;

public class Activity {
    private int id;
    private String name;
    private String groupName; // Changed from int groupId to String groupName
    private final ActivityEntryDAO activityEntryDAO = new ActivityEntryDAO();

    public Activity(int id, String name, String groupName) { // Updated constructor
        this.id = id;
        this.name = name;
        this.groupName = groupName;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getGroupName() { return groupName; } // Updated getter

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGroupName(String groupName) { this.groupName = groupName; } // Updated setter

    public List<ActivityEntry> getEntries() throws SQLException {
        return activityEntryDAO.getAllEntries(this.id);
    }
}
