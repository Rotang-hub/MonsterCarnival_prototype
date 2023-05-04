package io.github.rotanghub.monstercarnival.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GameProcess
{
    Plugin plugin;
    Manager manager;

    public int redDeathCount = 10;
    public int blueDeathCount = 10;
    public int redMonsterCount = 0;
    public int blueMonsterCount = 0;

    public GameProcess(Plugin plugin, Manager manager)
    {
        this.plugin = plugin;
        this.manager = manager;
    }

    public void startCoinCycle()
    {
        for(Player p : Bukkit.getOnlinePlayers())
        {
            p.getInventory().addItem(manager.getCoin(10));
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(!manager.process)
                {
                    cancel();
                    return;
                }

                for(Player p : Bukkit.getOnlinePlayers())
                {
                    p.getInventory().addItem(manager.getCoin(2));
                }
            }
        }.runTaskTimer(plugin, 10 * 20, 10 * 20);
    }
}
