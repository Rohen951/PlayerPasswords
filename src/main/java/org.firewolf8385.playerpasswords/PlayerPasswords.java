package org.firewolf8385.playerpasswords;

import org.firewolf8385.playerpasswords.commands.Login;
import org.firewolf8385.playerpasswords.commands.PP;
import org.firewolf8385.playerpasswords.commands.Password;
import org.firewolf8385.playerpasswords.commands.Register;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.firewolf8385.playerpasswords.listeners.PlayerChat;
import org.firewolf8385.playerpasswords.listeners.PlayerCommandPreProcess;
import org.firewolf8385.playerpasswords.listeners.PlayerDropItem;
import org.firewolf8385.playerpasswords.listeners.PlayerInteract;
import org.firewolf8385.playerpasswords.listeners.PlayerJoin;
import org.firewolf8385.playerpasswords.listeners.PlayerMove;
import org.firewolf8385.playerpasswords.listeners.PlayerQuit;
import org.firewolf8385.playerpasswords.objects.PasswordPlayer;
import org.firewolf8385.playerpasswords.objects.SessionList; 

public class PlayerPasswords extends JavaPlugin
{
    /***************************************************************************************
     *    Title: PlayerPasswords
     *    Author: firewolf8385
     *    Date: November 19th, 2020
     *    Code version: 1.4
     ***************************************************************************************/
    SettingsManager settings = SettingsManager.getInstance();
    UpdateChecker updater = UpdateChecker.getInstance();

    private boolean verbose;
    private int scheduler;
    private boolean scheduler_running = false;
    
 
    /**
     * This runs necessary tasks when the plugin is enabled.
     */
    public void onEnable()
    {
        // Enables bStats
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this);

        settings.setup(this);

        // Read and set verbose variable
        verbose = settings.getConfig().getBoolean("ConsoleVerbose");
        
        registerCommands();
        registerEvents();

        // Checks for any new updates.
        updater.setVerbose(verbose);
        updater.UpdateCheck(this.getDescription().getVersion());

        // Adds all online players to the verified list.
        // This fixes issues with reloading.
        for(Player p : Bukkit.getOnlinePlayers())
        {
            PasswordPlayer pl = new PasswordPlayer(p.getUniqueId());
            pl.setVerified(true);
        }
        
        // If IPsession is true in config, start scheduler to remove obsolete sessions from SessionList in one minute intervals
        if(settings.getConfig().getBoolean("IPSession"))
        {
            startScheduler();
        }
    }

    /**
     * This registers the plugin's commands.
     */
    private void registerCommands()
    {
        if(verbose) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7Registering commands");
      
        getCommand("login").setExecutor(new Login());
        getCommand("register").setExecutor(new Register());
        getCommand("playerpasswords").setExecutor(new PP((PlayerPasswords)this));
        getCommand("password").setExecutor(new Password());
    }

    /**
     * This registers events the plugin uses.
     */
    private void registerEvents()
    {
        if(verbose) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7Registering events");
      
        Bukkit.getPluginManager().registerEvents(new PlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin((PlayerPasswords)this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreProcess(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropItem(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
    }

    /**
     * Run scheduler.
     */
    public void startScheduler()
    {
        if(!scheduler_running)
        {
            scheduler = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    SessionList.checkSessions();
                }
            }, 0L, 1200L);
            scheduler_running = true;
            if(verbose) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7Session Checker started");
        }
    }

    /**
     * Stop scheduler.
     */
    public void stopScheduler()
    {
        if(scheduler_running)
        {
            Bukkit.getScheduler().cancelTask(scheduler);
            scheduler_running = false;
            if(verbose) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7Session Checker stopped");
        }
    }
    
    public boolean isVerbose()
    {
        return verbose;
    }
}
