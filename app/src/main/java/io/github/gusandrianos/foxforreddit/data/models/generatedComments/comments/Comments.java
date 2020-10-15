package io.github.gusandrianos.foxforreddit.data.models.generatedComments.comments;

import com.google.gson.annotations.SerializedName;

public class Comments{

    @SerializedName("data")
    private Data data;

    @SerializedName("kind")
    private String kind;

    public Data getData(){
        return data;
    }

    public String getKind(){
        return kind;
    }
}