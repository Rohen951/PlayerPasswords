package org.firewolf8385.playerpasswords.listeners;

import org.firewolf8385.playerpasswords.SettingsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.firewolf8385.playerpasswords.objects.PasswordPlayer;
import org.firewolf8385.playerpasswords.Utils;

public class PlayerChat implements Listener
{
    SettingsManager settings = SettingsManager.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e)
    {
        PasswordPlayer p = PasswordPlayer.getPlayers().get(e.getPlayer().getUniqueId());

        // Return if Verified
        if(p.isVerified())
        {
            return;
        }

        // Exit if BlockChat is false.
        if(!settings.getConfig().getBoolean("BlockChat"))
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
