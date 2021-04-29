package app.wibeehomes.placesearch;

import com.google.gson.annotations.SerializedName;

public class ResponsePojo {
    @SerializedName("result")
    private ResultPojo result;
    @SerializedName("service")
    private ServicePojo service;
    @SerializedName("record")
    private RecordPojo record;
    @SerializedName("page")
    private PagePojo page;
    @SerializedName("status")
    private String status;

    public ResponsePojo(){
        status = "";
        page = new PagePojo();
        record = new RecordPojo();
        service = new ServicePojo();
        result = new ResultPojo();
    }

    public ResultPojo getResult () {
        return result;
    }
    public void setResult (ResultPojo result) {
        this.result = result;
    }
    public ServicePojo getService () {
        return service;
    }
    public void setService (ServicePojo service) {
        this.service = service;
    }
    public RecordPojo getRecord () {
        return record;
    }
    public void setRecord (RecordPojo record) {
        this.record = record;
    }
    public PagePojo getPage () {
        return page;
    }
    public void setPage (PagePojo page) {
        this.page = page;
    }
    public String getStatus () {
        return status;
    }
    public void setStatus (String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "ClassPojo [result = "+result+", service = "+service+", record = "+record+", page = "+page+", status = "+status+"]";
    }
}
