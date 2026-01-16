package org.lbynb.mCOmniSight;

import org.bukkit.plugin.java.JavaPlugin;
import org.lbynb.mCOmniSight.database.DatabaseManager;

public final class MCOmniSight extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MASS Plugin Enabled");
        DatabaseManager.initialize();
    }

    @Override
    public void onDisable() {
        getLogger().info("MASS Plugin Disabled");
    }
}
