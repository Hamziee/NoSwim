package net.heosystems.noswim;

import org.bukkit.plugin.java.JavaPlugin;

public class NoSwim extends JavaPlugin {

    private boolean enabled = true; // Default behavior

    @Override
    public void onEnable() {
        getLogger().info("NoSwim has been enabled!");

        // Save default configuration
        saveDefaultConfig();

        // Register the SwimListener
        getServer().getPluginManager().registerEvents(new SwimListener(this), this);

        // Register the command
        getCommand("noswim").setExecutor(new NoSwimCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("NoSwim has been disabled!");
    }

    public boolean isEnabledBehavior() {
        return enabled;
    }

    public void setEnabledBehavior(boolean enabled) {
        this.enabled = enabled;
    }
}
