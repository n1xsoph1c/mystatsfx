package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Activity.Activity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ActivityDAO {

    private static final String INSERT_SQL = "INSERT INTO activities (name, group_name) VALUES (?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM activities";

    public int addActivity(Activity activity) throws SQLException, InvalidActivityException {
        validateActivity(activity);

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, activity.getName());
            ps.setString(2, activity.getGroupName());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to insert the activity: " + activity.getName());
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("No ID obtained for activity: " + activity.getName());
                }
            }
        }
    }

    private void validateActivity(Activity activity) throws InvalidActivityException {
        if (activity.getName() == null || activity.getName().isEmpty()) {
            throw new InvalidActivityException("Activity name cannot be null or empty.");
        }
        if (activity.getGroupName() == null || activity.getGroupName().isEmpty()) {
            throw new InvalidActivityException("Group name cannot be null or empty.");
        }
    }

    public ObservableList<Activity> getActivities() throws SQLException {
        ObservableList<Activity> activities = FXCollections.observableArrayList();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Activity activity = new Activity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("group_name")
                );
                activities.add(activity);
            }
        }
        return activities;
    }

    public ObservableList<Activity> getActivitiesForGroup(String groupName) throws SQLException {
        ObservableList<Activity> activities = FXCollections.observableArrayList();
        String query = "SELECT * FROM activities WHERE group_name = ?";  // Assuming group_id is stored in the table

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, groupName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Activity activity = new Activity(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("group_name")
                    );
                    activities.add(activity);
                }
            }
        }

        return activities;
    }


}
