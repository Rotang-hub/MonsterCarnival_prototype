package io.github.rotanghub.monstercarnival.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ScrollShop2GUI implements Listener
{
    Manager manager;
    Inventory menuTab;
    private Player player;

    public ScrollShop2GUI(Manager manager, Player player)
    {
        this.manager = manager;
        this.player = player;
        menuTab = Bukkit.createInventory(player, 45);
        setButton();
    }

    public Inventory getMenuTab()
    {
        return menuTab;
    }

    public void setButton()
    {
        menuTab.setItem(20, getNetherZombieScrollButton());
        menuTab.setItem(24, getMinerZombieScrollButton());
    }

    public ItemStack getNetherZombieScrollButton()
    {
        ItemStack item = new ItemStack(manager.getNetherZombieScroll());
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getMythicPrice("NetherZombie") + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getMinerZombieScrollButton()
    {
        ItemStack item = new ItemStack(manager.getMinerZombieScroll());
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getMythicPrice("MinerZombie") + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public void buyNetherZombieScroll(Player player)
    {
        int price = manager.statManager.getMythicPrice("NetherZombie");
        if(hasItems(player, manager.getCoin(1), price))
        {
            removeItems(player, manager.getCoin(1), price);
            player.getInventory().addItem(manager.getNetherZombieScroll());
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);
        }
        else player.playSound(player, Sound.UI_BUTTON_CLICK, 5, 1);
    }

    public void buyMinerZombieScroll(Player player)
    {
        int price = manager.statManager.getMythicPrice("MinerZombie");
        if(hasItems(player, manager.getCoin(1), price))
        {
            removeItems(player, manager.getCoin(1), price);
            player.getInventory().addItem(manager.getMinerZombieScroll());
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5, 1);
        }
        else player.playSound(player, Sound.UI_BUTTON_CLICK, 5, 1);
    }

    public boolean hasItems(Player player, ItemStack item, int amount)
    {
        Inventory inv = player.getInventory();
        int currentAmount = 0;

        for(ItemStack i : inv.getContents())
        {
            if(i == null || i.getType().equals(Material.AIR))
                continue;
            if(i.getType().equals(item.getType()))
            {
                currentAmount += i.getAmount();
            }
        }

        return amount <= currentAmount;
    }

    public void removeItems(Player player, ItemStack item, int amount)
    {
        Inventory inv = player.getInventory();
        for(ItemStack i : inv.getContents())
        {
            if(i == null || i.getType().equals(Material.AIR))
                continue;
            if(i.getType().equals(item.getType()))
            {
                if(i.getAmount() < amount)
                {
                    amount = amount - i.getAmount();
                    i.setAmount(0);
                }
                else
                {
                    i.setAmount(i.getAmount() - amount);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().equals(getMenuTab()))
        {
            Player player = (Player) event.getWhoClicked();
            if(!player.equals(this.player)) return;

            ItemStack item = event.getCurrentItem();
            if(item == null || item.getType().equals(Material.AIR)) return;

            event.setCancelled(true);

            if(event.getClick().equals(ClickType.LEFT))
            {
                if(item.getType().equals(getNetherZombieScrollButton().getType()))
                {
                    buyNetherZombieScroll(player);
                }
                if(item.getType().equals(getMinerZombieScrollButton().getType()))
                {
                    buyMinerZombieScroll(player);
                }
            }
        }

        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))
        {
            Player player = (Player) event.getWhoClicked();
            Inventory openInv = player.getOpenInventory().getTopInventory();

            if(player != this.player) return;
            if(openInv.equals(getMenuTab())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(event.getInventory().equals(getMenuTab()))
        {
            event.getHandlers().unregisterAll(this);
        }
    }
}
