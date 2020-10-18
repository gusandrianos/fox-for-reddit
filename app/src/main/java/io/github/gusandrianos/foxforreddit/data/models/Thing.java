package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;

public class Thing {

    @SerializedName("data")
    private Data data;

    @SerializedName("kind")
    private String kind;

    public Data getData() {
        return data;
    }

    public String getKind() {
        return kind;
    }
}