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
}
