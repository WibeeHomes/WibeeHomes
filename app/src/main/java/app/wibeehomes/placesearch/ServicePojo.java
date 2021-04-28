package app.wibeehomes.placesearch;

import com.google.gson.annotations.SerializedName;

public class ServicePojo {
    @SerializedName("name")
    private String name;
    @SerializedName("time")
    private String time;
    @SerializedName("version")
    private String version;
    @SerializedName("operation")
    private String operation;

    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }
    public String getTime () {
        return time;
    }
    public void setTime (String time) {
        this.time = time;
    }
    public String getVersion () {
        return version;
    }
    public void setVersion (String version) {
        this.version = version;
    }
    public String getOperation () {
        return operation;
    }
    public void setOperation (String operation) {
        this.operation = operation;
    }
    @Override
    public String toString() {
        return "ClassPojo [name = "+name+", time = "+time+", version = "+version+", operation = "+operation+"]";
    }
}
