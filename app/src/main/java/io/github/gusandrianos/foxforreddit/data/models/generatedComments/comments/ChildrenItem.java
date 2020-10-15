package io.github.gusandrianos.foxforreddit.data.models.generatedComments.comments;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChildrenItem{

    @SerializedName("data")
    private Data data;

    @SerializedName("kind")
    private String kind;

    private String loadMoreChild;

    public ChildrenItem(String loadMoreChild) {
        this.loadMoreChild = loadMoreChild;
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