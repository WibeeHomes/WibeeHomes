package app.wibeehomes.WooriBankAPI;

public class bisangLoan {
    private int LONDCS; // 여신 결정 금리
    private int APLIR; // 적용 가산금리
    private int TGTCUS; // 대상 고객 우대 금리
    private int WooriPBOK; // 우리통장보유우대 금리
    private int APVAM; // 승인 금액
    private String PRC;// 처리 상태 코드

    public bisangLoan(int LONDCS, int APLIR,int TGTCUS,
                      int WooriPBOK,int APVAM,String PRC){
        this.APLIR =APLIR;
        this.APVAM = APVAM;
        this.LONDCS = LONDCS;
        this.TGTCUS =TGTCUS;
        this.WooriPBOK =WooriPBOK;
        this.PRC = PRC;
    }


}
