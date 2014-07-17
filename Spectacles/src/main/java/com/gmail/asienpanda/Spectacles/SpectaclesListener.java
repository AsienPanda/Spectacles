package com.gmail.asienpanda.Spectacles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class SpectaclesListener implements Listener {

    public static Spectacles instance;

    public SpectaclesListener(Spectacles plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        instance = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
       
        if (!Spectacles.storedPlayerData.containsKey(event.getPlayer()
                .getUniqueId().toString())) {
            Spectacles.storedPlayerData.put(event.getPlayer().getUniqueId()
                    .toString(), new ArrayList<String>());
        }
    }

    // Select the Spectacle region
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action eAction = event.getAction();

        if (p.hasPermission("spectacle.create")
                && p.getItemInHand().getTypeId() == Spectacles.toolID) {
            if (eAction == Action.LEFT_CLICK_BLOCK) {
                instance.setFirstLoc(event.getClickedBlock().getLocation());
                p.sendMessage(ChatColor.GREEN + "First point for Spectacle region set!");
                event.setCancelled(true);

            } else if (eAction == Action.RIGHT_CLICK_BLOCK) {
                instance.setSecondLoc(event.getClickedBlock().getLocation());
                p.sendMessage(ChatColor.GREEN + "Second point for Spectacle region set!");
                event.setCancelled(true);
            }

        }

    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
       
        if (instance.getConfig().contains("Regions")) {
            Player p = event.getPlayer();

            Location loc = p.getLocation();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            World currentWorld = loc.getWorld();

            // get config, then in the path for Regions, get the keys (each
            // region)
            // Loop for every value to check
            // loop all the way through, even if true (in case region within
            // region)
            for (Iterator<String> iterator = Spectacles.getInstance()
                    .getConfig().getConfigurationSection("Regions")
                    .getKeys(false).iterator(); iterator.hasNext();) {
                String regionNameKey = (iterator.next());
                Map coordMap = Spectacles.getInstance().getConfig()
                        .getConfigurationSection("Regions." + regionNameKey)
                        .getValues(false);
                int x1 = (int) coordMap.get("x1");
                int x2 = (int) coordMap.get("x2");
                int y1 = (int) coordMap.get("y1");
                int y2 = (int) coordMap.get("y2");
                int z1 = (int) coordMap.get("z1");
                int z2 = (int) coordMap.get("z2");
                String world = (String) coordMap.get("World");

                World regionWorld = Spectacles.getInstance().getServer()
                        .getWorld(world);

                boolean hasPrize = Spectacles.getInstance().getConfig()
                        .getConfigurationSection("Regions." + regionNameKey)
                        .contains("Prize");
                // find which coordinate is smaller or bigger

                int smallX = x1;
                int bigX = x2;
                int smallY = y1;
                int bigY = y2;
                int smallZ = z1;
                int bigZ = z2;

                if (smallX > bigX) {
                    smallX = x2;
                    bigX = x1;
                }

                if (smallY > bigY) {
                    smallY = y2;
                    bigY = y1;
                }
                if (smallZ > bigZ) {
                    smallZ = z2;
                    bigZ = z1;
                }
                if (!Spectacles.currentPlayerData.containsKey(p.getUniqueId()
                        .toString())) {

                    if (p.getWorld() == regionWorld) {
                        if (x >= smallX && x <= bigX) {
                            if (y >= smallY && y <= bigY) {
                                if (z >= smallZ && z <= bigZ) {
                                    if (!Spectacles.currentPlayerData
                                            .containsKey(p.getUniqueId()
                                                    .toString())) {

                                        Spectacles.currentPlayerData.put(p
                                                .getUniqueId().toString(),
                                                regionNameKey);

                                        if (!Spectacles.storedPlayerData.get(
                                                p.getUniqueId().toString())
                                                .contains(regionNameKey)) {
                                            SpectaclesStore.add(p.getUniqueId()
                                                    .toString(), regionNameKey);

                                            p.sendMessage(ChatColor.GOLD + "You just discovered "
                                                    + regionNameKey + "!");
                                            List<String> prizeList = null;
                                            if (!hasPrize) {
                                                prizeList = Spectacles
                                                        .getInstance()
                                                        .getConfig()
                                                        .getStringList(
                                                                "Default Prize");

                                            } else {
                                                prizeList = Spectacles
                                                        .getInstance()
                                                        .getConfig()
                                                        .getConfigurationSection(
                                                                "Regions."
                                                                        + regionNameKey)
                                                        .getStringList("Prize");
                                            }
                                            
                                            String awardString = "";
                                            
                                            for (String prizeInfo : prizeList) {
                                                if (prizeInfo.contains(" ")) {
                                                    int prizeID = Integer
                                                            .parseInt(prizeInfo
                                                                    .split(" ")[0]);

                                                    int prizeAmt = Integer
                                                            .parseInt(prizeInfo
                                                                    .split(" ")[1]);

                                                    p.getInventory().addItem(
                                                            new ItemStack(
                                                                    prizeID,
                                                                    prizeAmt));
                                                    String prizeName = Material.getMaterial(prizeID).name();
                                                    
                                                    awardString = awardString + prizeName + "(" + prizeAmt + "), ";
                                                }
                                            }
                                            awardString = awardString.substring(0, awardString.length() - 2);
                                            p.sendMessage(ChatColor.LIGHT_PURPLE + "You have been rewarded: " + ChatColor.GOLD + awardString);

                                        } else {
                                            /*
                                            p.sendMessage(ChatColor.LIGHT_PURPLE + "This is "
                                                    + regionNameKey + "!");
                                                    */

                                            Spectacles.currentPlayerData.put(p
                                                    .getUniqueId().toString(),
                                                    regionNameKey);

                                        }
                                    } else if (!Spectacles.currentPlayerData
                                            .get(p.getUniqueId().toString())
                                            .equals(regionNameKey)) {
                                        Spectacles.currentPlayerData.put(p
                                                .getUniqueId().toString(),
                                                regionNameKey);
                                    }
                                }
                            }

                        }

                    }
                } else if (Spectacles.currentPlayerData.get(
                        p.getUniqueId().toString()).equals(regionNameKey)) {
                    if (p.getWorld() != regionWorld) {
                        Spectacles.currentPlayerData.remove(p.getUniqueId()
                                .toString());
                    }
                    if (x < smallX || x > bigX) {
                        Spectacles.currentPlayerData.remove(p.getUniqueId()
                                .toString());
                    }
                    if (y < smallY || y > bigY) {
                        Spectacles.currentPlayerData.remove(p.getUniqueId()
                                .toString());
                    }
                    if (z < smallZ || z > bigZ) {
                        Spectacles.currentPlayerData.remove(p.getUniqueId()
                                .toString());
                    }
                }

            }
        }
    }
}
