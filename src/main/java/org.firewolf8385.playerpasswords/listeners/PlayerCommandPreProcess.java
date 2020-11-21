package org.firewolf8385.playerpasswords.listeners;

import org.firewolf8385.playerpasswords.SettingsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.firewolf8385.playerpasswords.objects.PasswordPlayer;
import org.firewolf8385.playerpasswords.Utils;


public class PlayerCommandPreProcess implements Listener
{
    SettingsManager settings = SettingsManager.getInstance();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e)
    {
        PasswordPlayer p = PasswordPlayer.getPlayers().get(e.getPlayer().getUniqueId());

        // Exit if player is verified.
        if(p.isVerified())
        {
            return;
        }

        String[] args = e.getMessage().split(" ");

        // Exit if command is /login or /register
        if(args[0].equalsIgnoreCase("/login") || args[0].equalsIgnoreCase("/register"))
        {
            return;
        }

        // Exit if BlockCommands is disabled.
        if(!settings.getConfig().getBoolean("BlockCommands"))
        {
            return;
        }

        // Cancel Event.
        if(settings.getConfig().getBoolean("ShowMsgMustBeLoggedIn"))
        {
            Utils.chat(e.getPlayer(), settings.getConfig().getString("MustBeLoggedIn"));
        }
        e.setCancelled(true);
    }

}