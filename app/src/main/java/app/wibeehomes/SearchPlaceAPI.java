package app.wibeehomes;

import app.wibeehomes.placesearch.placeSearchPoJo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchPlaceAPI {
    @GET("/req/search?")
    Call<placeSearchPoJo> getData(@Query("request") String request,
                                  @Query("size") String size,
                                  @Query("page") String page,
                                  @Query("query") String queryStr,
                                  @Query("type") String typeStr,
                                  @Query("errorFormat") String errorFormatStr,
                                  @Query("key") String apiKey);
}
