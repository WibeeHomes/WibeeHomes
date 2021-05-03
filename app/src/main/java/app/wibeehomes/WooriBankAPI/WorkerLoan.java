package app.wibeehomes.WooriBankAPI;

import java.io.Serializable;

public class WorkerLoan implements Serializable {
    public String getENCY() {
        return ENCY;
    }

    public void setENCY(String ENCY) {
        this.ENCY = ENCY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRQAM() {
        return RQAM;
    }

    public void setRQAM(String RQAM) {
        this.RQAM = RQAM;
    }

    public String getBZPE() {
        return BZPE;
    }

    public void setBZPE(String BZPE) {
        this.BZPE = BZPE;
    }

    public String getENCN() {
        return ENCN;
    }

    public void setENCN(String ENCN) {
        this.ENCN = ENCN;
    }

    private String ENCY; // 여신 결정 금리
    private String name; //  적용 가산 금리
    private String RQAM; // 승인 금액
    private String BZPE;//처리상태코드
    private String ENCN;//여신승인번호

    public WorkerLoan(String ENCY, String name, String RQAM,
                      String BZPE,String ENCN){
        this.BZPE =BZPE;
        this.ENCN=ENCN;
        this.ENCY = ENCY;
        this.name=name;
        this.RQAM =RQAM;
    }
}
