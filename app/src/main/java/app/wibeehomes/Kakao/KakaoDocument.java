package app.wibeehomes.Kakao;

import com.google.gson.annotations.SerializedName;

public class KakaoDocument {
    @SerializedName("place_url")
    private String place_url; // 장소 상세 페이지 URL
    @SerializedName("place_name")
    private String place_name; // 장소명, 업체명
    @SerializedName("road_address_name")
    private String road_address_name; // 전체 도로명 주소
    @SerializedName("category_group_name")
    private String category_group_name; // 중요 카테고리만 그룹핑한 카테고리 그룹명
    @SerializedName("category_name")
    private String category_name; // 카테고리 이름
    @SerializedName("distance")
    private String distance; //중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재 단위 meter
    @SerializedName("phone")
    private String phone; // 전화번호
    @SerializedName("category_group_code")
    private String category_group_code; // 중요 카테고리만 그룹핑한 카테고리 그룹 코드
    @SerializedName("x")
    private String x; // X 좌표 혹은 longitude
    @SerializedName("y")
    private String y; // Y 좌표 혹은 latitude
    @SerializedName("address_name")
    private String address_name; // 전체 지번 주소
    @SerializedName("id")
    private String id; //장소 ID

    public String getPlace_url () {
        return place_url;
    }
    public void setPlace_url (String place_url) {
        this.place_url = place_url;
    }
    public String getPlace_name () {
        return place_name;
    }
    public void setPlace_name (String place_name) {
        this.place_name = place_name;
    }
    public String getRoad_address_name () {
        return road_address_name;
    }
    public void setRoad_address_name (String road_address_name) {
        this.road_address_name = road_address_name;
    }
    public String getCategory_group_name () {
        return category_group_name;
    }
    public void setCategory_group_name (String category_group_name) {
        this.category_group_name = category_group_name;
    }
    public String getCategory_name () {
        return category_name;
    }
    public void setCategory_name (String category_name) {
        this.category_name = category_name;
    }
    public String getDistance () {
        return distance;
    }
    public void setDistance (String distance) {
        this.distance = distance;
    }
    public String getPhone () {
        return phone;
    }
    public void setPhone (String phone) {
        this.phone = phone;
    }
    public String getCategory_group_code () {
        return category_group_code;
    }
    public void setCategory_group_code (String category_group_code) {
        this.category_group_code = category_group_code;
    }
    public String getX () {
        return x;
    }
    public void setX (String x) {
        this.x = x;
    }
    public String getY () {
        return y;
    }
    public void setY (String y) {
        this.y = y;
    }
    public String getAddress_name () {
        return address_name;
    }
    public void setAddress_name (String address_name) {
        this.address_name = address_name;
    }
    public String getId () {
        return id;
    }
    public void setId (String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "ClassPojo [place_url = "+place_url+", place_name = "+place_name+", road_address_name = "+road_address_name+", category_group_name = "+category_group_name+", category_name = "+category_name+", distance = "+distance+", phone = "+phone+", category_group_code = "+category_group_code+", x = "+x+", y = "+y+", address_name = "+address_name+", id = "+id+"]";
    }
}
