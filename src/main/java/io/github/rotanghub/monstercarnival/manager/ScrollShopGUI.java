package io.github.rotanghub.monstercarnival.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public class ScrollShopGUI implements Listener
{
    Manager manager;
    Inventory menuTab;
    private Player player;

    public ScrollShopGUI(Manager manager, Player player)
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
        menuTab.setItem(10, getZombieScrollButton(1));
        menuTab.setItem(11, getZombieScrollButton(2));
        menuTab.setItem(12, getZombieScrollButton(3));

        menuTab.setItem(14, getSkeletonScrollButton(1));
        menuTab.setItem(15, getSkeletonScrollButton(2));
        menuTab.setItem(16, getSkeletonScrollButton(3));

        menuTab.setItem(19, getSpiderScrollButton(1));
        menuTab.setItem(20, getSpiderScrollButton(2));
        menuTab.setItem(21, getSpiderScrollButton(3));
    }

    public ItemStack getZombieScrollButton(int level)
    {
        ItemStack item = new ItemStack(manager.getZombieSummonScroll(level));
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.ZOMBIE, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getSkeletonScrollButton(int level)
    {
        ItemStack item = new ItemStack(manager.getSkeletonSummonScroll(level));
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.SKELETON, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getSpiderScrollButton(int level)
    {
        ItemStack item = new ItemStack(manager.getSpiderSummonScroll(level));
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.SPIDER, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public void buyZombieScroll(Player player, int level)
    {
        int price = manager.statManager.getPrice(EntityType.ZOMBIE, level);
        if(hasItems(player, manager.getCoin(1), price))
        {
            removeItems(player, manager.getCoin(1), price);
            player.getInventory().addItem(manager.getZombieSummonScroll(level));
        }
    }

    public void buySkeletonScroll(Player player, int level)
    {
        int price = manager.statManager.getPrice(EntityType.SKELETON, level);
        if(hasItems(player, manager.getCoin(1), price))
        {
            removeItems(player, manager.getCoin(1), price);
            player.getInventory().addItem(manager.getSkeletonSummonScroll(level));
        }
    }

    public void buySpiderScroll(Player player, int level)
    {
        int price = manager.statManager.getPrice(EntityType.SPIDER, level);
        if(hasItems(player, manager.getCoin(1), price))
        {
            removeItems(player, manager.getCoin(1), price);
            player.getInventory().addItem(manager.getSpiderSummonScroll(level));
        }
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
        if(event.getClickedInventory().equals(getMenuTab()) && event.getClick().equals(ClickType.LEFT))
        {
            Player player = (Player) event.getWhoClicked();
            if(!player.equals(this.player)) return;

            ItemStack item = event.getCurrentItem();
            if(item == null || item.getType().equals(Material.AIR)) return;

            event.setCancelled(true);

            if(item.getType().equals(getZombieScrollButton(1).getType()))
            {
                buyZombieScroll(player, manager.getMonsterLevel(item));
            }
            if(item.getType().equals(getSkeletonScrollButton(1).getType()))
            {
                buySkeletonScroll(player, manager.getMonsterLevel(item));
            }
            if(item.getType().equals(getSpiderScrollButton(1).getType()))
            {
                buySpiderScroll(player, manager.getMonsterLevel(item));
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
