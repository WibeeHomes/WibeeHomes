package app.wibeehomes.placesearch;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResultPojo {

    @SerializedName("crs")
    private String crs;
    @SerializedName("type")
    private String type;
    @SerializedName("items")
    private ArrayList<ItemPojo> items;

    public ResultPojo(){
        crs = "";
        type = "";
        items = new ArrayList<ItemPojo>();
    }

    public String getCrs ()
    {
        return crs;
    }
    public void setCrs (String crs)
    {
        this.crs = crs;
    }
    public String getType ()
    {
        return type;
    }
    public void setType (String type)
    {
        this.type = type;
    }
    public ArrayList<ItemPojo>getItems ()
    {
        return items;
    }
    public void setItems (ArrayList<ItemPojo> items)
    {
        this.items = items;
    }
    @Override
    public String toString() {
        return "ClassPojo [crs = "+crs+", type = "+type+", items = "+items+"]";
    }
}
