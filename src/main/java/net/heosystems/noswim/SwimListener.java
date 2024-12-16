package net.heosystems.noswim;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.block.BlockFace;

public class SwimListener implements Listener {

    private final NoSwim plugin;

    public SwimListener(NoSwim plugin) {
        this.plugin = plugin;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Exclude Creative and Spectator modes
                    if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                        continue;
                    }

                    // Check if the player is in water
                    if (player.getLocation().getBlock().getType() == Material.WATER) {

                        // Check Water Breathing Exemption
                        if (plugin.getConfig().getBoolean("water-breath-exemption")
                                && player.hasPotionEffect(PotionEffectType.WATER_BREATHING)) {
                            continue;
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
            }
        }.runTaskTimer(plugin, 0L, 1L);  // Run every tick (1L)
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
        Block block = event.getBlock();
        int minWaterHeight = plugin.getConfig().getInt("min-water-height", 2);

        if (plugin.getConfig().getBoolean("deny-building-in-water")) {
            Block placedBlock = event.getBlock();
            // Check if the block being placed is inside water
            for (BlockFace face : BlockFace.values()) {
                Block adjacentBlock = placedBlock.getRelative(face);
                if (adjacentBlock.getType() == Material.WATER || adjacentBlock.getType() == Material.AIR) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("[NoSwim] You are not allowed to build in water!");
                    return;
                }
            }
        }

        // Restrict building
        if (plugin.getConfig().getBoolean("deny-building-above-water")) {
            int waterHeightBelow = 0;
            Block blockBelow = block.getRelative(0, -1, 0);

            while (blockBelow.getY() > 0) {
                if (blockBelow.getType() == Material.WATER) {
                    waterHeightBelow++;
                    blockBelow = blockBelow.getRelative(0, -1, 0);
                } else if (blockBelow.getType() != Material.AIR) {
                    break;
                } else {
                    blockBelow = blockBelow.getRelative(0, -1, 0);
                }
            }

            if (waterHeightBelow >= minWaterHeight) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("[NoSwim] You are not allowed to build above water of this depth!");
                return;
            }
        }

        if (plugin.getConfig().getBoolean("deny-building-underneath-water")) {
            int waterHeightAbove = 0;
            Block blockAbove = block.getRelative(0, 1, 0);
            int consecutiveAirBlocks = 0;

            while (blockAbove.getY() < block.getWorld().getMaxHeight()) {
                if (blockAbove.getType() == Material.WATER) {
                    waterHeightAbove++;
                    blockAbove = blockAbove.getRelative(0, 1, 0);
                    consecutiveAirBlocks = 0;
                } else if (blockAbove.getType() == Material.AIR) {
                    consecutiveAirBlocks++;
                    if (consecutiveAirBlocks >= 10) {
                        break;
                    }
                    blockAbove = blockAbove.getRelative(0, 1, 0);
                } else {
                    break;
                }
            }

            if (waterHeightAbove >= minWaterHeight) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("[NoSwim] You are not allowed to build underneath water of this depth!");
            }
        }
    }
}
