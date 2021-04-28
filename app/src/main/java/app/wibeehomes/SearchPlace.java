package app.wibeehomes;

import java.util.ArrayList;

import app.wibeehomes.placesearch.ItemPojo;
import app.wibeehomes.placesearch.ResultPojo;
import app.wibeehomes.placesearch.placeSearchPoJo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/*
* {
"response" :
	{
		"service" :
		{
			"name" : "search",
			"version" : "2.0",
			"operation" : "search",
			"time" : "7(ms)"
		},
		"status" : "OK",
		"record" :
		{
			"total" : "1",
			"current" : "1"
		},
		"page" :
		{
			"total" : "1",
			"current" : "1",
			"size" : "10"
		},
		"result" :
		{
			"crs" : "EPSG:900913",
			"type" : "place",
			"items" : [
			{
				"id" : "AA0010790323",
				"title" : "공간정보산업진흥원",
				"category" : "정부공공기관 > 정부투자기관 > 미분류",
				"address" :
				{
					"road" : "경기도 성남시 분당구 판교로 242",
					"parcel" : "경기도 성남시 분당구 삼평동 624"
				},
				"point" :
				{
					"x" : "14148906.416053323",
					"y" : "4495379.988284272"
				}
			}]
		}
	}
}
* */

public class SearchPlace {
    private static String nameTemp;
    private ArrayList<Place> result;
    public ArrayList<Place> getResult() {
        return result;
    }

    public void setResult(ArrayList<Place> result) {
        this.result = result;
    }

    public static void searchPlaceAction(String name){
        nameTemp=name;
        RetrofitAction.SearchPlaceAction().getData("search","search","200","2.0",
                name,"place","xml","xml","340FCCC5-C1C9-31D4-B7D8-56BC7558298A").enqueue(new Callback<placeSearchPoJo>() {
            @Override
            public void onResponse(Call<placeSearchPoJo> call, Response<placeSearchPoJo> response) {
                if(response.isSuccessful()){
                    placeSearchPoJo data = response.body();
                    ArrayList<Place> temp = new ArrayList<Place>();

                    for(int i =0; i < Integer.parseInt(data.getResponse().getRecord().getCurrent()); i++){
                        ItemPojo rePojo = data.getResponse().getResult().getItems().get(i);
                        temp.add(new Place(rePojo.getTitle(),rePojo.getAddress().getRoad(),Double.parseDouble(rePojo.getPoint().getX()) , Double.parseDouble(rePojo.getPoint().getY())));
                    }

                    if( Integer.parseInt(data.getResponse().getPage().getTotal()) !=  Integer.parseInt(data.getResponse().getPage().getCurrent())){
                        searchPlaceAction(nameTemp);
                    }
                }
            }

            @Override
            public void onFailure(Call<placeSearchPoJo> call, Throwable t) {

            }
        });

    }
}
