package io.github.gusandrianos.foxforreddit.data.models.singlepost.comments;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChildrenTypeAdapter implements JsonDeserializer<List<ChildrenItem>> {
    public List<ChildrenItem> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<ChildrenItem> vals = new ArrayList<ChildrenItem>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                if (e.isJsonObject()) {
                    vals.add((ChildrenItem) ctx.deserialize(e, ChildrenItem.class));
                } else {
                    vals.add(new ChildrenItem(e.getAsString()));
                }
            }
        } else {
            throw new RuntimeException("Unexpected JSON type: " + json.getClass());
        }
        return vals;
    }
}

