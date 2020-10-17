package io.github.gusandrianos.foxforreddit.data.models.trophies;

import com.google.gson.annotations.SerializedName;

public class TrophiesResponse {

	@SerializedName("data")
	private Data data;

	public Data getData(){
		return data;
	}
}