package app.wibeehomes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import static android.content.Context.LOCATION_SERVICE;

public class UserLoc {
    private static Place userPlace = new Place("집", 37.28496752588621,  126.99445809325184);
    //유저 장소 표시할 변수 디폴트 값으로 동국대학교 정보 문화관 넣어주고 있음
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // GPS를 업데이트할 최소 거리
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // GPS를 업데이트할 시간

    public UserLoc() {
    }

    public static Place getUserPlace() {
        return userPlace;
    }

    /* 현재 위치 정보 제공 동의했는지 안했는지를 반환해주는 함수
    1: 사용자 위치정보 허용 상태, 2: 사용자 위치 정보 허용하지 않은 상태*/

    public static void setUser_place(Place place) {
        userPlace = place;
    }

    public static void LocBy_gps(Activity activity, LocationListener gpsListener) {
        try {
            LocationManager locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Location location = null;
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
                System.out.println("hasFineLocationPermission: " + Integer.toString(hasFineLocationPermission));
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) gpsListener);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                UserLoc.getUserPlace().set_placeX(location.getLatitude());
                                UserLoc.getUserPlace().set_placeY(location.getLongitude());//위
                            }
                        }
                    }
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) gpsListener);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    UserLoc.getUserPlace().set_placeX(location.getLatitude());
                                    UserLoc.getUserPlace().set_placeY(location.getLongitude());//위도
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d("@@@", "" + e.toString());
        }
    }// GPS 작동 함수
}
