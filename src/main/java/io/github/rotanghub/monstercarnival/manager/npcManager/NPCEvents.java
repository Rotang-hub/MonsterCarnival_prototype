package io.github.rotanghub.monstercarnival.manager.npcManager;

import io.github.rotanghub.monstercarnival.manager.Manager;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;

public class NPCEvents implements Listener
{
    CitizenNPC citizenNPC;
    Manager manager;

    public NPCEvents(CitizenNPC citizenNPC, Manager manager)
    {
        this.citizenNPC = citizenNPC;
        this.manager = manager;
    }

    @EventHandler
    public void onInteractNPC(NPCRightClickEvent event)
    {
        if(manager.process)
        {
            Player player = event.getClicker();
            NPC npc = event.getNPC();

            if(npc.getName().equalsIgnoreCase("강화 상점"))
            {
                manager.openUpgradeShop(player);
            }
            if(npc.getName().equalsIgnoreCase("스크롤 상점"))
            {
                manager.openScrollShop(player);
            }
        }
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

            if(action.toString().contains("RIGHT"))
            {
                Location redLoc = citizenNPC.getAvailableRedField();
                Location blueLoc = citizenNPC.getAvailableBlueField();

                if(item.isSimilar(manager.getZombieSummonerScroll()))
                {
                    if(manager.isRedTeam(player) && redLoc != null)
                    {
                        citizenNPC.spawnZombieNPC(redLoc, "red");
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "좀비 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                    if(manager.isBlueTeam(player) && blueLoc != null)
                    {
                        citizenNPC.spawnZombieNPC(blueLoc, "blue");
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "좀비 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                }
                if(item.isSimilar(manager.getSkeletonSummonerScroll()))
                {
                    if(manager.isRedTeam(player) && redLoc != null)
                    {
                        citizenNPC.spawnSkeletonNPC(redLoc, "red");
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "스켈레톤 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                    if(manager.isBlueTeam(player) && blueLoc != null)
                    {
                        citizenNPC.spawnSkeletonNPC(blueLoc, "blue");
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "스켈레톤 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                }
                if(item.isSimilar(manager.getSpiderSummonerScroll()))
                {
                    if(manager.isRedTeam(player) && redLoc != null)
                    {
                        citizenNPC.spawnSpiderNPC(redLoc, "red");
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "거미 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                    if(manager.isBlueTeam(player) && blueLoc != null)
                    {
                        citizenNPC.spawnSpiderNPC(blueLoc, "blue");
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "거미 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                }
                if(item.isSimilar(manager.getZoglinSummonerScroll()))
                {
                    if(manager.isRedTeam(player) && redLoc != null)
                    {
                        citizenNPC.spawnZoglinNPC(redLoc, "red");
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "조글린 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                    if(manager.isBlueTeam(player) && blueLoc != null)
                    {
                        citizenNPC.spawnZoglinNPC(blueLoc, "blue");
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "조글린 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                }
                if(item.isSimilar(manager.getWolfSummonerScroll()))
                {
                    if(manager.isRedTeam(player) && redLoc != null)
                    {
                        citizenNPC.spawnWolfNPC(redLoc, "red");
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "늑대 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                    if(manager.isBlueTeam(player) && blueLoc != null)
                    {
                        citizenNPC.spawnWolfNPC(blueLoc, "blue");
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "늑대 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                }
                if(item.isSimilar(manager.getZombifiedPiglinSummonerScroll()))
                {
                    if(manager.isRedTeam(player) && redLoc != null)
                    {
                        citizenNPC.spawnZombifiedPiglinNPC(citizenNPC.getAvailableRedField(), "red");
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "좀비피글린 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                    if(manager.isBlueTeam(player) && blueLoc != null)
                    {
                        citizenNPC.spawnZombifiedPiglinNPC(citizenNPC.getAvailableBlueField(), "blue");
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "좀비피글린 소환사" + ChatColor.WHITE + "를 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                }
                if(item.isSimilar(manager.getIncomeSummonerScroll()))
                {
                    if(manager.isRedTeam(player) && redLoc != null)
                    {
                        citizenNPC.spawnIncomeNPC(citizenNPC.getAvailableRedField(), "red", player.getName());
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "인컴" + ChatColor.WHITE + "을 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                    if(manager.isBlueTeam(player) && blueLoc != null)
                    {
                        citizenNPC.spawnIncomeNPC(citizenNPC.getAvailableBlueField(), "blue", player.getName());
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "에서 "
                                + ChatColor.GOLD + "인컴" + ChatColor.WHITE + "을 고용했습니다");
                        item.setAmount(item.getAmount() - 1);
                    }
                }
                //------------------------------------
                if(item.isSimilar(manager.getZombieUpgradeScroll()))
                {
                    if(manager.isRedTeam(player) && citizenNPC.hasNPC(citizenNPC.redFieldNPC, "좀비"))
                    {
                        NPC npc = citizenNPC.upgradeZombieNPC("red");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "좀비 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                    if(manager.isBlueTeam(player) && citizenNPC.hasNPC(citizenNPC.blueFieldNPC, "좀비"))
                    {
                        NPC npc = citizenNPC.upgradeZombieNPC("blue");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "좀비 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                }
                if(item.isSimilar(manager.getSkeletonUpgradeScroll()))
                {
                    if(manager.isRedTeam(player) && citizenNPC.hasNPC(citizenNPC.redFieldNPC, "스켈레톤"))
                    {
                        NPC npc = citizenNPC.upgradeSkeletonNPC("red");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "스켈레톤 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                    if(manager.isBlueTeam(player) && citizenNPC.hasNPC(citizenNPC.blueFieldNPC, "스켈레톤"))
                    {
                        NPC npc = citizenNPC.upgradeSkeletonNPC("blue");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "스켈레톤 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                }
                if(item.isSimilar(manager.getSpiderUpgradeScroll()))
                {
                    if(manager.isRedTeam(player) && citizenNPC.hasNPC(citizenNPC.redFieldNPC, "거미"))
                    {
                        NPC npc = citizenNPC.upgradeSpiderNPC("red");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "거미 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                    if(manager.isBlueTeam(player) && citizenNPC.hasNPC(citizenNPC.blueFieldNPC, "거미"))
                    {
                        NPC npc = citizenNPC.upgradeSpiderNPC("blue");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "거미 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                }
                if(item.isSimilar(manager.getZoglinUpgradeScroll()))
                {
                    if(manager.isRedTeam(player) && citizenNPC.hasNPC(citizenNPC.redFieldNPC, "조글린"))
                    {
                        NPC npc = citizenNPC.upgradeZoglinNPC("red");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "조글린 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                    if(manager.isBlueTeam(player) && citizenNPC.hasNPC(citizenNPC.blueFieldNPC, "조글린"))
                    {
                        NPC npc = citizenNPC.upgradeZoglinNPC("blue");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "조글린 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                }
                if(item.isSimilar(manager.getWolfUpgradeScroll()))
                {
                    if(manager.isRedTeam(player) && citizenNPC.hasNPC(citizenNPC.redFieldNPC, "늑대"))
                    {
                        NPC npc = citizenNPC.upgradeWolfNPC("red");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "늑대 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                    if(manager.isBlueTeam(player) && citizenNPC.hasNPC(citizenNPC.blueFieldNPC, "늑대"))
                    {
                        NPC npc = citizenNPC.upgradeWolfNPC("blue");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "늑대 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                }
                if(item.isSimilar(manager.getZombifiedPiglinUpgradeScroll()))
                {
                    if(manager.isRedTeam(player) && citizenNPC.hasNPC(citizenNPC.redFieldNPC, "좀비피글린"))
                    {
                        NPC npc = citizenNPC.upgradeZombifiedPiglinNPC("red");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "좀비피글린 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                    if(manager.isBlueTeam(player) && citizenNPC.hasNPC(citizenNPC.blueFieldNPC, "좀비피글린"))
                    {
                        NPC npc = citizenNPC.upgradeZombifiedPiglinNPC("blue");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "좀비피글린 소환사" + ChatColor.WHITE + "가 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                }
                if(item.isSimilar(manager.getIncomeUpgradeScroll()))
                {
                    if(manager.isRedTeam(player) && citizenNPC.hasNPC(citizenNPC.redFieldNPC, "인컴"))
                    {
                        NPC npc = citizenNPC.upgradeIncomeNPC("red");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.RED + "레드팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "인컴" + ChatColor.WHITE + "이 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                    if(manager.isBlueTeam(player) && citizenNPC.hasNPC(citizenNPC.blueFieldNPC, "인컴"))
                    {
                        NPC npc = citizenNPC.upgradeIncomeNPC("blue");
                        if(npc == null) return;
                        Bukkit.broadcastMessage(ChatColor.BLUE + "블루팀" + ChatColor.WHITE + "의 "
                                + ChatColor.GOLD + "인컴" + ChatColor.WHITE + "이 "
                                + ChatColor.GOLD + "lv" + citizenNPC.getLevel(npc.getName()) + ChatColor.WHITE + "로 강화되었습니다!");
                        item.setAmount(item.getAmount() - 1);
                        citizenNPC.glowingNPC(npc);
                    }
                }
            }
        }
    }
}
