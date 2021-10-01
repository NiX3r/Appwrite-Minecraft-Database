package eu.ncodes.appwritedatabase;

import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import io.appwrite.Client;
import io.appwrite.services.Database;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCache {
    public TestCache() {
    }

    // TODO: Tests does not cover documents

    @Test
    public void testEverything() {
        CacheManager cm = CacheManager.getInstance();

        // Test for SUCCESS

        cm.setValue("meldiron", "token", 5, null);
        cm.setValue("meldiron", "name", "Meld", null);
        cm.setValue("meldiron", "btc", 0.02D, null);

        cm.setValue("donnie", "token", 73, null);
        cm.setValue("donnie", "name", "Dong", null);
        cm.setValue("donnie", "btc", 1.5D, null);

        Integer tokenOne = Integer.parseInt(cm.getValue("meldiron", "token").value.toString());
        String nameOne = cm.getValue("meldiron", "name").value.toString();
        Double btcOne = Double.parseDouble(cm.getValue("meldiron", "btc").value.toString());

        Integer tokenTwo = Integer.parseInt(cm.getValue("donnie", "token").value.toString());
        String nameTwo =  cm.getValue("donnie", "name").value.toString();
        Double btcTwo = Double.parseDouble(cm.getValue("donnie", "btc").value.toString());

        assertEquals((Object) 5, (Object) tokenOne);
        assertEquals((Object) "Meld", (Object) nameOne);
        assertEquals((Object) 0.02D, (Object) btcOne);

        assertEquals((Object) 73, (Object) tokenTwo);
        assertEquals((Object) "Dong", (Object) nameTwo);
        assertEquals((Object) 1.5D, (Object) btcTwo);

        cm.setValue("meldiron", "token", 99, null);
        tokenOne = Integer.parseInt(cm.getValue("meldiron", "token").value.toString());
        assertEquals((Object) 99, (Object) tokenOne);

        cm.setValue("meldiron", "token", "Hello World!", null);
        String helloWorldValue = cm.getValue("meldiron", "token").value.toString();
        assertEquals((Object) "Hello World!", (Object) helloWorldValue);

        cm.setValue("donnie", "name", "Dongaabe", null);
        nameTwo = cm.getValue("donnie", "name").value.toString();
        assertEquals((Object) "Dongaabe", (Object) nameTwo);



        // Test for FAILURE

    }

}
