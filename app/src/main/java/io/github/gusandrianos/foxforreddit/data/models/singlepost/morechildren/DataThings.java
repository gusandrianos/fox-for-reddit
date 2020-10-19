package io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;

public class DataThings {
    @SerializedName("things")
    private List<ChildrenItem> children;

    public List<ChildrenItem> getChildren() {
        return children;
    }
}
