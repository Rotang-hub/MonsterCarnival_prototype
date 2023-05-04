package io.github.rotanghub.monstercarnival.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationManager
{
    private Location redCenter;
    private Location blueCenter;
    private Location redSouthCorner;
    private Location redNorthCorner;
    private Location blueSouthCorner;
    private Location blueNorthCorner;

    private Location centerDoor;
    private Location northDoor;
    private Location southDoor;

    private World world = Bukkit.getWorld("world");

    public LocationManager()
    {
        setRedCenter();
        setBlueCenter();
        setRedSouthCorner();
        setRedNorthCorner();
        setBlueSouthCorner();
        setBlueNorthCorner();

        setCenterDoor();
        setSouthDoor();
        setNorthDoor();
    }

    public Location getRedCenter()
    {
        return redCenter;
    }

    public Location getBlueCenter()
    {
        return blueCenter;
    }

    public Location getRedSouthCorner()
    {
        return redSouthCorner;
    }

    public Location getRedNorthCorner()
    {
        return redNorthCorner;
    }

    public Location getBlueSouthCorner()
    {
        return blueSouthCorner;
    }

    public Location getBlueNorthCorner()
    {
        return blueNorthCorner;
    }

    public Location getCenterDoor()
    {
        return centerDoor;
    }

    public Location getSouthDoor()
    {
        return southDoor;
    }

    public Location getNorthDoor()
    {
        return northDoor;
    }

    public void setRedCenter()
    {
        redCenter = new Location(world, 105.5, 60, -19.5);
    }

    public void setBlueCenter()
    {
        blueCenter = new Location(world, 35.5, 60, -19.5);
    }

    public void setRedSouthCorner()
    {
        redSouthCorner = new Location(world, 105.5, 60, 2.5);
    }

    public void setRedNorthCorner()
    {
        redNorthCorner = new Location(world, 105.5, 60, -41.5);
    }

    public void setBlueSouthCorner()
    {
        blueSouthCorner = new Location(world, 35.5, 60, 2.5);
    }

    public void setBlueNorthCorner()
    {
        blueNorthCorner = new Location(world, 35.5, 60, -41.5);
    }

    public void setCenterDoor()
    {
        centerDoor = new Location(world, 70.5, 60, -19.5);
    }

    public void setSouthDoor()
    {
        southDoor = new Location(world, 70.5, 60, 2.5);
    }

    public void setNorthDoor()
    {
        northDoor = new Location(world, 70.5, 60, -41.5);
    }

    public boolean isRedCorner(Location loc)
    {
        return loc.equals(getRedSouthCorner()) || loc.equals(getRedNorthCorner());
    }

    public boolean isBlueCorner(Location loc)
    {
        return loc.equals(getBlueSouthCorner()) || loc.equals(getBlueNorthCorner());
    }
}
