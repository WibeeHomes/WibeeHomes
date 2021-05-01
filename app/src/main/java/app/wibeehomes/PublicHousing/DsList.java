package app.wibeehomes.PublicHousing;

import com.google.gson.annotations.SerializedName;

public class DsList {
    @SerializedName("RNUM")
    private String RNUM;
    @SerializedName("CCR_CNNT_SYS_DS_CD")
    private String CCR_CNNT_SYS_DS_CD;
    @SerializedName("INQ_CNT")
    private String INQ_CNT;
    @SerializedName("DEP_NM")
    private String DEP_NM;
    @SerializedName("AIS_TP_CD_NM")
    private String AIS_TP_CD_NM;
    @SerializedName("BBS_WOU_DTTM")
    private String BBS_WOU_DTTM;
    @SerializedName("PAGE")
    private String PAGE;
    @SerializedName("ALL_CNT")
    private String ALL_CNT;
    @SerializedName("LINK_URL")
    private String LINK_URL;
    @SerializedName("BBS_TL")
    private String BBS_TL;
    @SerializedName("BBS_SN")
    private String BBS_SN;

    public String getRNUM () {
        return RNUM;
    }
    public void setRNUM (String RNUM) {
        this.RNUM = RNUM;
    }
    public String getCCR_CNNT_SYS_DS_CD () {
        return CCR_CNNT_SYS_DS_CD;
    }
    public void setCCR_CNNT_SYS_DS_CD (String CCR_CNNT_SYS_DS_CD) {
        this.CCR_CNNT_SYS_DS_CD = CCR_CNNT_SYS_DS_CD;
    }
    public String getINQ_CNT () {
        return INQ_CNT;
    }
    public void setINQ_CNT (String INQ_CNT) {
        this.INQ_CNT = INQ_CNT;
    }
    public String getDEP_NM () {
        return DEP_NM;
    }
    public void setDEP_NM (String DEP_NM) {
        this.DEP_NM = DEP_NM;
    }
    public String getAIS_TP_CD_NM () {
        return AIS_TP_CD_NM;
    }
    public void setAIS_TP_CD_NM (String AIS_TP_CD_NM) {
        this.AIS_TP_CD_NM = AIS_TP_CD_NM;
    }
    public String getBBS_WOU_DTTM () {
        return BBS_WOU_DTTM;
    }
    public void setBBS_WOU_DTTM (String BBS_WOU_DTTM) {
        this.BBS_WOU_DTTM = BBS_WOU_DTTM;
    }
    public String getPAGE () {
        return PAGE;
    }
    public void setPAGE (String PAGE) {
        this.PAGE = PAGE;
    }
    public String getALL_CNT () {
        return ALL_CNT;
    }
    public void setALL_CNT (String ALL_CNT) {
        this.ALL_CNT = ALL_CNT;
    }
    public String getLINK_URL () {
        return LINK_URL;
    }
    public void setLINK_URL (String LINK_URL) {
        this.LINK_URL = LINK_URL;
    }
    public String getBBS_TL () {
        return BBS_TL;
    }
    public void setBBS_TL (String BBS_TL) {
        this.BBS_TL = BBS_TL;
    }
    public String getBBS_SN () {
        return BBS_SN;
    }
    public void setBBS_SN (String BBS_SN) {
        this.BBS_SN = BBS_SN;
    }
    @Override
    public String toString() {
        return "ClassPojo [RNUM = "+RNUM+", CCR_CNNT_SYS_DS_CD = "+CCR_CNNT_SYS_DS_CD+", INQ_CNT = "+INQ_CNT+", DEP_NM = "+DEP_NM+", AIS_TP_CD_NM = "+AIS_TP_CD_NM+", BBS_WOU_DTTM = "+BBS_WOU_DTTM+", PAGE = "+PAGE+", ALL_CNT = "+ALL_CNT+", LINK_URL = "+LINK_URL+", BBS_TL = "+BBS_TL+", BBS_SN = "+BBS_SN+"]";
    }
}
