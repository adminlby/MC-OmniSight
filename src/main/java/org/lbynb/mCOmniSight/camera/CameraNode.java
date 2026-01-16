package org.lbynb.mCOmniSight.camera;

import java.util.List;
import java.util.UUID;
import org.bukkit.Location;

public class CameraNode {
    private UUID id;
    private Location loc;
    private String type; // Changed to String to match prompt
    private float yaw;
    private float pitch;
    private boolean isActive;
    private List<String> whitelist;

    public CameraNode(UUID id, Location loc, String type, float yaw, float pitch, boolean isActive, List<String> whitelist) {
        this.id = id;
        this.loc = loc;
        this.type = type;
        this.yaw = yaw;
        this.pitch = pitch;
        this.isActive = isActive;
        this.whitelist = whitelist;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }
}
