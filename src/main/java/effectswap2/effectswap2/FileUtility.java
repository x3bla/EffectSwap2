package effectswap2.effectswap2;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class FileUtility {
    File file = null;

    FileConfiguration configuration = null;

    OfflinePlayer player;

    JavaPlugin plugin;

    public FileUtility(OfflinePlayer player, JavaPlugin plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    public FileConfiguration getConfig() {
        if (this.configuration == null) {
            this.configuration = YamlConfiguration.loadConfiguration(getFile());
            return this.configuration;
        }
        return this.configuration;
    }

    public void saveConfig() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Cannot save to " + this.file.getName());
            this.plugin.getLogger().log(Level.SEVERE, ExceptionUtils.getStackTrace(e));
        }
    }

    public void set(String path, Object object) {
        getConfig().set(this.player.getName() + "." + path, object);
        saveConfig();
    }

    public File getFile() {
        if (this.file == null) {
            this.file = new File(this.plugin.getDataFolder() + "/players", this.player.getUniqueId() + ".yml");
            if (!this.file.exists())
                try {
                    if (this.file.createNewFile()) {
                        this.plugin.getLogger().log(Level.INFO, "File for player " + this.player.getName() + " has been created");
                        this.plugin.getLogger().log(Level.INFO, "Saved as " + this.player.getUniqueId() + ".yml");
                    }
                } catch (IOException e) {
                    this.plugin.getLogger().log(Level.SEVERE, "Cannot create data for " + this.player.getName());
                    this.plugin.getLogger().log(Level.SEVERE, ExceptionUtils.getStackTrace(e));
                }
            return this.file;
        }
        return this.file;
    }

    public void reloadConfig() {
        YamlConfiguration.loadConfiguration(this.file);
    }
}