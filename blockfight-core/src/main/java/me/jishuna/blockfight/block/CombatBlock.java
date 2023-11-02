package me.jishuna.blockfight.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.jishuna.blockfight.Settings;
import me.jishuna.jishlib.JishLib;
import me.jishuna.jishlib.item.ItemTypes;

public class CombatBlock {
    private final Interaction hitbox;
    private final Block block;
    private final double maxHealth;
    private double health;
    private boolean dead;
    private Material cachedType;

    private CombatBlock(Block block, double health, Interaction hitbox) {
        this.block = block;
        this.maxHealth = health;
        this.health = health;
        this.hitbox = hitbox;
        this.cachedType = block.getType();
    }

    public static CombatBlock create(Block block, double health) {
        World world = block.getWorld();
        Location location = block.getLocation();
        Interaction hitbox = world.spawn(location.clone().add(0.5, 0, 0.5), Interaction.class, en -> {
            en.setInteractionHeight(1.001f);
            en.setInteractionWidth(1.001f);
            en.setPersistent(false);
        });

        return new CombatBlock(block, health, hitbox);
    }

    public static double getDamageDealt(ItemStack item, Material target) {
        Material weaponType = item.getType();
        double damage = Settings.ITEM_BASE_DAMAGE.getOrDefault(weaponType, 1d) + Settings.getEfficiencyBonus(item);

        if (Settings.REQUIRE_CORRECT_TOOL) {
            if (Tag.MINEABLE_PICKAXE.isTagged(target) && !ItemTypes.PICKAXES.contains(weaponType)) {
                damage = 1;
            } else if (Tag.MINEABLE_AXE.isTagged(target) && !ItemTypes.AXES.contains(weaponType)) {
                damage = 1;
            } else if (Tag.MINEABLE_SHOVEL.isTagged(target) && !ItemTypes.SHOVELS.contains(weaponType)) {
                damage = 1;
            } else if (Tag.MINEABLE_HOE.isTagged(target) && !ItemTypes.HOES.contains(weaponType)) {
                damage = 1;
            }
        }

        return damage;
    }

    public void attackedBy(Player player) {
        ItemStack item = player.getEquipment().getItemInMainHand();

        double damage = getDamageDealt(item, getType());
        damage(damage);
    }

    public void damage(double amount) {
        this.health -= amount;
        if (this.health <= 0) {
            die();
        }
    }

    public void die() {
        this.dead = true;
        this.block.breakNaturally();
        JishLib.runLater(this.hitbox::remove, 10);
    }

    public Material getType() {
        if (!this.dead) {
            this.cachedType = this.block.getType();
        }
        return this.cachedType;
    }

    public boolean isDead() {
        return this.dead;
    }

    public double getHealth() {
        return this.health;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }
}
