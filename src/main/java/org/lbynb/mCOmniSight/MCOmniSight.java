package org.lbynb.mCOmniSight;

import org.bukkit.plugin.java.JavaPlugin;
import org.lbynb.mCOmniSight.commands.CameraCommand;
import org.lbynb.mCOmniSight.database.DatabaseManager;
import org.lbynb.mCOmniSight.listeners.CameraProtectListener;

public final class MCOmniSight extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MASS Plugin Enabled");
        DatabaseManager.initialize();
        
        CameraProtectListener.reloadProtectedChunks();
        
        getCommand("mass").setExecutor(new CameraCommand());
        
        getServer().getPluginManager().registerEvents(new CameraProtectListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MASS Plugin Disabled");
    }
}
