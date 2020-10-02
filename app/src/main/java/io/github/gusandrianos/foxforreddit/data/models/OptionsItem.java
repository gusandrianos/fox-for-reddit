package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;

public class OptionsItem{

    @SerializedName("text")
    private String text;

    @SerializedName("id")
    private String id;

    public String getText(){
        return text;
    }

    public String getId(){
        return id;
    }
}