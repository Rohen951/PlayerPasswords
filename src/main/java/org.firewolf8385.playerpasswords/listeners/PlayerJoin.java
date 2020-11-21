package org.firewolf8385.playerpasswords.listeners;

import org.firewolf8385.playerpasswords.PlayerPasswords;
import org.firewolf8385.playerpasswords.SettingsManager;
import org.firewolf8385.playerpasswords.UpdateChecker;
import org.firewolf8385.playerpasswords.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.firewolf8385.playerpasswords.objects.PasswordPlayer;
import org.firewolf8385.playerpasswords.objects.SessionList;
import org.bukkit.Bukkit;

public class PlayerJoin implements Listener
{
    SettingsManager settings = SettingsManager.getInstance();
    UpdateChecker updater = UpdateChecker.getInstance();

    private String gold = settings.getConfig().getString("color1");
    private String gray = settings.getConfig().getString("color3");
    private PlayerPasswords plugin;
    
    public PlayerJoin(PlayerPasswords plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler (priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString();

        // Creates a new section if the player has not joined before.
        if(!settings.getData().contains("passwords." + uuid))
        {
            settings.getData().set("passwords." + uuid + ".password", "");
            settings.getData().set("passwords." + uuid + ".enabled", false);
            settings.saveData();
            settings.reloadData();
        }

        PasswordPlayer pl = new PasswordPlayer(p.getUniqueId());
        
        // If IPsession is true in config then check player if he is back after logout within session timeout
        // and log him automatically
        if(settings.getConfig().getBoolean("IPSession") && pl.isRequired())
        {
            if(SessionList.isBackInTime(p.getUniqueId()))
            {
                pl.setVerified(true);
            }
        }

        // Delay showing messages because of lowest priority, sometimes this messages do not show in chat when other plugins do they work on join
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {

              if(p.hasPermission("playerpasswords.admin"))
                {
                    if(updater.updateAvailable())
                    {
                        Utils.chat(p, settings.getConfig().getString("UpdateAvailable").replace("%version%", updater.getLatestVersion()));
                    }
        
                    if(settings.isConfigOutdated())
                    {
                        Utils.chat(p, settings.getConfig().getString("OutdatedConfig"));
                    }
                }
        
                if(pl.isRequired())
                {
                    if(pl.isVerified())
                    {
                        Utils.chat(p, settings.getConfig().getString("LogInSuccessful"));
                    }
                    else
                    {
                        if(settings.getData().getString("passwords." + uuid + ".password").equals(""))
                        {
                            Utils.chat(p, "\n" + gold + "&l[" + gray + "&m---------------------" + gold + "&lRegister" + gray + "&m----------------------" + gold +"&l]");
                            if(!settings.getConfig().getBoolean("RegisterPasswordRetype"))
                                Utils.chat(p, "   " + settings.getConfig().getString("Register"));
                            else
                                Utils.chat(p, "   " + settings.getConfig().getString("RegisterRetype"));
                        }
                        else
                        {
                            Utils.chat(p, "\n" + gold + "&l[" + gray + "&m-----------------------" + gold + "&lLogin" + gray + "&m-----------------------" + gold +"&l]");
                            Utils.chat(p, "   " + settings.getConfig().getString("Login"));
                        }
                        Utils.chat(p, gold + "&l[" + gray +"&m---------------------------------------------------" + gold + "&l]\n");
                    }
                }
            }
        }, 5L);
    }
}
