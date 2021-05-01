package app.wibeehomes.PublicHousing;

import com.google.gson.annotations.SerializedName;

public class ResHeader {
    @SerializedName("RS_DTTM")
    private String RS_DTTM;
    @SerializedName("SS_CODE")
    private String SS_CODE;

    public String getRS_DTTM () {
        return RS_DTTM;
    }
    public void setRS_DTTM (String RS_DTTM) {
        this.RS_DTTM = RS_DTTM;
    }
    public String getSS_CODE () {
        return SS_CODE;
    }
    public void setSS_CODE (String SS_CODE) {
        this.SS_CODE = SS_CODE;
    }
    @Override
    public String toString() {
        return "ClassPojo [RS_DTTM = "+RS_DTTM+", SS_CODE = "+SS_CODE+"]";
    }
}
