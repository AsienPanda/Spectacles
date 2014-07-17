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
        
        SpectaclesListener.instance.config.set("Regions" + "." + regionName + ".World", firstLoc.getWorld().getName());
        SpectaclesListener.instance.config.set("Regions" + "." + regionName + ".x1", firstLoc.getBlockX());
        SpectaclesListener.instance.config.set("Regions" + "." + regionName + ".y1", firstLoc.getBlockY());
        SpectaclesListener.instance.config.set("Regions" + "." + regionName + ".z1", firstLoc.getBlockZ());
        
        SpectaclesListener.instance.config.set("Regions" + "." + regionName + ".x2", secondLoc.getBlockX());
        SpectaclesListener.instance.config.set("Regions" + "." + regionName + ".y2", secondLoc.getBlockY());
        SpectaclesListener.instance.config.set("Regions" + "." + regionName + ".z2", secondLoc.getBlockZ());
        
        SpectaclesListener.instance.saveConfig();
        SpectaclesListener.instance.places.add(regionName);
        
     } 
        
}
    //end result is to write new region into .yml file

