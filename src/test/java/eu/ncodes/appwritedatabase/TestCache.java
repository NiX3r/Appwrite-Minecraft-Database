package eu.ncodes.appwritedatabase;

import eu.ncodes.appwritedatabase.Managers.CacheManager;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import io.appwrite.Client;
import io.appwrite.services.Database;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCache {
    public TestCache() {
    }

    // TODO: Tests does not cover documents

    @Test
    public void testEverything() {
        CacheManager cm = CacheManager.getInstance();

        // Test for SUCCESS

        // Test setValue
        cm.setValue("meldiron", "token", 5, null);
        cm.setValue("meldiron", "name", "Meld", null);
        cm.setValue("meldiron", "btc", 0.02D, null);

        cm.setValue("donnie", "token", 73, null);
        cm.setValue("donnie", "name", "Dong", null);
        cm.setValue("donnie", "btc", 1.5D, null);

        // test getValue
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

        // test setValue as update
        cm.setValue("meldiron", "token", 99, null);
        tokenOne = Integer.parseInt(cm.getValue("meldiron", "token").value.toString());
        assertEquals((Object) 99, (Object) tokenOne);

        cm.setValue("meldiron", "token", "Hello World!", null);
        String helloWorldValue = cm.getValue("meldiron", "token").value.toString();
        assertEquals((Object) "Hello World!", (Object) helloWorldValue);

        cm.setValue("donnie", "name", "Dongaabe", null);
        nameTwo = cm.getValue("donnie", "name").value.toString();
        assertEquals((Object) "Dongaabe", (Object) nameTwo);

        // test removeValue
        assertNotNull(cm.getValue("donnie", "name"));
        cm.removeValue("donnie", "name");
        assertNull(cm.getValue("donnie", "name"));

        assertNotNull(cm.getValue("donnie", "token"));
        cm.removeValue("donnie", "token");
        assertNull(cm.getValue("donnie", "token"));

        assertNotNull(cm.getValue("meldiron", "token"));
        cm.removeValue("meldiron", "token");
        assertNull(cm.getValue("meldiron", "token"));

        assertNull(cm.getValue("meldiron", "blabla"));
        assertNull(cm.getValue("donnie", "blublu"));
        assertNull(cm.getValue("blibli", "bleble"));

        // test removeAll
        cm.removeAll("meldiron");
        assertNull(cm.getValue("meldiron", "token"));
        assertNull(cm.getValue("meldiron", "tokens"));
        assertNull(cm.getValue("meldiron", "name"));
        assertNull(cm.getValue("meldiron", "btc"));

        cm.removeAll("donnie");
        assertNull(cm.getValue("donnie", "token"));
        assertNull(cm.getValue("donnie", "tokens"));
        assertNull(cm.getValue("donnie", "name"));
        assertNull(cm.getValue("donnie", "btc"));

        assertNull(cm.getValue("random123", "btc"));
        assertNull(cm.getValue("random123", "random456"));

        // Test for FAILURE

    }

}
