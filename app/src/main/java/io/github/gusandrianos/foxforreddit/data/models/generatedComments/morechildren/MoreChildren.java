package io.github.gusandrianos.foxforreddit.data.models.generatedComments.morechildren;

import com.google.gson.annotations.SerializedName;

public class MoreChildren{

    @SerializedName("json")
    private Json json;

    public Json getJson(){
        return json;
    }
}