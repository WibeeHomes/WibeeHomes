package app.wibeehomes;

import app.wibeehomes.Kakao.KakaoCategory;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BusRetrofitAPI {
    @GET("/openapi/service/BusSttnInfoInqireService/getSttnNoList?")
    Call<KakaoCategory> getData(@Query("ServiceKey") String apiKey,
                                @Query("gpsLati") String x , @Query("gpsLati") String y);
}
