package app.wibeehomes.Map;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;

import app.wibeehomes.Place;
import app.wibeehomes.R;
import app.wibeehomes.SurrFacilities;

public class KakaoMapAPI {
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private SurrFacilities surrFacilities;
    private Place centerLoc;

    private ArrayList<MapPOIItem> subwayMarkers;
    private ArrayList<MapPOIItem> busMarkers;
    private ArrayList<MapPOIItem> conviMarkers;
    private ArrayList<MapPOIItem> MartMarkers;

    public KakaoMapAPI(Activity activity, ViewGroup viewGroup,Place centerLoc) throws IOException {
        this.surrFacilities = new SurrFacilities(centerLoc);
        this.centerLoc = centerLoc;

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
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);
    }

    public void changeLoc(Place centerLoc){
        this.centerLoc = centerLoc;
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(this.centerLoc.get_placeX(), this.centerLoc.get_placeY()), 3, true);
        changeMarker();
    }

    public void changeMarker(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MartMarker();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                busMarker();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                conviMarker();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                subwayMarker();
            }
        }).start();
    }

    public void subwayMarker(){
        for(int i =0; i <this.surrFacilities.getSubwayStation().size();i++){
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(this.surrFacilities.getSubwayStation().get(i).get_placeAddress());
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(centerLoc.get_placeX(),centerLoc.get_placeY())); //좌표
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);
            this.subwayMarkers.add(marker);
        }
    }

    public void conviMarker(){
        for(int i =0; i <this.surrFacilities.getConvenience().size();i++){
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(this.surrFacilities.getConvenience().get(i).get_placeAddress());
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(centerLoc.get_placeX(),centerLoc.get_placeY())); //좌표
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);
            this.conviMarkers.add(marker);
        }
    }

    public void busMarker(){
        for(int i =0; i <this.surrFacilities.getBusStation().size();i++){
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(this.surrFacilities.getBusStation().get(i).get_placeAddress());
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(centerLoc.get_placeX(),centerLoc.get_placeY())); //좌표
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);
            this.busMarkers.add(marker);
        }
    }

    public void MartMarker(){
        for(int i =0; i <this.surrFacilities.getSupermarket().size();i++){
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(this.surrFacilities.getSupermarket().get(i).get_placeAddress());
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(centerLoc.get_placeX(),centerLoc.get_placeY())); //좌표
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker);
            this.MartMarkers.add(marker);
        }
    }

    public MapView getMapView() {
        return mapView;
    }
    public ViewGroup getMapViewContainer() {
        return mapViewContainer;
    }

}
