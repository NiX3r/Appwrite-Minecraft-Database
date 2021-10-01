package eu.ncodes.appwritedatabase.Utils;

import co.aikar.commands.BukkitCommandManager;
import io.appwrite.Client;
import io.appwrite.services.Database;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginVariables {

    // Instance for minecraft prefix
    public static String Prefix;
    // Instance for minecraft command manager
    public static BukkitCommandManager CommandManager;
    // Instance for minecraft plugin
    public static JavaPlugin Plugin;
    // Instance for appwrite client
    public static Client AppwriteClient;
    // Instance for appwrite database
    public static Database AppwriteDatabase;
    // Instance for data collection ID
    public static String DataCollectionID;

}
