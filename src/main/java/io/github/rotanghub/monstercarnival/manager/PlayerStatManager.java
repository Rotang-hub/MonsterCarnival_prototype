package io.github.rotanghub.monstercarnival.manager;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStatManager
{
    Manager manager;

    Map<Player, Integer> coinLimit = new HashMap<>();

    public PlayerStatManager(Manager manager)
    {
        this.manager = manager;
    }


    public void setCoinLimit(Player player, int limit)
    {
        coinLimit.put(player, limit);
    }

    public int getCoinLimit(Player player)
    {
        return coinLimit.get(player);
    }

    public int getCoinAmount(Player player)
    {
        int amount = 0;

        for(ItemStack item : player.getInventory().getContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;
            if(item.getType().equals(manager.getCoin(1).getType()))
            {
                amount += item.getAmount();
            }
        }
        return amount;
    }

    public boolean canUpgradeHealth(Player player, int max)
    {
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() < max;
    }

    public void addPlayerMaxHealth(Player player, int add)
    {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue((int) player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + add);
    }

    public boolean canUpgradeSword(Player player, int max)
    {
        int damage = 0;
        for(ItemStack item : player.getInventory().getContents())
        {
            if(item == null || item.getType().isAir()) continue;

            if(item.getType().equals(Material.WOODEN_SWORD))
            {
                if(damage <= getWeaponDamage(item)) damage = getWeaponDamage(item);
            }
        }

        return damage < max;
    }

    public void addSwordDamage(Player player, int add)
    {
        for(ItemStack item : player.getInventory().getContents())
        {
            if(item == null || item.getType().isAir()) continue;
            if(item.getType().equals(Material.WOODEN_SWORD))
            {
                ItemMeta meta = addAttackAttribute(item, add).getItemMeta();
                item.setItemMeta(meta);
            }
        }
    }

    public ItemStack addAttackAttribute(ItemStack item, int stat)
    {
        int currentStat = getWeaponDamage(item);
        return setAttackAttribute(item, stat + currentStat);
    }

    public ItemStack setAttackAttribute(ItemStack item, int add)
    {
        ItemMeta meta = item.getItemMeta();

        AttributeModifier attackModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage",
                add, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attackModifier);

        AttributeModifier speedModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed",
                -1.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedModifier);

        item.setItemMeta(meta);

        return item;
    }

    public int getWeaponDamage(ItemStack weapon)
    {
        ItemMeta meta = weapon.getItemMeta();
        double currentStat = 0;

        for(AttributeModifier modifier : meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE))
        {
            if(modifier == null)
                continue;
            currentStat += modifier.getAmount();
        }

        return (int) Math.round(currentStat);
    }
}
