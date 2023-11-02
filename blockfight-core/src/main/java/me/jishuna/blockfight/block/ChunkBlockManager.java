package me.jishuna.blockfight.block;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Chunk;
import org.bukkit.Location;
import me.jishuna.blockfight.BlockVector;
import me.jishuna.jishlib.util.Utils;

public class ChunkBlockManager {
    private final Map<Long, CombatBlockManager> chunkMap = new HashMap<>();

    public void addBlock(Location location, CombatBlock block) {
        addBlock(location.getChunk(), BlockVector.fromLocation(location), block);
    }

    public void addBlock(Chunk chunk, BlockVector position, CombatBlock block) {
        Long key = Utils.getChunkKey(chunk);
        this.chunkMap.computeIfAbsent(key, k -> new CombatBlockManager()).addBlock(position, block);
    }

    public CombatBlock getBlock(Location location) {
        return getBlock(location.getChunk(), BlockVector.fromLocation(location));
    }

    public CombatBlock getBlock(Chunk chunk, BlockVector position) {
        Long key = Utils.getChunkKey(chunk);
        CombatBlockManager manager = this.chunkMap.get(key);
        if (manager == null) {
            return null;
        }

        return manager.getBlock(position);
    }

    public void removeBlock(Location location) {
        removeBlock(location.getChunk(), BlockVector.fromLocation(location));
    }

    public void removeBlock(Chunk chunk, BlockVector position) {
        Long key = Utils.getChunkKey(chunk);
        CombatBlockManager manager = this.chunkMap.get(key);
        if (manager != null) {
            manager.removeBlock(position);
        }
    }

    public void removeChunk(Chunk chunk) {
        this.chunkMap.remove(Utils.getChunkKey(chunk));
    }
}
