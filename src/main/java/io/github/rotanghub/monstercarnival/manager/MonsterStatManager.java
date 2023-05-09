package io.github.rotanghub.monstercarnival.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

public class MonsterStatManager
{
    DataConfigurationManager dataConfigurationManager;
    FileConfiguration dataConfig;
    public MonsterStatManager(DataConfigurationManager dataConfigurationManager, FileConfiguration dataConfig)
    {
        this.dataConfigurationManager = dataConfigurationManager;
        this.dataConfig = dataConfig;
    }

    public int getHealth(EntityType type, int level)
    {
        return dataConfig.getInt("Monster." + type.toString() + ".Level." + level + ".health");
    }

    public double getSpeed(EntityType type, int level)
    {
        return dataConfig.getDouble("Monster." + type.toString() + ".Level." + level + ".speed");
    }

    public int getAttack(EntityType type, int level)
    {
        return dataConfig.getInt("Monster." + type.toString() + ".Level." + level + ".attack");
    }

    public int getPrice(EntityType type, int level)
    {
        return dataConfig.getInt("Monster." + type.toString() + ".Level." + level + ".price");
    }

    public int getDrop(EntityType type, int level)
    {
        return dataConfig.getInt("Monster." + type.toString() + ".Level." + level + ".drop");
    }

    public int getMythicHealth(String name)
    {
        return dataConfig.getInt("Monster." + name + ".health");
    }

    public double getMythicSpeed(String name)
    {
        return dataConfig.getDouble("Monster." + name + ".speed");
    }

    public int getMythicAttack(String name)
    {
        return dataConfig.getInt("Monster." + name + ".attack");
    }

    public int getMythicPrice(String name)
    {
        return dataConfig.getInt("Monster." + name + ".price");
    }

    public int getMythicDrop(String name)
    {
        return dataConfig.getInt("Monster." + name + ".drop");
    }
}
