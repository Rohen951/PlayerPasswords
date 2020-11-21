package org.firewolf8385.playerpasswords.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.firewolf8385.playerpasswords.objects.PasswordPlayer;
import org.firewolf8385.playerpasswords.objects.SessionList;
import org.firewolf8385.playerpasswords.SettingsManager;

public class PlayerQuit implements Listener
{
    SettingsManager settings = SettingsManager.getInstance();
  
    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        // If IPsession is true in config, save last seen time, IP and player UUID for session login
        PasswordPlayer pl = PasswordPlayer.getPlayers().get(e.getPlayer().getUniqueId());
        if(settings.getConfig().getBoolean("IPSession") && pl.isVerified() && pl.isRequired())
        {

            // TODO: Maybe do not save session for kicked player

            new SessionList(e.getPlayer().getUniqueId());
        }

        // Remove player from list of players.
        PasswordPlayer.getPlayers().remove(e.getPlayer().getUniqueId());
    }

}
