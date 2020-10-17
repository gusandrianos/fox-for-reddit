package io.github.gusandrianos.foxforreddit.data.models.singlepost.comments;

import com.google.gson.annotations.SerializedName;

//@JsonAdapter(ChildrenItemAdapterFactory.class)
public class ChildrenItem{

    @SerializedName("data")
    private Data data;

    @SerializedName("kind")
    private String kind;

    private String loadMoreChild;

    public ChildrenItem(String loadMoreChild) {
        this.loadMoreChild = loadMoreChild;
        data = null;
        kind = null;
    }

    public Data getData(){
        return data;
    }

    public String getKind(){
        return kind;
    }

    public String getLoadMoreChild() {
        return loadMoreChild;
    }
}