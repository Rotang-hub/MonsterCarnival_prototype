package io.github.rotanghub.monstercarnival;

import io.github.rotanghub.monstercarnival.manager.*;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MonsterCarnival extends JavaPlugin
{
    Manager manager;
    DataConfigurationManager dataConfigurationManager = new DataConfigurationManager(this);
    FileConfiguration dataConfig = dataConfigurationManager.getConfig();
    @Override
    public void onEnable()
    {
        manager = new Manager(this, dataConfigurationManager, dataConfig);
        getServer().getPluginManager().registerEvents(new Events(manager), this);
    }

    @Override
    public void onDisable()
    {
        manager.stop();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(sender.isOp() && sender instanceof Player)
        {
            Player player = (Player) sender;

            if(label.equalsIgnoreCase("carnival"))
            {
                if(args[0].equalsIgnoreCase("start"))
                {
                    manager.start();
                }
                if(args[0].equalsIgnoreCase("stop"))
                {
                    manager.stop();
                }
                if(args[0].equalsIgnoreCase("coin"))
                {
                    player.getInventory().addItem(manager.getCoin(64));
                }
                if(args[0].equalsIgnoreCase("deathcount"))
                {
                    if(args.length >= 2)
                    {
                        int count = Integer.parseInt(args[1]);
                        getServer().broadcastMessage("데스카운트: " + manager.gameProcess.maxDeathCount + "회 -> " + count + "회");
                        manager.gameProcess.maxDeathCount = count;
                    }
                    else
                    {
                        player.sendMessage("데스카운트: " + manager.gameProcess.maxDeathCount + "회");
                    }
                }
                if(args[0].equalsIgnoreCase("score"))
                {
                    if(args.length >= 2)
                    {
                        int count = Integer.parseInt(args[1]);
                        getServer().broadcastMessage("목표 스코어: " + manager.gameProcess.maxCount + "점 -> " + count + "점");
                        manager.gameProcess.maxCount = count;
                    }
                    else
                    {
                        player.sendMessage("목표 스코어: " + manager.gameProcess.maxCount + "점");
                    }
                }
                if(args[0].equalsIgnoreCase("scroll"))
                {
                    manager.citizenNPC.spawnScrollNPC(player.getLocation());
                }
                if(args[0].equalsIgnoreCase("upgrade"))
                {
                    manager.citizenNPC.spawnUpgradeNPC(player.getLocation());
                }
                if(args[0].equalsIgnoreCase("red"))
                {
                    manager.addRedPlayer(getServer().getPlayer(args[1]));
                }
                if(args[0].equalsIgnoreCase("blue"))
                {
                    manager.addBluePlayer(getServer().getPlayer(args[1]));
                }
                if(args[0].equalsIgnoreCase("teamchat"))
                {
                    if(args[1].equalsIgnoreCase("enable"))
                    {
                        manager.teamChat = true;
                        getServer().broadcastMessage("팀 채팅이 활성화되었습니다.");
                    }
                    if(args[1].equalsIgnoreCase("disable"))
                    {
                        manager.teamChat = false;
                        getServer().broadcastMessage("팀 채팅이 비활성화되었습니다.");
                    }
                }
            }
        }

        return super.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> arguments = Arrays.asList("start", "stop", "deathcount", "score", "upgrade", "scroll", "coin", "red", "blue", "teamchat");
        List<String> able = Arrays.asList("enable", "disable");
        List<String> result = new ArrayList<>();

        if(args.length == 1)
        {
            String a = args[0].toLowerCase();
            for(String s : arguments)
            {
                if(s.startsWith(a))
                {
                    result.add(s);
                }
            }
        }

        if(args.length >= 2)
        {
            if(args[0].equalsIgnoreCase("red") || args[0].equalsIgnoreCase("blue"))
            {
                String a = args[1].toLowerCase();
                for(Player p : getServer().getOnlinePlayers())
                {
                    if(p.getName().toLowerCase().startsWith(a))
                    {
                        result.add(p.getName());
                    }
                }
            }
            if(args[0].equalsIgnoreCase("teamchat"))
            {
                String a = args[1].toLowerCase();
                for(String s : able)
                {
                    if(s.startsWith(a))
                    {
                        result.add(s);
                    }
                }
            }
        }

        return result;
    }
}
