package eu.ncodes.appwritedatabase.Utils;

import eu.ncodes.appwritedatabase.AppwriteDatabase;
import eu.ncodes.appwritedatabase.AppwriteDatabaseAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPI extends PlaceholderExpansion {


    @Override
    public @NotNull String getIdentifier() {
        return "aw";
    }

    @Override
    public @NotNull String getAuthor() {
        return "nCodes";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.0.1";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        try {
            if(player == null) {
                return "0";
            }

            String[] splitter = params.split("_");

            if(splitter.length >= 2) {

                switch (splitter[0]) {
                    // If placeholder stands for global
                    case "g":
                        return AppwriteDatabaseAPI.getGlobalValueSync(splitter[1]).toString();
                    // If placeholder stands for player
                    case "p":
                        return AppwriteDatabaseAPI.getValueSync(player.getUniqueId().toString(), splitter[1]).toString();
                    // If placeholder stands for player other
                    case "po":
                        String uuid = Bukkit.getOfflinePlayer(splitter[1]).getUniqueId().toString();
                        return AppwriteDatabaseAPI.getValueSync(uuid, splitter[2]).toString();
                }

            }
        } catch(Exception exp) {
            return "0";
        }

        return "0";
    }

}
