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
        menuTab.setItem(0, getZombieScrollButton());
        menuTab.setItem(3, getSkeletonScrollButton());
        menuTab.setItem(6, getSpiderScrollButton());
        menuTab.setItem(18, getZoglinScrollButton());
        menuTab.setItem(21, getWolfScrollButton());
        menuTab.setItem(24, getZombifiedPiglinScrollButton());
        menuTab.setItem(36, getIncomeScrollButton());

        menuTab.setItem(1, getZombieUpgradeButton());
        menuTab.setItem(4, getSkeletonUpgradeButton());
        menuTab.setItem(7, getSpiderUpgradeButton());
        menuTab.setItem(19, getZoglinUpgradeButton());
        menuTab.setItem(22, getWolfUpgradeButton());
        menuTab.setItem(25, getZombifiedPiglinUpgradeButton());
        menuTab.setItem(37, getIncomeUpgradeButton());
    }

    public ItemStack getZombieScrollButton()
    {
        ItemStack item = new ItemStack(manager.getZombieSummonerScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.ZOMBIE, 0) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getSkeletonScrollButton()
    {
        ItemStack item = new ItemStack(manager.getSkeletonSummonerScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.SKELETON, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getSpiderScrollButton()
    {
        ItemStack item = new ItemStack(manager.getSpiderSummonerScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.SPIDER, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getZoglinScrollButton()
    {
        ItemStack item = new ItemStack(manager.getZoglinSummonerScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.ZOGLIN, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getWolfScrollButton()
    {
        ItemStack item = new ItemStack(manager.getWolfSummonerScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.WOLF, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getZombifiedPiglinScrollButton()
    {
        ItemStack item = new ItemStack(manager.getZombifiedPiglinSummonerScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.WOLF, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getIncomeScrollButton()
    {
        ItemStack item = new ItemStack(manager.getIncomeSummonerScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.WOLF, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getZombieUpgradeButton()
    {
        ItemStack item = new ItemStack(manager.getZombieUpgradeScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.ZOMBIE, 0) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getSkeletonUpgradeButton()
    {
        ItemStack item = new ItemStack(manager.getSkeletonUpgradeScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.SKELETON, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getSpiderUpgradeButton()
    {
        ItemStack item = new ItemStack(manager.getSpiderUpgradeScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.SPIDER, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getZoglinUpgradeButton()
    {
        ItemStack item = new ItemStack(manager.getZoglinUpgradeScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.ZOGLIN, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getWolfUpgradeButton()
    {
        ItemStack item = new ItemStack(manager.getWolfUpgradeScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.WOLF, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getZombifiedPiglinUpgradeButton()
    {
        ItemStack item = new ItemStack(manager.getZombifiedPiglinUpgradeScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.ZOMBIFIED_PIGLIN, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getIncomeUpgradeButton()
    {
        ItemStack item = new ItemStack(manager.getIncomeUpgradeScroll());
        ItemMeta meta = item.getItemMeta();
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "가격: 코인" + manager.statManager.getPrice(EntityType.WOLF, level) + "개"));
        item.setItemMeta(meta);

        return item;
    }

    public void buyItem(Player player, ItemStack item, int price)
    {
        if(hasItems(player, manager.getCoin(1), price))
        {
            removeItems(player, manager.getCoin(1), price);
            player.getInventory().addItem(item);
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
                if(item.isSimilar(getZombieScrollButton()))
                {
                    buyItem(player, manager.getZombieSummonerScroll(), 0);
                }
                if(item.isSimilar(getSkeletonScrollButton()))
                {
                    buyItem(player, manager.getSkeletonSummonerScroll(), 0);
                }
                if(item.isSimilar(getSpiderScrollButton()))
                {
                    buyItem(player, manager.getSpiderSummonerScroll(), 0);
                }
                if(item.isSimilar(getZoglinScrollButton()))
                {
                    buyItem(player, manager.getZoglinSummonerScroll(), 0);
                }
                if(item.isSimilar(getWolfScrollButton()))
                {
                    buyItem(player, manager.getWolfSummonerScroll(), 0);
                }
                if(item.isSimilar(getZombifiedPiglinScrollButton()))
                {
                    buyItem(player, manager.getZombifiedPiglinSummonerScroll(), 0);
                }
                if(item.isSimilar(getIncomeScrollButton()))
                {
                    buyItem(player, manager.getIncomeSummonerScroll(), 0);
                }

                if(item.isSimilar(getZombieUpgradeButton()))
                {
                    buyItem(player, manager.getZombieUpgradeScroll(), 0);
                }
                if(item.isSimilar(getSkeletonUpgradeButton()))
                {
                    buyItem(player, manager.getSkeletonUpgradeScroll(), 0);
                }
                if(item.isSimilar(getSpiderUpgradeButton()))
                {
                    buyItem(player, manager.getSpiderUpgradeScroll(), 0);
                }
                if(item.isSimilar(getZoglinUpgradeButton()))
                {
                    buyItem(player, manager.getZoglinUpgradeScroll(), 0);
                }
                if(item.isSimilar(getWolfUpgradeButton()))
                {
                    buyItem(player, manager.getWolfUpgradeScroll(), 0);
                }
                if(item.isSimilar(getZombifiedPiglinUpgradeButton()))
                {
                    buyItem(player, manager.getZombifiedPiglinUpgradeScroll(), 0);
                }
                if(item.isSimilar(getIncomeUpgradeButton()))
                {
                    buyItem(player, manager.getIncomeUpgradeScroll(), 0);
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
