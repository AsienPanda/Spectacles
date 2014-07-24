package com.gmail.asienpanda.Spectacles;

import org.bukkit.Location;

public class SpectaclesRegion 
{
    private Location firstLoc;
    private Location secondLoc;
    private String regionName;
    
    public SpectaclesRegion(Location first, Location second, String name)
    {
        firstLoc = first;
        secondLoc = second;

        regionName = name;
    }
    
    public void create()
    {
        
        Spectacles.config.set("Regions" + "." + regionName + ".World", firstLoc.getWorld().getName());
        Spectacles.config.set("Regions" + "." + regionName + ".x1", firstLoc.getBlockX());
        Spectacles.config.set("Regions" + "." + regionName + ".y1", firstLoc.getBlockY());
        Spectacles.config.set("Regions" + "." + regionName + ".z1", firstLoc.getBlockZ());
        
        Spectacles.config.set("Regions" + "." + regionName + ".x2", secondLoc.getBlockX());
        Spectacles.config.set("Regions" + "." + regionName + ".y2", secondLoc.getBlockY());
        Spectacles.config.set("Regions" + "." + regionName + ".z2", secondLoc.getBlockZ());
        
        SpectaclesListener.instance.saveConfig();
        Spectacles.places.add(regionName);
        
     } 
        
}
    //end result is to write new region into .yml file

