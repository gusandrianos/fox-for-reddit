package io.github.gusandrianos.foxforreddit.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {
    @SerializedName("secure_media")
    private Object secureMedia;

    @SerializedName("saved")
    private boolean saved;

    @SerializedName("hide_score")
    private boolean hideScore;

    @SerializedName("subreddit_id")
    private String subredditId;

    @SerializedName("score")
    private int score;

    @SerializedName("num_comments")
    private int numComments;

    @SerializedName("spoiler")
    private boolean spoiler;

    @SerializedName("id")
    private String id;

    @SerializedName("created_utc")
    private long createdUtc;

    @SerializedName("link_flair_template_id")
    private String linkFlairTemplateId;

    @SerializedName("edited")
    private String edited;

    @SerializedName("author_flair_background_color")
    private Object authorFlairBackgroundColor;

    @SerializedName("domain")
    private String domain;

    @SerializedName("no_follow")
    private boolean noFollow;

    @SerializedName("ups")
    private int ups;

    @SerializedName("selftext")
    private String selftext;

    @SerializedName("author_flair_type")
    private String authorFlairType;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("author_flair_css_class")
    private Object authorFlairCssClass;


    @SerializedName("post_hint")
    private String postHint;


    @SerializedName("mod_reports")
    private List<Object> modReports;

    @SerializedName("send_replies")
    private boolean sendReplies;

    @SerializedName("archived")
    private boolean archived;

    @SerializedName("author_flair_text_color")
    private Object authorFlairTextColor;

    @SerializedName("is_self")
    private boolean isSelf;

    @SerializedName("author_fullname")
    private String authorFullname;

    @SerializedName("link_flair_css_class")
    private Object linkFlairCssClass;

    @SerializedName("upvote_ratio")
    private double upvoteRatio;

    @SerializedName("clicked")
    private boolean clicked;

    @SerializedName("author_flair_template_id")
    private Object authorFlairTemplateId;

    @SerializedName("url")
    private String url;

    @SerializedName("url_overridden_by_dest")
    private String urlOverriddenByDest;

    @SerializedName("stickied")
    private boolean stickied;

    @SerializedName("view_count")
    private Object viewCount;

    @SerializedName("link_flair_richtext")
    private List<Object> linkFlairRichtext;

    @SerializedName("link_flair_background_color")
    private String linkFlairBackgroundColor;

    @SerializedName("author_flair_richtext")
    private List<Object> authorFlairRichtext;

    @SerializedName("over_18")
    private boolean over18;

    @SerializedName("subreddit")
    private String subreddit;

    @SerializedName("suggested_sort")
    private Object suggestedSort;

    @SerializedName("locked")
    private boolean locked;

    @SerializedName("likes")
    private Object likes;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("created")
    private Long created;

    @SerializedName("author")
    private String author;

    @SerializedName("link_flair_text_color")
    private String linkFlairTextColor;

    @SerializedName("is_video")
    private boolean isVideo;

    @SerializedName("subreddit_name_prefixed")
    private String subredditNamePrefixed;

    @SerializedName("name")
    private String name;

    @SerializedName("media_only")
    private boolean mediaOnly;

    @SerializedName("preview")
    private Preview preview;

    @SerializedName("hidden")
    private boolean hidden;

    @SerializedName("author_patreon_flair")
    private boolean authorPatreonFlair;

    @SerializedName("media")
    private Object media;

    @SerializedName("title")
    private String title;

    @SerializedName("author_flair_text")
    private Object authorFlairText;

    @SerializedName("thumbnail_width")
    private int thumbnailWidth;

    @SerializedName("link_flair_text")
    private Object linkFlairText;

    @SerializedName("subreddit_subscribers")
    private int subredditSubscribers;

    @SerializedName("thumbnail_height")
    private int thumbnailHeight;

    @SerializedName("link_flair_type")
    private String linkFlairType;

    @SerializedName("visited")
    private boolean visited;

    @SerializedName("is_reddit_media_domain")
    private boolean isRedditMediaDomain;

    public String getTitle() {
        return title;
    }

    public int getScore() {
        return score;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isStickied() {
        return stickied;
    }

    public Long getCreated() {
        return created;
    }

    public String getEdited() {
        return edited;
    }

    public long getCreatedUtc() {
        return createdUtc;
    }

    public String getPost_hint() {
        return postHint;
    }

    public String getSelftext() {
        return selftext;
    }

    public String getUrlOverriddenByDest() {
        return urlOverriddenByDest;
    }

    public boolean isSelf() { return isSelf; }
}
