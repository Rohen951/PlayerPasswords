package org.firewolf8385.playerpasswords.objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.firewolf8385.playerpasswords.SettingsManager;

public class SessionList
{
    public static HashMap<UUID, SessionList> sessions = new HashMap<>();
  
    static SettingsManager settings = SettingsManager.getInstance();
  
    @SuppressWarnings("unused")
    private UUID uuid;
    private String IPaddress;
    private long lastSeen;
  
    /**
     * Create a SessionList
     * @param uuid - UUID of player.
     */
    public SessionList(UUID uuid)
    {
        this.uuid = uuid;
        lastSeen = System.currentTimeMillis();
        IPaddress = Bukkit.getPlayer(uuid).getAddress().getAddress().getHostAddress();
        sessions.put(uuid, this);
    }

    /**
     * Get if the player is can be verified by session.
     * @param uuid - UUID of player.
     * @return Whether or not it is verified by session.
     */
    public static boolean isBackInTime(UUID uuid)
    {
        if(sessions.containsKey(uuid))
        {
            String sessionIP = sessions.get(uuid).IPaddress;
            long sessionLastSeen = sessions.get(uuid).lastSeen;
            sessions.remove(uuid);
            if(Bukkit.getPlayer(uuid).getAddress().getAddress().getHostAddress().equals(sessionIP))
            {
                long sessionTimeOut = (long) settings.getConfig().getInt("SessionTimeOut");
                if(sessionTimeOut > 300) sessionTimeOut = 300; // maximum 5 minute
                sessionTimeOut = sessionTimeOut * 1000;
                if((System.currentTimeMillis() - sessionLastSeen) <= sessionTimeOut) return true;
            }
        }
        return false;
    }
  
    /**
     * Check sessions if they are obsolete and remove them.
     */
    public static void checkSessions()
    {
        long sessionTimeOut = (long) settings.getConfig().getInt("SessionTimeOut");
        if(sessionTimeOut > 300) sessionTimeOut = 300; // maximum 5 minute
        sessionTimeOut = sessionTimeOut * 1000;
        Iterator<Map.Entry<UUID, SessionList>> iterator = sessions.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<UUID, SessionList> entry = iterator.next(); 
            if((System.currentTimeMillis() - entry.getValue().lastSeen) > sessionTimeOut)
            {
                iterator.remove(); 
            }
        }
    }
  
    /**
     * Get a list of all sessions.
     * @return Sessions
     */
    public static Map<UUID, SessionList> getSessions()
    {
        return sessions;
    }
}