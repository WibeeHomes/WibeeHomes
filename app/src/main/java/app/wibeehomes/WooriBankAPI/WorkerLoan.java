package app.wibeehomes.WooriBankAPI;

public class WorkerLoan {
    private String ENCY; // 여신 결정 금리
    private String name; //  적용 가산 금리
    private int RQAM; // 승인 금액
    private int BZPE;//처리상태코드
    private int ENCN;//여신승인번호

    public WorkerLoan(String ENCY, String name, int RQAM,
                      int BZPE,int ENCN){
        this.BZPE =BZPE;
        this.ENCN=ENCN;
        this.ENCY = ENCY;
        this.name=name;
        this.RQAM =RQAM;
    }
}
