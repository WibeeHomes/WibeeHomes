package app.wibeehomes.PublicHousing;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.text.*;

public class searchPulbicHousing {
    private static ArrayList<PublicHousingData> result = new ArrayList<PublicHousingData>();
    private static TotalPublicHousing data = new TotalPublicHousing();
    private static int pageNum;
    private static String apiKey = "0eMMHHcbnpAK1eXmexxzB4pMr9lfDCq4Tl6P4wh2DrYWPkvQfiB0u9Vr5mMh39H6x63xk%2FesCnLgUfMbHBQV8g%3D%3D";
    public static ArrayList<PublicHousingData> searchPlaceAction(String startDate, String finishDate) throws IOException {
        result.clear();
        pageNum = 1;
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552555/lhNoticeInfo/getNoticeInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + apiKey); /*공공데이터포털에서 발급받은 인증키*/
        urlBuilder.append("&" + URLEncoder.encode("PG_SZ","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("SCH_ST_DT","UTF-8") + "=" + URLEncoder.encode(startDate, "UTF-8")); /*기간검색-시작일*/
        urlBuilder.append("&" + URLEncoder.encode("SCH_ED_DT","UTF-8") + "=" + URLEncoder.encode(finishDate, "UTF-8")); /*기간검색-종료일*/
        urlBuilder.append("&" + URLEncoder.encode("PAGE","UTF-8") + "=" + URLEncoder.encode(Integer.toString(pageNum), "UTF-8")); /*페이지번호*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        String str = sb.toString();

        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(str);

        JsonObject object2 = (JsonObject) jsonArray.get(1); // dsListNm등등

        JsonArray dsListArray = (JsonArray) jsonParser.parse(String.valueOf(object2.get("dsList")));
        for(int i =0; i < dsListArray.size();i++){
            JsonObject object = (JsonObject)dsListArray.get(i);
            String cata = String.valueOf(object.get("AIS_TP_CD_NM"));

            String title = String.valueOf(object.get("BBS_TL"));
            StringBuffer strT = new StringBuffer(title);
            strT.deleteCharAt(0);
            strT.deleteCharAt(str.length()-1);
            title=strT.toString();
            String urlLink = String.valueOf(object.get("LINK_URL"));
            StringBuffer strb= new StringBuffer(urlLink);
            strb.deleteCharAt(0);
            strb.deleteCharAt(strb.length()-1);
            strb.insert(4,"s");
            urlLink = strb.toString();
            String dateG = String.valueOf(object.get("BBS_WOU_DTTM"));
            result.add(new PublicHousingData(cata,title,urlLink,dateG));
        }
        return result;
    }
}
