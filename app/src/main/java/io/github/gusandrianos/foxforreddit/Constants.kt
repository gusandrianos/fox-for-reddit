package io.github.gusandrianos.foxforreddit

object Constants {
    // Reddit URL
    const val REDDIT_URL = "https://www.reddit.com"

    // Edit Profile URL
    const val EDIT_PROFILE_URL = "https://new.reddit.com/settings/profile"

    // Activities
    const val REDIRECT_URI = "https://gusandrianos.github.io/login"
    const val LAUNCH_SECOND_ACTIVITY = 1
    const val CLIENT_ID = "n1R0bc_lPPTtVg"
    const val BASE_OAUTH_URL = "https://www.reddit.com/api/v1/authorize.compact"
    const val STATE = "foxForReddit"

    // Post adapter
    const val POST_SUBREDDIT = "PostSubreddit"
    const val POST_USER = "PostUser"
    const val POST_VOTE_UP = "PostVoteUp"
    const val POST_VOTE_DOWN = "PostVoteDown"
    const val POST_COMMENTS_NUM = "PostCommentsNum"
    const val POST_SHARE = "PostShare"
    const val POST_THUMBNAIL = "PostThumbnail"
    const val POST_VOTE_NOW = "PostVoteNow"
    const val POST_ITEM = "PostItem"

    // Reddit paging source
    const val STARTER_PAGE = ""
    const val MODE_POST = 1
    const val MODE_SUBREDDIT = 2

    // Post types
    const val SELF = 1
    const val LINK = 2
    const val IMAGE = 3
    const val VIDEO = 4
    const val POLL = 5
    const val COMMENT = 6

    // Subreddit Repository
    const val ACTION_SUBSCRIBE = 0
    const val ACTION_UNSUBSCRIBE = 1

    // Video Type
    const val REDDIT_VIDEO = 1
    const val PLAYABLE_VIDEO = 2
    const val UNPLAYABLE_VIDEO = 3

    // Image Type
    const val IS_IMAGE = 1
    const val IS_GALLERY = 2
    const val IS_GIF = 3

    // Screen Mode
    const val NORMAL_SCREEN = 0
    const val FULLSCREEN = 1
}