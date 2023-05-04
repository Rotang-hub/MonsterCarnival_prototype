package io.github.rotanghub.monstercarnival.manager;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener
{
    Manager manager;

    public Events(Manager manager)
    {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if(manager.process && event.getHand().equals(EquipmentSlot.HAND))
        {
            Player player = event.getPlayer();
            Action action = event.getAction();
            ItemStack item = player.getInventory().getItemInMainHand();

            if(item.getType().equals(manager.getZombieSummonScroll(1).getType()))
            {
                if(manager.isRedTeam(player))
                {
                    if(action.toString().contains("LEFT"))    //south
                        manager.summonZombie(manager.locationManager.getSouthDoor().clone().add(-2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getBlueSouthCorner());
                    if(action.toString().contains("RIGHT"))   //north
                        manager.summonZombie(manager.locationManager.getNorthDoor().clone().add(-2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getBlueNorthCorner());
                }
                if(manager.isBlueTeam(player))
                {
                    if(action.toString().contains("LEFT"))    //north
                        manager.summonZombie(manager.locationManager.getNorthDoor().clone().add(2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getRedNorthCorner());
                    if(action.toString().contains("RIGHT"))   //south
                        manager.summonZombie(manager.locationManager.getSouthDoor().clone().add(2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getRedSouthCorner());
                }
                item.setAmount(item.getAmount() - 1);
            }
            if(item.getType().equals(manager.getSkeletonSummonScroll(1).getType()))
            {
                if(manager.isRedTeam(player))
                {
                    if(action.toString().contains("LEFT"))    //south
                        manager.summonSkeleton(manager.locationManager.getSouthDoor().clone().add(-2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getBlueSouthCorner());
                    if(action.toString().contains("RIGHT"))   //north
                        manager.summonSkeleton(manager.locationManager.getNorthDoor().clone().add(-2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getBlueNorthCorner());
                }
                if(manager.isBlueTeam(player))
                {
                    if(action.toString().contains("LEFT"))    //north
                        manager.summonSkeleton(manager.locationManager.getNorthDoor().clone().add(2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getRedNorthCorner());
                    if(action.toString().contains("RIGHT"))   //south
                        manager.summonSkeleton(manager.locationManager.getSouthDoor().clone().add(2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getRedSouthCorner());
                }
                item.setAmount(item.getAmount() - 1);
            }
            if(item.getType().equals(manager.getSpiderSummonScroll(1).getType()))
            {
                if(manager.isRedTeam(player))
                {
                    if(action.toString().contains("LEFT"))    //south
                        manager.summonSpider(manager.locationManager.getSouthDoor().clone().add(-2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getBlueSouthCorner());
                    if(action.toString().contains("RIGHT"))   //north
                        manager.summonSpider(manager.locationManager.getNorthDoor().clone().add(-2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getBlueNorthCorner());
                }
                if(manager.isBlueTeam(player))
                {
                    if(action.toString().contains("LEFT"))    //north
                        manager.summonSpider(manager.locationManager.getNorthDoor().clone().add(2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getRedNorthCorner());
                    if(action.toString().contains("RIGHT"))   //south
                        manager.summonSpider(manager.locationManager.getSouthDoor().clone().add(2, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getRedSouthCorner());
                }
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onPlayerItemSwap(PlayerSwapHandItemsEvent event)
    {
        if(manager.process)
        {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();

            if(item.getType().equals(manager.getZombieSummonScroll(1).getType()))
            {
                event.setCancelled(true);
                if(manager.isRedTeam(player))
                    manager.summonZombie(manager.locationManager.getCenterDoor().clone().add(-2, 1, 0),
                            manager.getMonsterLevel(item), manager.locationManager.getRedCenter());
                if(manager.isBlueTeam(player))
                    manager.summonZombie(manager.locationManager.getCenterDoor().clone().add(2, 1, 0),
                            manager.getMonsterLevel(item), manager.locationManager.getBlueCenter());

                item.setAmount(item.getAmount() - 1);
            }
            if(item.getType().equals(manager.getSkeletonSummonScroll(1).getType()))
            {
                event.setCancelled(true);
                if(manager.isRedTeam(player))
                    manager.summonSkeleton(manager.locationManager.getCenterDoor().clone().add(-2, 1, 0),
                            manager.getMonsterLevel(item), manager.locationManager.getRedCenter());
                if(manager.isBlueTeam(player))
                    manager.summonSkeleton(manager.locationManager.getCenterDoor().clone().add(2, 1, 0),
                            manager.getMonsterLevel(item), manager.locationManager.getBlueCenter());

                item.setAmount(item.getAmount() - 1);
            }
            if(item.getType().equals(manager.getSpiderSummonScroll(1).getType()))
            {
                event.setCancelled(true);
                if(manager.isRedTeam(player))
                    manager.summonSpider(manager.locationManager.getCenterDoor().clone().add(-2, 1, 0),
                            manager.getMonsterLevel(item), manager.locationManager.getRedCenter());
                if(manager.isBlueTeam(player))
                    manager.summonSpider(manager.locationManager.getCenterDoor().clone().add(2, 1, 0),
                            manager.getMonsterLevel(item), manager.locationManager.getBlueCenter());

                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        if(manager.process && event.getHand().equals(EquipmentSlot.HAND))
        {
            if(event.getRightClicked().getCustomName().equalsIgnoreCase("강화 상점"))
            {
                Player player = event.getPlayer();
                UpgradeShopGUI upgradeShopGUI = new UpgradeShopGUI(manager, player);
                manager.plugin.getServer().getPluginManager().registerEvents(upgradeShopGUI, manager.plugin);
                player.openInventory(upgradeShopGUI.getMenuTab());
            }
            if(event.getRightClicked().getCustomName().equalsIgnoreCase("소환서 상점"))
            {
                Player player = event.getPlayer();
                UpgradeShopGUI upgradeShopGUI = new UpgradeShopGUI(manager, player);
                manager.plugin.getServer().getPluginManager().registerEvents(upgradeShopGUI, manager.plugin);
                player.openInventory(upgradeShopGUI.getMenuTab());
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        LivingEntity entity = event.getEntity();
        if(manager.monster.contains(entity))
        {
            for(ItemStack item : event.getDrops()) item.setType(Material.AIR);

            int drop = manager.statManager.getDrop(entity.getType(), manager.getMonsterLevel(entity));
            Item coin = entity.getWorld().dropItemNaturally(entity.getLocation(), manager.getCoin(drop));
            if(entity.getKiller() != null)
                coin.setOwner(entity.getKiller().getUniqueId());

            manager.monster.remove(entity);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if(manager.process)
        {
            Player player = event.getPlayer();
            if(manager.isRedTeam(player)) event.setRespawnLocation(manager.locationManager.getRedCenter());
            if(manager.isBlueTeam(player)) event.setRespawnLocation(manager.locationManager.getBlueCenter());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if(manager.process)
        {
            Player player = event.getEntity();

            if(manager.isRedTeam(player)) manager.gameProcess.redDeathCount--;
            if(manager.isBlueTeam(player)) manager.gameProcess.blueDeathCount--;

            if(manager.gameProcess.redDeathCount <= 0 || manager.gameProcess.blueDeathCount <= 0)
            {
                manager.stop();
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event)
    {
        if(manager.process)
        {
            if(event.getHitEntity() != null)
            {
                if(event.getEntity() instanceof Arrow)
                {
                    Arrow arrow = (Arrow) event.getEntity();
                    Entity shooter = (Entity) arrow.getShooter();

                    if(manager.monster.contains(shooter))
                    {
                        int level = manager.getMonsterLevel(shooter);
                        int damage = manager.statManager.getAttack(EntityType.SKELETON, level);
                        arrow.setDamage(damage);
                    }
                }
            }
        }
    }
}
