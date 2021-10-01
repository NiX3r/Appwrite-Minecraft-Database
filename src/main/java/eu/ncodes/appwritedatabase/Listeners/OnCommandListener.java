package eu.ncodes.appwritedatabase.Listeners;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import eu.ncodes.appwritedatabase.AppwriteDatabase;
import eu.ncodes.appwritedatabase.Instances.PluginMessagesInstance;
import eu.ncodes.appwritedatabase.Utils.PluginUtils;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("aw")
public class OnCommandListener extends BaseCommand {

    @Subcommand("database|db")
    public class DatabaseClass extends BaseCommand {

        @Subcommand("player|p")
        public class PersonalClass extends BaseCommand{

            @Subcommand("set")
            @CommandPermission("appwrite.player.set")
            public void SetCommand(CommandSender sender, String player, String key, String value) {

                // TODO Convert player nick to UUID
                String uuid = player;
                // Set value to document
                PluginUtils.SetValue(sender, uuid, player, key, value);

            }

            @Subcommand("add")
            @CommandPermission("appwrite.player.add")
            public void AddCommand(CommandSender sender, String player, String key, String value) {

                // TODO Convert player nick to UUID
                String uuid = player;
                // Add value to document
                PluginUtils.AddValue(sender, uuid, player, key, value);

            }

            @Subcommand("take")
            @CommandPermission("appwrite.player.take")
            public void TakeCommand(CommandSender sender, String player, String key, String value) {

                // TODO Convert player nick to UUID
                String uuid = player;
                // Take value from document
                PluginUtils.TakeValue(sender, uuid, player, key, value);

            }

            @Subcommand("get")
            @CommandPermission("appwrite.player.get")
            public void GetCommand(CommandSender sender, String player, String key) {

                // TODO Convert player nick to UUID
                String uuid = player;
                // Get value from document
                PluginUtils.GetValue(sender, uuid, player, key);


            }

        }

        @Subcommand("global|g")
        public class GlobalClass extends BaseCommand{

            @Subcommand("set")
            @CommandPermission("appwrite.global.set")
            public void SetCommand(CommandSender sender, String key, String value) {

                // Set value to document
                PluginUtils.SetValue(sender, "global-document", "", key, value);

            }

            @Subcommand("add")
            @CommandPermission("appwrite.global.add")
            public void AddCommand(CommandSender sender, String key, String value) {

                // Add value to document
                PluginUtils.AddValue(sender, "global-document", "", key, value);

            }

            @Subcommand("take")
            @CommandPermission("appwrite.global.take")
            public void TakeCommand(CommandSender sender, String key, String value) {

                // Take value from document
                PluginUtils.TakeValue(sender, "global-document", "", key, value);

            }

            @Subcommand("get")
            @CommandPermission("appwrite.global.get")
            public void GetCommand(CommandSender sender, String key) {

                // Get value from document
                PluginUtils.GetValue(sender, "global-document", "", key);

            }

        }

    }

    @Subcommand("reload|r")
    public class ReloadClass extends BaseCommand{

        @Subcommand("all|a")
        @CommandPermission("appwrite.reload.all")
        public void AllCommand(CommandSender sender) {

            if(AppwriteDatabase.ReloadConfig() && AppwriteDatabase.ReloadMessages()) {

                String msg = PluginVariables.Prefix + PluginMessagesInstance.SuccessReload;
                msg = msg.replace("&", "§");
                sender.sendMessage(msg);

            }
            else {
                String msg = PluginVariables.Prefix + PluginMessagesInstance.UnSuccessReload;
                msg = msg.replace("&", "§");
                sender.sendMessage(msg);
            }

        }

        @Subcommand("messages|msg|m")
        @CommandPermission("appwrite.reload.messages")
        public void MessagesCommand(CommandSender sender) {

            if(AppwriteDatabase.ReloadMessages()) {

                String msg = PluginVariables.Prefix + PluginMessagesInstance.SuccessReload;
                msg = msg.replace("&", "§");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));

            }
            else {
                String msg = PluginVariables.Prefix + PluginMessagesInstance.UnSuccessReload;
                msg = msg.replace("&", "§");
                sender.sendMessage(msg);
            }

        }

        @Subcommand("config|c")
        @CommandPermission("appwrite.reload.config")
        public void ConfigCommand(CommandSender sender) {

            if(AppwriteDatabase.ReloadConfig()) {

                String msg = PluginVariables.Prefix + PluginMessagesInstance.SuccessReload;
                msg = msg.replace("&", "§");
                sender.sendMessage(msg);

            }
            else {
                String msg = PluginVariables.Prefix + PluginMessagesInstance.UnSuccessReload;
                msg = msg.replace("&", "§");
                sender.sendMessage(msg);
            }

        }

    }

}
