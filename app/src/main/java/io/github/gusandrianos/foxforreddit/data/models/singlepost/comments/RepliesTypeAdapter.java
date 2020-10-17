package io.github.gusandrianos.foxforreddit.data.models.singlepost.comments;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public class RepliesTypeAdapter implements JsonDeserializer<Replies> {
    public Replies deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        Replies val;
        if (json.isJsonObject()) {
            JsonElement e = json.getAsJsonObject();
            val = (Replies) ctx.deserialize(e, Replies.class);
        }else if (json.getAsString().equals("")){
            val = null;
        } else {
            throw new RuntimeException("Unexpected JSON type: " + json.getClass());
        }
        return val;
    }
}

