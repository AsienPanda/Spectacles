package com.gmail.asienpanda.Spectacles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spectacles extends JavaPlugin {
    static FileConfiguration config;
    File cfile;

    private File playerDataFile;
    public static HashMap<String, String> currentPlayerData;
    public static HashMap<String, ArrayList<String>> storedPlayerData;
    public static ArrayList<String> places;
    private static Plugin instance;
    public static int toolID;
    private Location firstLoc = null;
    private Location secondLoc = null;

    public void onEnable()

    {
        instance = this;
        new SpectaclesListener(this);
        getCommand("spectacles").setExecutor(new SpectaclesCommandExecutor());
        getCommand("tour").setExecutor(new SpectaclesCommandExecutor());
        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
        cfile = new File(getDataFolder(), "config.yml");
        toolID = config.getInt("Wand ID");
        
        String path = getDataFolder() + File.separator + "playerdata.bin";
        
        playerDataFile = new File(path);
                
        places = new ArrayList<String>();
        if (playerDataFile.exists()) {
            SpectaclesStore.cleanup(path);
            storedPlayerData = SpectaclesStore.load(path);
        } else {
            File dataFile = new File(path);
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            storedPlayerData = new HashMap<String, ArrayList<String>>();

        }

        if (getConfig().contains("Regions")) {
            for (String x : getConfig().getConfigurationSection("Regions")
                    .getKeys(false)) {
                places.add(x);
            }

        }
        if (currentPlayerData == null) {
            currentPlayerData = new HashMap<String, String>();
        }
        
    }

    public void onDisable() {
        SpectaclesStore.save(storedPlayerData, getDataFolder() + File.separator
                + "playerdata.bin");
       
    }

    public static Plugin getInstance() {
        return instance;
    }

    public Location getFirstLoc() {
        return firstLoc;
    }

    public Location getSecondLoc() {
        return secondLoc;

    }

    public void setFirstLoc(Location l) {
        firstLoc = l;
    }

    public void setSecondLoc(Location l) {
        secondLoc = l;
    }
}
