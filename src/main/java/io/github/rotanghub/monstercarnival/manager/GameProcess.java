package io.github.rotanghub.monstercarnival.manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameProcess
{
    Plugin plugin;
    Manager manager;

    public int maxCount = 30;
    public int maxDeathCount = 10;
    public int redDeathCount = 0;
    public int blueDeathCount = 0;
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

    public void sendActionBarScore()
    {
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

                String text = "데스카운트: " + ChatColor.RED + redDeathCount + ChatColor.BLACK + "/" + ChatColor.BLUE + blueDeathCount +
                        "       " + ChatColor.RESET + "점수: " + ChatColor.RED + redMonsterCount + ChatColor.BLACK + "/" + ChatColor.BLUE + blueMonsterCount;

                for(Player p : manager.redTeam)
                {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
                }
                for(Player p : manager.blueTeam)
                {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
