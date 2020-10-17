package io.github.gusandrianos.foxforreddit.data.models.singlepost.comments;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class RepliesAdapter extends TypeAdapter<Replies> {
    private final Gson gson;

    public RepliesAdapter(Gson gson) {
        this.gson = gson;
    }
    @Override
    public void write(JsonWriter out, Replies value) throws IOException {
        Log.i("RunTime", "REPLIES: Not Implemented");
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public Replies read(JsonReader in) throws IOException {
        switch (in.peek()){
            case BEGIN_OBJECT:
                return gson.fromJson(in, Replies.class);
            case STRING:
                return null;
            default:
                throw new RuntimeException("Expected object or string not, " + in.peek());
        }
    }
}
