package eu.ncodes.appwritedatabase.Listeners;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import eu.ncodes.appwritedatabase.AppwriteDatabase;
import eu.ncodes.appwritedatabase.AppwriteDatabaseAPI;
import eu.ncodes.appwritedatabase.Enums.TypeOfValueEnum;
import eu.ncodes.appwritedatabase.Instances.AppwriteCallbackError;
import eu.ncodes.appwritedatabase.Instances.PluginMessagesInstance;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import eu.ncodes.appwritedatabase.Utils.PluginUtils;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import org.bukkit.Bukkit;
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
                String uuid = Bukkit.getPlayer(player).getUniqueId().toString();
                sharedSetCommand(sender, key, value, uuid);
            }

            @Subcommand("add")
            @CommandPermission("appwrite.player.add")
            public void AddCommand(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getPlayer(player).getUniqueId().toString();
                sharedAddCommand(sender, key, value, uuid);
            }

            @Subcommand("take")
            @CommandPermission("appwrite.player.take")
            public void TakeCommand(CommandSender sender, String player, String key, String value) {
                String uuid = Bukkit.getPlayer(player).getUniqueId().toString();
                sharedTakeCommand(sender, key, value, uuid);

            }

            @Subcommand("get")
            @CommandPermission("appwrite.player.get")
            public void GetCommand(CommandSender sender, String player, String key) {
                String uuid = Bukkit.getPlayer(player).getUniqueId().toString();
                sharedGetCommand(sender, key, uuid);
            }
        }

        @Subcommand("global|g")
        public class GlobalClass extends BaseCommand{

            @Subcommand("set")
            @CommandPermission("appwrite.global.set")
            public void SetCommand(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedSetCommand(sender, key, value, uuid);
            }

            @Subcommand("add")
            @CommandPermission("appwrite.global.add")
            public void AddCommand(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedAddCommand(sender, key, value, uuid);
            }

            @Subcommand("take")
            @CommandPermission("appwrite.global.take")
            public void TakeCommand(CommandSender sender, String key, String value) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedTakeCommand(sender, key, value, uuid);

            }

            @Subcommand("get")
            @CommandPermission("appwrite.global.get")
            public void GetCommand(CommandSender sender, String key) {
                String uuid = AppwriteDatabaseAPI.GLOBAL_GROUP_NAME;
                sharedGetCommand(sender, key, uuid);
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

    private void sharedSetCommand(CommandSender sender, String key, String value, String uuid) {
        DocumentService.setDocument(uuid, key, value, (response) -> {
            if(response.error != null) {
                if(response.error.equals(AppwriteCallbackError.DOCUMENT_NOT_FOUND)) {
                    PluginUtils.SendMessage(sender, "setCommand.documentNotFound");
                } else if(response.error.equals(AppwriteCallbackError.UNEXPECTED_ERROR)) {
                    PluginUtils.SendMessage(sender, "setCommand.unexpectedError");
                }
            } else {
                PluginUtils.SendMessage(sender, "setCommand.success");
            }
        });
    }

    private void sharedGetCommand(CommandSender sender, String key, String uuid) {
        DocumentService.getDocument(uuid, key, (response) -> {
            if(response.error != null) {
                if(response.error.equals(AppwriteCallbackError.DOCUMENT_NOT_FOUND)) {
                    PluginUtils.SendMessage(sender, "getCommand.documentNotFound");
                } else if(response.error.equals(AppwriteCallbackError.UNEXPECTED_ERROR)) {
                    PluginUtils.SendMessage(sender, "getCommand.unexpectedError");
                }
            } else {
                String value = response.value.toString();
                PluginUtils.SendMessage(sender, "getCommand.success: " + value);
            }
        });
    }

    private void sharedAddCommand(CommandSender sender, String key, String value, String uuid) {
        DocumentService.getDocument(uuid, key, (response) -> {
            if(response.error != null) {
                sharedSetCommand(sender, key, value, uuid);
                return;
            }

            String oldValue = response.value.toString();
            String newValue = PluginUtils.addValue(oldValue, value);

            DocumentService.setDocument(uuid, key, newValue, (setResponse) -> {
                if(setResponse.error != null) {
                    if(setResponse.error.equals(AppwriteCallbackError.DOCUMENT_NOT_FOUND)) {
                        PluginUtils.SendMessage(sender, "addCommand.documentNotFound");
                    } else if(setResponse.error.equals(AppwriteCallbackError.UNEXPECTED_ERROR)) {
                        PluginUtils.SendMessage(sender, "addCommand.unexpectedError");
                    }
                } else {
                    PluginUtils.SendMessage(sender, "addCommand.success");
                }
            });
        });
    }

    private void sharedTakeCommand(CommandSender sender, String key, String value, String uuid) {
        DocumentService.getDocument(uuid, key, (response) -> {
            if(response.error != null) {
                sharedSetCommand(sender, key, value, uuid);
                return;
            }

            String oldValue = response.value.toString();
            String newValue = PluginUtils.takeValue(oldValue, value);

            DocumentService.setDocument(uuid, key, newValue, (setResponse) -> {
                if(setResponse.error != null) {
                    if(setResponse.error.equals(AppwriteCallbackError.DOCUMENT_NOT_FOUND)) {
                        PluginUtils.SendMessage(sender, "takeCommand.documentNotFound");
                    } else if(setResponse.error.equals(AppwriteCallbackError.UNEXPECTED_ERROR)) {
                        PluginUtils.SendMessage(sender, "takeCommand.unexpectedError");
                    }
                } else {
                    PluginUtils.SendMessage(sender, "takeCommand.success");
                }
            });
        });
    }
}
