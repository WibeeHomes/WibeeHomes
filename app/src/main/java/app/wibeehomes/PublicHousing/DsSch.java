package app.wibeehomes.PublicHousing;

import com.google.gson.annotations.SerializedName;

public class DsSch {
    @SerializedName("SCH_ED_DT")
    private String SCH_ED_DT;
    @SerializedName("SCH_ST_DT")
    private String SCH_ST_DT;
    @SerializedName("SL_BBS_KD_CD")
    private String SL_BBS_KD_CD;
    @SerializedName("PG_SZ")
    private String PG_SZ;
    @SerializedName("PAGE")
    private String PAGE;
    @SerializedName("MMLY_VW")
    private String MMLY_VW;

    public String getSCH_ED_DT () {
        return SCH_ED_DT;
    }
    public void setSCH_ED_DT (String SCH_ED_DT) {
        this.SCH_ED_DT = SCH_ED_DT;
    }
    public String getSCH_ST_DT () {
        return SCH_ST_DT;
    }
    public void setSCH_ST_DT (String SCH_ST_DT) {
        this.SCH_ST_DT = SCH_ST_DT;
    }
    public String getSL_BBS_KD_CD () {
        return SL_BBS_KD_CD;
    }
    public void setSL_BBS_KD_CD (String SL_BBS_KD_CD) {
        this.SL_BBS_KD_CD = SL_BBS_KD_CD;
    }
    public String getPG_SZ () {
        return PG_SZ;
    }
    public void setPG_SZ (String PG_SZ) {
        this.PG_SZ = PG_SZ;
    }
    public String getPAGE () {
        return PAGE;
    }
    public void setPAGE (String PAGE) {
        this.PAGE = PAGE;
    }
    public String getMMLY_VW () {
        return MMLY_VW;
    }
    public void setMMLY_VW (String MMLY_VW) {
        this.MMLY_VW = MMLY_VW;
    }
    @Override
    public String toString() {
        return "ClassPojo [SCH_ED_DT = "+SCH_ED_DT+", SCH_ST_DT = "+SCH_ST_DT+", SL_BBS_KD_CD = "+SL_BBS_KD_CD+", PG_SZ = "+PG_SZ+", PAGE = "+PAGE+", MMLY_VW = "+MMLY_VW+"]";
    }
}
