package com.ghostcompany.mystats.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL = "jdbc:sqlite:C:/Users/gh0st/IdeaProjects/mystatsfx/src/db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
