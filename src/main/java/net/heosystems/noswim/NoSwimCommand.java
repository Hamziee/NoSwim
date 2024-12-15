package net.heosystems.noswim;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class NoSwimCommand implements CommandExecutor {

    private final NoSwim plugin;

    public NoSwimCommand(NoSwim plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /noswim <enable|disable|reload>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "enable":
                plugin.setEnabledBehavior(true);
                sender.sendMessage("NoSwim behavior has been enabled!");
                break;

            case "disable":
                plugin.setEnabledBehavior(false);
                sender.sendMessage("NoSwim behavior has been disabled!");
                break;

            case "reload":
                plugin.reloadConfig();
                sender.sendMessage("NoSwim configuration has been reloaded!");
                break;

            default:
                sender.sendMessage("Usage: /noswim <enable|disable|reload>");
                break;
        }

        return true;
    }
}
