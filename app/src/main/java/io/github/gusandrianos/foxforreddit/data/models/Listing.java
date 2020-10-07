package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;


public class Listing{

    @SerializedName("kind")
    private String kind;

    @SerializedName("data")
    private TreeData treeData;

    public TreeData getTreeData() {
        return treeData;
    }

    public String getKind() {
        return kind;
    }
}