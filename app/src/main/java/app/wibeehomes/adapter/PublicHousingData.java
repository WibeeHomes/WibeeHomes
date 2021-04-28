package app.wibeehomes.adapter;

/**
 * 공공주택사업 RecyclerView 데이터 클래스
 */
public class PublicHousingData {

    private String category;
    private String title;
    private String url;
    private String date;

    public PublicHousingData(String category, String title, String url, String date) {
        this.category = category;
        this.title = title;
        this.url = url;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
