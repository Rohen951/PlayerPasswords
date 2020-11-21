package org.firewolf8385.playerpasswords;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;

public class SettingsManager
{

    private SettingsManager() {}
    static SettingsManager instance = new SettingsManager();

    /**
     * This allows us to access an instance of this class.
     */
    public static SettingsManager getInstance()
    {
        return instance;
    }

    Plugin pl;
    private FileConfiguration config;
    private File configFile;

    private FileConfiguration data;
    private File dataFile;

    // Set current config version for plugin
    private int currentConfigVersion = 3;


    /**
     * This allows us to set up the config file if it does not exist.
     * @param pl Instance of the Plugin
     */
    public void setup(Plugin pl)
    {
        config = pl.getConfig();
        config.options().copyDefaults(true);
        configFile = new File(pl.getDataFolder(), "config.yml");

        if(config.getInt("ConfigVersion") != currentConfigVersion)
        {
            // If version of config.yml is outdated then rename it a save default
            File curConfigFile = new File(pl.getDataFolder(),"config.yml");
            File renConfigFile = new File(pl.getDataFolder(),"config.yml.old");
            curConfigFile.renameTo(renConfigFile);
            if(config.getBoolean("ConsoleVerbose")) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &cFile config.yml outdated, saving default to plugin directory");
        }
        else if(!configFile.exists())
        {
            if(config.getBoolean("ConsoleVerbose")) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7File config.yml created");
        }
        else
        {
            if(config.getBoolean("ConsoleVerbose")) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7File config.yml readed");
        }
        pl.saveDefaultConfig();

        dataFile = new File(pl.getDataFolder(), "data.yml");
        data = YamlConfiguration.loadConfiguration(dataFile);

        if(!dataFile.exists())
        {
            try
            {
                dataFile.createNewFile();
                if(config.getBoolean("ConsoleVerbose")) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7File data.yml created");
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            if(config.getBoolean("ConsoleVerbose")) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7File data.yml readed");
        }
    }

    /**
     * Allows us to access the config file.
     * @return config file
     */
    public FileConfiguration getConfig()
    {
        return config;
    }

    /**
     * Allows us to save the config file after changes are made.
     */
    public void saveConfig()
    {
        try
        {
            config.save(configFile);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This updates the config in case changes are made.
     */
    public void reloadConfig()
    {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Allows us to access the data file.
     * @return data file
     */
    public FileConfiguration getData()
    {
        return data;
    }

    /**
     * Allows us to save the config file after changes are made.
     */
    public void saveData()
    {
        try
        {
            data.save(dataFile);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This updates the data in case changes are made.
     */
    public void reloadData()
    {
        data = YamlConfiguration.loadConfiguration(dataFile);

        if(!dataFile.exists())
        {
            try
            {
                dataFile.createNewFile();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public boolean isConfigOutdated()
    {
        if(config.getInt("ConfigVersion") != currentConfigVersion)
        {
            return true;
        }
        return false;
    }
}