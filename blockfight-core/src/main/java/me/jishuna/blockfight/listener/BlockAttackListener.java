package me.jishuna.blockfight.listener;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import me.jishuna.blockfight.Settings;
import me.jishuna.blockfight.block.CombatBlock;
import me.jishuna.blockfight.block.WorldBlockManager;
import me.jishuna.blockfight.player.PlayerManager;

public class BlockAttackListener implements Listener {
    private final WorldBlockManager blockManager;
    private final PlayerManager playerManager;

    public BlockAttackListener(WorldBlockManager blockManager, PlayerManager playerManager) {
        this.blockManager = blockManager;
        this.playerManager = playerManager;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();

        CombatBlock combatBlock = this.blockManager.getBlock(location);
        if (combatBlock == null) {
            combatBlock = create(player, block);
            if (combatBlock == null) {
                return;
            }

            this.blockManager.addBlock(location, combatBlock);
            this.playerManager.getOrCreate(player).updateHealth(combatBlock);
        }

        attack(player, location, combatBlock);
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        Location location = event.getEntity().getLocation();
        CombatBlock combatBlock = this.blockManager.getBlock(location);
        if (combatBlock == null) {
            return;
        }

        attack(player, location, combatBlock);
    }

    private CombatBlock create(Player player, Block block) {
        double health = Settings.getHealth(block.getType());
        if (health <= 0 || block.getBreakSpeed(player) <= 0) {
            return null;
        }

        return CombatBlock.create(block, health);
    }

    private void attack(Player player, Location location, CombatBlock block) {
        block.attackedBy(player);
        this.playerManager.getOrCreate(player).updateHealth(block);

        if (block.isDead()) {
            this.blockManager.removeBlock(location);
        }
    }
}
