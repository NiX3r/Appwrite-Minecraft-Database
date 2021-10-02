package eu.ncodes.appwritedatabase.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.ncodes.appwritedatabase.Enums.TypeOfValueEnum;
import eu.ncodes.appwritedatabase.Instances.AppwriteCallback;
import eu.ncodes.appwritedatabase.Services.DocumentService;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

public class PluginUtils {

    public static void SendMessage(CommandSender target, boolean usePrefix, String messageKey, LinkedHashMap<String, String> values) {

        String message = "";

        if(usePrefix) {
            message = PluginVariables.lang.get("prefix");
        }

        message += PluginVariables.lang.get(messageKey);

        for(String key : values.keySet()) {
            message = message.replace("{{" + key + "}}", values.get(key));
        }

        target.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void SendMessage(CommandSender target, String messageKey, LinkedHashMap<String, String> values) {
        SendMessage(target, true, messageKey, values);
    }

    public static void SendMessage(CommandSender target, String messageKey) {
        SendMessage(target, true, messageKey, new LinkedHashMap<>());
    }



    public static String addValue(String oldValue, String newValue) {
        TypeOfValueEnum newValueType = PluginUtils.GetValueType(newValue);
        TypeOfValueEnum oldValueType = PluginUtils.GetValueType(oldValue);

        String value = newValue;

        if(oldValueType.equals(TypeOfValueEnum.STRING)) {
            value = oldValue + newValueType;
        } else if(oldValueType.equals(TypeOfValueEnum.INT)) {
            value = "" + (NumberUtils.toInt(oldValue, 0) + NumberUtils.toInt(newValue, 0));
        } else if(oldValueType.equals(TypeOfValueEnum.DOUBLE)) {
            value = "" + (NumberUtils.toDouble(oldValue, 0D) + NumberUtils.toDouble(newValue, 0D));
        }

        return value;
    }

    public static String takeValue(String oldValue, String newValue) {
        TypeOfValueEnum oldValueType = PluginUtils.GetValueType(oldValue);

        String value = newValue;

        if(oldValueType.equals(TypeOfValueEnum.STRING)) {
            value = oldValue.replace(newValue, "");
        } else if(oldValueType.equals(TypeOfValueEnum.INT)) {
            value = "" + (NumberUtils.toInt(oldValue, 0) - NumberUtils.toInt(newValue, 0));
        } else if(oldValueType.equals(TypeOfValueEnum.DOUBLE)) {
            value = "" + (NumberUtils.toDouble(oldValue, 0D) - NumberUtils.toDouble(newValue, 0D));
        }

        return value;
    }

    public static TypeOfValueEnum GetValueType(String value) {
        try {
            Integer.parseInt(value);
            return TypeOfValueEnum.INT;
        } catch(Exception ex) {}

        try {
            Double.parseDouble(value);
            return TypeOfValueEnum.DOUBLE;
        } catch(Exception ex) {}

        return TypeOfValueEnum.STRING;
    }

    private static void recursiveFetch(LinkedHashMap<String, Object> data, String group, int currentPage, int maxPage, Consumer<AppwriteCallback> callback) {
        if(currentPage > maxPage) {
            callback.accept(new AppwriteCallback(null, data, null));
            return;
        }

        DocumentService.listDocuments(group, currentPage, (response) -> {
            JsonObject documentListObject = (JsonObject) response.value;
            for(JsonElement documentEl : documentListObject.get("documents").getAsJsonArray()) {
                JsonObject document = documentEl.getAsJsonObject();
                data.put(
                        document.get("key").getAsString(),
                        (Object) document.get("value")
                );
            }

            recursiveFetch(data, group, currentPage + 1, maxPage, callback);
        });
    }

    public static void getAllDocuments(String group, Consumer<AppwriteCallback> callback) {
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();

        DocumentService.listDocuments(group, 1, (response) -> {
            if(response.error != null) {
                callback.accept(response);
                return;
            }

            JsonObject documentListObject = (JsonObject) response.value;
            Integer sum = documentListObject.get("sum").getAsInt();

            for(JsonElement documentEl : documentListObject.get("documents").getAsJsonArray()) {
                JsonObject document = documentEl.getAsJsonObject();
                data.put(
                        document.get("key").getAsString(),
                        new HashMap<String, Object>(){{
                            put("value", document.get("value"));
                            put("document", document.getAsJsonObject());
                        }}
                );
            }

            int maxPage = (sum / 10) + 1;

            recursiveFetch(data, group, 2, maxPage, callback);
        });
    }

}
