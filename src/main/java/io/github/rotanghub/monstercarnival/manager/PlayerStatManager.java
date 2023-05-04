package io.github.rotanghub.monstercarnival.manager;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class PlayerStatManager
{
    public void addPlayerMaxHealth(Player player, int add)
    {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getHealthScale() + add);
        player.setHealth(player.getHealth() + add);
    }

    public void addSwordDamage(Player player, int add)
    {
        for(ItemStack item : player.getInventory().getContents())
        {
            if(item.getType().equals(Material.WOODEN_SWORD))
            {
                addAttackAttribute(item, add);
            }
        }
    }

    public void addAttackAttribute(ItemStack item, int stat)
    {
        int currentStat = getWeaponDamage(item);
        setAttackAttribute(item, stat + currentStat);
    }

    public void setAttackAttribute(ItemStack item, int add)
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
