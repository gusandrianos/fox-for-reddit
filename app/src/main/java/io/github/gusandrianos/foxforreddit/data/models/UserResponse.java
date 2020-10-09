package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

	@SerializedName("data")
	private User user;

	@SerializedName("kind")
	private String kind;

	public User getUser(){
		return user;
	}

	public String getKind(){
		return kind;
	}
}