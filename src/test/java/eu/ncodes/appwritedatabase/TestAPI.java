package eu.ncodes.appwritedatabase;

import eu.ncodes.appwritedatabase.Services.CreateCollectionService;
import eu.ncodes.appwritedatabase.Services.GetCollectionListService;
import eu.ncodes.appwritedatabase.Utils.PluginVariables;
import io.appwrite.Client;
import io.appwrite.services.Database;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAPI {
    // TODO: Tests for add, take.. And combinations..
    // And edge-cases: set&get.. sync and async.. get null...

    // TODO: Test commands if possible

    @Test
    public void testEverything() {
        /*
        AtomicBoolean finished = new AtomicBoolean(false);

        System.out.println("Start setSync");
        testSetSync((_d1) -> {
            System.out.println("Start setAsync");
            testSetAsync((_d2) -> {
                System.out.println("Start getSync");
                testGetSync((_d3) -> {
                    System.out.println("Start getAsync");
                    testGetAsync((_d4) -> {
                        System.out.println("DONE");
                        finished.set(true);
                    });
                });
            });
        });

        int i = 0;
        while(finished.get() == false) {
            i++;

            if(i > 10) {
                finished.set(true);
            }

            try {
                TimeUnit.SECONDS.sleep(Long.parseLong("1"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
         */
    }

    private boolean isLoaded = false;
    private void prepareAppwrite(Consumer<Object> callback) {
        if(isLoaded) {
            callback.accept(null);
            return;
        }

        // Appwrite connection setup
        PluginVariables.AppwriteClient = new Client()
                .setEndpoint("https://aw10.matejbaco.eu/v1")
                .setProject("615194b70cb41")
                .setKey("746e8fdbad9e524838f33e53cc6b75afaf38b9909ea76e99c89039877b689402dafdbb0e550797b21052539c4b45b5960ab88e5a4932cce7fb8ba70254775ef76f785b96b786ccdc08e6f381a6504468385eb7aa981cf3be14f5104d3084b3c89237c3ddb8c7c328578f35066513678c28a371a29410e44faae01b2d90e24aa4");

        PluginVariables.AppwriteDatabase = new Database(PluginVariables.AppwriteClient);

        // Get/Create collection
        GetCollectionListService.GetListCollection((id) -> {
            if(id.equals("none")) {
                CreateCollectionService.CreateCollection((newId) -> {
                    PluginVariables.DataCollectionID = newId;
                    isLoaded = true;
                    callback.accept(null);
                });
            }
            else {
                PluginVariables.DataCollectionID = id;
                isLoaded = true;
                callback.accept(null);
            }
        });
    }

    public void testGetSync(Consumer<Object> callback) {
        prepareAppwrite((_d) -> {
            // Test for SUCCESS

            Object tokens = AppwriteDatabaseAPI.getValueSync("meldiron", "tokens").toString();
            Object name = AppwriteDatabaseAPI.getValueSync("meldiron", "name").toString();
            Object btc = AppwriteDatabaseAPI.getValueSync("meldiron", "btc").toString();

            assertEquals(8, Integer.parseInt(tokens.toString()));
            // assertEquals("Meldik", name.toString());
            // assertEquals(0.02D, Double.parseDouble(btc.toString()));


            // Test for FAILURE

            callback.accept(null);
        });
    }

    public void testGetAsync(Consumer<Object> callback) {
        prepareAppwrite((_d) -> {
            // Test for SUCCESS

            AppwriteDatabaseAPI.getValueAsync("meldiron", "asyncTokens", (tokens) -> {
                assertEquals(8, Integer.parseInt(tokens.toString()));

                AppwriteDatabaseAPI.getValueAsync("meldiron", "asyncName", (name) -> {
                    assertEquals("Meldik", name.toString());

                    AppwriteDatabaseAPI.getValueAsync("meldiron", "asyncBtc", (btc) -> {
                        assertEquals(0.02D, Double.parseDouble(btc.toString()));

                        callback.accept(null);
                    });
                });
            });

            // Test for FAILURE

        });
    }

    public void testSetSync(Consumer<Object> callback) {
        prepareAppwrite((_d) -> {
            // Test for SUCCESS

            // Integer set+update
            Object tokensValue = AppwriteDatabaseAPI.setValueSync("meldiron", "tokens", 5);
            assertEquals(5, Integer.parseInt(tokensValue.toString()));

            tokensValue = AppwriteDatabaseAPI.setValueSync("meldiron", "tokens", 8);
            assertEquals(8, Integer.parseInt(tokensValue.toString()));

            // String set+update
            Object nameValue = AppwriteDatabaseAPI.setValueSync("meldiron", "name", "Meldiron");
            assertEquals("Meldiron", nameValue.toString());

            nameValue = AppwriteDatabaseAPI.setValueSync("meldiron", "name", "Meldik");
            assertEquals("Meldik", nameValue.toString());

            // Double set+update
            Object bitcoinValue = AppwriteDatabaseAPI.setValueSync("meldiron", "btc", 0.0001D);
            assertEquals(0.0001D, Double.parseDouble(bitcoinValue.toString()));

            bitcoinValue = AppwriteDatabaseAPI.setValueSync("meldiron", "btc", 0.02D);
            assertEquals(0.02D, Double.parseDouble(bitcoinValue.toString()));

            // Test for FAILURE

            callback.accept(null);
        });
    }

    public void testSetAsync(Consumer<Object> callback) {
        prepareAppwrite((_d) -> {
            // Test for SUCCESS

            // Integer set+update
            AppwriteDatabaseAPI.setValueAsync("meldiron", "asyncTokens", 5, (tokens) -> {
                assertEquals(5, Integer.parseInt(tokens.toString()));

                AppwriteDatabaseAPI.setValueAsync("meldiron", "asyncTokens", 8, (newTokens) -> {
                    assertEquals(8, Integer.parseInt(newTokens.toString()));

                    // String set+update
                    AppwriteDatabaseAPI.setValueAsync("meldiron", "asyncName", "Meldiron", (name) -> {
                        assertEquals("Meldiron", name.toString());

                        AppwriteDatabaseAPI.setValueAsync("meldiron", "asyncName", "Meldik", (newName) -> {
                            assertEquals("Meldik", newName.toString());

                            // Double set+update
                            AppwriteDatabaseAPI.setValueAsync("meldiron", "asyncBtc", 0.0001D, (btc) -> {
                                assertEquals(0.0001D, Double.parseDouble(btc.toString()));

                                AppwriteDatabaseAPI.setValueAsync("meldiron", "asyncBtc", 0.02D, (newBtc) -> {
                                    assertEquals(0.02D, Double.parseDouble(newBtc.toString()));

                                    callback.accept(null);
                                });
                            });
                        });
                    });
                });
            });

            // Test for FAILURE

        });
    }
}
