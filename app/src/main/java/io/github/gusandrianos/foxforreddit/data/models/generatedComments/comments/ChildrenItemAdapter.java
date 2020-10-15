package io.github.gusandrianos.foxforreddit.data.models.generatedComments.comments;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ChildrenItemAdapter extends TypeAdapter<ChildrenItem> {
    private final Gson gson;

    public ChildrenItemAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, ChildrenItem value) throws IOException {
        Log.i("RunTime", "CHILDRENITEM: Not Implemented");
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public ChildrenItem read(JsonReader in) throws IOException {

        switch (in.peek()) {
            case BEGIN_OBJECT:
                Log.i("RunTime", "read: "+ in.toString());
                return gson.fromJson(in, ChildrenItem.class);
            case STRING:
                return new ChildrenItem(in.nextString());
            default:
                throw new RuntimeException("Expected object or string, not " + in.peek());
        }
    }
}
