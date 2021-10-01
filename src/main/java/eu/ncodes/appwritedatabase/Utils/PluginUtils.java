package eu.ncodes.appwritedatabase.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import eu.ncodes.appwritedatabase.Enums.TypeOfValueEnum;
import eu.ncodes.appwritedatabase.Instances.PluginMessagesInstance;
import eu.ncodes.appwritedatabase.Services.CreateDocumentService;
import eu.ncodes.appwritedatabase.Services.GetDocumentService;
import eu.ncodes.appwritedatabase.Services.UpdateDocumentService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PluginUtils {

    public static void SendMessage(Player target, boolean usePrefix, String message) {

        String toSend = "";

        if(usePrefix) {
            toSend = PluginVariables.Prefix;
        }

        toSend += message;

        target.sendMessage(ChatColor.translateAlternateColorCodes('&', toSend));

    }

    public static void SendMessage(CommandSender target, boolean usePrefix, String message) {

        String toSend = "";

        if(usePrefix) {
            toSend = PluginVariables.Prefix;
        }

        toSend += message;

        target.sendMessage(ChatColor.translateAlternateColorCodes('&', toSend));

    }

    public static void SendMessage(Player target, String message) {
        SendMessage(target, true, message);
    }

    public static void SendMessage(CommandSender target, String message) {
        SendMessage(target, true, message);
    }

    public static void GetValue(CommandSender sender, String uuid, String player, String key) {
        GetDocumentService.GetDocument(player, key, (response) -> {
            // Checks if document exist
            if(response.equals("error"))
                PluginUtils.SendMessage(sender, PluginMessagesInstance.SomethingWrong);
            else if(response.equals("none"))
                // Document does not exist
                PluginUtils.SendMessage(sender, PluginMessagesInstance.CannotGetNonExistsDocument);
            else {
                // Document exist
                JsonElement root = new JsonParser().parse(response);
                String value = root.getAsJsonObject().get("value").getAsString();
                if(player.equals(""))
                    PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabaseGlobalGet.replace("%VALUE%", value));
                else
                    PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerGet.replace("%VALUE%", value).replace("%NICK%", player));
            }
        });
    }

    public static void TakeValue(CommandSender sender, String uuid, String player, String key, String value) {
        // Trying to get document - check if exists
        GetDocumentService.GetDocument(uuid, key, (response) -> {
            // Checks if document exist and error status
            if(response.equals("error")) {
                PluginUtils.SendMessage(sender, PluginMessagesInstance.SomethingWrong);
            }
            else if(response.equals("none")) {
                // Document does not exist
                // Just tell that it cannot be created by using take
                PluginUtils.SendMessage(sender, PluginMessagesInstance.CannotTakeNonExistsDocument);
            }
            else {
                // Document exist
                // Updating document
                JsonElement root = new JsonParser().parse(response);
                String id = root.getAsJsonObject().get("$id").getAsString();
                String oldValue = root.getAsJsonObject().get("value").getAsString();
                TypeOfValueEnum newValueType = PluginUtils.GetValueType(value);
                TypeOfValueEnum oldValueType = PluginUtils.GetValueType(oldValue);

                // Checks if value types are same
                if(newValueType.equals(oldValueType) || oldValueType.equals(TypeOfValueEnum.STRING)) {
                    // Checks - can move on

                    String toUpdate = "";

                    // Add logics
                    if(newValueType.equals(TypeOfValueEnum.DOUBLE))
                        toUpdate = ((Double)(Double.parseDouble(oldValue) - Double.parseDouble(value))).toString();
                    if(newValueType.equals(TypeOfValueEnum.INT))
                        toUpdate = ((Integer)(Integer.parseInt(oldValue) - Integer.parseInt(value))).toString();
                    else
                        toUpdate = oldValue.replaceAll(value, "");

                    UpdateDocumentService.UpdateDocument(id, uuid, key, toUpdate, (takeResponse) -> {
                        // Checks if document was successfully created or for error
                        if(takeResponse.equals("error"))
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.SomethingWrong);
                        else if(takeResponse.equals("true"))
                            if(player.equals(""))
                                PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabaseGlobalTake.replace("%STATUS%", "success"));
                            else
                                PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerTake.replace("%NICK%", player).replace("%STATUS%", "success"));
                        else if(takeResponse.equals("false"))
                            if(player.equals(""))
                                PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabaseGlobalTake.replace("%STATUS%", "unsuccess"));
                            else
                                PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerTake.replace("%NICK%", player).replace("%STATUS%", "unsuccess"));

                    });
                }
                else {
                    // Dont check - cannot move on
                    PluginUtils.SendMessage(sender, PluginMessagesInstance.UnccorectFormat.replace("%FORMAT%", oldValueType.toString()));
                }
            }
        });
    }

    public static void SetValue(CommandSender sender, String uuid, String player, String key, String value) {
        // Trying to get document - check if exists
        GetDocumentService.GetDocument(uuid, key, (response) -> {
            // Checks if document exist and error status
            if(response.equals("error")) {
                PluginUtils.SendMessage(sender, PluginMessagesInstance.SomethingWrong);
            }
            else if(response.equals("none")) {
                // Document does not exist
                // Creating new one
                System.out.println("Does not exists - creating");
                CreateDocumentService.CreateDocument(uuid, key, value, (create) -> {

                    // Checks if document was successfully created or for error
                    if(create.equals("error"))
                        PluginUtils.SendMessage(sender, PluginMessagesInstance.SomethingWrong);
                    else if(create.equals("true"))
                        if(player.equals(""))
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabaseGlobalSet.replace("%STATUS%", "success"));
                        else
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerSet.replace("%NICK%", player).replace("%STATUS%", "success"));
                    else if(create.equals("false"))
                        if(player.equals(""))
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabaseGlobalSet.replace("%STATUS%", "unsuccess"));
                        else
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerSet.replace("%NICK%", player).replace("%STATUS%", "unsuccess"));

                });
            }
            else {
                // Document exist
                // Updating document
                JsonElement root = new JsonParser().parse(response);
                String id = root.getAsJsonObject().get("$id").getAsString();
                String oldValue = root.getAsJsonObject().get("value").getAsString();
                TypeOfValueEnum newValueType = PluginUtils.GetValueType(value);
                TypeOfValueEnum oldValueType = PluginUtils.GetValueType(oldValue);

                // Checks if value types are same
                if(newValueType.equals(oldValueType) || oldValueType.equals(TypeOfValueEnum.STRING)) {
                    // Checks - can move on
                    UpdateDocumentService.UpdateDocument(id, uuid, key, value, (setResponse) -> {
                        // Checks if document was successfully created or for error
                        if(setResponse.equals("error")) {
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.SomethingWrong);
                        }
                        else if(setResponse.equals("true")) {
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerSet.replace("%NICK%", player).replace("%STATUS%", "success"));
                        }
                        else if(setResponse.equals("false")) {
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerSet.replace("%NICK%", player).replace("%STATUS%", "unsuccess"));
                        }
                    });
                }
                else {
                    // Dont check - cannot move on
                    PluginUtils.SendMessage(sender, PluginMessagesInstance.UnccorectFormat.replace("%FORMAT%", oldValueType.toString()));
                }
            }
        });
    }

    public static void AddValue(CommandSender sender, String uuid, String player, String key, String value) {
        // Trying to get document - check if exists
        GetDocumentService.GetDocument(uuid, key, (response) -> {
            // Checks if document exist and error status
            if(response.equals("error")) {
                PluginUtils.SendMessage(sender, PluginMessagesInstance.SomethingWrong);
            }
            else if(response.equals("none")) {
                // Document does not exist
                // Creating new one
                System.out.println("Does not exists - creating");
                CreateDocumentService.CreateDocument(uuid, key, value, (create) -> {

                    // Checks if document was successfully created
                    if(create.equals("error"))
                        PluginUtils.SendMessage(sender, PluginMessagesInstance.SomethingWrong);
                    else if(create.equals("true"))
                        if(player.equals(""))
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabaseGlobalAdd.replace("%STATUS%", "success"));
                        else
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerAdd.replace("%NICK%", player).replace("%STATUS%", "success"));
                    else if(create.equals("false"))
                        if(player.equals(""))
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabaseGlobalAdd.replace("%STATUS%", "unsuccess"));
                        else
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerAdd.replace("%NICK%", player).replace("%STATUS%", "unsuccess"));

                });
            }
            else {
                // Document exist
                // Updating document
                JsonElement root = new JsonParser().parse(response);
                String id = root.getAsJsonObject().get("$id").getAsString();
                String oldValue = root.getAsJsonObject().get("value").getAsString();
                TypeOfValueEnum newValueType = PluginUtils.GetValueType(value);
                TypeOfValueEnum oldValueType = PluginUtils.GetValueType(oldValue);

                // Checks if value types are same
                if(newValueType.equals(oldValueType) || oldValueType.equals(TypeOfValueEnum.STRING)) {
                    // Checks - can move on

                    String toUpdate = "";

                    // Add logics
                    if(newValueType.equals(TypeOfValueEnum.DOUBLE))
                        toUpdate = ((Double)(Double.parseDouble(value) + Double.parseDouble(oldValue))).toString();
                    if(newValueType.equals(TypeOfValueEnum.INT))
                        toUpdate = ((Integer)(Integer.parseInt(value) + Integer.parseInt(oldValue))).toString();
                    else
                        toUpdate = oldValue + value;

                    UpdateDocumentService.UpdateDocument(id, uuid, key, toUpdate, (setResponse) -> {
                        // Checks if document was successfully created or for error
                        if(setResponse.equals("error"))
                            PluginUtils.SendMessage(sender, PluginMessagesInstance.SomethingWrong);
                        else if(setResponse.equals("true"))
                            if(player.equals(""))
                                PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabaseGlobalAdd.replace("%STATUS%", "success"));
                            else
                                PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerAdd.replace("%NICK%", player).replace("%STATUS%", "success"));
                        else if(setResponse.equals("false"))
                            if(player.equals(""))
                                PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabaseGlobalAdd.replace("%STATUS%", "unsuccess"));
                            else
                                PluginUtils.SendMessage(sender, PluginMessagesInstance.DatabasePlayerAdd.replace("%NICK%", player).replace("%STATUS%", "unsuccess"));
                    });
                }
                else {
                    // Dont check - cannot move on
                    PluginUtils.SendMessage(sender, PluginMessagesInstance.UnccorectFormat.replace("%FORMAT%", oldValueType.toString()));
                }
            }
        });
    }

    public static TypeOfValueEnum GetValueType(String value) {

        try {
            int test = Integer.parseInt(value);
            return TypeOfValueEnum.INT;

        }catch(Exception ex) {}

        try {
            double test = Double.parseDouble(value);
            return TypeOfValueEnum.DOUBLE;

        }catch(Exception ex) {}

        return TypeOfValueEnum.STRING;

    }

}
