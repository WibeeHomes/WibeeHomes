package app.wibeehomes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 주변 편의 시설 검색 및 담아둘 클래스
// 대형 마트, 편의점, 지하철 역, 버스 정류장
public class SurrFacilities {
    private Place loc;

    private int marketIndex;
    private int subwayIndex;
    private int conviIndex;

    private ArrayList<Place> supermarket =new ArrayList<Place>(); // 대형 마트
    private ArrayList<Place> Convenience = new ArrayList<Place>(); // 편의점
    private ArrayList<Place> subwayStation = new ArrayList<Place>(); // 지하철역
    private ArrayList<Place> busStation = new ArrayList<Place>(); // 버스 정류장

    SurrFacilities(Place loc){
        this.loc = loc;
        this.marketIndex =0;
        this.subwayIndex=0;
        this.conviIndex =0;
    }

    // get
    public ArrayList<Place> getBusStation() { return busStation; }
    public ArrayList<Place> getConvenience() { return Convenience; }
    public ArrayList<Place> getSubwayStation() { return subwayStation; }
    public ArrayList<Place> getSupermarket() { return supermarket; }
    public Place getLoc() { return loc; }
    // ------------------

    //set ------------------
    public void setBusStation(ArrayList<Place> busStation) { this.busStation = busStation; }
    public void setConvenience(ArrayList<Place> convenience) { Convenience = convenience; }
    public void setSubwayStation(ArrayList<Place> subwayStation) { this.subwayStation = subwayStation; }
    public void setSupermarket(ArrayList<Place> supermarket) { this.supermarket = supermarket; }
    public void setLoc(Place loc) { loc = loc; }
    //---------------------------

    public void searchSuperMarket(){
        RetrofitAction.KaKaoAPIAction().getData("MT1","37.560886818737465","126.99912806269428",
                "2000",marketIndex).enqueue(new Callback<KakaoCategory>() {
            @Override
            public void onResponse(Call<KakaoCategory> call, Response<KakaoCategory> response) {
                if(response.isSuccessful()){
                    KakaoCategory data = response.body();
                    System.out.println("성공");
                    System.out.println(data.getDocuments().get(0).getPlace_name());
                }
                else{
                    System.out.println("실패");
                }
            }
            @Override
            public void onFailure(Call<KakaoCategory> call, Throwable t) {
            }
        }); // 대형 마트
    }

    public void searchSubwayStation(){
        RetrofitAction.KaKaoAPIAction().getData("SW8","37.560886818737465","126.99912806269428",
                "2000",subwayIndex).enqueue(new Callback<KakaoCategory>() {
            @Override
            public void onResponse(Call<KakaoCategory> call, Response<KakaoCategory> response) {
                if(response.isSuccessful()){
                    KakaoCategory data = response.body();
                    System.out.println("성공");
                    System.out.println(data.getDocuments().get(0).getPlace_name());
                }
                else{
                    System.out.println("실패");
                }
            }
            @Override
            public void onFailure(Call<KakaoCategory> call, Throwable t) {
            }
        }); // 대형 마트
    }

    public void searchConvenience(){
        RetrofitAction.KaKaoAPIAction().getData("CS2","37.560886818737465","126.99912806269428",
                "2000",conviIndex).enqueue(new Callback<KakaoCategory>() {
            @Override
            public void onResponse(Call<KakaoCategory> call, Response<KakaoCategory> response) {
                if(response.isSuccessful()){
                    KakaoCategory data = response.body();
                    System.out.println("성공");
                    System.out.println(data.getDocuments().get(0).getPlace_name());
                }
                else{
                    System.out.println("실패");
                }
            }
            @Override
            public void onFailure(Call<KakaoCategory> call, Throwable t) {
            }
        }); // 대형 마트
    }

    public void searchSurrPlace(){

    }

}
