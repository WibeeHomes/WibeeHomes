package app.wibeehomes.PublicHousing;

import java.io.IOException;
import java.util.ArrayList;
import app.wibeehomes.Place;
import app.wibeehomes.RetrofitAction;
import app.wibeehomes.placesearch.ItemPojo;

public class searchPulbicHousing {
    private static ArrayList<PublicHousingData> result = new ArrayList<PublicHousingData>();
    private static TotalPublicHousing data = new TotalPublicHousing();
    private static int pageNum;
    private static String apiKey = "0eMMHHcbnpAK1eXmexxzB4pMr9lfDCq4Tl6P4wh2DrYWPkvQfiB0u9Vr5mMh39H6x63xk%2FesCnLgUfMbHBQV8g%3D%3D";
    public static ArrayList<PublicHousingData> searchPlaceAction(String startDate, String finishDate) throws IOException {
        result.clear();
        pageNum = 1;
        do{
            data = RetrofitAction.publicHousingAPIAction().getData(apiKey,30,startDate,finishDate,pageNum).execute().body();
            if(data == null){
                break;
            }
            for (int i = 0; i < Integer.parseInt(data.getDsSch().getPG_SZ()); i++) {
                DsList node = data.getDsList().get(i);
                result.add(new PublicHousingData(node.getAIS_TP_CD_NM(),node.getBBS_TL(),node.getLINK_URL(),node.getBBS_WOU_DTTM()));
            }
            pageNum++;
        }while(Integer.parseInt(data.getDsSch().getPG_SZ()) > 0);
        return result;
    }
}
