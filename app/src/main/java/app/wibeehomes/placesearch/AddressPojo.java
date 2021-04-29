package app.wibeehomes.placesearch;

import com.google.gson.annotations.SerializedName;

public class AddressPojo {
    @SerializedName("parcel")
    private String parcel;
    @SerializedName("road")
    private String road;

    public AddressPojo(){
        parcel = "";
        road = "";
    }

    public String getParcel () {
        return parcel;
    }
    public void setParcel (String parcel) {
        this.parcel = parcel;
    }
    public String getRoad () {
        return road;
    }
    public void setRoad (String road) {
        this.road = road;
    }
    @Override
    public String toString() {
        return "ClassPojo [parcel = "+parcel+", road = "+road+"]";
    }
}
