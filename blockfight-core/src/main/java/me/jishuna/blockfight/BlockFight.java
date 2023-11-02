package me.jishuna.blockfight;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import me.jishuna.blockfight.block.WorldBlockManager;
import me.jishuna.blockfight.listener.BlockAttackListener;
import me.jishuna.blockfight.listener.UnloadListener;
import me.jishuna.blockfight.player.PlayerManager;
import me.jishuna.jishlib.JishLib;
import me.jishuna.jishlib.config.ConfigReloadable;

public class BlockFight extends JavaPlugin {
    private final WorldBlockManager worldBlockManager = new WorldBlockManager();
    private PlayerManager playerManager;
    private ConfigReloadable<Settings> settings;

    @Override
    public void onEnable() {
        JishLib.initialize(this);
        this.settings = JishLib.getConfigurationManager().createStaticReloadable(new File(getDataFolder(), "config.yml"), Settings.class).saveDefaults();
        reload();

        this.playerManager = new PlayerManager(this);

        Bukkit.getPluginManager().registerEvents(new BlockAttackListener(this.worldBlockManager, this.playerManager), this);
        Bukkit.getPluginManager().registerEvents(new UnloadListener(this.worldBlockManager), this);
    }

    @Override
    public void onDisable() {
        JishLib.cleanup();
    }

    public void reload() {
        JishLib.loadMessages("messages.txt");
        this.settings.load();
    }

    public WorldBlockManager getWorldBlockManager() {
        return this.worldBlockManager;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
}
