package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Activity.Activity;
import com.ghostcompany.mystats.Model.Activity.ActivityGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAO {
    public int addActivity(Activity activity) throws SQLException {
        String sql = "INSERT INTO activities (name, group_id) VALUES (?, ?)";
        List<ActivityGroup> activityGroups = new ActivityGroupDAO().getAllActivityGroups();
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, activity.getName());
            ps.setInt(2, activity.getGroupId());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to insert new activity on group " + activity.getName());
                }
            }
        }
    }

    public List<Activity> getActivities() throws SQLException {
        String sql = "SELECT * FROM activities";
        List<Activity> activities = new ArrayList<>();
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                Activity activity = new Activity(rs.getInt("id"), rs.getString("name"), rs.getInt("group_id"));
                activities.add(activity);
            }
        }
        return activities;
    }
}
