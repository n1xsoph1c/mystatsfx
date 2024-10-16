package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Activity.ActivityGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityGroupDAO {
    public int addActivityGroup(ActivityGroup activityGroup) throws SQLException {
        String sql = "insert into activity_groups (name) values (?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, activityGroup.getName());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    activityGroup.setId(rs.getInt(1));
                    return activityGroup.getId();

                } else {
                    throw new SQLException("Failed to insert new activity group");
                }
            }
        }
    }

    public List<ActivityGroup> getAllActivityGroups() throws SQLException {
        String sql = "select * from activity_groups";
        List<ActivityGroup> activityGroups = new ArrayList<>();

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ActivityGroup group = new ActivityGroup(rs.getInt(1), rs.getString(2));
                activityGroups.add(group);
            }

        }
        return activityGroups;
    }
}
