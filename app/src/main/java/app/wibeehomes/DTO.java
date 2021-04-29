package app.wibeehomes;

import java.util.ArrayList;

public class DTO {
    private static ArrayList<CityCode> cityArr = new ArrayList<CityCode>();
    public DTO(){
        cityArr.add(new CityCode("서울특별시",11000));
        cityArr.add(new CityCode("서울특별시 종로구",11110));
        cityArr.add(new CityCode("서울특별시 중구",11140));
        cityArr.add(new CityCode("서울특별시 용산구",11170));
        cityArr.add(new CityCode("서울특별시 성동구",11200));
        cityArr.add(new CityCode("서울특별시 광진구",11215));
        cityArr.add(new CityCode("서울특별시 동대문구",11230));
        cityArr.add(new CityCode("서울특별시 중랑구",11260));
        cityArr.add(new CityCode("서울특별시 성북구",11290));
        cityArr.add(new CityCode("서울특별시 강북구",11305));
        cityArr.add(new CityCode("서울특별시 도봉구",11320));
        cityArr.add(new CityCode("서울특별시 노원구",11350));
        cityArr.add(new CityCode("서울특별시 은평구",11380));
        cityArr.add(new CityCode("서울특별시 서대문구",11410));
        cityArr.add(new CityCode("서울특별시 마포구",11440));
        cityArr.add(new CityCode("서울특별시 양천구",11470));
        cityArr.add(new CityCode("서울특별시 강서구",11500));
        cityArr.add(new CityCode("서울특별시 구로구",11530));
        cityArr.add(new CityCode("서울특별시 금천구",11545));
        cityArr.add(new CityCode("서울특별시 영등포구",11560));
        cityArr.add(new CityCode("서울특별시 동작구",11590));
        cityArr.add(new CityCode("서울특별시 관악구",11620));
        cityArr.add(new CityCode("서울특별시 서초구",11650));
        cityArr.add(new CityCode("서울특별시 강남구",11680));
        cityArr.add(new CityCode("서울특별시 송파구",11710));
        cityArr.add(new CityCode("서울특별시 강동구",11740));

        cityArr.add(new CityCode("부산광역시 ",26000));
        cityArr.add(new CityCode("부산광역시 중구",26110));
        cityArr.add(new CityCode("부산광역시 서구",26140));
        cityArr.add(new CityCode("부산광역시 동구",26170));
        cityArr.add(new CityCode("부산광역시 영도구",26200));
        cityArr.add(new CityCode("부산광역시 부산진구",26230));
        cityArr.add(new CityCode("부산광역시 동래구",26260));
        cityArr.add(new CityCode("부산광역시 남구",26290));
        cityArr.add(new CityCode("부산광역시 북구",26320));
        cityArr.add(new CityCode("부산광역시 해운대구",26350));
        cityArr.add(new CityCode("부산광역시 사하구",26380));
        cityArr.add(new CityCode("부산광역시 금정구",26410));
        cityArr.add(new CityCode("부산광역시 강서구",26440));
        cityArr.add(new CityCode("부산광역시 연제구",26470));
        cityArr.add(new CityCode("부산광역시 수영구",26500));
        cityArr.add(new CityCode("부산광역시 사상구",26530));
        cityArr.add(new CityCode("부산광역시 기장군",26710));

        cityArr.add(new CityCode("대구광역시",27000));
        cityArr.add(new CityCode("대구광역시 중구",27110));
        cityArr.add(new CityCode("대구광역시 동구",27140));
        cityArr.add(new CityCode("대구광역시 서구",27170));
        cityArr.add(new CityCode("대구광역시 남구",27200));
        cityArr.add(new CityCode("대구광역시 북구",27230));
        cityArr.add(new CityCode("대구광역시 수성구",27260));
        cityArr.add(new CityCode("대구광역시 달서구",27290));
        cityArr.add(new CityCode("대구광역시 달성군",27710));

        cityArr.add(new CityCode("인천광역시",28000));
        cityArr.add(new CityCode("인천광역시 중구",28110));
        cityArr.add(new CityCode("인천광역시 동구",28140));
        cityArr.add(new CityCode("인천광역시 미추홀구",28177));
        cityArr.add(new CityCode("인천광역시 연수구",28185));
        cityArr.add(new CityCode("인천광역시 남동구",28200));
        cityArr.add(new CityCode("인천광역시 부평구",28237));
        cityArr.add(new CityCode("인천광역시 계양구",28245));
        cityArr.add(new CityCode("인천광역시 서구",28260));
        cityArr.add(new CityCode("인천광역시 강화군",28710));
        cityArr.add(new CityCode("인천광역시 옹진군",28720));

        cityArr.add(new CityCode("광주광역시",28000));

        cityArr.add(new CityCode("대전광역시",28000));

        cityArr.add(new CityCode("울산광역시",28000));

        cityArr.add(new CityCode("세종특별자치시",28000));

        cityArr.add(new CityCode("경기도",28000));

        cityArr.add(new CityCode("강원도",28000));

        cityArr.add(new CityCode("충청북도",28000));

        cityArr.add(new CityCode("충청남도",28000));

        cityArr.add(new CityCode("전라북도",28000));
        cityArr.add(new CityCode("전라북도",28000));
        cityArr.add(new CityCode("전라북도",28000));
        cityArr.add(new CityCode("전라북도",28000));

        cityArr.add(new CityCode("전라남도",46000));
        cityArr.add(new CityCode("전라남도 목포시",46110));
        cityArr.add(new CityCode("전라남도 여수시",46130));
        cityArr.add(new CityCode("전라남도 순천시",46150));
        cityArr.add(new CityCode("전라남도 나주시",46170));
        cityArr.add(new CityCode("전라남도 광양시",46230));
        cityArr.add(new CityCode("전라남도 딤양군",46710));
        cityArr.add(new CityCode("전라남도 곡성군",46720));
        cityArr.add(new CityCode("전라남도 구례군",46730));
        cityArr.add(new CityCode("전라남도 고흥군",46770));
        cityArr.add(new CityCode("전라남도 보성군",46780));
        cityArr.add(new CityCode("전라남도 화순군",46790));
        cityArr.add(new CityCode("전라남도 장흥군",46800));
        cityArr.add(new CityCode("전라남도 강진군",46810));
        cityArr.add(new CityCode("전라남도 해남군",46820));
        cityArr.add(new CityCode("전라남도 영암군",46830));
        cityArr.add(new CityCode("전라남도 무안군",46840));
        cityArr.add(new CityCode("전라남도 함평군",46860));
        cityArr.add(new CityCode("전라남도 영광군",46870));
        cityArr.add(new CityCode("전라남도 장성군",46880));
        cityArr.add(new CityCode("전라남도 완도군",46890));
        cityArr.add(new CityCode("전라남도 진도군",46900));
        cityArr.add(new CityCode("전라남도 신안군",46910));

        cityArr.add(new CityCode("경상북도",47000));
        cityArr.add(new CityCode("경상북도 포항시",47110));
        cityArr.add(new CityCode("경상북도 포항시 남구",47111));
        cityArr.add(new CityCode("경상북도 포항시 북구",47113));
        cityArr.add(new CityCode("경상북도 경주시",47130));
        cityArr.add(new CityCode("경상북도 김천시",47150));
        cityArr.add(new CityCode("경상북도 안동시",47170));
        cityArr.add(new CityCode("경상북도 구미시",47190));
        cityArr.add(new CityCode("경상북도 영주시",47210));
        cityArr.add(new CityCode("경상북도 영천시",47230));
        cityArr.add(new CityCode("경상북도 상주시",47250));
        cityArr.add(new CityCode("경상북도 문경시",47280));
        cityArr.add(new CityCode("경상북도 경산시",47290));
        cityArr.add(new CityCode("경상북도 군위군",47720));
        cityArr.add(new CityCode("경상북도 의성군",47730));
        cityArr.add(new CityCode("경상북도 청송군",47750));
        cityArr.add(new CityCode("경상북도 영양군",47760));
        cityArr.add(new CityCode("경상북도 영덕군",47770));
        cityArr.add(new CityCode("경상북도 청도군",47820));
        cityArr.add(new CityCode("경상북도 고령군",47830));
        cityArr.add(new CityCode("경상북도 성주군",47840));
        cityArr.add(new CityCode("경상북도 칠곡군",47850));
        cityArr.add(new CityCode("경상북도 예천군",47900));
        cityArr.add(new CityCode("경상북도 봉화군",47920));
        cityArr.add(new CityCode("경상북도 울진군",47930));
        cityArr.add(new CityCode("경상북도 울릉군",47940));

        cityArr.add(new CityCode("경상남도",48000));
        cityArr.add(new CityCode("경상남도 창원시",48120));
        cityArr.add(new CityCode("경상남도 창원시 의창구",48121));
        cityArr.add(new CityCode("경상남도 창원시 마산합포구",48125));
        cityArr.add(new CityCode("경상남도 창원시 마산회원구",48127));
        cityArr.add(new CityCode("경상남도 창원시 성산구",48123));
        cityArr.add(new CityCode("경상남도 창원시 진해구",48129));
        cityArr.add(new CityCode("경상남도 진주시",48170));
        cityArr.add(new CityCode("경상남도 통영시",48220));
        cityArr.add(new CityCode("경상남도 사천시",48240));
        cityArr.add(new CityCode("경상남도 김해시",48250));
        cityArr.add(new CityCode("경상남도 밀양시",48270));
        cityArr.add(new CityCode("경상남도 거제시",48310));
        cityArr.add(new CityCode("경상남도 양산시",48330));
        cityArr.add(new CityCode("경상남도 의령군",48720));
        cityArr.add(new CityCode("경상남도 함안군",48730));
        cityArr.add(new CityCode("경상남도 창녕군",48740));
        cityArr.add(new CityCode("경상남도 고성군",48820));
        cityArr.add(new CityCode("경상남도 남해군",48840));
        cityArr.add(new CityCode("경상남도 하동군",48850));
        cityArr.add(new CityCode("경상남도 산청군",48860));
        cityArr.add(new CityCode("경상남도 함양군",48870));
        cityArr.add(new CityCode("경상남도 거창군",48880));
        cityArr.add(new CityCode("경상남도 합천군",48890));
        
        cityArr.add(new CityCode("제주특별자치도",50000));
        cityArr.add(new CityCode("제주특별자치도 제주시",50110));
        cityArr.add(new CityCode("제주특별자치도 서귀포시",50130));
    }
}
