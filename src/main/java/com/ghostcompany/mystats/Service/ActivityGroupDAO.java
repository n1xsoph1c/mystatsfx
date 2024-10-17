package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Activity.ActivityGroup;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityGroupDAO {

    // Adds a new activity group and returns its generated ID
    public int addActivityGroup(ActivityGroup activityGroup) throws SQLException {
        String sql = "INSERT INTO activity_groups (name) VALUES (?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, activityGroup.getName());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    activityGroup.setId(generatedId);
                    return generatedId;
                } else {
                    throw new SQLException("Failed to insert new activity group, no ID obtained.");
                }
            }
        }
    }

    // Fetches all activity groups from the database
    public List<ActivityGroup> getAllActivityGroups() throws SQLException {
        String sql = "SELECT id, name FROM activity_groups";
        List<ActivityGroup> activityGroups = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                ActivityGroup group = new ActivityGroup(id, name);
                activityGroups.add(group);
            }
        }
        return activityGroups;
    }

    // Updates an existing activity group
    public boolean updateActivityGroup(ActivityGroup activityGroup) throws SQLException {
        String sql = "UPDATE activity_groups SET name = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, activityGroup.getName());
            ps.setInt(2, activityGroup.getId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Deletes an activity group by ID
    public boolean deleteActivityGroup(int id) throws SQLException {
        String sql = "DELETE FROM activity_groups WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
}
