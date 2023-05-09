package io.github.rotanghub.monstercarnival.manager.npcManager;

import io.github.rotanghub.monstercarnival.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NPCLocationManager
{
    Manager manager;
    List<Location> redNPCLocationList = new ArrayList<>();
    List<Location> blueNPCLocationList = new ArrayList<>();

    public NPCLocationManager(Manager manager)
    {
        this.manager = manager;
        setRedNPCLocationList();
        setBlueNPCLocationList();
    }

    public void setRedNPCLocationList()
    {
        World world = Bukkit.getWorld("world");
        Location rd = new Location(world, 90.5, 59.5, -34.5);
        Location ld = new Location(world, 90.5, 59.5, -12.5);
        Location ru = new Location(world, 92.5, 60.5, -34.5);
        Location lu = new Location(world, 92.5, 60.5, -12.5);

        autoAdd(redNPCLocationList, rd);
        autoAdd(redNPCLocationList, ld);
        autoAdd(redNPCLocationList, ru);
        autoAdd(redNPCLocationList, lu);
    }

    public void setBlueNPCLocationList()
    {
        World world = Bukkit.getWorld("world");
        Location ld = new Location(world, 50.5, 59.5, -34.5);
        Location rd = new Location(world, 50.5, 59.5, -12.5);
        Location lu = new Location(world, 48.5, 60.5, -34.5);
        Location ru = new Location(world, 48.5, 60.5, -12.5);

        autoAdd(blueNPCLocationList, ld);
        autoAdd(blueNPCLocationList, rd);
        autoAdd(blueNPCLocationList, lu);
        autoAdd(blueNPCLocationList, ru);
    }

    public void autoAdd(List<Location> list, Location start)
    {
        for(int z = 0; z < 5; z++)
        {
            Location loc = start.clone().add(0, 0, z * 2);
            list.add(loc);
        }
    }

    public Location[] getRedSpawnTarget()
    {
        int redSeed = new Random().nextInt(3);

        Location redSpawnLoc = manager.locationManager.getCenterDoor();
        Location redTargetLoc = manager.locationManager.getRedCenter();

        if(redSeed == 0)
        {
            redSpawnLoc = manager.locationManager.getSouthDoor().clone().add(3, 1, 0);
            redTargetLoc = manager.locationManager.getRedSouthCorner().clone();
        }
        else if(redSeed == 1)
        {
            redSpawnLoc = manager.locationManager.getCenterDoor().clone().add(3, 1, 0);
            redTargetLoc = manager.locationManager.getRedCenter().clone();
        }
        else if(redSeed == 2)
        {
            redSpawnLoc = manager.locationManager.getNorthDoor().clone().add(3, 1, 0);
            redTargetLoc = manager.locationManager.getRedNorthCorner().clone();
        }

        return new Location[]{redSpawnLoc, redTargetLoc};
    }

    public Location[] getBlueSpawnTarget()
    {
        int blueSeed = new Random().nextInt(3);

        Location blueSpawnLoc = manager.locationManager.getCenterDoor();
        Location blueTargetLoc = manager.locationManager.getBlueCenter();

        if(blueSeed == 0)
        {
            blueSpawnLoc = manager.locationManager.getSouthDoor().clone().add(-3, 1, 0);
            blueTargetLoc = manager.locationManager.getBlueSouthCorner().clone();
        }
        else if(blueSeed == 1)
        {
            blueSpawnLoc = manager.locationManager.getCenterDoor().clone().add(-3, 1, 0);
            blueTargetLoc = manager.locationManager.getBlueCenter().clone();
        }
        else if(blueSeed == 2)
        {
            blueSpawnLoc = manager.locationManager.getNorthDoor().clone().add(-3, 1, 0);
            blueTargetLoc = manager.locationManager.getBlueNorthCorner().clone();
        }

        return new Location[]{blueSpawnLoc, blueTargetLoc};
    }
}
