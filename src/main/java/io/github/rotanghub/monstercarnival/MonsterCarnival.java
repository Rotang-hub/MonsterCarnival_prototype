package io.github.rotanghub.monstercarnival;

import io.github.rotanghub.monstercarnival.manager.*;
import io.lumine.mythic.lib.MythicLib;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MonsterCarnival extends JavaPlugin
{
    MythicLib mythicLib = (MythicLib) getServer().getPluginManager().getPlugin("MythicLib-1.5.2");
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
                if(args[0].equalsIgnoreCase("upgrade"))
                {
                    UpgradeShopGUI shop = new UpgradeShopGUI(manager, player);
                    getServer().getPluginManager().registerEvents(shop, this);
                    player.openInventory(shop.getMenuTab());
                }
                if(args[0].equalsIgnoreCase("scroll"))
                {
                    ScrollShopGUI shop = new ScrollShopGUI(manager, player);
                    getServer().getPluginManager().registerEvents(shop, this);
                    player.openInventory(shop.getMenuTab());
                }
                if(args[0].equalsIgnoreCase("red"))
                {
                    manager.addRedPlayer(getServer().getPlayer(args[1]));
                }
                if(args[0].equalsIgnoreCase("blue"))
                {
                    manager.addBluePlayer(getServer().getPlayer(args[1]));
                }
            }
        }

        return super.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> arguments = Arrays.asList("start", "stop", "upgrade", "scroll", "coin");
        List<String> result = new ArrayList<>();

        String a = args[0].toLowerCase();
        for(String s : arguments)
        {
            if(s.startsWith(a))
            {
                result.add(s);
            }
        }

        return result;
    }
}
