package eu.ncodes.appwritedatabase.Listeners;

import eu.ncodes.appwritedatabase.Managers.CacheManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {
    @EventHandler
    public void On(PlayerQuitEvent e)
    {
        String uuid = e.getPlayer().getUniqueId().toString();
        // TODO - Save remote data!
        CacheManager.getInstance().removeAll(uuid);
    }
}
