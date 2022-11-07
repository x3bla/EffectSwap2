package effectswap2.effectswap2.listeners;


import effectswap2.effectswap2.EffectSwap2;
import effectswap2.effectswap2.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class DrinkEffectRerollListener implements Listener {

    Random random = new Random();

    public void potion5050(Player p) {
        if (Utils.positiveEffectCheck(p, true)) {  // if there's no effects, or if there are positive effects, return
            p.sendMessage(ChatColor.YELLOW + "Nothing seemed to happen... Perhaps you don't have a negative effect on you?");
            return;
        }

        ArrayList<PotionEffectType> playerNegativeEffectList = EffectSwap2.getEffects(p);

        if (EffectSwap2.getEffects(p).containsAll(EffectSwap2.negativeEffectList)) {
            Utils.removeNegativeEffect(p);  // if the poor fella has all the negative effects, just give them a freebie
            return;
        }

        if (random.nextBoolean()) {
            if (playerNegativeEffectList.isEmpty()) {
                p.sendMessage(ChatColor.YELLOW + "Nothing seemed to happen...");
                return;  // nothing happens if they have no negative effects
            }

            Utils.removeNegativeEffect(p);

        } else {
            Utils.giveNegativeEffect(p);
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {

        if (!ItemCraftListener.itemIsEffectReroll(e.getItem())) return;

        final Player p = e.getPlayer();
        ItemStack item = e.getItem();
        item.setAmount(item.getAmount() - 1);
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0));
        (new BukkitRunnable() {
            public void run() {
                potion5050(p);
            }
        }).runTaskLater(EffectSwap2.getInstance(), 100L);
    }


}
