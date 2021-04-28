package app.wibeehomes.placesearch;

import com.google.gson.annotations.SerializedName;

public class ItemPojo {
    @SerializedName("address")
    private AddressPojo address;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("category")
    private String category;
    @SerializedName("point")
    private PointPojo point;

    public AddressPojo getAddress () {
        return address;
    }
    public void setAddress (AddressPojo address) {
        this.address = address;
    }
    public String getId () {
        return id;
    }
    public void setId (String id) {
        this.id = id;
    }
    public String getTitle () {
        return title;
    }
    public void setTitle (String title) {
        this.title = title;
    }
    public String getCategory () {
        return category;
    }
    public void setCategory (String category) {
        this.category = category;
    }
    public PointPojo getPoint () {
        return point;
    }
    public void setPoint (PointPojo point) {
        this.point = point;
    }
    @Override
    public String toString() {
        return "ClassPojo [address = "+address+", id = "+id+", title = "+title+", category = "+category+", point = "+point+"]";
    }
}
