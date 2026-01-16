package org.lbynb.mCOmniSight.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.lbynb.mCOmniSight.camera.CameraNode;
import org.lbynb.mCOmniSight.camera.CameraType;
import org.lbynb.mCOmniSight.database.DatabaseManager;

public class CameraCommand implements CommandExecutor {
    
    private static final Map<UUID, Entity> cameraEntities = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("mass.admin")) {
            player.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cUsage: /mass <create|manage> [arguments]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                return handleCreate(player, args);
            case "manage":
                return handleManage(player, args);
            default:
                player.sendMessage("§cUnknown subcommand: " + args[0]);
                return true;
        }
    }

    private boolean handleCreate(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /mass create <type>");
            player.sendMessage("§cAvailable types: PTZ, DOME, BULLET");
            return true;
        }

        CameraType type;
        try {
            type = CameraType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid camera type! Available: PTZ, DOME, BULLET");
            return true;
        }

        Location loc = player.getLocation();
        UUID cameraId = UUID.randomUUID();
        float yaw = 0;
        float pitch = 0;

        if (type == CameraType.BULLET) {
            yaw = loc.getYaw();
            pitch = loc.getPitch();
        }

        CameraNode camera = new CameraNode(cameraId, loc, type, yaw, pitch, true, new ArrayList<>());
        DatabaseManager.saveCamera(camera);

        ArmorStand armorStand = loc.getWorld().spawn(loc, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setCustomName("§6[MASS Camera]");
        armorStand.setCustomNameVisible(false);
        armorStand.addScoreboardTag("mass_camera");
        armorStand.addScoreboardTag("camera_id_" + cameraId.toString());
        
        ItemStack helmet = new ItemStack(Material.OBSERVER);
        armorStand.getEquipment().setHelmet(helmet);

        cameraEntities.put(cameraId, armorStand);

        player.sendMessage("§aCamera created successfully!");
        player.sendMessage("§7ID: §f" + cameraId.toString());
        player.sendMessage("§7Type: §f" + type.name());
        if (type == CameraType.BULLET) {
            player.sendMessage("§7Direction: §fYaw=" + yaw + ", Pitch=" + pitch);
        }

        return true;
    }

    private boolean handleManage(Player player, String[] args) {
        if (args.length < 4) {
            player.sendMessage("§cUsage: /mass manage <id> <add|remove> <player>");
            return true;
        }

        UUID cameraId;
        try {
            cameraId = UUID.fromString(args[1]);
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid camera ID!");
            return true;
        }

        CameraNode camera = DatabaseManager.getCameraById(cameraId);
        if (camera == null) {
            player.sendMessage("§cCamera not found!");
            return true;
        }

        String action = args[2].toLowerCase();
        String targetPlayer = args[3];

        switch (action) {
            case "add":
                if (camera.getWhitelist().contains(targetPlayer)) {
                    player.sendMessage("§cPlayer is already in the whitelist!");
                } else {
                    camera.getWhitelist().add(targetPlayer);
                    DatabaseManager.saveCamera(camera);
                    player.sendMessage("§aPlayer §f" + targetPlayer + "§a added to whitelist!");
                }
                break;
            case "remove":
                if (camera.getWhitelist().remove(targetPlayer)) {
                    DatabaseManager.saveCamera(camera);
                    player.sendMessage("§aPlayer §f" + targetPlayer + "§a removed from whitelist!");
                } else {
                    player.sendMessage("§cPlayer not found in whitelist!");
                }
                break;
            default:
                player.sendMessage("§cInvalid action! Use 'add' or 'remove'");
                return true;
        }

        return true;
    }

    public static Map<UUID, Entity> getCameraEntities() {
        return cameraEntities;
    }
}
