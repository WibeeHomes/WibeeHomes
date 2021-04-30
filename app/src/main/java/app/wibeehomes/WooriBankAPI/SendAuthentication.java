package app.wibeehomes.WooriBankAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendAuthentication {
    StringBuilder urlBuilder = new StringBuilder("/oai/wb/v1/login/getCellCerti");
    URL url = new URL(urlBuilder.toString());
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    String str="{ " +
            "\"dataHeader\" :{"+
            "\"UTZPE_CNCT_IPAD\":\"\" ," +
            "\"UTZPE_CNCT_TEL_NO_TXT\": \"\","+
            "\"UTZPE_CNCT_MCHR_IDF_SRNO\": \"\","+
            "\"UTZ_MCHR_OS_DSCD\": \"\","+
            "\"UTZ_MCHR_OS_VER_NM\": \"\","+
            "\"UTZ_MCHR_MDL_NM\": \"\","+
            "\"UTZ_MCHR_APP_VER_NM\": \"\""+
            "},"+
            "\"dataBody\": {"+
            "\"COMC_DIS\": \"1\","+
            "\"HP_NO\": \"01012345678\","+
            "\"HP_CRTF_AGR_YN\": \"Y\","+
            "\"FNM\": \"홍길동\","+
            "\"RRNO_BFNB\": \"930216\","+
            "\"ENCY_RRNO_LSNM\": \"1234567\" } }";

    byte [] body = str.getBytes();

    public SendAuthentication() throws IOException {
        conn.setRequestMethod("POST");
        conn.setRequestProperty("appkey", "");
        conn.setFixedLengthStreamingMode(body.length);
        conn.setDoOutput(true);

        OutputStream out = conn.getOutputStream();
        out.write(body);
        //System.out.println("Response code: " + conn.getResponseCode());
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
        rd.close();
        conn.disconnect();
    }
}
