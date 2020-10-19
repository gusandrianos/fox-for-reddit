package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;

public class CommentListing {
    @SerializedName("data")
    private CommentData data;

    @SerializedName("kind")
    private String kind;

    public CommentData getData() {
        return data;
    }

    public String getKind() {
        return kind;
    }
}
