package io.github.rotanghub.monstercarnival.manager;

import io.github.rotanghub.monstercarnival.manager.npcManager.CitizenNPC;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.placeholders.parsers.StaticDouble;
import net.minecraft.world.entity.ai.navigation.NavigationAbstract;
import net.minecraft.world.entity.animal.EntityCow;
import net.minecraft.world.entity.animal.EntitySheep;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.entity.monster.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_19_R2.entity.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Manager
{
    public Plugin plugin;
    DataConfigurationManager dataConfigurationManager;
    FileConfiguration dataConfig;
    public LocationManager locationManager = new LocationManager();
    public CitizenNPC citizenNPC;
    MonsterStatManager statManager;
    PlayerStatManager playerStatManager;
    public GameProcess gameProcess;

    public boolean process = false;

    public boolean teamChat = false;

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
        playerStatManager = new PlayerStatManager(this);
        gameProcess = new GameProcess(plugin, this);
        citizenNPC = new CitizenNPC(this);
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
                red.getInventory().addItem(getWoodenSword());
                red.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 9999 * 20, 0, true, false));
                red.playSound(red, Sound.ENTITY_ENDER_DRAGON_GROWL, 5, 1);
                playerStatManager.setCoinLimit(red, 64);
            }

            for(Player blue : blueTeam)
            {
                blue.teleport(locationManager.getBlueCenter());
                blue.getInventory().clear();
                blue.getInventory().addItem(getWoodenSword());
                blue.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 9999 * 20, 0, true, false));
                blue.playSound(blue, Sound.ENTITY_ENDER_DRAGON_GROWL, 5, 1);
                playerStatManager.setCoinLimit(blue, 64);
            }

            gameProcess.redDeathCount = gameProcess.maxDeathCount;
            gameProcess.blueDeathCount = gameProcess.maxDeathCount;

            //gameProcess.startCoinCycle();
            //gameProcess.startCowSpawnCycle();
            gameProcess.sendActionBarScore();

            if(redTeam.isEmpty()) citizenNPC.spawnIncomeNPC(citizenNPC.getAvailableRedField(), "red", "steve");
            else citizenNPC.spawnIncomeNPC(citizenNPC.getAvailableRedField(), "red", redTeam.get(0).getName());
            if(blueTeam.isEmpty()) citizenNPC.spawnIncomeNPC(citizenNPC.getAvailableBlueField(), "blue", "steve");
            else citizenNPC.spawnIncomeNPC(citizenNPC.getAvailableBlueField(), "blue", blueTeam.get(0).getName());
        }
    }

    public void stop()
    {
        if(process)
        {
            process = false;

            sendGameResult(gameProcess.redDeathCount, gameProcess.blueDeathCount, gameProcess.redMonsterCount, gameProcess.blueMonsterCount);

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
                p.removePotionEffect(PotionEffectType.SATURATION);
                playerStatManager.setCoinLimit(p, 64);
            }

            gameProcess.redDeathCount = 10;
            gameProcess.blueDeathCount = 10;
            gameProcess.redMonsterCount = 0;
            gameProcess.blueMonsterCount = 0;

            citizenNPC.clearNPC();
            for(Entity entity : Bukkit.getServer().getWorld("world").getEntities())
            {
                if(entity instanceof Item) entity.remove();
            }
        }
    }

    public void sendGameResult(int redDeath, int blueDeath, int redMonster, int blueMonster)
    {
        String redTitle = "";
        String blueTitle = "";
        String subtitle= "";
        Sound redSound = null;
        Sound blueSound = null;
        int redPitch = 1;
        int bluePitch = 1;

        if(redDeath == 0)
        {
            redTitle = ChatColor.RED + "패배";
            blueTitle = ChatColor.GOLD + "승리";
            subtitle = "레드팀의 데스카운트가 0이 되었습니다.";
            redSound = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE;
            blueSound = Sound.UI_TOAST_CHALLENGE_COMPLETE;
            redPitch = 1;
            bluePitch = 2;
        }
        else if(blueDeath == 0)
        {
            redTitle = ChatColor.GOLD + "승리";
            blueTitle = ChatColor.BLUE + "패배";
            subtitle = "블루팀의 데스카운트가 0이 되었습니다.";
            redSound = Sound.UI_TOAST_CHALLENGE_COMPLETE;
            blueSound = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE;
            redPitch = 2;
            bluePitch = 1;
        }
        else if(redMonster == gameProcess.maxCount)
        {
            redTitle = ChatColor.GOLD + "승리";
            blueTitle = ChatColor.BLUE + "패배";
            subtitle = "레드팀이 " + gameProcess.maxCount + "점을 달성했습니다";
            redSound = Sound.UI_TOAST_CHALLENGE_COMPLETE;
            blueSound = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE;
            redPitch = 2;
            bluePitch = 1;
        }
        else if(blueMonster == gameProcess.maxCount)
        {
            redTitle = ChatColor.RED + "패배";
            blueTitle = ChatColor.GOLD + "승리";
            subtitle = "블루팀이 " + gameProcess.maxCount + "점을 달성했습니다";
            redSound = Sound.ENTITY_DRAGON_FIREBALL_EXPLODE;
            blueSound = Sound.UI_TOAST_CHALLENGE_COMPLETE;
            redPitch = 1;
            bluePitch = 2;
        }
        else
        {
            plugin.getServer().broadcastMessage("게임종료");
            return;
        }

        for(Player p : redTeam)
        {
            p.sendTitle(redTitle, subtitle, 5, 80, 20);
            p.playSound(p, redSound, 5, redPitch);
        }
        for(Player p : blueTeam)
        {
            p.sendTitle(blueTitle, subtitle, 5, 80, 20);
            p.playSound(p, blueSound, 5, bluePitch);
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

    public ItemStack getWoodenSword()
    {
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setUnbreakable(true);

        AttributeModifier attackModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage",
                5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attackModifier);

        AttributeModifier speedModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed",
                -1.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedModifier);

        sword.setItemMeta(meta);

        return sword;
    }

    public ItemStack getCoin(int amount)
    {
        ItemStack coin = new ItemStack(Material.FLINT, amount);
        ItemMeta meta = coin.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + "코인");
        coin.setItemMeta(meta);

        return coin;
    }

    public ItemStack getBundle()
    {
        ItemStack bundle = new ItemStack(Material.BUNDLE);
        BundleMeta meta = (BundleMeta) bundle.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + "주머니");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "우클릭 시 소지 가능한 코인의 수를 32개 늘립니다."));
        bundle.setItemMeta(meta);

        return bundle;
    }

    public void openScrollShop(Player player)
    {
        ScrollShopGUI scrollShopGUI = new ScrollShopGUI(this, player);
        plugin.getServer().getPluginManager().registerEvents(scrollShopGUI, plugin);
        player.openInventory(scrollShopGUI.getMenuTab());
    }

    public void openUpgradeShop(Player player)
    {
        UpgradeShopGUI upgradeShopGUI = new UpgradeShopGUI(this, player);
        plugin.getServer().getPluginManager().registerEvents(upgradeShopGUI, plugin);
        player.openInventory(upgradeShopGUI.getMenuTab());
    }

    public ItemStack getZombieSummonerScroll()
    {
        ItemStack scroll = new ItemStack(Material.GREEN_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "좀비 소환사 고용서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 좀비 소환사를 고용합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getSkeletonSummonerScroll()
    {
        ItemStack scroll = new ItemStack(Material.WHITE_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "스켈레톤 소환사 고용서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 스켈레톤 소환사를 고용합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getSpiderSummonerScroll()
    {
        ItemStack scroll = new ItemStack(Material.BLACK_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "거미 소환사 고용서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 거미 소환사를 고용합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getZoglinSummonerScroll()
    {
        ItemStack scroll = new ItemStack(Material.PINK_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "조글린 소환사 고용서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 조글린 소환사를 고용합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getWolfSummonerScroll()
    {
        ItemStack scroll = new ItemStack(Material.GRAY_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "늑대 소환사 고용서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 늑대 소환사를 고용합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getZombifiedPiglinSummonerScroll()
    {
        ItemStack scroll = new ItemStack(Material.ORANGE_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "좀비피글린 소환사 고용서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 좀비피글린 소환사를 고용합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getIncomeSummonerScroll()
    {
        ItemStack scroll = new ItemStack(Material.LIGHT_BLUE_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "인컴 고용서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 인컴을 고용합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getZombieUpgradeScroll()
    {
        ItemStack scroll = new ItemStack(Material.GREEN_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "좀비 소환사 강화서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 고용된 좀비 소환사 중",
                ChatColor.WHITE + "가장 낮은 레벨의 소환사를 강화합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getSkeletonUpgradeScroll()
    {
        ItemStack scroll = new ItemStack(Material.WHITE_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "스켈레톤 소환사 강화서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 고용된 스켈레톤 소환사 중",
                ChatColor.WHITE + "가장 낮은 레벨의 소환사를 강화합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getSpiderUpgradeScroll()
    {
        ItemStack scroll = new ItemStack(Material.BLACK_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "거미 소환사 강화서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 고용된 거미 소환사 중",
                ChatColor.WHITE + "가장 낮은 레벨의 소환사를 강화합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getZoglinUpgradeScroll()
    {
        ItemStack scroll = new ItemStack(Material.PINK_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "조글린 소환사 강화서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 고용된 조글린 소환사 중",
                ChatColor.WHITE + "가장 낮은 레벨의 소환사를 강화합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getWolfUpgradeScroll()
    {
        ItemStack scroll = new ItemStack(Material.GRAY_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "늑대 소환사 강화서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 늑대 소환사 중",
                ChatColor.WHITE + "가장 낮은 레벨의 소환사를 강화합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getZombifiedPiglinUpgradeScroll()
    {
        ItemStack scroll = new ItemStack(Material.ORANGE_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "좀비피글린 소환사 강화서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 고용된 좀비피글린 소환사 중",
                ChatColor.WHITE + "가장 낮은 레벨의 소환사를 강화합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getIncomeUpgradeScroll()
    {
        ItemStack scroll = new ItemStack(Material.LIGHT_BLUE_DYE);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "인컴 강화서");
        meta.setLore(Arrays.asList(ChatColor.WHITE + "우클릭 시 자신의 진영에 고용된 인컴 중",
                ChatColor.WHITE + "가장 낮은 레벨의 인컴을 강화합니다."));
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getNetherZombieScroll()
    {
        ItemStack scroll = new ItemStack(Material.NETHER_WART);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "네더 좀비 소환서");
        scroll.setItemMeta(meta);

        return scroll;
    }

    public ItemStack getMinerZombieScroll()
    {
        ItemStack scroll = new ItemStack(Material.NETHER_WART);
        ItemMeta meta = scroll.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.WHITE + "광부 좀비 소환서");
        scroll.setItemMeta(meta);

        return scroll;
    }

    public void summonZombie(Location loc, int level, Location targetLoc)
    {
        Zombie zombie = (Zombie) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.ZOMBIE);
        zombie.setAdult();
        zombie.setCustomName("Lv" + level + " 좀비");
        zombie.setCustomNameVisible(true);
        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.ZOMBIE, level));
        zombie.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(statManager.getAttack(EntityType.ZOMBIE, level));
        zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.ZOMBIE, level));
        zombie.setHealth(statManager.getHealth(EntityType.ZOMBIE, level));
        zombie.getEquipment().setHelmet(new ItemStack(Material.STONE_BUTTON));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999 * 20, 1, true, false));

        fireworkEffect(zombie.getLocation(), Color.GREEN);

        monster.add(zombie);

        EntityZombie entityZombie = ((CraftZombie) zombie).getHandle();
        NavigationAbstract navigation = entityZombie.E();

        mobPathFinding(zombie, navigation, targetLoc, statManager.getSpeed(EntityType.ZOMBIE, level) * 3);
        //pathFinding(zombie, targetLoc, statManager.getSpeed(EntityType.ZOMBIE, level) * 3);
    }

    public void summonSkeleton(Location loc, int level, Location targetLoc)
    {
        Skeleton skeleton = (Skeleton) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.SKELETON);
        skeleton.setCustomName("Lv" + level + " 스켈레톤");
        skeleton.setCustomNameVisible(true);
        skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.SKELETON, level));
        skeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(statManager.getAttack(EntityType.SKELETON, level));
        skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.SKELETON, level));
        skeleton.setHealth(statManager.getHealth(EntityType.SKELETON, level));
        skeleton.getEquipment().setHelmet(new ItemStack(Material.STONE_BUTTON));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999 * 20, 1, true, false));

        fireworkEffect(skeleton.getLocation(), Color.WHITE);

        monster.add(skeleton);

        EntitySkeleton entitySkeleton = ((CraftSkeleton) skeleton).getHandle();
        NavigationAbstract navigation = entitySkeleton.E();

        mobPathFinding(skeleton, navigation, targetLoc, statManager.getSpeed(EntityType.SKELETON, level) * 3);
        //pathFinding(skeleton, targetLoc, statManager.getSpeed(EntityType.SKELETON, level) * 3);
    }

    public void summonSpider(Location loc, int level, Location targetLoc)
    {
        Spider spider = (Spider) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.SPIDER);
        spider.setCustomName("Lv" + level + " 거미");
        spider.setCustomNameVisible(true);
        spider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.SPIDER, level));
        spider.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(statManager.getAttack(EntityType.SPIDER, level));
        spider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.SPIDER, level));
        spider.setHealth(statManager.getHealth(EntityType.SPIDER, level));

        fireworkEffect(spider.getLocation(), Color.BLACK);

        monster.add(spider);

        EntitySpider entitySpider = ((CraftSpider) spider).getHandle();
        NavigationAbstract navigation = entitySpider.E();

        mobPathFinding(spider, navigation, targetLoc, statManager.getSpeed(EntityType.SKELETON, level) * 3);
        //pathFinding(spider, targetLoc, statManager.getSpeed(EntityType.SPIDER, level) * 3);
    }

    public void summonZoglin(Location loc, int level, Location targetLoc)
    {
        Zoglin zoglin = (Zoglin) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.ZOGLIN);
        zoglin.setCustomName("Lv" + level + " 조글린");
        zoglin.setCustomNameVisible(true);
        zoglin.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.ZOGLIN, level));
        zoglin.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(statManager.getAttack(EntityType.ZOGLIN, level));
        zoglin.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.ZOGLIN, level));
        zoglin.setHealth(statManager.getHealth(EntityType.ZOGLIN, level));

        fireworkEffect(zoglin.getLocation(), Color.YELLOW);

        monster.add(zoglin);

        EntityZoglin entityZoglin = ((CraftZoglin) zoglin).getHandle();
        NavigationAbstract navigation = entityZoglin.E();

        mobPathFinding(zoglin, navigation, targetLoc, statManager.getSpeed(EntityType.ZOGLIN, level) * 3);
        //pathFinding(zoglin, targetLoc, statManager.getSpeed(EntityType.ZOGLIN, level) * 3);
    }

    public void summonWolf(Location loc, int level, Location targetLoc)
    {
        Wolf wolf = (Wolf) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.WOLF);
        wolf.setCustomName("Lv" + level + " 늑대");
        wolf.setCustomNameVisible(true);
        wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.WOLF, level));
        wolf.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(statManager.getAttack(EntityType.WOLF, level));
        wolf.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.WOLF, level));
        wolf.setHealth(statManager.getHealth(EntityType.WOLF, level));

        fireworkEffect(wolf.getLocation(), Color.GRAY);

        monster.add(wolf);

        EntityWolf entityWolf = ((CraftWolf) wolf).getHandle();
        NavigationAbstract navigation = entityWolf.E();

        mobPathFinding(wolf, navigation, targetLoc, statManager.getSpeed(EntityType.WOLF, level) * 3);
        //pathFinding(wolf, targetLoc, statManager.getSpeed(EntityType.WOLF, level) * 3);
    }

    public void summonZombifiedPiglin(Location loc, int level, Location targetLoc)
    {
        PigZombie zombifiedPiglin = (PigZombie) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.ZOMBIFIED_PIGLIN);
        zombifiedPiglin.setCustomName("Lv" + level + " 좀비피글린");
        zombifiedPiglin.setCustomNameVisible(true);
        zombifiedPiglin.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.ZOMBIFIED_PIGLIN, level));
        zombifiedPiglin.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(statManager.getAttack(EntityType.ZOMBIFIED_PIGLIN, level));
        zombifiedPiglin.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.ZOMBIFIED_PIGLIN, level));
        zombifiedPiglin.setHealth(statManager.getHealth(EntityType.ZOMBIFIED_PIGLIN, level));

        fireworkEffect(zombifiedPiglin.getLocation(), Color.ORANGE);

        monster.add(zombifiedPiglin);

        EntityPigZombie entityPigZombie = ((CraftPigZombie) zombifiedPiglin).getHandle();
        NavigationAbstract navigation = entityPigZombie.E();

        mobPathFinding(zombifiedPiglin, navigation, targetLoc, statManager.getSpeed(EntityType.ZOMBIFIED_PIGLIN, level) * 3);
        //pathFinding(zombifiedPiglin, targetLoc, statManager.getSpeed(EntityType.WOLF, level) * 3);
    }

    public void summonIncome(Location loc, int level, Location targetLoc)
    {
        if(level <= 3)
        {
            Cow cow = (Cow) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.COW);
            cow.setCustomName("Lv" + level + " 소");
            cow.setCustomNameVisible(true);
            cow.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.COW, level));
            cow.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.COW, level));
            cow.setHealth(statManager.getHealth(EntityType.COW, level));

            fireworkEffect(cow.getLocation(), Color.GRAY);

            monster.add(cow);

            EntityCow entityCow = ((CraftCow) cow).getHandle();
            NavigationAbstract navigation = entityCow.E();

            noneMobPathFinding(cow, navigation, targetLoc, statManager.getSpeed(EntityType.COW, level) * 3);
        }
        else if(level > 3)
        {
            Sheep sheep = (Sheep) loc.getWorld().spawnEntity(getRandomValueLoc(loc), EntityType.SHEEP);
            sheep.setCustomName("Lv" + (level - 3) + " 양");
            sheep.setCustomNameVisible(true);
            sheep.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(statManager.getHealth(EntityType.SHEEP, level - 3));
            sheep.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(statManager.getSpeed(EntityType.SHEEP, level - 3));
            sheep.setHealth(statManager.getHealth(EntityType.SHEEP, level - 3));

            fireworkEffect(sheep.getLocation(), Color.GRAY);

            monster.add(sheep);

            EntitySheep entitySheep = ((CraftSheep) sheep).getHandle();
            NavigationAbstract navigation = entitySheep.E();

            noneMobPathFinding(sheep, navigation, targetLoc, statManager.getSpeed(EntityType.SHEEP, level - 3) * 3);
        }
    }

    public void summonNetherZombie(Location loc, int health, int attack, double speed)
    {
        MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob("네더 좀비").orElse(null);
        if(mob != null)
        {
            ActiveMob zomzom = mob.spawn(BukkitAdapter.adapt(loc),1);
            Entity entity = zomzom.getEntity().getBukkitEntity();

            ((LivingEntity) entity).setHealth(health);
            mob.setAttrHealth(new StaticDouble(health));
            mob.setAttrDamage(new StaticDouble(attack));
            mob.setAttrMovementSpeed(new StaticDouble(speed));
            monster.add(entity);
        }
    }

    public void summonMinerZombie(Location loc, int health, int attack, double speed)
    {
        MythicMob mob = MythicBukkit.inst().getMobManager().getMythicMob("광부 좀비").orElse(null);
        if(mob != null)
        {
            ActiveMob zomzom = mob.spawn(BukkitAdapter.adapt(loc),1);
            Entity entity = zomzom.getEntity().getBukkitEntity();

            ((LivingEntity) entity).setHealth(health);
            mob.setAttrHealth(new StaticDouble(health));
            mob.setAttrDamage(new StaticDouble(attack));
            mob.setAttrMovementSpeed(new StaticDouble(speed));
            monster.add(entity);
        }
    }

    public void mobPathFinding(Mob entity, NavigationAbstract navigation, Location targetLoc, double speed)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Player player = getClosestPlayer(entity);

                if(player == null)
                {
                    navigation.a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), speed);
                }
                else if(isCollide(entity, player, 3.5))
                {
                    if(isSameTeam(player, targetLoc))
                    {
                        entity.setTarget(null);
                        navigation.a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), speed);
                    }
                    else entity.setTarget(player);
                }
                else
                {
                    entity.setTarget(null);
                    navigation.a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), speed);
                }

                if(entity.isDead())
                {
                    cancel();
                    monster.remove(entity);
                    return;
                }

                if(isCollide(entity, targetLoc, 2.0))
                {
                    if(locationManager.isRedCorner(targetLoc))
                    {
                        cancel();
                        mobPathFinding(entity, navigation, locationManager.getRedCenter(), speed);
                        return;
                    }
                    else if(locationManager.isBlueCorner(targetLoc))
                    {
                        cancel();
                        mobPathFinding(entity, navigation, locationManager.getBlueCenter(), speed);
                        return;
                    }
                    else
                    {
                        entity.remove();
                        monster.remove(entity);
                        if(targetLoc.equals(locationManager.getRedCenter()))
                        {
                            gameProcess.blueMonsterCount++;
                            explosion(targetLoc);
                            sendEnterMessage(entity.getCustomName(), "red");
                            if(gameProcess.blueMonsterCount >= gameProcess.maxCount) stop();
                        }
                        if(targetLoc.equals(locationManager.getBlueCenter()))
                        {
                            gameProcess.redMonsterCount++;
                            explosion(targetLoc);
                            sendEnterMessage(entity.getCustomName(), "blue");
                            if(gameProcess.redMonsterCount >= gameProcess.maxCount) stop();
                        }
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public void noneMobPathFinding(Entity entity, NavigationAbstract navigation, Location targetLoc, double speed)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(entity.isDead())
                {
                    cancel();
                    monster.remove(entity);
                    return;
                }

                navigation.a(targetLoc.getX(), targetLoc.getY(), targetLoc.getZ(), speed);

                if(isCollide(entity, targetLoc, 2.0))
                {
                    if(locationManager.isRedCorner(targetLoc))
                    {
                        cancel();
                        noneMobPathFinding(entity, navigation, locationManager.getRedCenter(), speed);
                        return;
                    }
                    else if(locationManager.isBlueCorner(targetLoc))
                    {
                        cancel();
                        noneMobPathFinding(entity, navigation, locationManager.getBlueCenter(), speed);
                        return;
                    }
                    else
                    {
                        entity.remove();
                        monster.remove(entity);
                        if(targetLoc.equals(locationManager.getRedCenter()))
                        {
                            gameProcess.blueMonsterCount++;
                            explosion(targetLoc);
                            sendEnterMessage(entity.getCustomName(), "red");
                            if(gameProcess.blueMonsterCount >= gameProcess.maxCount) stop();
                        }
                        if(targetLoc.equals(locationManager.getBlueCenter()))
                        {
                            gameProcess.redMonsterCount++;
                            explosion(targetLoc);
                            sendEnterMessage(entity.getCustomName(), "blue");
                            if(gameProcess.redMonsterCount >= gameProcess.maxCount) stop();
                        }
                        cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public int getMonsterLevel(Entity entity)
    {
        String name = entity.getCustomName();
        String nameToInt = name.replaceAll("[^\\d]", "");

        if(nameToInt == "") return 0;

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

    public boolean isSameTeam(Player player, Location targetLoc)
    {
        return (isRedTeam(player) && locationManager.isBlue(targetLoc)) || (isBlueTeam(player) && locationManager.isRed(targetLoc));
    }

    public boolean isSameTeam(Player player1, Player player2)
    {
        return ((isRedTeam(player1) && isRedTeam(player2)) || (isBlueTeam(player1) && isBlueTeam(player2)));
    }

    public void explosion(Location loc)
    {
        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1, 0, 0,0);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 5, 1);
    }

    public void dropBundle(Location loc)
    {
        if(new Random().nextInt(100) < 5) //5%
        {
            loc.getWorld().dropItemNaturally(loc, getBundle());
        }
    }

    public void fireworkEffect(Location loc, Color color)
    {
        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(2);
        meta.addEffect(FireworkEffect.builder().withColor(color).flicker(true).build());

        firework.setFireworkMeta(meta);
        firework.detonate();
    }

    public void sendEnterMessage(String name, String team)
    {
        if(team.equalsIgnoreCase("red"))
        {
            Bukkit.broadcastMessage(ChatColor.GOLD + name + ChatColor.WHITE + "이(가) " + ChatColor.RED + "레드팀" + ChatColor.WHITE + " 크리스탈에 도착했습니다!  " +
                    ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "이 점수를 획득합니다!");
        }
        if(team.equalsIgnoreCase("blue"))
        {
            Bukkit.broadcastMessage(ChatColor.GOLD + name + ChatColor.WHITE + "이(가) " + ChatColor.BLUE + "블루팀" + ChatColor.WHITE + " 크리스탈에 도착했습니다!  " +
                    ChatColor.RED + "레드팀" + ChatColor.WHITE + "이 점수를 획득합니다!");
        }
    }
}
