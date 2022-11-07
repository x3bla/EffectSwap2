package effectswap2.effectswap2.listeners;

import effectswap2.effectswap2.PositiveEffectPotionManager;
import effectswap2.effectswap2.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.function.Supplier;

public class DrinkPositiveEffectPotionListener implements Listener {

    @EventHandler
    public void onDrinkPositiveEffectPotion(PlayerItemConsumeEvent e) {
        if (!(e.getItem().getType().equals(Material.POTION))) return;

        ItemStack potion = e.getItem();
        Supplier<PotionEffectType> potionEffectType = () -> {  // retrieve PotionEffectType from ItemStack
                PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                return (PotionEffectType) potionMeta;
            };
        Player p = e.getPlayer();

        if (PositiveEffectPotionManager.isPositivePotion(potion)) {
            PotionEffectType playerEffect = Utils.givePositiveEffect(p, potionEffectType.get());
            if (playerEffect != null) {
                p.sendMessage(ChatColor.AQUA + "You have gained the effect of the potion: " + potionEffectType.get());
            } else {
                p.sendMessage(ChatColor.YELLOW + "You already have the effect in the potion");
                e.setCancelled(true);
            }
        }


    }
}
