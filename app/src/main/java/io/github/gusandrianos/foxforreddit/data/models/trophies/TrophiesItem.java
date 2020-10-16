package io.github.gusandrianos.foxforreddit.data.models.trophies;

import com.google.gson.annotations.SerializedName;

public class TrophiesItem{

	@SerializedName("data")
	private Data data;

	@SerializedName("kind")
	private String kind;

	public Data getData(){
		return data;
	}

	public String getKind(){
		return kind;
	}
}