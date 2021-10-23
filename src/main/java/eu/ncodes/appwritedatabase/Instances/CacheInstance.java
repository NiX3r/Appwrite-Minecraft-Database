package eu.ncodes.appwritedatabase.Instances;

import java.util.LinkedHashMap;

public class CacheInstance {

    public String PlayerUUID;
    public String DocumentID;
    public LinkedHashMap<String, CacheValueInstance> Values;

    public CacheInstance(String PlayerUUID, String DocumentID, LinkedHashMap<String, CacheValueInstance> Values){
        this.PlayerUUID = PlayerUUID;
        this.DocumentID = DocumentID;
        this.Values = Values;
    }

}
