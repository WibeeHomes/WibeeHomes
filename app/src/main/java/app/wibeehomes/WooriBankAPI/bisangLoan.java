package app.wibeehomes.WooriBankAPI;

import java.io.Serializable;

public class bisangLoan implements Serializable {
    private String LONDCS; // 여신 결정 금리
    private String APLIR; // 적용 가산금리
    private String TGTCUS; // 대상 고객 우대 금리
    private String WooriPBOK; // 우리통장보유우대 금리
    private String APVAM; // 승인 금액
    private String PRC;// 처리 상태 코드

    public String getLONDCS() {
        return LONDCS;
    }

    public void setLONDCS(String LONDCS) {
        this.LONDCS = LONDCS;
    }

    public String getAPLIR() {
        return APLIR;
    }

    public void setAPLIR(String APLIR) {
        this.APLIR = APLIR;
    }

    public String getTGTCUS() {
        return TGTCUS;
    }

    public void setTGTCUS(String TGTCUS) {
        this.TGTCUS = TGTCUS;
    }

    public String getWooriPBOK() {
        return WooriPBOK;
    }

    public void setWooriPBOK(String wooriPBOK) {
        WooriPBOK = wooriPBOK;
    }

    public String getAPVAM() {
        return APVAM;
    }

    public void setAPVAM(String APVAM) {
        this.APVAM = APVAM;
    }

    public String getPRC() {
        return PRC;
    }

    public void setPRC(String PRC) {
        this.PRC = PRC;
    }

    public bisangLoan(String LONDCS, String APLIR, String TGTCUS,
                      String WooriPBOK, String APVAM, String PRC){
        this.APLIR =APLIR;
        this.APVAM = APVAM;
        this.LONDCS = LONDCS;
        this.TGTCUS =TGTCUS;
        this.WooriPBOK =WooriPBOK;
        this.PRC = PRC;
    }


}
