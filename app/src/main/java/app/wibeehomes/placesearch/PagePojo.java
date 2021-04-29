package app.wibeehomes.placesearch;

import com.google.gson.annotations.SerializedName;

public class PagePojo {
    @SerializedName("total")
    private String total;
    @SerializedName("current")
    private String current;
    @SerializedName("size")
    private String size;

    public PagePojo(){
        total = "-1";
        current = "-1";
        size = "-1";
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
    public String getSize () {
        return size;
    }
    public void setSize (String size) {
        this.size = size;
    }
    @Override
    public String toString() {
        return "ClassPojo [total = "+total+", current = "+current+", size = "+size+"]";
    }
}
