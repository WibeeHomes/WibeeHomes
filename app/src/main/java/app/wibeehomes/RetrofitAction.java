package app.wibeehomes;

import app.wibeehomes.Kakao.KakaoRetrofitAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// retrofit2 실행 클래스
// 앱 안에서 작동 시킬 모든 api 실행 객체 생성은 여기서 할거임

public class RetrofitAction {
    public static KakaoRetrofitAPI KaKaoAPIAction(){
         Retrofit kakaoAPIIns = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        KakaoRetrofitAPI retrofitAPI = kakaoAPIIns.create(KakaoRetrofitAPI.class);
        return retrofitAPI;
    }// 카카오 API 실행 함수

    public static SearchPlaceAPI SearchPlaceAction (){
        Retrofit searchAPIIns = new Retrofit.Builder()
                .baseUrl("https://api.vworld.kr")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SearchPlaceAPI retrofitAPI = searchAPIIns.create(SearchPlaceAPI.class);
        return retrofitAPI;
    }
}
