package org.lbynb.mCOmniSight.listeners;

import java.util.List;
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        
        if (player.hasPermission("mass.admin")) {
            return;
        }

        Chunk chunk = event.getBlock().getChunk();
        List<CameraNode> cameras = DatabaseManager.loadAllCameras();

        for (CameraNode camera : cameras) {
            if (camera.getLoc().getChunk().equals(chunk)) {
                if (!camera.getWhitelist().contains(player.getName())) {
                    event.setCancelled(true);
                    player.sendMessage("§cYou cannot break blocks in this camera's protected area!");
                    return;
                }
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
