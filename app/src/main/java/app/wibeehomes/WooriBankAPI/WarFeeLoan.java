package app.wibeehomes.WooriBankAPI;

import java.io.Serializable;

public class WarFeeLoan implements Serializable {
    private String CRINF; // 응답 결과 코드
    private String PSN; // 응답 오류 내용
    private String ADR; // 예상 가능 대출 금액
    private String BLD; // 예산 보증서 금액
    private String MDBT; // 예상 가능 보증 비율
    private String LEAS; // 예상 가능 보증료율
    private String LAWC; // 예상 가능 보증료

    public WarFeeLoan(String CRINF, String PSN,
                      String ADR, String BLD,
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
