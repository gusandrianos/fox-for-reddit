package io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataThings {
    @SerializedName("things")
    private List<ThingsItem> things;

    public List<ThingsItem> getThings(){
        return things;
    }
}
