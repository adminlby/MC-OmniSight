package org.lbynb.mCOmniSight.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.lbynb.mCOmniSight.camera.CameraNode;
import org.lbynb.mCOmniSight.camera.CameraType;

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

    public static void saveCamera(CameraNode node) {
        String sql = "INSERT OR REPLACE INTO CameraNode (id, x, y, z, world, type, yaw, pitch, isActive, whitelist) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, node.getId().toString());
            pstmt.setDouble(2, node.getLoc().getX());
            pstmt.setDouble(3, node.getLoc().getY());
            pstmt.setDouble(4, node.getLoc().getZ());
            pstmt.setString(5, node.getLoc().getWorld().getName());
            pstmt.setString(6, node.getType().name());
            pstmt.setFloat(7, node.getYaw());
            pstmt.setFloat(8, node.getPitch());
            pstmt.setInt(9, node.isActive() ? 1 : 0);
            pstmt.setString(10, String.join(",", node.getWhitelist()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCamera(UUID id) {
        String sql = "DELETE FROM CameraNode WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<CameraNode> loadAllCameras() {
        List<CameraNode> cameras = new ArrayList<>();
        String sql = "SELECT * FROM CameraNode";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                String worldName = rs.getString("world");
                org.bukkit.World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    continue;
                }
                Location loc = new Location(world, x, y, z);
                CameraType type = CameraType.valueOf(rs.getString("type"));
                float yaw = rs.getFloat("yaw");
                float pitch = rs.getFloat("pitch");
                boolean isActive = rs.getInt("isActive") == 1;
                String whitelistStr = rs.getString("whitelist");
                List<String> whitelist = new ArrayList<>();
                if (whitelistStr != null && !whitelistStr.isEmpty()) {
                    whitelist = new ArrayList<>(List.of(whitelistStr.split(",")));
                }
                cameras.add(new CameraNode(id, loc, type, yaw, pitch, isActive, whitelist));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cameras;
    }

    public static CameraNode getCameraById(UUID id) {
        String sql = "SELECT * FROM CameraNode WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                String worldName = rs.getString("world");
                org.bukkit.World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    return null;
                }
                Location loc = new Location(world, x, y, z);
                CameraType type = CameraType.valueOf(rs.getString("type"));
                float yaw = rs.getFloat("yaw");
                float pitch = rs.getFloat("pitch");
                boolean isActive = rs.getInt("isActive") == 1;
                String whitelistStr = rs.getString("whitelist");
                List<String> whitelist = new ArrayList<>();
                if (whitelistStr != null && !whitelistStr.isEmpty()) {
                    whitelist = new ArrayList<>(List.of(whitelistStr.split(",")));
                }
                return new CameraNode(id, loc, type, yaw, pitch, isActive, whitelist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
