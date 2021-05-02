package app.wibeehomes.WooriBankAPI;

public class bisangLoan {
    private String LONDCS; // 여신 결정 금리
    private String APLIR; // 적용 가산금리
    private String TGTCUS; // 대상 고객 우대 금리
    private String WooriPBOK; // 우리통장보유우대 금리
    private String APVAM; // 승인 금액
    private String PRC;// 처리 상태 코드

    public bisangLoan(String LONDCS, String APLIR,String TGTCUS,
                      String WooriPBOK,String APVAM,String PRC){
        this.APLIR =APLIR;
        this.APVAM = APVAM;
        this.LONDCS = LONDCS;
        this.TGTCUS =TGTCUS;
        this.WooriPBOK =WooriPBOK;
        this.PRC = PRC;
    }


}
