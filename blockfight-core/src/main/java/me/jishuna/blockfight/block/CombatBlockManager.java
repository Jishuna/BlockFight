package me.jishuna.blockfight.block;

import java.util.HashMap;
import java.util.Map;
import me.jishuna.blockfight.BlockVector;

public class CombatBlockManager {

    private final Map<BlockVector, CombatBlock> blockMap = new HashMap<>();

    public void addBlock(BlockVector position, CombatBlock block) {
        this.blockMap.put(position, block);
    }

    public CombatBlock getBlock(BlockVector position) {
        return this.blockMap.get(position);
    }

    public void removeBlock(BlockVector position) {
        this.blockMap.remove(position);
    }

}
