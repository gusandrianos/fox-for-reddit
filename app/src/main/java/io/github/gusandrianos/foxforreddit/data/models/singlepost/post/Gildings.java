package io.github.gusandrianos.foxforreddit.data.models.singlepost.post;

import com.google.gson.annotations.SerializedName;

public class Gildings{

    @SerializedName("gid_1")
    private int gid1;

    public int getGid1(){
        return gid1;
    }
}