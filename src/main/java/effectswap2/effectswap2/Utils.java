package effectswap2.effectswap2;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;



// might seem messy/bulky. If you have a better way of optimizing it, go ahead
public class Utils {  // I wanted to move more functions from the main file to here but that's too much work lol

    private final static ArrayList<PotionEffectType> positiveEffectList = EffectSwap2.positiveEffectList;

    private final static ArrayList<PotionEffectType> negativeEffectList = EffectSwap2.negativeEffectList;

    private static final Random random = new Random();

    private static ArrayList<PotionEffectType> findPossibleEffects(Player p, ArrayList<PotionEffectType> compareList) {
        ArrayList<PotionEffectType> possibleEffects = new ArrayList<>();
        ArrayList<PotionEffectType> playerEffects = new ArrayList<>();
        for (PotionEffect potionEffect : p.getActivePotionEffects()) {  // the other guy used PotionEffectType instead of PotionEffect :/
            playerEffects.add(potionEffect.getType());
        }

        for (PotionEffectType effect : compareList) {
            if (!playerEffects.contains(effect)) {
                possibleEffects.add(effect);
            }
        }
        return possibleEffects;
    }

    // true if you want to return a true if the player has no effects
    public static boolean positiveEffectCheck(Player p, boolean emptyCheck) {
        ArrayList<PotionEffectType> playerEffectList = EffectSwap2.getEffects(p);

        if (playerEffectList == null || playerEffectList.isEmpty()) {
            return emptyCheck;
        }

        return positiveEffectList.containsAll(playerEffectList);
    }

    public static PotionEffectType givePositiveEffect(Player p) {
        ArrayList<PotionEffectType> possibleEffects = findPossibleEffects(p, positiveEffectList);
        if (possibleEffects.isEmpty()) { // if player has all positive effects, return null
            return null;
        }

        PotionEffectType newEffect = possibleEffects.get(random.nextInt(possibleEffects.size()));
        EffectSwap2.addEffect(p, newEffect);
        return newEffect;
    }

    public static PotionEffectType givePositiveEffect(Player p, PotionEffectType effectType) {
        ArrayList<PotionEffectType> possibleEffects = findPossibleEffects(p, positiveEffectList);
        if (possibleEffects.isEmpty()) { // if player has all positive effects, return null
            return null;
        }

        if (possibleEffects.contains(effectType)) {
            EffectSwap2.addEffect(p, effectType);
            return effectType;
        } else {
            return null;   // if player already has the effect, return null
        }
    }

    public static PotionEffectType removePositiveEffect(Player p) {
        ArrayList<PotionEffectType> playerEffects = EffectSwap2.getEffects(p);

        PotionEffectType effect = playerEffects.get(random.nextInt(playerEffects.size()));
        EffectSwap2.removeEffect(p, effect);
        return effect;
    }

    public static PotionEffectType removePositiveEffect(Player p, PotionEffectType effectType) {
        ArrayList<PotionEffectType> playerEffects = EffectSwap2.getEffects(p);

        EffectSwap2.removeEffect(p, effectType);
        return effectType;
    }

    public static PotionEffectType giveNegativeEffect(Player p) {
        ArrayList<PotionEffectType> possibleEffects = findPossibleEffects(p, negativeEffectList);
        if (possibleEffects.isEmpty()) { // if person has all negative effects, return null
            return null;
        }

        PotionEffectType newEffect = possibleEffects.get(random.nextInt(possibleEffects.size()));
        EffectSwap2.addEffect(p, newEffect);
        return newEffect;
    }

    public static PotionEffectType removeNegativeEffect(Player p) {
        ArrayList<PotionEffectType> playerEffects = EffectSwap2.getEffects(p);

        PotionEffectType effect = playerEffects.get(random.nextInt(playerEffects.size()));
        EffectSwap2.removeEffect(p, effect);
        return effect;
    }

    public static ArrayList<PotionEffectType> swapEffect(Player victim, Player killer) {
        ArrayList<PotionEffectType> victimEffects = EffectSwap2.getEffects(victim);
        ArrayList<PotionEffectType> killerEffects = EffectSwap2.getEffects(killer);
        ArrayList<PotionEffectType> effects = new ArrayList<>();

        if (victimEffects.size() == 1 && killerEffects.size() == 1) {
            PotionEffectType victimEffect = victimEffects.get(0);
            PotionEffectType killerEffect = killerEffects.get(0);
            EffectSwap2.setEffect(killer, victimEffect);
            EffectSwap2.setEffect(victim, killerEffect);

            effects.add(victimEffect);
            effects.add(killerEffect);
            return effects;
        }

        PotionEffectType victimEffect = victimEffects.get(random.nextInt(victimEffects.size()));
        PotionEffectType killerEffect = killerEffects.get(random.nextInt(killerEffects.size()));

        EffectSwap2.removeEffect(victim, victimEffect);
        EffectSwap2.addEffect(victim, killerEffect);
        EffectSwap2.removeEffect(killer, killerEffect);
        EffectSwap2.addEffect(killer, victimEffect);

        effects.add(victimEffect);
        effects.add(killerEffect);
        return effects;

    }
}
