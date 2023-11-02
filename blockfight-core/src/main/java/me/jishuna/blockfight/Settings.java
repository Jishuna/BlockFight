package me.jishuna.blockfight;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import me.jishuna.jishlib.config.annotation.ConfigEntry;
import me.jishuna.jishlib.config.annotation.PostLoad;

public class Settings {
    @ConfigEntry("blocks.disabled-blocks")
    public static Set<Material> DISABLED_BLOCKS = Collections.emptySet();

    @ConfigEntry("blocks.custom-health")
    public static Map<Material, Double> BLOCK_HEALTH = ImmutableMap.of(Material.OBSIDIAN, 100d);

    @ConfigEntry("blocks.default-health")
    public static double DEFAULT_HEALTH = 10;

    @ConfigEntry("damage.base-damage")
    public static Map<Material, Double> ITEM_BASE_DAMAGE = getBaseDamageMap();

    @ConfigEntry("damage.efficiency-bonus")
    public static Map<Integer, Double> EFFICIENCY_DAMAGE_BONUS = ImmutableMap.of(1, 1.5d, 2, 3d, 3, 4.5d, 4, 6d, 5, 7.5d);

    @ConfigEntry("damage.require-correct-tool")
    public static boolean REQUIRE_CORRECT_TOOL = true;

    @ConfigEntry("bar-hide-delay")
    public static int BAR_HIDE_DELAY = 100;

    private static double maxEfficiencyBonus;

    public static double getEfficiencyBonus(ItemStack item) {
        return EFFICIENCY_DAMAGE_BONUS.getOrDefault(item.getEnchantmentLevel(Enchantment.DIG_SPEED), maxEfficiencyBonus);
    }

    public static double getHealth(Material material) {
        if (DISABLED_BLOCKS.contains(material)) {
            return 0d;
        }

        return BLOCK_HEALTH.getOrDefault(material, DEFAULT_HEALTH);
    }

    @PostLoad
    public static void postLoad() {
        maxEfficiencyBonus = EFFICIENCY_DAMAGE_BONUS.values().stream().max(Double::compare).orElse(0d);
    }

    private static Map<Material, Double> getBaseDamageMap() {
        Map<Material, Double> map = new LinkedHashMap<>();

        map.put(Material.WOODEN_PICKAXE, 3d);
        map.put(Material.WOODEN_AXE, 3d);
        map.put(Material.WOODEN_SHOVEL, 3d);
        map.put(Material.WOODEN_HOE, 3d);

        map.put(Material.STONE_PICKAXE, 4d);
        map.put(Material.STONE_AXE, 4d);
        map.put(Material.STONE_SHOVEL, 4d);
        map.put(Material.STONE_HOE, 4d);

        map.put(Material.IRON_PICKAXE, 5d);
        map.put(Material.IRON_AXE, 5d);
        map.put(Material.IRON_SHOVEL, 5d);
        map.put(Material.IRON_HOE, 5d);

        map.put(Material.GOLDEN_PICKAXE, 3d);
        map.put(Material.GOLDEN_AXE, 3d);
        map.put(Material.GOLDEN_SHOVEL, 3d);
        map.put(Material.GOLDEN_HOE, 3d);

        map.put(Material.DIAMOND_PICKAXE, 6d);
        map.put(Material.DIAMOND_AXE, 6d);
        map.put(Material.DIAMOND_SHOVEL, 6d);
        map.put(Material.DIAMOND_HOE, 6d);

        map.put(Material.NETHERITE_PICKAXE, 7d);
        map.put(Material.NETHERITE_AXE, 7d);
        map.put(Material.NETHERITE_SHOVEL, 7d);
        map.put(Material.NETHERITE_HOE, 7d);

        return map;
    }
}
