package org.firewolf8385.playerpasswords.commands;

import org.firewolf8385.playerpasswords.SettingsManager;
import org.firewolf8385.playerpasswords.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.firewolf8385.playerpasswords.objects.PasswordPlayer;
import org.firewolf8385.playerpasswords.utils.StringUtils;

public class Register implements CommandExecutor
{
    SettingsManager settings = SettingsManager.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        // Exit if not a player.
        if(!(sender instanceof Player))
        {
            Utils.chat(sender, "&6[&aPlayerPasswords&6] &cCommand not supported");
            return true;
        }

        Player p = (Player) sender;
        String uuid = p.getUniqueId().toString();
        PasswordPlayer pl = PasswordPlayer.getPlayers().get(p.getUniqueId());
        int minimum = settings.getConfig().getInt("MinimumPasswordLength");
        int maximum = settings.getConfig().getInt("MaximumPasswordLength");

        // If the player is already logged in.
        if(pl.isVerified())
        {
            Utils.chat(p, settings.getConfig().getString("AlreadyLoggedIn"));
            return true;
        }

        // If the player already set their password.
        if(!settings.getData().getString("passwords." + uuid + ".password").equals(""))
        {
            Utils.chat(p, settings.getConfig().getString("AlreadyRegistered"));
            return true;
        }

        // If the player did not enter a password.
        if(args.length == 0)
        {
            if(!settings.getConfig().getBoolean("RegisterPasswordRetype"))
                Utils.chat(p, settings.getConfig().getString("RegisterUsage"));
            else
                Utils.chat(p, settings.getConfig().getString("RegisterRetypeUsage"));
            return true;
        }

        // If the player did not re-type a password while it is required.
        if(settings.getConfig().getBoolean("RegisterPasswordRetype") && args.length == 1)
        {
            Utils.chat(p, settings.getConfig().getString("RegisterRetypeUsage"));
            return true;
        }
        
        // Shows the player a message if their password does not fit the requirements.
        if(!(args[0].length() >= minimum && args[0].length() <= maximum))
        {
            Utils.chat(p, settings.getConfig().getString("OutOfBounds").replace("%minpass%", String.valueOf(minimum)).replace("%maxpass%", String.valueOf(maximum)));
            return true;
        }

        // If the passwords are not identical.
        if(settings.getConfig().getBoolean("RegisterPasswordRetype") && !(args[0].equals(args[1])))
        {
            Utils.chat(p, settings.getConfig().getString("NotIdentical"));
            return true;
        }
        
        settings.getData().set("passwords." + uuid + ".password", StringUtils.hash(args[0]));
        Utils.chat(p, settings.getConfig().getString("RegisterSuccessful"));

        if(!pl.isVerified())
        {
            pl.setVerified(true);
        }

        if(!(settings.getData().getBoolean("passwords." + uuid + ".enabled")))
        {
            settings.getData().set("passwords." + uuid + ".enabled", true);
        }

        settings.saveData();
        settings.reloadData();

        return true;
    }
}