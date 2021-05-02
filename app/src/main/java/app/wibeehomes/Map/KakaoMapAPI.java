package app.wibeehomes.Map;

import android.app.Activity;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;

import app.wibeehomes.Place;
import app.wibeehomes.R;
import app.wibeehomes.ResidentialFacilities;
import app.wibeehomes.SurrFacilities;

public class KakaoMapAPI {

    private MapView mapView;
    private ViewGroup mapViewContainer;
    private SurrFacilities surrFacilities;
    private Place centerLoc;
    private int makerNumber =0;

    private MapPOIItem workMarker = null;
    private MapPOIItem homeMarker = null;

    private ArrayList<MapPOIItem> martMarker = new ArrayList<MapPOIItem>();
    private ArrayList<MapPOIItem> subwayMarker = new ArrayList<MapPOIItem>();
    private ArrayList<MapPOIItem> conviMarker = new ArrayList<MapPOIItem>();
    private ArrayList<MapPOIItem> busMarker = new ArrayList<MapPOIItem>();
    private ArrayList<MapPOIItem> residentMarker = new ArrayList<MapPOIItem>();


    public KakaoMapAPI(Activity activity, ViewGroup viewGroup,Place centerLoc) throws IOException, InterruptedException {
        this.centerLoc = centerLoc;
        this.surrFacilities = new SurrFacilities(centerLoc);

        mapView = new MapView(activity);
        mapViewContainer = viewGroup;
        mapViewContainer.addView(mapView);
        // 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(this.centerLoc.get_placeX(), this.centerLoc.get_placeY()), 3, true);
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(centerLoc.get_placeX(),centerLoc.get_placeY())); //좌표

        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomImageResourceId(R.drawable.marker_home);
        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        makerNumber++;
        this.mapView.addPOIItem(marker);

        sidePlaceMaker();
    }

    public void changeLoc(Place centerLoc){
        this.centerLoc = centerLoc;
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(this.centerLoc.get_placeX(), this.centerLoc.get_placeY()), 3, true);
        conviMarker();
        subwayMarker();
        busMarker();
        MartMarker();
    }

    public void sidePlaceMaker(){
        this.conviMarker();
        this.subwayMarker();
        this.busMarker();
        this.MartMarker();
    }

    public void workMarker(Place workPlace){
        MapPOIItem marker = new MapPOIItem();
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(workPlace.get_placeY(), workPlace.get_placeX());
        marker.setItemName(workPlace.get_placeAddress());
        marker.setTag(makerNumber);
        marker.setMapPoint(mapPoint); //좌표

        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        marker.setCustomImageResourceId(R.drawable.marker_work_pin);
        marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

        this.mapView.addPOIItem(marker);
        this.workMarker=marker;
        makerNumber++;
    }

    public void residentMaker(ArrayList<ResidentialFacilities> residetns){
        for(int i =0;i < residetns.size();i++){
            Place temp =residetns.get(i).getResident();
            MapPOIItem marker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(temp.get_placeY(), temp.get_placeX());
            marker.setItemName(temp.get_placeAddress());
            marker.setTag(makerNumber);
            marker.setMapPoint(mapPoint); //좌표

            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.marker_home_pin);
            marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            this.mapView.addPOIItem(marker);
            residentMarker.add(marker);
            makerNumber++;
        }
    }

    public void subwayMarker(){
        for(int i =0; i <this.surrFacilities.getSubwayStation().size();i++){
            Place temp =this.surrFacilities.getSubwayStation().get(i);
            MapPOIItem marker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(temp.get_placeY(), temp.get_placeX());
            marker.setItemName(temp.get_placeAddress());
            marker.setTag(makerNumber);
            marker.setMapPoint(mapPoint); //좌표

            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.marker_subway);
            marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            this.mapView.addPOIItem(marker);
            subwayMarker.add(marker);
            makerNumber++;
        }
    }

    public void conviMarker(){
        for(int i =0; i <this.surrFacilities.getConvenience().size();i++){
            Place temp =this.surrFacilities.getConvenience().get(i);
            MapPOIItem marker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(temp.get_placeY(), temp.get_placeX());
            marker.setItemName(temp.get_placeAddress());
            marker.setTag(makerNumber);
            marker.setMapPoint(mapPoint); //좌표

            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.marker_convenience);
            marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            this.mapView.addPOIItem(marker);
            conviMarker.add(marker);
            makerNumber++;
        }
    }

    public void busMarker(){
        for(int i =0; i <this.surrFacilities.getBusStation().size();i++){
            Place temp =this.surrFacilities.getBusStation().get(i);
            MapPOIItem marker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(temp.get_placeY(), temp.get_placeX());
            marker.setItemName(temp.get_placeAddress());
            marker.setTag(makerNumber);
            marker.setMapPoint(mapPoint); //좌표

            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.marker_bus);
            marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            this.mapView.addPOIItem(marker);
            busMarker.add(marker);
            makerNumber++;
        }
    }

    public void MartMarker(){
        for(int i =0; i <this.surrFacilities.getSupermarket().size();i++){
            Place temp =this.surrFacilities.getSupermarket().get(i);
            MapPOIItem marker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(temp.get_placeY(), temp.get_placeX());
            marker.setItemName(temp.get_placeAddress());
            marker.setTag(makerNumber);
            marker.setMapPoint(mapPoint); //좌표

            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.marker_mart);
            marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            this.mapView.addPOIItem(marker);
            martMarker.add(marker);
            makerNumber++;
        }
    }

    public MapView getMapView() {
        return mapView;
    }
    public ViewGroup getMapViewContainer() {
        return mapViewContainer;
    }

}
