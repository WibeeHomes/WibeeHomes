package app.wibeehomes.placesearch;

import java.io.IOException;
import java.util.ArrayList;

import app.wibeehomes.Place;
import app.wibeehomes.RetrofitAction;
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
    private static ArrayList<Place> result = new ArrayList<Place>();
    private static placeSearchPoJo data = new placeSearchPoJo();
    private static int pageNum;
    public static ArrayList<Place> searchPlaceAction(String name) throws IOException {
        result.clear();
        pageNum = 1;
        do {
            data = RetrofitAction.SearchPlaceAction().getData("search","100",Integer.toString(pageNum),
                    name,"place","json","340FCCC5-C1C9-31D4-B7D8-56BC7558298A").execute().body();
            for (int i = 0; i < Integer.parseInt(data.getResponse().getRecord().getCurrent()); i++) {
                ItemPojo rePojo = data.getResponse().getResult().getItems().get(i);
                result.add(new Place(rePojo.getTitle(), rePojo.getAddress().getRoad(), Double.parseDouble(rePojo.getPoint().getX()), Double.parseDouble(rePojo.getPoint().getY())));
            }
            pageNum++;
        }while(Integer.parseInt(data.getResponse().getPage().getTotal()) != Integer.parseInt(data.getResponse().getPage().getCurrent()));
        return result;
    }
}
