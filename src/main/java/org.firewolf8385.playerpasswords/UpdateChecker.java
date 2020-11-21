package org.firewolf8385.playerpasswords;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker
{
    private UpdateChecker() {}
    static UpdateChecker instance = new UpdateChecker();

    /**
     * This allows us to access an instance of this class.
     */
    public static UpdateChecker getInstance()
    {
        return instance;
    }
    
    private String currentVersion;
    private String latestVersion;
    private boolean verbose;


    public void UpdateCheck(String currentVersion)
    {
        this.currentVersion = currentVersion;
        if(verbose) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7Checking update ...");
        check();
    }

    public boolean updateAvailable()
    {
        int lver = getInt(String.format("%1$-4s",latestVersion.replaceAll("[^0-9]","")).replace(" ", "0"));
        int cver = getInt(String.format("%1$-4s",currentVersion.replaceAll("[^0-9]","")).replace(" ", "0"));
        if (cver < lver)
        {
            return true;
        }

        return false;
    }

    private void check()
    {
            URL url = null;
            try
            {
                url = new URL("https://api.spigotmc.org/legacy/update.php?resource=70520");
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }

            URLConnection conn = null;
            try
            {
                conn = url.openConnection();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                latestVersion = reader.readLine();
                int lver = getInt(String.format("%1$-4s",latestVersion.replaceAll("[^0-9]","")).replace(" ", "0"));
                int cver = getInt(String.format("%1$-4s",currentVersion.replaceAll("[^0-9]","")).replace(" ", "0"));
                if (cver < lver) {
                    if(verbose) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7There is an update available. Your Version: " + currentVersion + ", Latest Version: " + latestVersion);
                }
                else
                {
                    if(verbose) Utils.chat(Bukkit.getConsoleSender(),"&6[&aPlayerPasswords&6] &7No update found");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
    }

    public void setVerbose(boolean verbose)
    {
        this.verbose = verbose;
    }

    public String getLatestVersion()
    {
        return latestVersion;
    }

    private int getInt(String s)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return 0;
    }
}
