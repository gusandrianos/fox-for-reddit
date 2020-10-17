package io.github.gusandrianos.foxforreddit.data.models.generatedComments.morechildren;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Json{

    @SerializedName("data")
    private DataThings data;

    @SerializedName("errors")
    private List<Object> errors;

    public DataThings getData(){
        return data;
    }

    public List<Object> getErrors(){
        return errors;
    }
}