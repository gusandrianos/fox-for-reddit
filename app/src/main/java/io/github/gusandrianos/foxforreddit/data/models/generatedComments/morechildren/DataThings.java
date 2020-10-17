package io.github.gusandrianos.foxforreddit.data.models.generatedComments.morechildren;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataThings {
    @SerializedName("things")
    private List<ThingsItem> things;

    public List<ThingsItem> getThings(){
        return things;
    }
}
