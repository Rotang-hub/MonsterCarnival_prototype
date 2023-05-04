package io.github.rotanghub.monstercarnival.manager;

import net.minecraft.world.entity.ai.navigation.NavigationAbstract;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.entity.monster.EntitySpider;
import net.minecraft.world.entity.monster.EntityZombie;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftSkeleton;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftSpider;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftZombie;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Manager
{
    Plugin plugin;
    DataConfigurationManager dataConfigurationManager;
    FileConfiguration dataConfig;
    public LocationManager locationManager = new LocationManager();
    MonsterStatManager statManager;
    PlayerStatManager playerStatManager;
    GameProcess gameProcess;

    public boolean process = false;

    public List<Player> redTeam = new ArrayList<>();
    public List<Player> blueTeam = new ArrayList<>();
    public List<Player> already = new ArrayList<>();
    public List<Entity> monster = new ArrayList<>();

    public Manager(Plugin plugin, DataConfigurationManager dataConfigurationManager, FileConfiguration dataConfig)
    {
        this.plugin = plugin;
        this.dataConfigurationManager = dataConfigurationManager;
        this.dataConfig = dataConfig;
        statManager = new MonsterStatManager(dataConfigurationManager, dataConfig);
        playerStatManager = new PlayerStatManager();
        gameProcess = new GameProcess(plugin, this);
    }

    public void start()
    {
        if(!process)
        {
            process = true;

            setRandomTeam();

            for(Player red : redTeam)
            {
                red.teleport(locationManager.getRedCenter());
                red.getInventory().clear();
            }

            for(Player blue : blueTeam)
            {
                blue.teleport(locationManager.getBlueCenter());
                blue.getInventory().clear();
            }

            gameProcess.redDeathCount = 10;
            gameProcess.blueDeathCount = 10;

            gameProcess.startCoinCycle();
        }
    }

    public void stop()
    {
        if(process)
        {
            process = false;

            already.clear();
            redTeam.clear();
            blueTeam.clear();

            for(Entity mob : monster) mob.remove();
            monster.clear();

            for(Player p : Bukkit.getOnlinePlayers())
            {
                p.getInventory().clear();
                p.teleport(p.getWorld().getSpawnLocation());
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            }

            gameProcess.redDeathCount = 10;
            gameProcess.blueDeathCount = 10;
            gameProcess.redMonsterCount = 0;
            gameProcess.blueMonsterCount = 0;
        }
    }

    public void addRedPlayer(Player player)
    {
        if(blueTeam.contains(player)) blueTeam.remove(player);
        redTeam.add(player);
        already.add(player);

        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ChatColor.WHITE + "님을 " + ChatColor.RED + "레드팀" + ChatColor.WHITE + "에 추가했습니다.");
    }

    public void addBluePlayer(Player player)
    {
        if(redTeam.contains(player)) redTeam.remove(player);
        blueTeam.add(player);
        already.add(player);

        Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ChatColor.WHITE + "님을 " + ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "에 추가했습니다.");
    }

    public boolean isRedTeam(Player player)
    {
        return redTeam.contains(player);
    }

    public boolean isBlueTeam(Player player)
    {
        return blueTeam.contains(player);
    }

    public void setRandomTeam()
    {
        List<Player> players = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers())
        {
            players.add(p);
        }

        Collections.shuffle(players);

        for(Player p : players)
        {
            if(already.contains(p)) continue;

            if((players.size() - players.indexOf(p)) % 2 == 0)    //짝수
            {
                redTeam.add(p);
            }
            else if((players.size() - players.indexOf(p)) % 2 == 1)
            {
                blueTeam.add(p);
            }
        }
    }

    public ItemStack getZombieSummonScroll(int level)
    {
        ItemStack scroll = new ItemStack(Material.GREEN_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "Lv." + level + " 좀비 소환서");
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭: "));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getSkeletonSummonScroll(int level)
    {
        ItemStack scroll = new ItemStack(Material.WHITE_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "Lv." + level + " 스켈레톤 소환서");
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭: "));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getSpiderSummonScroll(int level)
    {
        ItemStack scroll = new ItemStack(Material.BLACK_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "Lv." + level + " 거미 소환서");
        //meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭: "));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getCoin(int amount)
    {
        ItemStack coin = new ItemStack(Material.FLINT, amount);
        ItemMeta meta = coin.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + "코인");
        coin.setItemMeta(meta);

        return coin;
    }

    public void summonZombie(Location loc, int level, Location targetLoc)
    {
        Zombie zombie = (Zombie) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.ZOMBIE);
        zombie.setAdult();
        zombie.setCustomName(ChatColor.BOLD + "Lv" + level + " 좀비");
        zombie.setCustomNameVisible(true);
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.ZOMBIE, level));
        zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(statManager.getAttack(EntityType.ZOMBIE, level));
        zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.ZOMBIE, level));
        zombie.setHealth(statManager.getHealth(EntityType.ZOMBIE, level));
        zombie.setVisualFire(false);
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999 * 20, 1, true, false));

        monster.add(zombie);

        pathFinding(zombie, targetLoc, statManager.getSpeed(EntityType.ZOMBIE, level) * 3);
    }

    public void summonSkeleton(Location loc, int level, Location targetLoc)
    {
        Skeleton skeleton = (Skeleton) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.SKELETON);
        skeleton.setCustomName(ChatColor.BOLD + "Lv" + level + " 스켈레톤");
        skeleton.setCustomNameVisible(true);
        skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.SKELETON, level));
        skeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(statManager.getAttack(EntityType.SKELETON, level));
        skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.SKELETON, level));
        skeleton.setHealth(statManager.getHealth(EntityType.SKELETON, level));
        skeleton.setVisualFire(false);
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999 * 20, 1, true, false));

        monster.add(skeleton);

        pathFinding(skeleton, targetLoc, statManager.getSpeed(EntityType.SKELETON, level) * 3);
    }

    public void summonSpider(Location loc, int level, Location targetLoc)
    {
        Spider spider = (Spider) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.SPIDER);
        spider.setCustomName(ChatColor.BOLD + "Lv" + level + " 거미");
        spider.setCustomNameVisible(true);
        spider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.SPIDER, level));
        spider.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(statManager.getAttack(EntityType.SPIDER, level));
        spider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.SPIDER, level));
        spider.setHealth(statManager.getHealth(EntityType.SPIDER, level));
        spider.setVisualFire(false);
        spider.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999 * 20, 1, true, false));

        monster.add(spider);

        pathFinding(spider, targetLoc, statManager.getSpeed(EntityType.SPIDER, level) * 3);
    }

    public void pathFinding(Zombie zombie, Location targetLoc, double speed)
    {
        EntityZombie entityZombie = ((CraftZombie) zombie).getHandle();
        NavigationAbstract navigation = entityZombie.E();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Player player = getClosestPlayer(zombie);

                if(player == null) { navigation.a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), speed); }
                else if(isCollide(zombie, player, 3.5)) {    }
                else { navigation.a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), speed); }

                if(zombie.isDead())
                {
                    cancel();
                    return;
                }

                if(isCollide(zombie, targetLoc, 2.5))
                {
                    if(locationManager.isRedCorner(targetLoc))
                    {
                        cancel();
                        pathFinding(zombie, locationManager.getRedCenter(), speed);
                        return;
                    }
                    else if(locationManager.isBlueCorner(targetLoc))
                    {
                        cancel();
                        pathFinding(zombie, locationManager.getBlueCenter(), speed);
                        return;
                    }
                    else
                    {
                        zombie.remove();
                        if(targetLoc.equals(locationManager.getRedCenter()))
                        {
                            gameProcess.redMonsterCount++;
                            if(gameProcess.redMonsterCount >= 30) stop();
                        }
                        if(targetLoc.equals(locationManager.getBlueCenter()))
                        {
                            gameProcess.blueMonsterCount++;
                            if(gameProcess.blueMonsterCount >= 30) stop();
                        }
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void pathFinding(Skeleton skeleton, Location targetLoc, double speed)
    {
        EntitySkeleton entitySkeleton = ((CraftSkeleton) skeleton).getHandle();
        NavigationAbstract navigation = entitySkeleton.E();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Player player = getClosestPlayer(skeleton);

                if(player == null) { navigation.a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), speed); }
                else if(isCollide(skeleton, player, 3.5)) {    }
                else { navigation.a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), speed); }

                if(skeleton.isDead())
                {
                    cancel();
                    return;
                }

                if(skeleton.getLocation().subtract(0, 0.5, 0).getBlock().getType().equals(Material.OAK_PLANKS) || isCollide(skeleton, targetLoc, 2.5))
                {
                    if(locationManager.isRedCorner(targetLoc))
                    {
                        cancel();
                        pathFinding(skeleton, locationManager.getRedCenter(), speed);
                        return;
                    }
                    else if(locationManager.isBlueCorner(targetLoc))
                    {
                        cancel();
                        pathFinding(skeleton, locationManager.getBlueCenter(), speed);
                        return;
                    }
                    else
                    {
                        skeleton.remove();
                        if(targetLoc.equals(locationManager.getRedCenter()))
                        {
                            gameProcess.redMonsterCount++;
                            if(gameProcess.redMonsterCount >= 30) stop();
                        }
                        if(targetLoc.equals(locationManager.getBlueCenter()))
                        {
                            gameProcess.blueMonsterCount++;
                            if(gameProcess.blueMonsterCount >= 30) stop();
                        }
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void pathFinding(Spider spider, Location targetLoc, double speed)
    {
        EntitySpider entitySpider = ((CraftSpider) spider).getHandle();
        NavigationAbstract navigation = entitySpider.E();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Player player = getClosestPlayer(spider);

                if(player == null) { navigation.a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), speed); }
                else if(isCollide(spider, player, 3.5)) {    }
                else { navigation.a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), speed); }

                if(spider.isDead())
                {
                    cancel();
                    return;
                }

                if(spider.getLocation().subtract(0, 0.5, 0).getBlock().getType().equals(Material.OAK_PLANKS) || isCollide(spider, targetLoc, 2.5))
                {
                    if(locationManager.isRedCorner(targetLoc))
                    {
                        cancel();
                        pathFinding(spider, locationManager.getRedCenter(), speed);
                        return;
                    }
                    else if(locationManager.isBlueCorner(targetLoc))
                    {
                        cancel();
                        pathFinding(spider, locationManager.getBlueCenter(), speed);
                        return;
                    }
                    else
                    {
                        spider.remove();
                        if(targetLoc.equals(locationManager.getRedCenter()))
                        {
                            gameProcess.redMonsterCount++;
                            if(gameProcess.redMonsterCount >= 30) stop();
                        }
                        if(targetLoc.equals(locationManager.getBlueCenter()))
                        {
                            gameProcess.blueMonsterCount++;
                            if(gameProcess.blueMonsterCount >= 30) stop();
                        }
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public int getMonsterLevel(ItemStack egg)
    {
        String name = egg.getItemMeta().getDisplayName();
        String nameToInt = name.replaceAll("[^\\d]", "");
        int level = Integer.parseInt(nameToInt);
        return level;
    }

    public int getMonsterLevel(Entity entity)
    {
        String name = entity.getCustomName();
        String nameToInt = name.replaceAll("[^\\d]", "");
        int level = Integer.parseInt(nameToInt);
        return level;
    }

    public Location getRandomValueLoc(Location loc)
    {
        Random random = new Random();
        int value = random.nextInt(7) - 3;
        return loc.clone().add(0, 0, value);
    }

    public Player getClosestPlayer(Entity entity)
    {
        double maxDist = Double.MAX_VALUE;
        Player closest = null;

        for(Player player : entity.getWorld().getPlayers())
        {
            double dist = player.getLocation().distance(entity.getLocation());
            if(dist <= maxDist)
            {
                maxDist = dist;
                closest = player;
            }
        }

        return closest;
    }

    public boolean isCollide(Entity entity, Entity target, double dist)
    {
        return entity.getLocation().distance(target.getLocation()) <= dist;
    }

    public boolean isCollide(Entity entity, Location target, double dist)
    {
        return entity.getLocation().distance(target) <= dist;
    }
}
