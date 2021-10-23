package eu.ncodes.appwritedatabase.Instances;

public class CacheValueInstance {
    public Object value;
    public Boolean isRemote;

    public CacheValueInstance(Object value, Boolean isRemote) {
        this.value = value;
        this.isRemote = isRemote;
    }
}
