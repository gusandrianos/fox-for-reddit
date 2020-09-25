package io.github.gusandrianos.foxforreddit.data.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TreeData{
    @SerializedName("children")
    private List<ChildrenItem> children;

    @SerializedName("before")
    private String before;

    @SerializedName("dist")
    private int dist;

    @SerializedName("after")
    private String after;
}