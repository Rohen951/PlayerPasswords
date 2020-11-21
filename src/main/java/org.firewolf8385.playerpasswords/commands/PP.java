package org.firewolf8385.playerpasswords.commands;

import org.bukkit.Bukkit;
import org.firewolf8385.playerpasswords.PlayerPasswords;
import org.firewolf8385.playerpasswords.SettingsManager;
import org.firewolf8385.playerpasswords.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.firewolf8385.playerpasswords.objects.PasswordPlayer;
import org.firewolf8385.playerpasswords.objects.SessionList;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.ConsoleCommandSender;

public class PP implements CommandExecutor
{
    SettingsManager settings = SettingsManager.getInstance();
    String gold = settings.getConfig().getString("color1");
    String yellow = settings.getConfig().getString("color2");
    String gray = settings.getConfig().getString("color3");
    PlayerPasswords plugin;
    
    public PP(PlayerPasswords plugin)
    {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if(!sender.hasPermission("playerpasswords.admin") && !(sender instanceof ConsoleCommandSender))
        {
            // Shows No permission message if it is allowed in config
            if(settings.getConfig().getBoolean("ShowMsgNoPermission"))
            {
                Utils.chat(sender, settings.getConfig().getString("NoPermission"));
            }
            // Or shows rather Unknown Command message if it is allowed in config
            else if(settings.getConfig().getBoolean("ShowMsgUnknownCommand"))
            {
                Utils.chat(sender, settings.getConfig().getString("UnknownCommand"));
            }
            return true;
        }
      
        // adjust dash characters between console and player chat
        int width = 59;
        if(sender instanceof Player) width = 57;

        // If no arguments, show plugin help.
        if(args.length == 0)
        {
            Utils.chat(sender, gold + "&l]" + gray + "&m" + StringUtils.center(gold + "&lPlayerPasswords" + gray + "&m", width, '-') + gold +"&l[");
            Utils.chat(sender, " " + gray + "» " + gold + "/pp info " + gray + "- " + yellow + settings.getConfig().getString("PPinfo"));
            Utils.chat(sender, " " + gray + "» " + gold + "/pp support " + gray + "- " + yellow + settings.getConfig().getString("PPsupport"));
            Utils.chat(sender, " " + gray + "» " + gold + "/pp version " + gray + "- " + yellow + settings.getConfig().getString("PPversion"));
            Utils.chat(sender, " " + gray + "» " + gold + "/pp players " + gray + "- " + yellow + settings.getConfig().getString("PPplayers"));
            Utils.chat(sender, " " + gray + "» " + gold + "/pp reload " + gray + "- " + yellow + settings.getConfig().getString("PPreload"));
            Utils.chat(sender, " " + gray + "» " + gold + "/pp clearsessions " + gray + "- " + yellow + settings.getConfig().getString("PPclearsessions"));
            Utils.chat(sender, gold + "&l]" + gray +"&m---------------------------------------------------" + gold + "&l[");
            return true;
        }

        switch(args[0])
        {
            default:
                Utils.chat(sender, gold + "&l]" + gray + "&m" + StringUtils.center(gold + "&lPlayerPasswords" + gray + "&m", width, '-') + gold +"&l[");
                Utils.chat(sender, " " + gray + "» " + gold + settings.getConfig().getString("PPauthor") + gray + " - " + yellow + plugin.getDescription().getAuthors().get(0));
                Utils.chat(sender, " " + gray + "» " + gold + settings.getConfig().getString("PPpluginVersion") + gray + " - " + yellow + plugin.getDescription().getVersion());
                Utils.chat(sender, " " + gray + "» " + gold + "Spigot " + gray + "- " + yellow + "https://www.spigotmc.org/resources/70520/");
                Utils.chat(sender, gold + "&l]" + gray +"&m---------------------------------------------------" + gold + "&l[");
                break;

            case "version":
                Utils.chat(sender, gold + "&l]" + gray + "&m" + StringUtils.center(gold + "&lPlayerPasswords" + gray + "&m", width, '-') + gold +"&l[");
                Utils.chat(sender, " " + gray + "» " + gold + settings.getConfig().getString("PPpluginVersion") + gray + " - " + yellow + plugin.getDescription().getVersion());
                Utils.chat(sender, " " + gray + "» " + gold + settings.getConfig().getString("PPconfigVersion") + gray + " - " + yellow + settings.getConfig().getString("ConfigVersion"));
                Utils.chat(sender, gold + "&l]" + gray +"&m---------------------------------------------------" + gold + "&l[");
                if(sender instanceof Player && settings.isConfigOutdated())
                {
                    Utils.chat(sender, settings.getConfig().getString("OutdatedConfig"));
                }
                break;

            case "support":
                Utils.chat(sender, gold + "&l]" + gray + "&m" + StringUtils.center(gold + "&lPlayerPasswords" + gray + "&m", width, '-') + gold +"&l[");
                Utils.chat(sender, " " + gray + "» " + gold + "Discord " + gray + "- " + yellow + "https://discord.gg/FtBteC8");
                Utils.chat(sender, gold + "&l]" + gray +"&m---------------------------------------------------" + gold + "&l[");
                break;

            case "disable":
                break;

            case "help":
                Utils.chat(sender, gold + "&l]" + gray + "&m" + StringUtils.center(gold + "&lPlayerPasswords" + gray + "&m", width, '-') + gold +"&l[");
                Utils.chat(sender, " " + gray + "» " + gold + "/pp info " + gray + "- " + yellow + settings.getConfig().getString("PPinfo"));
                Utils.chat(sender, " " + gray + "» " + gold + "/pp support " + gray + "- " + yellow + settings.getConfig().getString("PPsupport"));
                Utils.chat(sender, " " + gray + "» " + gold + "/pp version " + gray + "- " + yellow + settings.getConfig().getString("PPversion"));
                Utils.chat(sender, " " + gray + "» " + gold + "/pp players " + gray + "- " + yellow + settings.getConfig().getString("PPplayers"));
                Utils.chat(sender, " " + gray + "» " + gold + "/pp reload " + gray + "- " + yellow + settings.getConfig().getString("PPreload"));
                Utils.chat(sender, " " + gray + "» " + gold + "/pp clearsessions " + gray + "- " + yellow + settings.getConfig().getString("PPclearsessions"));
                Utils.chat(sender, gold + "&l]" + gray +"&m---------------------------------------------------" + gold + "&l[");
                break;

            case "players":
                String playerStatus = "";
                Utils.chat(sender, gold + "&l]" + gray + "&m" + StringUtils.center(gold + "&lPlayerPasswords" + gray + "&m", width, '-') + gold +"&l[");
                for(Player po : Bukkit.getOnlinePlayers())
                {
                    PasswordPlayer pl = PasswordPlayer.getPlayers().get(po.getUniqueId());
                    if(pl.isRequired() && pl.isVerified() && !po.hasPermission("playerpasswords.bypass")) {
                        playerStatus = gray + " - " + settings.getConfig().getString("PPverified");
                    }
                    else if(pl.isRequired() && po.hasPermission("playerpasswords.bypass")) {
                        playerStatus = gray + " - " + settings.getConfig().getString("PPbypassed");
                    }
                    else if(!pl.isRequired()) {
                        playerStatus = gray + " - " +settings.getConfig().getString("PPnotRequired");
                    }
                    else {
                        playerStatus = gray + " - " + settings.getConfig().getString("PPunverified");
                    }
                    Utils.chat(sender, "  " + gray + "» " + yellow + po.getName() + playerStatus);
                }
                if(Bukkit.getOnlinePlayers().size() == 0) Utils.chat(sender, "  " + gray + "» No online players on server");
                Utils.chat(sender, gold + "&l]" + gray +"&m---------------------------------------------------" + gold + "&l[");
                break;
                
            case "reload":
                Boolean oldSessionSettings = settings.getConfig().getBoolean("IPSession");
                settings.reloadConfig();
                Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &3config.yml reloaded");
                if(settings.isConfigOutdated())
                {
                    Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &cconfig.yml outdated !!!");
                }
                
                settings.reloadData();
                Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &3data.yml reloaded");
              
                if(oldSessionSettings && !settings.getConfig().getBoolean("IPSession"))
                {
                    // If session was on and now is off after reload, cancel scheduler
                    plugin.stopScheduler();
                    // And clear whole SessionList
                    SessionList.getSessions().clear();
                }
                if(!oldSessionSettings && settings.getConfig().getBoolean("IPSession"))
                {
                    // If session was off and now is on after reload, start scheduler to repeatedly remove obsolete sessions from SessionList
                    plugin.startScheduler();
                }
              
                if(sender instanceof Player)
                {
                    Utils.chat(sender, settings.getConfig().getString("ReloadSuccessful"));
                    if(settings.isConfigOutdated())
                    {
                        Utils.chat(sender, settings.getConfig().getString("OutdatedConfig"));
                    }
                }
                break;

            case "clearsessions":
                SessionList.getSessions().clear();
                Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &3Sessions cleared");
                if(sender instanceof Player)
                {
                    Utils.chat(sender, settings.getConfig().getString("SessionsCleared"));
                }
                break;
        }

        return true;
    }
}
