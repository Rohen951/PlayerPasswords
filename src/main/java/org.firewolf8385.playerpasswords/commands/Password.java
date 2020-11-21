package org.firewolf8385.playerpasswords.commands;

import org.firewolf8385.playerpasswords.SettingsManager;
import org.firewolf8385.playerpasswords.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.firewolf8385.playerpasswords.objects.PasswordPlayer;
import org.firewolf8385.playerpasswords.utils.StringUtils;

public class Password implements CommandExecutor
{
    SettingsManager settings = SettingsManager.getInstance();
    String gold = settings.getConfig().getString("color1");
    String yellow = settings.getConfig().getString("color2");
    String gray = settings.getConfig().getString("color3");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        // Exit if not a player.
        if(!(sender instanceof Player))
        {

            // TODO: Maybe add console command for resetting password for users that forget their password 

            Utils.chat(sender, "&6[&aPlayerPasswords&6] &cCommand not supported");
            return true;
        }

        Player p = (Player) sender;
        String uuid = p.getUniqueId().toString();
        PasswordPlayer pl = PasswordPlayer.getPlayers().get(p.getUniqueId());

        // Player cannot use command if they aren't logged in.
        if(!pl.isVerified())
        {
            // Shows No permission message if it is allowed in config
            if(settings.getConfig().getBoolean("ShowMsgMustBeLoggedIn"))
            {
                Utils.chat(p, settings.getConfig().getString("MustBeLoggedIn"));
            }
            // Or shows rather Unknown Command message if it is allowed in config
            else if(settings.getConfig().getBoolean("ShowMsgUnknownCommand"))
            {
                Utils.chat(p, settings.getConfig().getString("UnknownCommand"));
            }
            return true;
        }


        // Shows default page if no arguments are given.
        if(args.length == 0)
        {
            if((p.hasPermission("playerpasswords.enable") && settings.getConfig().getBoolean("Optional")) || (p.hasPermission("playerpasswords.disable") && settings.getConfig().getBoolean("Optional")) || p.hasPermission("playerpasswords.set"))
            {
                Utils.chat(p, gold + "&l]" + gray + "&m--------------------" + gold + "&lPasswords" + gray + "&m--------------------" + gold +"&l[");
                if(settings.getConfig().getBoolean("Optional") && p.hasPermission("playerpasswords.enable"))
                {
                    Utils.chat(p, " " + gray + "» " + gold + "/password enable " + gray + "- " + yellow + settings.getConfig().getString("PassEnable"));
                }
                if(settings.getConfig().getBoolean("Optional") && p.hasPermission("playerpasswords.disable"))
                {
                    Utils.chat(p, " " + gray + "» " + gold + "/password disable " + gray + "- " + yellow + settings.getConfig().getString("PassDisable"));
                }
                if(p.hasPermission("playerpasswords.set"))
                {
                    Utils.chat(p, " " + gray + "» " + gold + "/password set [password] " + gray + "- " + yellow + settings.getConfig().getString("PassChange"));
                }
                Utils.chat(p, gold + "&l]" + gray +"&m---------------------------------------------------" + gold + "&l[");
            }
            else
            {
                if(settings.getConfig().getBoolean("ShowMsgNoPermission"))
                {
                    Utils.chat(p, settings.getConfig().getString("NoPermission"));
                }
                else if(settings.getConfig().getBoolean("ShowMsgUnknownCommand"))
                {
                    Utils.chat(p, settings.getConfig().getString("UnknownCommand"));
                }
            }
            return true;
        }


        switch(args[0].toLowerCase())
        {
            default:
                if((p.hasPermission("playerpasswords.enable") && settings.getConfig().getBoolean("Optional")) || (p.hasPermission("playerpasswords.disable") && settings.getConfig().getBoolean("Optional")) || p.hasPermission("playerpasswords.set"))
                {
                    Utils.chat(p, gold + "&l]" + gray + "&m--------------------" + gold + "&lPasswords" + gray + "&m--------------------" + gold +"&l[");
                    if(settings.getConfig().getBoolean("Optional") && p.hasPermission("playerpasswords.enable"))
                    {
                        Utils.chat(p, " " + gray + "» " + gold + "/password enable " + gray + "- " + yellow + settings.getConfig().getString("PassEnable"));
                    }
                    if(settings.getConfig().getBoolean("Optional") && p.hasPermission("playerpasswords.disable"))
                    {
                        Utils.chat(p, " " + gray + "» " + gold + "/password disable " + gray + "- " + yellow + settings.getConfig().getString("PassDisable"));
                    }
                    if(p.hasPermission("playerpasswords.set"))
                    {
                        Utils.chat(p, " " + gray + "» " + gold + "/password set [password] " + gray + "- " + yellow + settings.getConfig().getString("PassChange"));
                    }
                    Utils.chat(p, gold + "&l]" + gray +"&m---------------------------------------------------" + gold + "&l[");
                }
                else
                {
                    if(settings.getConfig().getBoolean("ShowMsgNoPermission"))
                    {
                        Utils.chat(p, settings.getConfig().getString("NoPermission"));
                    }
                    else if(settings.getConfig().getBoolean("ShowMsgUnknownCommand"))
                    {
                        Utils.chat(p, settings.getConfig().getString("UnknownCommand"));
                    }
                }
                break;

            case "enable":
                if(settings.getConfig().getBoolean("Optional"))
                {
                    if(!p.hasPermission("playerpasswords.enable"))
                    {
                        if(settings.getConfig().getBoolean("ShowMsgNoPermission"))
                        {
                            Utils.chat(p, settings.getConfig().getString("NoPermission"));
                        }
                        else if(settings.getConfig().getBoolean("ShowMsgUnknownCommand"))
                        {
                            Utils.chat(p, settings.getConfig().getString("UnknownCommand"));
                        }
                        return true;
                    }

                    if(settings.getData().getString("passwords." + uuid + ".password").equals(""))
                    {
                        Utils.chat(p, settings.getConfig().getString("PasswordNeedSet"));
                        Utils.chat(p, settings.getConfig().getString("PasswordSetUsage"));
                        return true;
                    }

                    settings.getData().set("passwords." + uuid + ".enabled", true);
                    settings.saveData();
                    settings.reloadData();
                    Utils.chat(p, settings.getConfig().getString("PasswordEnabled"));
                }
                else
                {
                    if(settings.getConfig().getBoolean("ShowMsgOptionalPasswordsDisabled"))
                    {
                        Utils.chat(p, settings.getConfig().getString("OptionalPasswordsDisabled"));
                    }
                    else if(settings.getConfig().getBoolean("ShowMsgUnknownCommand"))
                    {
                        Utils.chat(p, settings.getConfig().getString("UnknownCommand"));
                    }
                }
                break;

            case "disable":
                if(settings.getConfig().getBoolean("Optional"))
                {
                    if(!p.hasPermission("playerpasswords.disable"))
                    {
                        if(settings.getConfig().getBoolean("ShowMsgNoPermission"))
                        {
                            Utils.chat(p, settings.getConfig().getString("NoPermission"));
                        }
                        else if(settings.getConfig().getBoolean("ShowMsgUnknownCommand"))
                        {
                            Utils.chat(p, settings.getConfig().getString("UnknownCommand"));
                        }
                        return true;
                    }

                    settings.getData().set("passwords." + uuid + ".enabled", false);
                    settings.saveData();
                    settings.reloadData();
                    Utils.chat(p, settings.getConfig().getString("PasswordDisabled"));
                }
                else
                {
                    if(settings.getConfig().getBoolean("ShowMsgOptionalPasswordsDisabled"))
                    {
                        Utils.chat(p, settings.getConfig().getString("OptionalPasswordsDisabled"));
                    }
                    else if(settings.getConfig().getBoolean("ShowMsgUnknownCommand"))
                    {
                        Utils.chat(p, settings.getConfig().getString("UnknownCommand"));
                    }
                }
                break;

            case "set": 

                // TODO: If changing password, adjust command to /password set [old pass] [new pass] 
                //       If setting password, adjust command to /password set [pass] [re-type]

                if(!p.hasPermission("playerpasswords.set"))
                {
                    if(settings.getConfig().getBoolean("ShowMsgNoPermission"))
                    {
                        Utils.chat(p, settings.getConfig().getString("NoPermission"));
                    }
                    else if(settings.getConfig().getBoolean("ShowMsgUnknownCommand"))
                    {
                        Utils.chat(p, settings.getConfig().getString("UnknownCommand"));
                    }
                    return true;
                }
                if(args.length > 1)
                {
                    // Test new password if it is out of bounds
                    int minimum = settings.getConfig().getInt("MinimumPasswordLength");
                    int maximum = settings.getConfig().getInt("MaximumPasswordLength");
                    if(!(args[1].length() >= minimum && args[1].length() <= maximum))
                    {
                        Utils.chat(p, settings.getConfig().getString("OutOfBounds").replace("%minpass%", String.valueOf(minimum)).replace("%maxpass%", String.valueOf(maximum)));
                        return true;
                    }
                    settings.getData().set("passwords." + uuid + ".password", StringUtils.hash(args[1]));
                    settings.saveData();
                    settings.reloadData();
                    Utils.chat(p, settings.getConfig().getString("PasswordSet").replace("%password%", args[1]));
                }
                else
                {
                    Utils.chat(p, settings.getConfig().getString("PasswordSetUsage"));
                }
                break;

            case "reset":

                // TODO: Add admin command for resetting password for users that forgot their password 

                break;
        }


        return true;
    }
}
