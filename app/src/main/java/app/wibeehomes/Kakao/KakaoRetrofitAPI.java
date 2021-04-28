package app.wibeehomes.Kakao;

import app.wibeehomes.Kakao.KakaoCategory;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

// 카카오 카테고리 API 실행을 위한 인터페이스
public interface KakaoRetrofitAPI {
    String kakaoApiKey ="217b8d497b4c36107560aa74e77a1cc4";
    @Headers("Authorization: KakaoAK "+kakaoApiKey)
    @GET("/v2/local/search/category.json?")
    Call<KakaoCategory> getData(@Query("category_group_code") String groupCode,
                                @Query("x") String x , @Query("y") String y, @Query("radius") String radius, @Query("page") int page);
}
