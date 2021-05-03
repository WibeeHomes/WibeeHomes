package app.wibeehomes.WooriBankAPI;

import java.io.Serializable;

public class WarFeeLoan implements Serializable {
    public String getCRINF() {
        return CRINF;
    }

    public void setCRINF(String CRINF) {
        this.CRINF = CRINF;
    }

    public String getPSN() {
        return PSN;
    }

    public void setPSN(String PSN) {
        this.PSN = PSN;
    }

    public String getADR() {
        return ADR;
    }

    public void setADR(String ADR) {
        this.ADR = ADR;
    }

    public String getBLD() {
        return BLD;
    }

    public void setBLD(String BLD) {
        this.BLD = BLD;
    }

    public String getMDBT() {
        return MDBT;
    }

    public void setMDBT(String MDBT) {
        this.MDBT = MDBT;
    }

    public String getLEAS() {
        return LEAS;
    }

    public void setLEAS(String LEAS) {
        this.LEAS = LEAS;
    }

    public String getLAWC() {
        return LAWC;
    }

    public void setLAWC(String LAWC) {
        this.LAWC = LAWC;
    }

    private String CRINF; // 응답 결과 코드
    private String PSN; // 응답 오류 내용
    private String ADR; // 예상 가능 대출 금액
    private String BLD; // 예산 보증서 금액
    private String MDBT; // 예상 가능 보증 비율
    private String LEAS; // 예상 가능 보증료율
    private String LAWC; // 예상 가능 보증료

    public WarFeeLoan(String CRINF,
                       String BLD, String ADR,String PSN,
                      String MDBT , String LEAS, String LAWC){
        this.ADR=ADR;
        this.PSN =PSN;
        this.BLD=BLD;
        this.CRINF=CRINF;
        this.LAWC = LAWC;
        this.LEAS=LEAS;
        this.MDBT = MDBT;
    }
}
