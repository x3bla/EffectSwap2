package effectswap2.effectswap2.listeners;

import effectswap2.effectswap2.EffectSwap2;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class LoseEffectListener implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        if (EffectSwap2.getEffects(p) == null)
            return;
        (new BukkitRunnable() {
            public void run() {
                EffectSwap2.loadPlayerEffect(p);
            }
        }).runTaskLater(EffectSwap2.getInstance(), 2L);
    }

    @EventHandler
    public void onTotemPop(EntityResurrectEvent e) {
        if (e.getEntityType() != EntityType.PLAYER)
            return;
        final Player p = (Player)e.getEntity();
        (new BukkitRunnable() {
            public void run() {
                EffectSwap2.loadPlayerEffect(p);
            }
        }).runTaskLater(EffectSwap2.getInstance(), 2L);
    }

    @EventHandler
    public void onConsume(final PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.MILK_BUCKET)
            (new BukkitRunnable() {
                public void run() {
                    EffectSwap2.loadPlayerEffect(e.getPlayer());
                }
            }).runTaskLater(EffectSwap2.getInstance(), 2L);
    }
}