package io.github.gusandrianos.foxforreddit.data.models.generatedComments.singlepost;

import com.google.gson.annotations.SerializedName;

public class SinglePost{

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