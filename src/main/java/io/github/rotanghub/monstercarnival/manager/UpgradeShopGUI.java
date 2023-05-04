package io.github.rotanghub.monstercarnival.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class UpgradeShopGUI implements Listener
{
    Manager manager;
    Inventory menuTab;
    private Player player;

    public UpgradeShopGUI(Manager manager, Player player)
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
        menuTab.setItem(19, getSwordUpgradeButton());
        menuTab.setItem(21, getHpUpgradeButton());
        menuTab.setItem(23, getPotion1Button());
        menuTab.setItem(25, getPotion2Button());
    }

    public ItemStack getSwordUpgradeButton()
    {
        ItemStack item = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "검 공격력 강화");
        meta.setLore(Arrays.asList(ChatColor.RESET + "보유중인 무기의 공격력을 1 증가시킵니다.", ChatColor.RESET + "가격: 코인5개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getHpUpgradeButton()
    {
        ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "체력 강화");
        meta.setLore(Arrays.asList(ChatColor.RESET + "자신의 체력을 2 증가시킵니다.", ChatColor.RESET + "가격: 코인5개"));
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getPotion1Button()
    {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        PotionEffect regeneration = PotionEffectType.REGENERATION.createEffect(45 * 20, 1);
        meta.addCustomEffect(regeneration, true);
        potion.setItemMeta(meta);

        return potion;
    }

    public ItemStack getPotion2Button()
    {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        PotionEffect regeneration = PotionEffectType.REGENERATION.createEffect(90 * 20, 1);
        meta.addCustomEffect(regeneration, true);
        potion.setItemMeta(meta);

        return potion;
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
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();

            if(item == null || item.getType().equals(Material.AIR)) return;

            if(item.isSimilar(getSwordUpgradeButton()))
            {
                manager.playerStatManager.addSwordDamage(player, 1);
            }
            if(item.isSimilar(getHpUpgradeButton()))
            {
                manager.playerStatManager.addPlayerMaxHealth(player, 2);
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
