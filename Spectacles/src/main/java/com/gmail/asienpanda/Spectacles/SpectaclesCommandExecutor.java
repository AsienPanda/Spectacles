package com.gmail.asienpanda.Spectacles;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class SpectaclesCommandExecutor implements CommandExecutor {
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spectacles")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("wand")) {
                    if (sender.hasPermission("spectacles.create")) {
                        if (sender instanceof Player)// if sender is instance of
                                                     // Player
                        {
                            Player p = (Player) sender;
                            p.getInventory().addItem(
                                    new ItemStack(Spectacles.toolID));
                            return true;

                        } else {

                            sender.sendMessage(ChatColor.RED
                                    + "You must be a player!");
                            return false;
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "No permission!");
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("create")) {
                    if (sender.hasPermission("spectacles.create")) {
                        if (sender instanceof Player)// if sender is instance of
                                                     // Player
                        {

                            if (SpectaclesListener.instance.getFirstLoc() == null) {
                                sender.sendMessage(ChatColor.YELLOW
                                        + "First point not set!");
                                return false;
                            } else if (SpectaclesListener.instance
                                    .getSecondLoc() == null) {
                                sender.sendMessage(ChatColor.YELLOW
                                        + "Second point not set!");
                                return false;
                            } else {
                                if (args.length == 2) {

                                    if (!Spectacles.places.contains(args[1])) {
                                        SpectaclesRegion region = new SpectaclesRegion(
                                                SpectaclesListener.instance
                                                        .getFirstLoc(),
                                                SpectaclesListener.instance
                                                        .getSecondLoc(),
                                                args[1]);
                                        region.create();
                                        sender.sendMessage(ChatColor.GREEN
                                                + "The spectacle " + args[1]
                                                + " has been created!");
                                        return true;
                                    } else {
                                        sender.sendMessage(ChatColor.RED
                                                + "The name given already exists!");
                                        return false;
                                    }
                                } else if (args.length < 2) {

                                    sender.sendMessage(ChatColor.RED
                                            + "A name for this spectacle is required!");
                                    return false;
                                } else {
                                    sender.sendMessage(ChatColor.RED
                                            + "Too many arguments!");
                                    return false;
                                }
                            }

                        } else {
                            sender.sendMessage(ChatColor.RED
                                    + "You must be a player!");
                            return false;
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "No permission!");
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (sender.hasPermission("spectacles.remove"))

                    {
                        if (args.length == 2) {
                            if (Spectacles.places.contains(args[1])) {
                                SpectaclesStore.remove(args[1]);
                                sender.sendMessage("Spectacle " + args[1]
                                        + " removed!");
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.RED
                                        + "Spectacle given does not exist in config.yml!");
                                return false;
                            }
                        } else if (args.length > 2) {
                            sender.sendMessage(ChatColor.RED
                                    + "Too many arguments!");
                            return false;
                        } else {
                            sender.sendMessage(ChatColor.RED
                                    + "You must provide the name of the spectacle you wish to remove");
                            return false;
                        }

                    } else {
                        sender.sendMessage(ChatColor.RED + "No permission!");
                        return false;
                    }
                } else if (args[0].equalsIgnoreCase("tour")) {
                    if (sender.hasPermission("spectacles.tour")) {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;

                            ArrayList<String> seenSpectacles = SpectaclesStore
                                    .getPlayerOwnList(p.getUniqueId()
                                            .toString());
                            /*
                             * String seenSpectaclesString = "";
                             * 
                             * for (String x : seenSpectacles) {
                             * seenSpectaclesString = x + ", "; }
                             * 
                             * if (seenSpectaclesString.length() > 1)
                             * seenSpectaclesString = seenSpectaclesString
                             * .substring( 0, seenSpectaclesString.length() -
                             * 2);
                             */

                            ArrayList<String> notSeen = Spectacles.places;

                            for (String x : seenSpectacles) {
                                if (notSeen.contains(x)) {
                                    notSeen.remove(x);
                                }
                            }

                            String notSeenString = notSeen.toString();

                            sender.sendMessage(ChatColor.GREEN
                                    + "You have already seen " + ChatColor.GOLD
                                    + seenSpectacles.size() + ". "
                                    + ChatColor.GRAY + notSeen.size()
                                    + ChatColor.GREEN
                                    + " are still left to discover.");

                            sender.sendMessage(ChatColor.AQUA
                                    + "You have yet to see:");
                            sender.sendMessage(ChatColor.GRAY + notSeenString);

                            sender.sendMessage(ChatColor.AQUA
                                    + "You have already seen:");
                            sender.sendMessage(ChatColor.GOLD
                                    + seenSpectacles.toString());
                            return true;

                        } else {
                            sender.sendMessage(ChatColor.RED
                                    + "You must be a player!");
                            return false;
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "No permission!");
                        return false;
                    }
                } else {
                    return false;
                }

            }
            return false;
        } else if (command.getName().equalsIgnoreCase("tour")) {
            if (sender.hasPermission("spectacles.tour")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;

                    ArrayList<String> seenSpectacles = SpectaclesStore
                            .getPlayerOwnList(p.getUniqueId().toString());

                    ArrayList<String> notSeen = Spectacles.places;

                    for (String x : seenSpectacles) {
                        if (notSeen.contains(x)) {
                            notSeen.remove(x);
                        }
                    }

                    String notSeenString = notSeen.toString();

                    sender.sendMessage(ChatColor.GREEN
                            + "You have already seen " + ChatColor.GOLD
                            + seenSpectacles.size() + ". " + ChatColor.GRAY
                            + notSeen.size() + ChatColor.GREEN
                            + " are still left to discover.");

                    sender.sendMessage(ChatColor.AQUA + "You have yet to see:");
                    sender.sendMessage(ChatColor.GRAY + notSeenString);

                    sender.sendMessage(ChatColor.AQUA
                            + "You have already seen:");
                    sender.sendMessage(ChatColor.GOLD
                            + seenSpectacles.toString());
                    return true;

                } else {
                    sender.sendMessage(ChatColor.RED + "You must be a player!");
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "No permission!");
                return false;
            }

        }
        return false;
    }
}
