package com.ghostcompany.mystats.Model.Activity;

import com.ghostcompany.mystats.Service.ActivityEntryDAO;

import java.sql.SQLException;
import java.util.List;

public class Activity {
    private int id;
    private String name;
    private int groupId;
    private ActivityEntryDAO activityEntryDAO = new ActivityEntryDAO();

    public Activity(int id, String name, int groupId) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
    }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGroupId(int groupId) { this.groupId = groupId; }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGroupId() {
        return groupId;
    }

    public List<ActivityEntry> getEntries() throws SQLException {
        return activityEntryDAO.getAllEntries(this.getId());
    }

}