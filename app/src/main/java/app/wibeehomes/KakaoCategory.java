package app.wibeehomes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KakaoCategory {
    @SerializedName("documents")
    private List<KakaoDocument> documents;
    @SerializedName("meta")
    private KakaoMeta meta;

    public List<KakaoDocument> getDocuments () {
        return documents;
    }
    public void setDocuments (List<KakaoDocument> documents) {
        this.documents = documents;
    }
    public KakaoMeta getMeta () {
        return meta;
    }
    public void setMeta (KakaoMeta meta) {
        this.meta = meta;
    }
    @Override
    public String toString() {
        return "ClassPojo [documents = "+documents+", meta = "+meta+"]";
    }
}
