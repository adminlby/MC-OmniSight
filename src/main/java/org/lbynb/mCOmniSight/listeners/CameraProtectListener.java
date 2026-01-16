package org.lbynb.mCOmniSight.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.lbynb.mCOmniSight.camera.CameraNode;
import org.lbynb.mCOmniSight.database.DatabaseManager;

public class CameraProtectListener implements Listener {
    
    private static final Map<String, UUID> protectedChunks = new HashMap<>();

    public static void reloadProtectedChunks() {
        protectedChunks.clear();
        List<CameraNode> cameras = DatabaseManager.loadAllCameras();
        for (CameraNode camera : cameras) {
            Chunk chunk = camera.getLoc().getChunk();
            String chunkKey = getChunkKey(chunk);
            protectedChunks.put(chunkKey, camera.getId());
        }
    }

    private static String getChunkKey(Chunk chunk) {
        return chunk.getWorld().getName() + "_" + chunk.getX() + "_" + chunk.getZ();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        
        if (player.hasPermission("mass.admin")) {
            return;
        }

        Chunk chunk = event.getBlock().getChunk();
        String chunkKey = getChunkKey(chunk);
        
        UUID cameraId = protectedChunks.get(chunkKey);
        if (cameraId != null) {
            CameraNode camera = DatabaseManager.getCameraById(cameraId);
            if (camera != null && !camera.getWhitelist().contains(player.getName())) {
                event.setCancelled(true);
                player.sendMessage("§cYou cannot break blocks in this camera's protected area!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();
        
        if (player.hasPermission("mass.admin")) {
            return;
        }

        Entity entity = event.getEntity();
        
        if (entity.getScoreboardTags().contains("mass_camera")) {
            event.setCancelled(true);
            player.sendMessage("§cYou cannot damage camera entities!");
        }
    }
}
