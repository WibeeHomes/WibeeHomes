package app.wibeehomes.PublicHousing;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TotalPublicHousing {
    @SerializedName("dsSch")
    private DsSch dsSch;
    @SerializedName("dsListNm")
    private DsListNm dsListNm;
    @SerializedName("resHeader")
    private ResHeader resHeader;
    @SerializedName("dsList")
    private ArrayList<DsList> dsList;

    public DsSch getDsSch() {
        return dsSch;
    }
    public ArrayList<DsList> getDsList() {
        return dsList;
    }
    public DsListNm getDsListNm() {
        return dsListNm;
    }
    public ResHeader getResHeader() {
        return resHeader;
    }

    public void setDsList(ArrayList<DsList> dsList) {
        this.dsList = dsList;
    }
    public void setDsListNm(DsListNm dsListNm) {
        this.dsListNm = dsListNm;
    }
    public void setDsSch(DsSch dsSch) {
        this.dsSch = dsSch;
    }
    public void setResHeader(ResHeader resHeader) {
        this.resHeader = resHeader;
    }
    @Override
    public String toString() {
        return "ClassPojo [dsSch = "+dsSch+", dsList = "+dsList+", resHeader = "+resHeader+", dsList = "+dsList+"]";
    }
}
