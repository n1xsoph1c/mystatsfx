package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Activity.ActivityEntry;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityEntryDAO {
    public int addActivityEntry(ActivityEntry entry) throws SQLException {
        String sql = "insert into activity_entries (description, start_time, end_time, activity_id) values (?,?,?,?)";
        try(Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entry.getDescription());
            ps.setTimestamp(2, Timestamp.valueOf(entry.getStartTime()));
            ps.setTimestamp(3, Timestamp.valueOf(entry.getEndTime()));
            ps.setInt(4, entry.getActivityId());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to insert " + entry.getDescription() + " into ActivityEntry");
                }
            }
        }
    }

    public List<ActivityEntry> entries () throws SQLException {
        String sql = "select * from activity_entries";
        List<ActivityEntry> entries = new ArrayList<ActivityEntry>();
        try(Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                ActivityEntry entry =  new ActivityEntry(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getTimestamp(3).toLocalDateTime(),
                        rs.getTimestamp(4).toLocalDateTime(),
                        rs.getInt(5));
                entries.add(entry);
            }
        }

        return entries;
    }
}
