package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;

public class ChildrenItem {

    @SerializedName("data")
    private CommentData data;

    @SerializedName("kind")
    private String kind;

    private String loadMoreChild;

    public ChildrenItem(String loadMoreChild) {
        this.loadMoreChild = loadMoreChild;
        data = null;
        kind = null;
    }

    public CommentData getData() {
        return data;
    }

    public String getKind() {
        return kind;
    }

    public String getLoadMoreChild() {
        return loadMoreChild;
    }
}