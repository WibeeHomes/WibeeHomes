package app.wibeehomes.Map;

import android.app.Activity;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import app.wibeehomes.R;

public class KakaoMapAPI {
    private MapView mapView;
    private ViewGroup mapViewContainer;

    public KakaoMapAPI(Activity activity, ViewGroup viewGroup, double x, double y){
        mapView = new MapView(activity);
        mapViewContainer = viewGroup;
        mapViewContainer.addView(mapView);
        // 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(x, y), 3, true);
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);
    }

    public MapView getMapView() {
        return mapView;
    }
    public ViewGroup getMapViewContainer() {
        return mapViewContainer;
    }
}
