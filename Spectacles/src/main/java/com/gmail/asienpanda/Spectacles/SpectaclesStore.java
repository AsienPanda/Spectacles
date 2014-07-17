package com.gmail.asienpanda.Spectacles;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;

public class SpectaclesStore {

    public static HashMap<String, ArrayList<String>> load(String path) {
        Object result = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            if (br.readLine() == null) {
                result = new HashMap<String, ArrayList<String>>();
            } else {
                try {

                    ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(path));
                    result = ois.readObject();
                    ois.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (HashMap<String, ArrayList<String>>) result;

    }

    public static void save(HashMap<String, ArrayList<String>> map, String path) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(path));
            oos.writeObject(map);
            oos.flush();
            oos.close();
            // Handle I/O exceptions
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getPlayerOwnList(String playerUUID) {
        ArrayList<String> places = new ArrayList<String>();
        if (Spectacles.storedPlayerData == null) {
            return places;
        } else {
            places = Spectacles.storedPlayerData.get(playerUUID);
            return places;
        }
    }

    public static void add(String playerUUID, String regionName) {
        ArrayList<String> places = Spectacles.storedPlayerData.get(playerUUID);
        if (!places.contains(regionName)) {
            places.add(regionName);
        }
        Spectacles.storedPlayerData.put(playerUUID, places);
        save(Spectacles.storedPlayerData, Spectacles.getInstance()
                .getDataFolder() + File.separator + "playerdata.bin");

    }

    public static void remove(String regionName) // remove from
    // config.yml AND
    // from all players
    {
        // remove from config.yml
        Spectacles.getInstance().getConfig().getConfigurationSection("Regions")
                .set(regionName, null);
        Spectacles.getInstance().saveConfig();

        // remove from all players
        HashMap<String, ArrayList<String>> newPlayerData = Spectacles.storedPlayerData;
        for (ArrayList<String> value : newPlayerData.values()) {
            value.remove(regionName);
        }
        save(newPlayerData, Spectacles.getInstance().getDataFolder()
                + File.separator + "playerdata.bin");
        Spectacles.places.remove(regionName);
    }

    public static void cleanup(String path) {
        HashMap<String, ArrayList<String>> savedPlayerData = load(path);
        ConfigurationSection ConfigSec = Spectacles.getInstance().getConfig()
                .getConfigurationSection("Regions");
        if (ConfigSec == null) {
            if (savedPlayerData != null) {
                for (ArrayList<String> arrayList : savedPlayerData.values()) {
                    arrayList.clear();
                }
            }
            System.out.println(savedPlayerData.values());

        } else {

            for (ArrayList<String> arrayList : savedPlayerData.values()) {
                for (Iterator<String> itr = arrayList.iterator(); itr.hasNext(); ) {
                    String arrayListString = itr.next();
                    boolean verified = false;
                    for (Iterator<String> iterator = ConfigSec.getKeys(false)
                            .iterator(); iterator.hasNext();) {
                        String regionNameKey = (iterator.next());
                        if (arrayListString.equals(regionNameKey)) {
                            verified = true;
                        }
                    }
                    if (!verified) {
                        itr.remove();
                    }
                }

            }
        }
        save(savedPlayerData, Spectacles.getInstance().getDataFolder()
                + File.separator + "playerdata.bin");
    }
}