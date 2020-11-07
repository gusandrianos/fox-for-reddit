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

    // Sorting
    const val SORTING_BEST = "best"
    const val SORTING_HOT = "hot"
    const val SORTING_NEW = "new"
    const val SORTING_TOP = "top"
    const val SORTING_CONTROVERSIAL = "controversial"
    const val SORTING_RISING = "rising"
    const val TIME_HOUR = "hour"
    const val TIME_DAY = "day"
    const val TIME_WEEK = "week"
    const val TIME_MONTH = "month"
    const val TIME_YEAR = "year"
    const val TIME_ALL = "all"

    // Post Fragment
    const val ARG_SUBREDDIT_NAME = "subreddit"
    const val ARG_FILTER_NAME = "filter"
    const val ARG_TIME_NAME = "time"
    const val SHARE_TEXT = "Share via"

    // User Fragment
    const val USER_UI_TAB_POSTS = "Posts"
    const val USER_UI_TAB_COMMENTS = "Comments"
    const val USER_UI_TAB_ABOUT = "About"
    const val USER_UI_TAB_UPVOTED = "Upvoted"
    const val USER_UI_TAB_DOWNVOTED = "Downvoted"
    const val USER_UI_TAB_HIDDEN = "Hidden"
    const val USER_UI_TAB_SAVED = "Saved"
    const val USER_UI_BUTTON_FOLLOW = "Follow"
    const val USER_UI_BUTTON_UNFOLLOW = "Unfollow"
    const val USER_UI_BUTTON_EDIT = "Edit"

    // About User Fragment
    const val ARG_USERNAME_NAME = "username"
    const val ARG_POST_KARMA_NAME = "post_karma"
    const val ARG_COMMENT_KARMA_NAME = "comment_karma"

    // Subreddit Fragment
    const val SUBREDDIT_POST_FRAGMENT_TAG = "SubredditPostFragment"

    // Subreddit List Fragment
    const val AUTHORIZED_SUB_LIST_LOCATION = "mine/subscriber"
    const val VISITOR_SUB_LIST_LOCATION = "default"

    // Compose types
    const val COMPOSE_TEXT = 0
    const val COMPOSE_IMAGE = 1
    const val COMPOSE_LINK = 2
    const val COMPOSE_VIDEO = 3
}