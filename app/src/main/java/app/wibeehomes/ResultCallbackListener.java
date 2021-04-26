package app.wibeehomes;

import android.widget.Toast;

import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 경로 검색을 수행하는 오딧세이 api의 결과를 콜백하는 비동기식 class
 */

public class ResultCallbackListener implements OnResultCallbackListener {
    private ArrayList<SearchPath> ResultPath;// 경로 검색 결과
    public ResultCallbackListener(){
        this.ResultPath = new ArrayList<SearchPath>();
    }
    public ArrayList<SearchPath> getResultPath() {
        return ResultPath;
    }
    @Override
    public void onSuccess(ODsayData oDsayData, API api) {
        SubPath sp=null;
        try{
            if(api==API.SEARCH_PUB_TRANS_PATH){
                int totalCount = oDsayData.getJson().getJSONObject("result").getInt("busCount")
                        + oDsayData.getJson().getJSONObject("result").getInt("subwayCount")
                        + oDsayData.getJson().getJSONObject("result").getInt("subwayBusCount");//총 경로 결과 개수
                for(int i =0; i < totalCount;i++){
                    JSONObject path = oDsayData.getJson().getJSONObject("result").getJSONArray("path").getJSONObject(i);
                    JSONArray subPathList = path.getJSONArray("subPath");
                    ArrayList<SubPath> temp =new ArrayList<SubPath>();
                    for(int s =0 ; s<subPathList.length();s++){
                        JSONObject subPath = subPathList.getJSONObject(s);
                        int trafficType = subPath.getInt("trafficType");
                        sp = new SubPath();
                        sp.setTrafficType(trafficType);
                        sp.setDistance(subPath.getDouble("distance"));
                        sp.setSectionTime(subPath.getInt("sectionTime"));
                        if(trafficType != 3){
                            JSONArray laneList = subPath.getJSONArray("lane");
                            for(int l =0 ; l < laneList.length();l++){
                                JSONObject lane = laneList.getJSONObject(l);
                                if(trafficType==2){
                                    Lane busLane =new Lane(lane.getString("busNo")
                                            ,lane.getInt("type")
                                            ,lane.getInt("busID"));
                                    sp.addList(busLane);
                                } // 버스
                                else if(trafficType==1){
                                    Lane subwayLane = new Lane(lane.getString("name")
                                            ,lane.getInt("subwayCode")
                                            ,lane.getInt("subwayCityCode"));
                                    sp.addList(subwayLane);
                                }//지하철
                            }
                            sp.setStationCount(subPath.getInt("stationCount"));
                            if(trafficType==1){
                                sp.setWay(subPath.getString("way"));
                                sp.setWayCode(subPath.getInt("wayCode"));
                                sp.setDoor(subPath.getString("door"));
                                if(!subPath.isNull("startExitNo")){
                                    sp.setStartExitNo(new Place(subPath.getString("startExitNo")
                                            ,subPath.getDouble("startExitY")
                                            ,subPath.getDouble("startExitX")));
                                }
                                if(!subPath.isNull("endExitNo")){
                                    sp.setStartExitNo(new Place(subPath.getString("endExitNo")
                                            ,subPath.getDouble("endExitY")
                                            ,subPath.getDouble("endExitX")));
                                }

                            }
                            sp.setStartStation(new Place(subPath.getString("startName")
                                    ,subPath.getDouble("startY")
                                    ,subPath.getDouble("startX")));
                            sp.setEndStation(new Place(subPath.getString("endName")
                                    ,subPath.getDouble("endY")
                                    ,subPath.getDouble("endX")));
                        }
                        temp.add(sp);
                    }
                    ResultPath.add(new SearchPath(path.getInt("pathType"),new ExtendNode(path.getJSONObject("info").getDouble("trafficDistance")
                            ,path.getJSONObject("info").getInt("totalWalk")
                            ,path.getJSONObject("info").getInt("totalTime")
                            ,path.getJSONObject("info").getInt("payment")
                            ,path.getJSONObject("info").getInt("busTransitCount")
                            ,path.getJSONObject("info").getInt("subwayTransitCount")
                            ,path.getJSONObject("info").getString("mapObj")
                            ,path.getJSONObject("info").getString("firstStartStation")
                            ,path.getJSONObject("info").getString("lastEndStation")
                            ,path.getJSONObject("info").getInt("totalStationCount")
                            ,path.getJSONObject("info").getInt("totalDistance")),temp));
                }
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
    }/*경로 검색 파싱*/
    @Override
    public void onError(int i, String s, API api) {
    }
}