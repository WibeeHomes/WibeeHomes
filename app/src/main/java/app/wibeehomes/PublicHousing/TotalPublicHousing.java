package app.wibeehomes.PublicHousing;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TotalPublicHousing {
    @SerializedName("dsSch")
    private ArrayList<DsSch> dsSch;
    @SerializedName("dsListNm")
    private ArrayList<DsListNm> dsListNm;
    @SerializedName("resHeader")
    private ArrayList<ResHeader> resHeader;
    @SerializedName("dsList")
    private ArrayList<DsList> dsList;

    public ArrayList<DsSch> getDsSch() {
        return dsSch;
    }
    public ArrayList<DsList> getDsList() {
        return dsList;
    }
    public ArrayList<DsListNm> getDsListNm() {
        return dsListNm;
    }
    public ArrayList<ResHeader> getResHeader() {
        return resHeader;
    }

    public void setDsList(ArrayList<DsList> dsList) {
        this.dsList = dsList;
    }
    public void setDsListNm(ArrayList<DsListNm> dsListNm) {
        this.dsListNm = dsListNm;
    }
    public void setDsSch(ArrayList<DsSch> dsSch) {
        this.dsSch = dsSch;
    }
    public void setResHeader(ArrayList<ResHeader> resHeader) {
        this.resHeader = resHeader;
    }
    @Override
    public String toString() {
        return "ClassPojo [dsSch = "+dsSch+", dsList = "+dsList+", resHeader = "+resHeader+", dsList = "+dsList+"]";
    }
}
