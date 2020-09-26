package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;

public class ChildrenItem{

    @SerializedName("data")
    private Post post;

    @SerializedName("kind")
    private String kind;

    public Post getPost() {
        return post;
    }

    public String getKind() {
        return kind;
    }
}