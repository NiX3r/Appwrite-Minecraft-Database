package eu.ncodes.appwritedatabase.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileDefaults {

    public static void ConfigYAML() {

        File msg = new File(PluginVariables.Plugin.getDataFolder() + "/config.yml");
        if (!msg.exists()) {

            try {

                msg.createNewFile();

            } catch (IOException ex) {

                System.out.println("ERROR: Failed to create en.yml file!");
                ex.printStackTrace();

            }

            FileConfiguration msgfc = YamlConfiguration.loadConfiguration(msg);

            //Default values
            msgfc.set("SuccessReload", "&4[&cAppwrite&4] &7");
            msgfc.set("Appwrite.Endpoint", "https://[HOSTNAME_OR_IP]/v1");
            msgfc.set("Appwrite.Project", "5df5acd0d48c2");
            msgfc.set("Appwrite.Key", "919c2d18fb5d4...a2ae413da83346ad2");

            try {
                msgfc.save(msg);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void MessagesYAML() {

        File msg = new File(PluginVariables.Plugin.getDataFolder() + "/messages.yml");
        if (!msg.exists()) {

            try {

                msg.createNewFile();

            } catch (IOException ex) {

                System.out.println("ERROR: Failed to create en.yml file!");
                ex.printStackTrace();

            }

            FileConfiguration msgfc = YamlConfiguration.loadConfiguration(msg);

            //Default values
            msgfc.set("SuccessReload", "Plugin files successfully reloaded.");
            msgfc.set("UnSuccessReload", "Plugin files failed to reload. Error outputted in console");
            msgfc.set("UnknownCommand", "You have typed something wrong. Please type it again.");
            msgfc.set("YouDoNotHavePerm", "Unfortunately you don't have permission &4%PERM%&c for this.");
            msgfc.set("SomethingWrong", "Something went wrong. Error outputted in console.");
            msgfc.set("DatabasePlayerGet", "%NICK%'s value is '%VALUE%'");
            msgfc.set("DatabasePlayerSet", "Set new value to %NICK% was %STATUS%");
            msgfc.set("DatabasePlayerAdd", "Add value to %NICK% was %STATUS%");
            msgfc.set("DatabasePlayerTake", "Take value from %NICK% was %STATUS%");
            msgfc.set("DatabaseGlobalGet", "Global value is '%VALUE%'");
            msgfc.set("DatabaseGlobalSet", "Set new global value was %STATUS%");
            msgfc.set("DatabaseGlobalAdd", "Add to global value was %STATUS%");
            msgfc.set("DatabaseGlobalTake", "Take from global value was %STATUS%");
            msgfc.set("CannotTakeNonExistsDocument", "You can not take from non existing document");
            msgfc.set("CannotGetNonExistsDocument", "You can not get non existing document");
            msgfc.set("UnccorectFormat", "You type bad format of value. It must be: %FORMAT%");

            try {
                msgfc.save(msg);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
