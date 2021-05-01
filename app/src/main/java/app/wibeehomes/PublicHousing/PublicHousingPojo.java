package app.wibeehomes.PublicHousing;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import retrofit2.http.Query;

public class PublicHousingPojo {
    @SerializedName("dsSch")
    private ArrayList<DsSch> dsSch;

    public ArrayList<DsSch> getDsSch () {
        return dsSch;
    }
    public void setDsSch (ArrayList<DsSch> dsSch) {
        this.dsSch = dsSch;
    }
    @Override
    public String toString() {
        return "ClassPojo [dsSch = "+dsSch+"]";
    }
}
