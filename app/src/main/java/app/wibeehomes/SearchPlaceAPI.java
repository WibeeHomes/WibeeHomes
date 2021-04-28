package app.wibeehomes;

import app.wibeehomes.placesearch.placeSearchPoJo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchPlaceAPI {
    @GET("/req/search?")
    Call<placeSearchPoJo> getData(@Query("service") String service, @Query("request") String request , @Query("size") String size,
                                  @Query("version") String version, @Query("query") String queryStr, @Query("type") String typeStr,
                                  @Query("format") String formatStr , @Query("errorformat") String errorFormatStr, @Query("key") String apiKey);
}
