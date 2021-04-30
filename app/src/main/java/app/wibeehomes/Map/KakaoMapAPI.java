package app.wibeehomes.Map;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;

import app.wibeehomes.Place;
import app.wibeehomes.R;
import app.wibeehomes.SurrFacilities;

public class KakaoMapAPI {
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private SurrFacilities surrFacilities;
    private Place centerLoc;

    public KakaoMapAPI(Activity activity, ViewGroup viewGroup,Place centerLoc,double x, double y) throws IOException {
        this.centerLoc = centerLoc;
        surrFacilities = new SurrFacilities(centerLoc);

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

    public void changeLoc(double x, double y){
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(x, y), true);
    }

    public void changeMarker(){

    }

    public MapView getMapView() {
        return mapView;
    }
    public ViewGroup getMapViewContainer() {
        return mapViewContainer;
    }

}
