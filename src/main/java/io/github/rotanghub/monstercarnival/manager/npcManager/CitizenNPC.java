package io.github.rotanghub.monstercarnival.manager.npcManager;

import io.github.rotanghub.monstercarnival.manager.Manager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CitizenNPC
{
    NPCLocationManager npcLocationManager;
    Manager manager;
    List<NPC> redFieldNPC = new ArrayList<>();
    List<NPC> blueFieldNPC = new ArrayList<>();

    public CitizenNPC(Manager manager)
    {
        this.manager = manager;
        npcLocationManager = new NPCLocationManager(manager);
        manager.plugin.getServer().getPluginManager().registerEvents(new NPCEvents(this, manager), manager.plugin);
    }

    public void clearNPC()
    {
        for(NPC npc : redFieldNPC)
        {
            CitizensAPI.getNPCRegistry().deregister(npc);
        }
        for(NPC npc : blueFieldNPC)
        {
            CitizensAPI.getNPCRegistry().deregister(npc);
        }
        redFieldNPC.clear();
        blueFieldNPC.clear();
    }

    public Location getAvailableRedField()
    {
        int length = redFieldNPC.size();
        if(length >= npcLocationManager.redNPCLocationList.size()) return null;
        return npcLocationManager.redNPCLocationList.get(length);
    }

    public Location getAvailableBlueField()
    {
        int length = blueFieldNPC.size();
        if(length >= npcLocationManager.blueNPCLocationList.size()) return null;
        return npcLocationManager.blueNPCLocationList.get(length);
    }

    public void spawnScrollNPC(Location loc)
    {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "dororong13_");
        npc.setName("스크롤 상점");
        npc.spawn(loc);
        npc.setUseMinecraftAI(true);
    }

    public void spawnUpgradeNPC(Location loc)
    {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "GoldGrass");
        npc.setName("강화 상점");
        npc.spawn(loc);
        npc.setUseMinecraftAI(true);
    }

    public void spawnZombieNPC(Location loc, String team)
    {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.ZOMBIE, "fullwall");
        npc.setName("좀비 소환사 lv1");
        npc.spawn(loc);

        if(team == "red")
        {
            redFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( -1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        if(team == "blue")
        {
            blueFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( 1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        spawnCycle(npc, EntityType.ZOMBIE, team);
    }

    public void spawnSkeletonNPC(Location loc, String team)
    {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.SKELETON, "fullwall");
        npc.setName("스켈레톤 소환사 lv1");
        npc.spawn(loc);

        if(team == "red")
        {
            redFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( -1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        if(team == "blue")
        {
            blueFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( 1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        spawnCycle(npc, EntityType.SKELETON, team);
    }

    public void spawnSpiderNPC(Location loc, String team)
    {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.SPIDER, "fullwall");
        npc.setName("거미 소환사 lv1");
        npc.spawn(loc);

        if(team == "red")
        {
            redFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( -1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        if(team == "blue")
        {
            blueFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( 1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        spawnCycle(npc, EntityType.SPIDER, team);
    }

    public void spawnZoglinNPC(Location loc, String team)
    {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.ZOGLIN, "fullwall");
        npc.setName("조글린 소환사 lv1");
        npc.spawn(loc);

        if(team == "red")
        {
            redFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( -1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        if(team == "blue")
        {
            blueFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( 1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        spawnCycle(npc, EntityType.ZOGLIN, team);
    }

    public void spawnWolfNPC(Location loc, String team)
    {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.WOLF, "fullwall");
        npc.setName("늑대 소환사 lv1");
        npc.spawn(loc);

        if(team == "red")
        {
            redFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( -1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        if(team == "blue")
        {
            blueFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( 1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        spawnCycle(npc, EntityType.WOLF, team);
    }

    public void spawnZombifiedPiglinNPC(Location loc, String team)
    {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.ZOMBIFIED_PIGLIN, "fullwall");
        npc.setName("좀비피글린 소환사 lv1");
        npc.spawn(loc);

        if(team == "red")
        {
            redFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( -1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        if(team == "blue")
        {
            blueFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( 1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        spawnCycle(npc, EntityType.ZOMBIFIED_PIGLIN, team);
    }

    public void spawnIncomeNPC(Location loc, String team, String name)
    {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "fullwall");
        npc.setName("인컴 lv1");
        npc.getOrAddTrait(SkinTrait.class).setSkinName(name);
        npc.spawn(loc);

        if(team == "red")
        {
            redFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( -1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        if(team == "blue")
        {
            blueFieldNPC.add(npc);
            Location dir = npc.getEntity().getLocation().setDirection(new Vector( 1, 0, 0));
            npc.teleport(dir, PlayerTeleportEvent.TeleportCause.COMMAND);
        }

        incomeSpawnCycle(npc, team);
    }

    public NPC getLowLevelNPC(List<NPC> list, String name)
    {
        int level = 3;
        NPC npc = null;

        for(NPC n : list)
        {
            if(n.getName().contains(name) && getLevel(n.getName()) < level)
            {
                npc = n;
                level = getLevel(npc.getName());
            }
        }

        return npc;
    }

    public NPC getLowLevelIncomeNPC(List<NPC> list)
    {
        int level = 6;
        NPC npc = null;

        for(NPC n : list)
        {
            if(n.getName().contains("인컴") && getLevel(n.getName()) < level)
            {
                npc = n;
                level = getLevel(npc.getName());
            }
        }

        return npc;
    }

    public NPC upgradeZombieNPC(String team)
    {
        NPC npc = null;

        if(team == "red")
        {
            npc = getLowLevelNPC(redFieldNPC, "좀비");
        }
        if(team == "blue")
        {
            npc = getLowLevelNPC(blueFieldNPC, "좀비");
        }

        if(npc == null) return npc;

        int level = getLevel(npc.getName()) + 1;
        npc.setName("좀비 소환사 lv" + level);

        return npc;
    }

    public NPC upgradeSkeletonNPC(String team)
    {
        NPC npc = null;

        if(team == "red")
        {
            npc = getLowLevelNPC(redFieldNPC, "스켈레톤");
        }
        if(team == "blue")
        {
            npc = getLowLevelNPC(blueFieldNPC, "스켈레톤");
        }

        if(npc == null) return npc;

        int level = getLevel(npc.getName()) + 1;
        npc.setName("스켈레톤 소환사 lv" + level);

        return npc;
    }

    public NPC upgradeSpiderNPC(String team)
    {
        NPC npc = null;

        if(team == "red")
        {
            npc = getLowLevelNPC(redFieldNPC, "거미");
        }
        if(team == "blue")
        {
            npc = getLowLevelNPC(blueFieldNPC, "거미");
        }

        if(npc == null) return npc;

        int level = getLevel(npc.getName()) + 1;
        npc.setName("거미 소환사 lv" + level);

        return npc;
    }

    public NPC upgradeZoglinNPC(String team)
    {
        NPC npc = null;

        if(team == "red")
        {
            npc = getLowLevelNPC(redFieldNPC, "조글린");
        }
        if(team == "blue")
        {
            npc = getLowLevelNPC(blueFieldNPC, "조글린");
        }

        if(npc == null) return npc;

        int level = getLevel(npc.getName()) + 1;
        npc.setName("조글린 소환사 lv" + level);

        return npc;
    }

    public NPC upgradeWolfNPC(String team)
    {
        NPC npc = null;

        if(team == "red")
        {
            npc = getLowLevelNPC(redFieldNPC, "늑대");
        }
        if(team == "blue")
        {
            npc = getLowLevelNPC(blueFieldNPC, "늑대");
        }

        if(npc == null) return npc;

        int level = getLevel(npc.getName()) + 1;
        npc.setName("늑대 소환사 lv" + level);

        return npc;
    }

    public NPC upgradeZombifiedPiglinNPC(String team)
    {
        NPC npc = null;

        if(team == "red")
        {
            npc = getLowLevelNPC(redFieldNPC, "좀비피글린");
        }
        if(team == "blue")
        {
            npc = getLowLevelNPC(blueFieldNPC, "좀비피글린");
        }

        if(npc == null) return npc;

        int level = getLevel(npc.getName()) + 1;
        npc.setName("좀비피글린 소환사 lv" + level);

        return npc;
    }

    public NPC upgradeIncomeNPC(String team)
    {
        NPC npc = null;

        if(team == "red")
        {
            npc = getLowLevelIncomeNPC(redFieldNPC);
        }
        if(team == "blue")
        {
            npc = getLowLevelIncomeNPC(blueFieldNPC);
        }

        if(npc == null) return npc;

        int level = getLevel(npc.getName()) + 1;
        npc.setName("인컴 lv" + level);

        return npc;
    }


    public int getLevel(String name)
    {
        String nameToInt = name.replaceAll("[^\\d]", "");
        if(nameToInt == "") return 0;
        return Integer.parseInt(nameToInt);
    }

    public void spawnCycle(NPC npc, EntityType type, String team)
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

                Location spawnLoc = null;
                Location targetLoc = null;

                if(team == "red")
                {
                    Location[] spawntarget = npcLocationManager.getBlueSpawnTarget();
                    spawnLoc = spawntarget[0];
                    targetLoc = spawntarget[1];
                }
                if(team == "blue")
                {
                    Location[] spawntarget = npcLocationManager.getRedSpawnTarget();
                    spawnLoc = spawntarget[0];
                    targetLoc = spawntarget[1];
                }

                if(type.equals(EntityType.ZOMBIE)) manager.summonZombie(spawnLoc, getLevel(npc.getName()), targetLoc);
                if(type.equals(EntityType.SKELETON)) manager.summonSkeleton(spawnLoc, getLevel(npc.getName()), targetLoc);
                if(type.equals(EntityType.SPIDER)) manager.summonSpider(spawnLoc, getLevel(npc.getName()), targetLoc);
                if(type.equals(EntityType.ZOGLIN)) manager.summonZoglin(spawnLoc, getLevel(npc.getName()), targetLoc);
                if(type.equals(EntityType.WOLF)) manager.summonWolf(spawnLoc, getLevel(npc.getName()), targetLoc);
                if(type.equals(EntityType.ZOMBIFIED_PIGLIN)) manager.summonZombifiedPiglin(spawnLoc, getLevel(npc.getName()), targetLoc);
            }
        }.runTaskTimer(manager.plugin, 20, 20 * 5); //5초마다
    }

    public void incomeSpawnCycle(NPC npc, String team)
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

                Location spawnLoc = null;
                Location targetLoc = null;

                if(team == "red")
                {
                    Location[] spawntarget = npcLocationManager.getRedSpawnTarget();
                    spawnLoc = spawntarget[0];
                    targetLoc = spawntarget[1];
                }
                if(team == "blue")
                {
                    Location[] spawntarget = npcLocationManager.getBlueSpawnTarget();
                    spawnLoc = spawntarget[0];
                    targetLoc = spawntarget[1];
                }

                manager.summonIncome(spawnLoc, getLevel(npc.getName()), targetLoc);
            }
        }.runTaskTimer(manager.plugin, 20, 20 * 5);
    }

    public boolean hasNPC(List<NPC> list, String name)
    {
        for(NPC npc : list)
        {
            if(npc.getName().contains(name)) return true;
        }
        return false;
    }

    public void glowingNPC(NPC npc)
    {
        npc.getEntity().getWorld().playSound(npc.getEntity().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 2);
        npc.getEntity().setGlowing(true);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                npc.getEntity().setGlowing(false);
            }
        }.runTaskLater(manager.plugin, 5);
    }
}
