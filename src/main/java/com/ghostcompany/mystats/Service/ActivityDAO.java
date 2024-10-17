package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Activity.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAO {

    // SQL Queries
    private static final String INSERT_SQL = "INSERT INTO activities (name, group_name) VALUES (?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM activities";

    // Add a new activity and return the generated ID.
    public int addActivity(Activity activity) throws SQLException {
        validateActivity(activity);

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters
            ps.setString(1, activity.getName());
            ps.setString(2, activity.getGroupName()); // Updated to match the new schema

            // Execute the query
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to insert the activity: " + activity.getName());
            }

            // Retrieve generated ID
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the generated ID
                } else {
                    throw new SQLException("No ID obtained for activity: " + activity.getName());
                }
            }
        }
    }

    // Retrieve all activities from the database.
    public List<Activity> getActivities() throws SQLException {
        List<Activity> activities = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Activity activity = new Activity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("group_name") // Updated to match the new schema
                );
                activities.add(activity);
            }
        }
        return activities;
    }

    // Validate the activity input.
    private void validateActivity(Activity activity) {
        if (activity.getName() == null || activity.getName().isEmpty()) {
            throw new IllegalArgumentException("Activity name cannot be null or empty.");
        }
        if (activity.getGroupName() == null || activity.getGroupName().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty.");
        }
    }
}
