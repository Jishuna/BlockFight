package me.jishuna.blockfight.player;

import java.time.Instant;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.joml.Math;
import me.jishuna.blockfight.Settings;
import me.jishuna.blockfight.block.CombatBlock;
import me.jishuna.jishlib.message.Messages;
import me.jishuna.jishlib.util.NumberUtils;
import me.jishuna.jishlib.util.StringUtils;

public class BlockHealthBar {
    private final UUID id;
    private final Player owner;
    private final BossBar bossBar = Bukkit.createBossBar("", BarColor.RED, BarStyle.SOLID);
    private Instant hideTime = Instant.now();

    public BlockHealthBar(UUID id) {
        this.id = id;
        this.owner = Bukkit.getPlayer(id);
    }

    public void updateHealth(CombatBlock block) {
        this.bossBar.setProgress(NumberUtils.clamp(block.getHealth() / block.getMaxHealth(), 0, 1));
        updateTitle(block);
        this.bossBar.addPlayer(this.owner);

        this.hideTime = Instant.now().plusMillis(Settings.BAR_HIDE_DELAY * 50l);
    }

    public void checkAndHide() {
        if (shouldHide()) {
            hide();
        }
    }

    public boolean shouldHide() {
        return this.hideTime.isBefore(Instant.now());
    }

    public void hide() {
        this.bossBar.removePlayer(this.owner);
    }

    public UUID getId() {
        return this.id;
    }

    private void updateTitle(CombatBlock block) {
        String name = StringUtils.capitalizeAll(block.getType().name().replace('_', ' '));
        int health = Math.max(0, (int) Math.ceil(block.getHealth()));
        int maxHealth = (int) Math.ceil(block.getMaxHealth());

        this.bossBar.setTitle(Messages.get("block.health-bar.title", name, health, maxHealth));
    }
}
