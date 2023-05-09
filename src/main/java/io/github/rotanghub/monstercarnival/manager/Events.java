package io.github.rotanghub.monstercarnival.manager;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.events.MythicDamageEvent;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import net.citizensnpcs.api.event.NPCClickEvent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

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

            if(item == null || item.getType().isAir()) return;

            if(item.getType().equals(manager.getBundle().getType()))
            {
                if(action.toString().contains("RIGHT"))
                {
                    PlayerStatManager sm =  manager.playerStatManager;
                    int coinLimit = sm.getCoinLimit(player);
                    player.sendMessage("주머니를 사용하여 코인의 소지한도가 증가했습니다. (" + coinLimit + " -> " + (coinLimit + 32) + ")");
                    sm.setCoinLimit(player, coinLimit + 32);

                    player.playSound(player, Sound.ITEM_BUNDLE_DROP_CONTENTS, 5, 1);
                    item.setAmount(item.getAmount() - 1);
                }
            }

            /*
            if(item.getType().equals(manager.getZombieSummonerScroll().getType()))    //좀비
            {
                if(manager.isRedTeam(player))
                {
                    if(action.toString().contains("LEFT"))    //south
                        manager.summonZombie(manager.locationManager.getSouthDoor().clone().add(-3, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getBlueSouthCorner());
                    if(action.toString().contains("RIGHT"))   //north
                        manager.summonZombie(manager.locationManager.getNorthDoor().clone().add(-3, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getBlueNorthCorner());
                }
                if(manager.isBlueTeam(player))
                {
                    if(action.toString().contains("LEFT"))    //north
                        manager.summonZombie(manager.locationManager.getNorthDoor().clone().add(3, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getRedNorthCorner());
                    if(action.toString().contains("RIGHT"))   //south
                        manager.summonZombie(manager.locationManager.getSouthDoor().clone().add(3, 1, 0),
                                manager.getMonsterLevel(item), manager.locationManager.getRedSouthCorner());
                }
                item.setAmount(item.getAmount() - 1);
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
            }
             */
        }
    }

    @EventHandler
    public void onPlayerItemSwap(PlayerSwapHandItemsEvent event)
    {
        if(manager.process)
        {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();

            /*
            if(item.getType().equals(manager.getZombieSummonerScroll().getType()))
            {
                event.setCancelled(true);
                if(manager.isRedTeam(player))
                    manager.summonZombie(manager.locationManager.getCenterDoor().clone().add(-3, 1, 0),
                            manager.getMonsterLevel(item), manager.locationManager.getBlueCenter());
                if(manager.isBlueTeam(player))
                    manager.summonZombie(manager.locationManager.getCenterDoor().clone().add(3, 1, 0),
                            manager.getMonsterLevel(item), manager.locationManager.getRedCenter());

                item.setAmount(item.getAmount() - 1);
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 5, 1);
            }
             */
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        if(event.getHand().equals(EquipmentSlot.HAND))
        {
            if(event.getRightClicked() instanceof EnderCrystal) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if(manager.process)
        {
            if(event.getEntity() instanceof EnderCrystal)
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if(manager.process)
        {
            if(event.getDamager() instanceof Firework) event.setCancelled(true);

            if(event.getDamager() instanceof Player && event.getEntity() instanceof Player)
            {
                Player damager = (Player) event.getDamager();
                Player player = (Player) event.getEntity();

                if(manager.isSameTeam(damager, player))
                {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event)
    {
        if(manager.process)
        {
            Entity entity = event.getEntity();
            Entity target = event.getTarget();

            if(manager.monster.contains(entity) && !(target instanceof Player))
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        if(manager.process)
        {
            LivingEntity entity = event.getEntity();

            if(manager.monster.contains(entity))
            {
                for(ItemStack item : event.getDrops()) item.setType(Material.AIR);

                int drop = manager.statManager.getDrop(entity.getType(), manager.getMonsterLevel(entity));
                if(drop == 0) return;

                Item coin = entity.getWorld().dropItemNaturally(entity.getLocation(), manager.getCoin(drop));
                if(entity.getKiller() != null)
                    coin.setOwner(entity.getKiller().getUniqueId());

                manager.monster.remove(entity);
                manager.dropBundle(entity.getLocation());
            }
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

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event)
    {
        if(manager.process)
        {
            if(event.getEntity() instanceof Player)
            {
                Player player = (Player) event.getEntity();
                ItemStack item = event.getItem().getItemStack();

                if(item.getType().equals(manager.getCoin(1).getType()))
                {
                    int pick = item.getAmount();
                    int amount = manager.playerStatManager.getCoinAmount(player);
                    int limit = manager.playerStatManager.getCoinLimit(player);

                    if(pick + amount > limit)   //20, 50, 64
                    {
                        event.setCancelled(true);
                        event.getItem().remove();
                        int result = pick + amount - limit;
                        ItemStack coin = manager.getCoin(result);
                        player.getWorld().dropItem(player.getLocation(), coin);
                        player.getInventory().addItem(manager.getCoin(limit - amount));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if(manager.process)
        {
            Player player = event.getPlayer();

            event.setCancelled(true);

            if(!manager.teamChat)
            {
                if(manager.isRedTeam(player)) event.setMessage(ChatColor.RED + "[레드팀] " + player.getName() + ChatColor.WHITE + " : " + event.getMessage());
                if(manager.isBlueTeam(player)) event.setMessage(ChatColor.BLUE + "[블루팀] " + player.getName() + ChatColor.WHITE + " : " + event.getMessage());
            }

            if(manager.teamChat)
            {
                if(manager.isRedTeam(player))
                {
                    for(Player red : manager.redTeam)
                    {
                        red.sendMessage(ChatColor.RED + "[레드팀] " + player.getName() + ChatColor.WHITE + " : " + event.getMessage());
                    }
                }
                if(manager.isBlueTeam(player))
                {
                    for(Player blue : manager.blueTeam)
                    {
                        blue.sendMessage(ChatColor.BLUE + "[블루팀] " + player.getName() + ChatColor.WHITE + " : " + event.getMessage());
                    }
                }
                player.getServer().getConsoleSender().sendMessage(player.getName() + " : " + event.getMessage());
            }

        }
    }

    @EventHandler
    public void onMythicMobDeath(MythicMobDeathEvent event)
    {
        ActiveMob mob = event.getMob();
        Entity entity = mob.getEntity().getBukkitEntity();

        if(mob.getName().equals("네더 좀비") || mob.getDisplayName().equals("네더 좀비"))
        {
            entity.getWorld().dropItemNaturally(entity.getLocation(), manager.getCoin(23));
        }
        if(mob.getName().equals("광부 좀비") || mob.getDisplayName().equals("광부 좀비"))
        {
            entity.getWorld().dropItemNaturally(entity.getLocation(), manager.getCoin(23));
        }
    }
}
