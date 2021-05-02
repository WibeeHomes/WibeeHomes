package app.wibeehomes.WooriBankAPI;

import java.io.Serializable;

public class WorkerLoan implements Serializable {
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
