package com.ghostcompany.mystats.Model.Activity;

public class Activity {
    private int id;
    private String name;
    private int groupId;

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
}