package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Activity.ActivityEntry;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActivityEntryDAO {

    // SQL Queries
    private static final String INSERT_SQL =
            "INSERT INTO activity_entries (description, start_time, end_time, activity_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM activity_entries";

    // Add an activity entry and return its generated ID.
    public int addActivityEntry(ActivityEntry entry) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters
            ps.setString(1, entry.getDescription());
            ps.setTimestamp(2, toTimestamp(entry.getStartTime()));
            ps.setTimestamp(3, toTimestamp(entry.getEndTime()));
            ps.setInt(4, entry.getActivityId());

            // Execute insert and get the generated key
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to insert " + entry.getDescription() + " into ActivityEntry");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the generated ID
                } else {
                    throw new SQLException("No ID obtained for " + entry.getDescription());
                }
            }
        }
    }

    // Fetch all activity entries from the database.
    public List<ActivityEntry> getAllEntries() throws SQLException {
        List<ActivityEntry> entries = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ActivityEntry entry = new ActivityEntry(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getInt("activity_id")
                );
                entries.add(entry);
            }
        }
        return entries;
    }

    // Helper: Convert LocalDateTime to Timestamp
    private Timestamp toTimestamp(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime)
                .map(Timestamp::valueOf)
                .orElse(null);
    }
}
