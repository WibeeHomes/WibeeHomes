package app.wibeehomes.Map;

import android.app.Activity;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapView;

import app.wibeehomes.R;

public class KakaoMapAPI {
    private MapView mapView;
    private ViewGroup mapViewContainer;

    KakaoMapAPI(Activity activity, ViewGroup viewGroup){
        mapView = new MapView(activity);
        mapViewContainer = viewGroup;
        mapViewContainer.addView(mapView);
    }

    public MapView getMapView() {
        return mapView;
    }
    public ViewGroup getMapViewContainer() {
        return mapViewContainer;
    }
}
