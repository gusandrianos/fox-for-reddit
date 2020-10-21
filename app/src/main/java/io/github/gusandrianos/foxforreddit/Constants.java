package io.github.gusandrianos.foxforreddit;

public class Constants {
    // Activities
    public static final String REDIRECT_URI = "https://gusandrianos.github.io/login";
    public static final int  LAUNCH_SECOND_ACTIVITY = 1;
    public static final String CLIENT_ID = "n1R0bc_lPPTtVg";
    public static final String BASE_OAUTH_URL = "https://www.reddit.com/api/v1/authorize.compact";
    public static final String STATE = "foxForReddit";

    // Post adapter
    public static final String POST_SUBREDDIT = "PostSubreddit";
    public static final String POST_USER = "PostUser";
    public static final String POST_VOTE_UP = "PostVoteUp";
    public static final String POST_VOTE_DOWN = "PostVoteDown";
    public static final String POST_COMMENTS_NUM = "PostCommentsNum";
    public static final String POST_SHARE = "PostShare";
    public static final String POST_THUMBNAIL = "PostThumbnail";
    public static final String POST_VOTE_NOW = "PostVoteNow";
    public static final String POST_ITEM = "PostItem";

    // Reddit paging source
    public static final String STARTER_PAGE = "";
    public static final int MODE_POST = 1;
    public static final int MODE_SUBREDDIT = 2;
}
