package app.wibeehomes;

public class CityCode {
    private String cityName;
    private int cityCode;
    public CityCode(String cityName, int cityCode){
        this.cityCode = cityCode;
        this.cityName = cityName;
    }
    
    public String getCode() {
    	return Integer.toString(cityCode);
    }
    
    public String getName() {
    	return cityName;
    }

}
