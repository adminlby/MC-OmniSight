package org.lbynb.mCOmniSight.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:mass.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        try (Connection conn = connect()) {
            if (conn != null) {
                String createTable = "CREATE TABLE IF NOT EXISTS CameraNode (" +
                        "id TEXT PRIMARY KEY," +
                        "x REAL," +
                        "y REAL," +
                        "z REAL," +
                        "world TEXT," +
                        "type TEXT," +
                        "yaw REAL," +
                        "pitch REAL," +
                        "isActive INTEGER," +
                        "whitelist TEXT" +
                        ");";
                conn.createStatement().execute(createTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
