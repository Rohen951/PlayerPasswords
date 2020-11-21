package org.firewolf8385.playerpasswords.commands;

import org.firewolf8385.playerpasswords.SettingsManager;
import org.firewolf8385.playerpasswords.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.firewolf8385.playerpasswords.objects.PasswordPlayer;
import org.firewolf8385.playerpasswords.utils.StringUtils;
import java.util.Date;
import org.bukkit.BanList;
import org.bukkit.Bukkit;

public class Login implements CommandExecutor
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

        // If the player don't have registered password 
        if(!pl.isVerified() && settings.getData().getString("passwords." + uuid + ".password").equals(""))
        {
            Utils.chat(p, settings.getConfig().getString("MustBeRegistered"));
            return true;
        }

        // If the player is already logged in, they can't log in again.
        if(pl.isVerified())
        {
            Utils.chat(p, settings.getConfig().getString("AlreadyLoggedIn"));
            return true;
        }
        
        // If The Command Is Run Without Args, Show Error Message
        if(args.length == 0)
        {
            Utils.chat(p, settings.getConfig().getString("LoginUsage"));
            return true;
        }

        // Entered password is OK
        if(StringUtils.hash(args[0]).equals(settings.getData().getString("passwords." + uuid + ".password")))
        {
            Utils.chat(p, settings.getConfig().getString("LogInSuccessful"));
            pl.setVerified(true);
        }
        // Entered password is wrong
        else
        {
            int manyTriesBan = settings.getConfig().getInt("TooManyTriesBanTime");
            if(manyTriesBan < 1) manyTriesBan = 1;
            int wrongPassBan = settings.getConfig().getInt("WrongPasswordBanTime");
            if(wrongPassBan < 1) wrongPassBan = 1;
            
            String manyTriesBanReason = settings.getConfig().getString("BanMessage").replace("%bantime%", String.valueOf(manyTriesBan));
            if(manyTriesBan > 1){manyTriesBanReason = manyTriesBanReason.replace("%hourtext%",settings.getConfig().getString("HoursText"));}
            else {manyTriesBanReason = manyTriesBanReason.replace("%hourtext%",settings.getConfig().getString("HourText"));}
            
            String wrongPassBanReason = settings.getConfig().getString("BanMessage").replace("%bantime%", String.valueOf(wrongPassBan));
            if(wrongPassBan > 1){wrongPassBanReason = wrongPassBanReason.replace("%hourtext%",settings.getConfig().getString("HoursText"));}
            else {wrongPassBanReason = wrongPassBanReason.replace("%hourtext%",settings.getConfig().getString("HourText"));}
            
            manyTriesBanReason = ChatColor.translateAlternateColorCodes('&', manyTriesBanReason);
            wrongPassBanReason = ChatColor.translateAlternateColorCodes('&', wrongPassBanReason);

            // Wrong password, but try again
            if(settings.getConfig().getString("WrongPassword").toLowerCase().equals("tryagain"))
            {
                // Too many tries and still wrong password
                if(pl.isTooManyTimes(settings.getConfig().getInt("WrongTries")))
                {
                    // Kick player for repeatedly entering wrong password
                    if(settings.getConfig().getString("TooManyTries").toLowerCase().equals("kick"))
                    {
                        p.kickPlayer(ChatColor.translateAlternateColorCodes('&', settings.getConfig().getString("KickMessageTooMuch")));
                    }
                    // Or ban player for repeatedly entering wrong password
                    else
                    {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), manyTriesBanReason, new Date(System.currentTimeMillis()+manyTriesBan*3600*1000), null);
                        p.kickPlayer(manyTriesBanReason);
                    }
                }
                // Increase wrong password counter if is bellow value from config 
                else
                {
                    Utils.chat(p, settings.getConfig().getString("PasswordIncorrect"));
                    pl.increaseWrongCounter();
                }
            }
            // Ban player for trying to guess password once
            else if(settings.getConfig().getString("WrongPassword").toLowerCase().equals("ban"))
            {
                Bukkit.getBanList(BanList.Type.NAME).addBan(p.getName(), wrongPassBanReason, new Date(System.currentTimeMillis()+wrongPassBan*3600*1000), null);
                p.kickPlayer(wrongPassBanReason);
            }
            // Kick player for trying to guess password once
            else
            {
                p.kickPlayer(ChatColor.translateAlternateColorCodes('&', settings.getConfig().getString("KickMessage")));
            }
        }
        return true;
    }
}