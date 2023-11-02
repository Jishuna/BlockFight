package me.jishuna.blockfight.block;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import me.jishuna.blockfight.BlockVector;

public class WorldBlockManager {
    private final Map<UUID, ChunkBlockManager> worldMap = new HashMap<>();

    public void addBlock(Location location, CombatBlock block) {
        addBlock(location.getChunk(), BlockVector.fromLocation(location), block);
    }

    public void addBlock(Chunk chunk, BlockVector position, CombatBlock block) {
        World world = chunk.getWorld();
        this.worldMap.computeIfAbsent(world.getUID(), k -> new ChunkBlockManager()).addBlock(chunk, position, block);
    }

    public CombatBlock getBlock(Location location) {
        return getBlock(location.getChunk(), BlockVector.fromLocation(location));
    }

    public CombatBlock getBlock(Chunk chunk, BlockVector position) {
        World world = chunk.getWorld();
        ChunkBlockManager manager = this.worldMap.get(world.getUID());
        if (manager == null) {
            return null;
        }

        return manager.getBlock(chunk, position);
    }

    public void removeBlock(Location location) {
        removeBlock(location.getChunk(), BlockVector.fromLocation(location));
    }

    public void removeBlock(Chunk chunk, BlockVector position) {
        World world = chunk.getWorld();
        ChunkBlockManager manager = this.worldMap.get(world.getUID());
        if (manager != null) {
            manager.removeBlock(chunk, position);
        }
    }

    public void clearWorld(World world) {
        this.worldMap.remove(world.getUID());
    }

    public void clearChunk(Chunk chunk) {
        World world = chunk.getWorld();
        ChunkBlockManager manager = this.worldMap.get(world.getUID());
        if (manager == null) {
            return;
        }

        manager.removeChunk(chunk);
    }
}
