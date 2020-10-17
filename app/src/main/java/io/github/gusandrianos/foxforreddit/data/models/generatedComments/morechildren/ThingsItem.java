package io.github.gusandrianos.foxforreddit.data.models.generatedComments.morechildren;

import com.google.gson.annotations.SerializedName;

public class ThingsItem{

    @SerializedName("data")
    private DataMoreChildren data;

    @SerializedName("kind")
    private String kind;

    public DataMoreChildren getData(){
        return data;
    }

    public String getKind(){
        return kind;
    }
}