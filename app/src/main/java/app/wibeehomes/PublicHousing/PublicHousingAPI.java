package app.wibeehomes.PublicHousing;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PublicHousingAPI {
    @GET("/B552555/lhNoticeInfo/getNoticeInfo?")
    Call<TotalPublicHousing> getData(@Query("ServiceKey") String apiKey,
                                     @Query("PG_SZ") int size,
                                     @Query("SCH_ST_DT") String startDate,
                                     @Query("SCH_ED_DT") String finishDate,
                                     @Query("PAGE") int page);
}
