package app.wibeehomes.placesearch;

import com.google.gson.annotations.SerializedName;

public class placeSearchPoJo {
    @SerializedName("responsePojo")
    private ResponsePojo responsePojo;

    public ResponsePojo getResponse () {
        return responsePojo;
    }
    public void setResponse (ResponsePojo response) {
        this.responsePojo = response;
    }
    @Override
    public String toString() {
        return "ClassPojo [response = "+responsePojo+"]";
    }
}
