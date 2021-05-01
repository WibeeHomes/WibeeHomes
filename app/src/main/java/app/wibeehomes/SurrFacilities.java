package app.wibeehomes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import app.wibeehomes.Kakao.KakaoCategory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 주변 편의 시설 검색 및 담아둘 클래스
// 대형 마트, 편의점, 지하철 역, 버스 정류장
public class SurrFacilities {
    private Place loc;

    private ArrayList<Place> supermarket =new ArrayList<Place>(); // 대형 마트
    private ArrayList<Place> convenience = new ArrayList<Place>(); // 편의점
    private ArrayList<Place> subwayStation = new ArrayList<Place>(); // 지하철역
    private ArrayList<Place> busStation = new ArrayList<Place>(); // 버스 정류장

    public SurrFacilities(Place loc1) throws IOException, InterruptedException {
        this.loc = loc1;
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    searchSuperMarket();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    searchSubwayStation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread3=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    searchConvenience();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread4 =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    busAPI(loc.get_placeX(),loc.get_placeY());
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
    }

    // get
    public ArrayList<Place> getBusStation() { return busStation; }
    public ArrayList<Place> getConvenience() { return convenience; }
    public ArrayList<Place> getSubwayStation() { return subwayStation; }
    public ArrayList<Place> getSupermarket() { return supermarket; }
    public Place getLoc() { return loc; }
    // ------------------

    //set ------------------
    public void setBusStation(ArrayList<Place> busStation) { this.busStation = busStation; }
    public void setConvenience(ArrayList<Place> convenience) { this.convenience = convenience; }
    public void setSubwayStation(ArrayList<Place> subwayStation) { this.subwayStation = subwayStation; }
    public void setSupermarket(ArrayList<Place> supermarket) { this.supermarket = supermarket; }
    public void setLoc(Place loc) { loc = loc; }
    //---------------------------
    public void addSetSupermarket(ArrayList<Place> supermarket) { this.supermarket.addAll(supermarket); }
    public void addSetSubwayStation(ArrayList<Place> subwayStation) { this.subwayStation.addAll(subwayStation); }
    public void addSetConvenience (ArrayList<Place> convenience) { this.convenience = convenience;}

    public synchronized void searchSuperMarket() throws IOException {
        KakaoCategory data = RetrofitAction.KaKaoAPIAction().getData("MT1",Double.toString(loc.get_placeX()),Double.toString(loc.get_placeY()),
                "2000",1,"distance").execute().body();
        if(data != null|| Integer.parseInt(data.getMeta().getPageable_count())!= 0) {
            for (int i = 0; i < 15; i++) {
                supermarket.add(new Place(data.getDocuments().get(i).getPlace_name(), data.getDocuments().get(i).getRoad_address_name(),
                        Double.parseDouble(data.getDocuments().get(i).getX()), Double.parseDouble(data.getDocuments().get(i).getY())
                        , data.getDocuments().get(i).getPhone(), data.getDocuments().get(i).getDistance()));
            }
        }
    }

    public synchronized void searchSubwayStation() throws IOException {
        KakaoCategory data = RetrofitAction.KaKaoAPIAction().getData("SW8",Double.toString(loc.get_placeX()),Double.toString(loc.get_placeY()),
                "2000",1,"distance").execute().body();
        if(data != null|| Integer.parseInt(data.getMeta().getPageable_count())!= 0) {
            for (int i = 0; i < 15; i++) {
                subwayStation.add(new Place(data.getDocuments().get(i).getPlace_name(), data.getDocuments().get(i).getRoad_address_name(),
                        Double.parseDouble(data.getDocuments().get(i).getX()), Double.parseDouble(data.getDocuments().get(i).getY())
                        , data.getDocuments().get(i).getPhone(), data.getDocuments().get(i).getDistance()));
            }
        }
    }

    public synchronized void searchConvenience() throws IOException {
        KakaoCategory data=RetrofitAction.KaKaoAPIAction().getData("CS2",Double.toString(loc.get_placeX()),Double.toString(loc.get_placeY()),
                "2000",1,"distance").execute().body();
        if(data != null|| Integer.parseInt(data.getMeta().getPageable_count())!= 0) {
            for (int i = 0; i < 15; i++) {
                convenience.add(new Place(data.getDocuments().get(i).getPlace_name(), data.getDocuments().get(i).getRoad_address_name(),
                        Double.parseDouble(data.getDocuments().get(i).getX()), Double.parseDouble(data.getDocuments().get(i).getY())
                        , data.getDocuments().get(i).getPhone(), data.getDocuments().get(i).getDistance()));
            }
        }
    }

    public synchronized ArrayList<Place> busAPI(double x, double y) throws IOException, ParserConfigurationException, SAXException {
        String parsingUrl="";
        StringBuilder urlBuilder = new StringBuilder("http://openapi.tago.go.kr/openapi/service/BusSttnInfoInqireService/getCrdntPrxmtSttnList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=0eMMHHcbnpAK1eXmexxzB4pMr9lfDCq4Tl6P4wh2DrYWPkvQfiB0u9Vr5mMh39H6x63xk%2FesCnLgUfMbHBQV8g%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("gpsLati","UTF-8") + "=" + URLEncoder.encode(Double.toString(x), "UTF-8")); /*WGS84 위도 좌표*/
        urlBuilder.append("&" + URLEncoder.encode("gpsLong","UTF-8") + "=" + URLEncoder.encode(Double.toString(y), "UTF-8")); /*WGS84 경도 좌표*/

        URL url = new URL(urlBuilder.toString());
        parsingUrl = url.toString();

        DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder=dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(parsingUrl);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("item");
        for(int i =0; i < nList.getLength();i++){
            Node nNode = nList.item(i);
            if(nNode.getNodeType()==Node.ELEMENT_NODE){
                Element eElement = (Element)nNode;
                String name = getTagValue("nodenm",eElement);
                double xLoc = Double.parseDouble(getTagValue("gpslati",eElement));
                double yLoc = Double.parseDouble(getTagValue("gpslong",eElement));
                busStation.add(new Place( name,xLoc,yLoc));
            }
        }
        return busStation;
    }
    private String getTagValue(String tag, Element eElement) {
        NodeList nlList=eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue=(Node)nlList.item(0);
        if(nValue==null) return null;
        return nValue.getNodeValue();
    }// 태그 밸류 얻어오는 함수

}
