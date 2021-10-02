package eu.ncodes.appwritedatabase.Utils;

import co.aikar.commands.BukkitCommandManager;
import eu.ncodes.appwritedatabase.Managers.FileManager;
import io.appwrite.Client;
import io.appwrite.services.Database;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginVariables {

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

    // File manager for configs (yml)
    public static FileManager FileManager;

    // Config classes to interact with
    public static FileManager.Config config;
    public static FileManager.Config lang;
    public static FileManager.Config defaults;

}
