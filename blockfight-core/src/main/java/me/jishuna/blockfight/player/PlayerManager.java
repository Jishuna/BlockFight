package me.jishuna.blockfight.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.jishuna.jishlib.collections.TypedDistributionTask;

public class PlayerManager {
    private final Map<UUID, BlockHealthBar> healthbars = new HashMap<>();
    private final TypedDistributionTask<BlockHealthBar> task;

    public PlayerManager(Plugin plugin) {
        this.task = new TypedDistributionTask<>(bar -> bar.checkAndHide(), bar -> !this.healthbars.containsKey(bar.getId()), 5);
        Bukkit.getScheduler().runTaskTimer(plugin, this.task, 1, 1);
    }

    public BlockHealthBar getOrCreate(Player player) {
        return getOrCreate(player.getUniqueId());
    }

    public BlockHealthBar getOrCreate(UUID id) {
        return this.healthbars.computeIfAbsent(id, k -> {
            BlockHealthBar bar = new BlockHealthBar(id);
            this.task.addValue(bar);

            return bar;
        });
    }

    public void remove(Player player) {
        remove(player.getUniqueId());
    }

    public void remove(UUID id) {
        this.healthbars.remove(id);
    }

}
