package io.github.gusandrianos.foxforreddit.data.repositories;

import io.github.gusandrianos.foxforreddit.data.network.RedditAPI
import io.github.gusandrianos.foxforreddit.data.network.RetrofitService

public class UserRepository {
    private val redditAPI: RedditAPI = RetrofitService.getRedditAPIInstance()


}
