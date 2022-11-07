package effectswap2.effectswap2;

import effectswap2.effectswap2.commands.DebugCommand;
import effectswap2.effectswap2.commands.ReloadConfigCommand;
import effectswap2.effectswap2.commands.TabCompleterWithdraw;
import effectswap2.effectswap2.commands.WithdrawCommand;
import effectswap2.effectswap2.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;

public final class EffectSwap2 extends JavaPlugin {

    private static final Random random = new Random();

    private static final HashMap<UUID, ArrayList<PotionEffectType>> effectMap = new HashMap<>();

    private static EffectSwap2 instance;

    public final static ArrayList<PotionEffectType> positiveEffectList = new ArrayList<>();

    public final static ArrayList<PotionEffectType> negativeEffectList = new ArrayList<>();

    public void onEnable() {
        instance = this;
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File playersDir = new File(getDataFolder() + "/players");
        if (!playersDir.exists())
            playersDir.mkdir();
        saveDefaultConfig();

        for (String s : EffectSwap2.getInstance().getConfig().getStringList("positive-effects")) {
            positiveEffectList.add(PotionEffectType.getByName(s));
        }

        for (String s : EffectSwap2.getInstance().getConfig().getStringList("negative-effects")) {
            negativeEffectList.add(PotionEffectType.getByName(s));
        }

        getCommand("reloadconfig").setExecutor(new ReloadConfigCommand());
        getCommand("debug").setExecutor(new DebugCommand());
        getCommand("withdraw").setExecutor(new WithdrawCommand());
        getCommand("withdraw").setTabCompleter(new TabCompleterWithdraw());
        registerEvents();
    }

    public static Player uuidToPlayer(UUID uuid) {
        return Bukkit.getServer().getPlayer(uuid);
    }

    public static HashMap<UUID, ArrayList<PotionEffectType>> getEffectMap() {
        return effectMap;
    }

    private static void addToEffectMap(UUID uuid, ArrayList<PotionEffectType> effects) {
        ArrayList<PotionEffectType> playerEffects = effectMap.get(uuid);
        if (playerEffects == null) {
            playerEffects = new ArrayList<>();
        }
        playerEffects.addAll(effects);

        effectMap.put(uuid, playerEffects);
    }

    // unused
    public static void removeFromEffectMap(UUID uuid, PotionEffectType effect) {
        ArrayList<PotionEffectType> playerEffects = effectMap.get(uuid);
        playerEffects.remove(effect);
        effectMap.put(uuid, playerEffects);
    }

    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new LoseEffectListener(), this);
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new PlayerKillPlayerListener(), this);
        pm.registerEvents(new DrinkEffectRerollListener(), this);
        pm.registerEvents(new DrinkLuckyPotionListener(), this);
        pm.registerEvents(new DrinkRemoveNegativeEffectListener(), this);
        pm.registerEvents(new ItemCraftListener(), this);
        pm.registerEvents(new NaturalDeathListener(), this);
        pm.registerEvents(new DrinkLuckyPotionListener(), this);

    }

    public static ArrayList<PotionEffectType> getEffects(Player p) {
        UUID uuid = p.getUniqueId();
        if (effectMap.containsKey(uuid))
            return effectMap.get(uuid);
        File file = new File(instance.getDataFolder() + "/players", uuid + ".yml");
        if (!file.exists()) {
            System.out.println("file/folder does not exist, please restart your server");
            return null;
        }
        FileUtility fileUtility = new FileUtility(p, instance);
        List<String> effectString = fileUtility.getConfig().getStringList(p.getName() + ".effect");
        if (effectString.isEmpty()) {
            ArrayList<PotionEffectType> emptyList = new ArrayList<>();
            addToEffectMap(uuid, emptyList);
            return effectMap.get(uuid);
        }
        ArrayList<PotionEffectType> effects = new ArrayList<>();
        for (String effect : effectString) {
            effects.add(PotionEffectType.getByName(effect));
        }
        addToEffectMap(uuid, effects);
        return effectMap.get(uuid);
    }

    // try not to use this
    public static void setEffect(Player p, PotionEffectType effect) {
        FileUtility fileUtility = new FileUtility(p, instance);
        ArrayList<PotionEffectType> oldEffects = getEffects(p);
        ArrayList<PotionEffectType> newEffects = new ArrayList<>();

        if (effect == null) {
            effectMap.remove(p.getUniqueId());
            fileUtility.set("effect", null);
        } else {
            newEffects.add(effect);
            effectMap.put(p.getUniqueId(), newEffects);
            fileUtility.set("effect", effect.getName());
        }

        if (oldEffects != null)
            for (PotionEffectType potionEffectType : oldEffects) {
                p.removePotionEffect(potionEffectType);
            }

        loadPlayerEffect(p);
    }

    // don't use this, use the one in Utils
    public static void addEffect(Player p, PotionEffectType effect) {
        FileUtility fileUtility = new FileUtility(p, instance);
        ArrayList<PotionEffectType> playerEffects = getEffects(p);
        playerEffects.add(effect);

        effectMap.put(p.getUniqueId(), playerEffects);
        ArrayList<String> effects = new ArrayList<>();
        for (PotionEffectType potionEffectType : effectMap.get(p.getUniqueId())) {
            effects.add(potionEffectType.getName());
        }
        fileUtility.set("effect", effects);

        loadPlayerEffect(p);
    }

    // don't use this, use the one in Utils
    public static void removeEffect(Player p, PotionEffectType effect) {
        FileUtility fileUtility = new FileUtility(p, instance);
        ArrayList<PotionEffectType> playerEffects = getEffects(p);
        playerEffects.remove(effect);

        effectMap.put(p.getUniqueId(), playerEffects);
        ArrayList<String> effects = new ArrayList<>();
        for (PotionEffectType potionEffectType : effectMap.get(p.getUniqueId())) {
            effects.add(potionEffectType.getName());
        }
        fileUtility.set("effect", effects);

        p.removePotionEffect(effect);
        loadPlayerEffect(p);
    }

    // this will be used automatically
    public static void loadPlayerEffect(Player p) {
        ArrayList<PotionEffectType> effects = getEffects(p);
        if (effects != null) {
            for (PotionEffectType effect : effects) {
                p.addPotionEffect(new PotionEffect(effect, Integer.MAX_VALUE, 0, false, false, true));
            }
        }
    }

    public static PotionEffectType setRandomEffect(Player p) {
        ArrayList<PotionEffectType> potionList = new ArrayList<>();
        instance.reloadConfig();

        potionList.addAll(positiveEffectList);
        potionList.addAll(negativeEffectList);

        PotionEffectType effect = potionList.get(random.nextInt(potionList.size()));
        setEffect(p, effect);
        return effect;
    }

    @Deprecated  // use setEffect or look in Utils
    public static PotionEffectType setNegativeEffect(Player p) {
        ArrayList<PotionEffectType> negativeList = new ArrayList<>(negativeEffectList);

        PotionEffectType effect = negativeList.get(random.nextInt(negativeList.size()));
        setEffect(p, effect);
        return effect;
    }

    @Deprecated  // use setEffect or look in Utils
    public static PotionEffectType setPositiveEffect(Player p) {
        ArrayList<PotionEffectType> positiveList = new ArrayList<>(positiveEffectList);

        PotionEffectType effect = positiveList.get(random.nextInt(positiveList.size()));
        setEffect(p, effect);
        return effect;
    }


    public static EffectSwap2 getInstance() {
        return instance;
    }
}
