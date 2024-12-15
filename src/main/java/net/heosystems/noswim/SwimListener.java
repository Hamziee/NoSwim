package net.heosystems.noswim;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SwimListener implements Listener {

    private final NoSwim plugin;

    public SwimListener(NoSwim plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Exclude Creative and Spectator modes
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        // Check if the player is in water
        if (player.getLocation().getBlock().getType() == Material.WATER) {

            // Check Water Breathing Exemption
            if (plugin.getConfig().getBoolean("water-breath-exemption")
                    && player.hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
                return;
            }

            // Check water depth
            int minHeight = plugin.getConfig().getInt("min-water-height");
            if (isDeepWater(player, minHeight)) {
                if (plugin.getConfig().getBoolean("downforce.enabled")) {
                    applySinkingEffect(player);
                }
                applyPlayerDamage(player);
            }
        }
    }

    private boolean isDeepWater(Player player, int minHeight) {
        int depth = 0;
        for (int i = 0; i < minHeight; i++) {
            Block block = player.getLocation().subtract(0, i, 0).getBlock();
            if (block.getType() == Material.WATER) {
                depth++;
            } else {
                break;
            }
        }
        return depth >= minHeight;
    }

    private void applySinkingEffect(Player player) {
        double downforce = plugin.getConfig().getDouble("downforce.amount", 0.2);
        player.setVelocity(player.getVelocity().setY(-downforce));
    }

    private void applyPlayerDamage(Player player) {
        if (!plugin.getConfig().getBoolean("player-damage.enabled")) return;

        String damageType = plugin.getConfig().getString("player-damage.type", "none");
        switch (damageType.toLowerCase()) {
            case "poison":
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0));
                break;
            case "drowning":
                player.damage(1.0); // simulate drowning with damage
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getConfig().getBoolean("allow-building-above-water")) {
            Block block = event.getBlock();
            Block blockBelow = block.getLocation().subtract(0, 1, 0).getBlock();

            if (blockBelow.getType() == Material.WATER) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("[NoSwim] You are not allowed to build above restricted waters!");
            }
        }
    }
}
