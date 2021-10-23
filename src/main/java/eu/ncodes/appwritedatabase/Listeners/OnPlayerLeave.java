package eu.ncodes.appwritedatabase.Listeners;

import eu.ncodes.appwritedatabase.AppwriteDatabaseAPI;
import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerLeave implements Listener {
    @EventHandler
    public void On(PlayerQuitEvent e)
    {

        AppwriteDatabaseAPI.saveGroupAsync(e.getPlayer().getUniqueId().toString(), response -> {

            CacheManager.getInstance().removeAll(e.getPlayer().getUniqueId().toString());

        });

    }
}
