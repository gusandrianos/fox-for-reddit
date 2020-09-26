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

    public List<ChildrenItem> getChildren() {
        return children;
    }

    public String getBefore() {
        return before;
    }

    public int getDist() {
        return dist;
    }

    public String getAfter() {
        return after;
    }
}