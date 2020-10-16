package io.github.gusandrianos.foxforreddit.data.models.trophies;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("trophies")
    private List<TrophiesItem> trophies;

    @SerializedName("icon_70")
    private String icon70;

    @SerializedName("name")
    private String name;

    public List<TrophiesItem> getTrophies() { return trophies; }

    public String getIcon70() { return icon70; }

    public String getName() { return name; }
}