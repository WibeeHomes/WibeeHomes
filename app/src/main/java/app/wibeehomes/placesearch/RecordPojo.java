package app.wibeehomes.placesearch;

import com.google.gson.annotations.SerializedName;

public class RecordPojo {
    @SerializedName("total")
    private String total;
    @SerializedName("current")
    private String current;

    public  RecordPojo(){
        total = "-1";
        current = "-1";
    }

    public String getTotal () {
        return total;
    }
    public void setTotal (String total) {
        this.total = total;
    }
    public String getCurrent () {
        return current;
    }
    public void setCurrent (String current) {
        this.current = current;
    }
    @Override
    public String toString() {
        return "ClassPojo [total = "+total+", current = "+current+"]";
    }
}
