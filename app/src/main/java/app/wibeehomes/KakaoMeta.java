package app.wibeehomes;

import com.google.gson.annotations.SerializedName;

public class KakaoMeta {
    @SerializedName("total_count")
    private String total_count; // 검색된 문서 수
    @SerializedName("is_end")
    private String is_end; // 현재 페이지가 마지막 페이지인지 여부 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
    @SerializedName("pageable_count")
    private String pageable_count; // total_count 중 노출 가능 문서 수 (최대값: 45)

    public String getTotal_count () {
        return total_count;
    }
    public void setTotal_count (String total_count) {
        this.total_count = total_count;
    }
    public String getIs_end () {
        return is_end;
    }
    public void setIs_end (String is_end) {
        this.is_end = is_end;
    }
    public String getPageable_count () {
        return pageable_count;
    }
    public void setPageable_count (String pageable_count) {
        this.pageable_count = pageable_count;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [total_count = "+total_count+", is_end = "+is_end+", pageable_count = "+pageable_count+"]";
    }
}
