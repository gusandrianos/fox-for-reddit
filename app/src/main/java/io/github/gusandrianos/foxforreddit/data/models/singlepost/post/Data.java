package io.github.gusandrianos.foxforreddit.data.models.singlepost.post;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

    @SerializedName("modhash")
    private String modhash;

    @SerializedName("children")
    private List<ChildrenItem> children;

    @SerializedName("before")
    private Object before;

    @SerializedName("dist")
    private int dist;

    @SerializedName("after")
    private Object after;

    @SerializedName("secure_media")
    private SecureMedia secureMedia;

    @SerializedName("saved")
    private boolean saved;

    @SerializedName("hide_score")
    private boolean hideScore;

    @SerializedName("total_awards_received")
    private int totalAwardsReceived;

    @SerializedName("subreddit_id")
    private String subredditId;

    @SerializedName("score")
    private int score;

    @SerializedName("num_comments")
    private int numComments;

    @SerializedName("mod_reason_title")
    private Object modReasonTitle;

    @SerializedName("whitelist_status")
    private String whitelistStatus;

    @SerializedName("removed_by")
    private Object removedBy;

    @SerializedName("spoiler")
    private boolean spoiler;

    @SerializedName("id")
    private String id;

    @SerializedName("created_utc")
    private int createdUtc;

    @SerializedName("link_flair_template_id")
    private String linkFlairTemplateId;

    @SerializedName("banned_at_utc")
    private Object bannedAtUtc;

    @SerializedName("discussion_type")
    private Object discussionType;

    @SerializedName("edited")
    private boolean edited;

    @SerializedName("allow_live_comments")
    private boolean allowLiveComments;

    @SerializedName("author_flair_background_color")
    private String authorFlairBackgroundColor;

    @SerializedName("approved_by")
    private Object approvedBy;

    @SerializedName("media_embed")
    private MediaEmbed mediaEmbed;

    @SerializedName("top_awarded_type")
    private Object topAwardedType;

    @SerializedName("domain")
    private String domain;

    @SerializedName("approved_at_utc")
    private Object approvedAtUtc;

    @SerializedName("no_follow")
    private boolean noFollow;

    @SerializedName("num_duplicates")
    private int numDuplicates;

    @SerializedName("ups")
    private int ups;

    @SerializedName("author_flair_type")
    private String authorFlairType;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("content_categories")
    private List<String> contentCategories;

    @SerializedName("wls")
    private int wls;

    @SerializedName("author_flair_css_class")
    private String authorFlairCssClass;

    @SerializedName("mod_reports")
    private List<Object> modReports;

    @SerializedName("gilded")
    private int gilded;

    @SerializedName("removal_reason")
    private Object removalReason;

    @SerializedName("send_replies")
    private boolean sendReplies;

    @SerializedName("archived")
    private boolean archived;

    @SerializedName("author_flair_text_color")
    private String authorFlairTextColor;

    @SerializedName("can_mod_post")
    private boolean canModPost;

    @SerializedName("is_self")
    private boolean isSelf;

    @SerializedName("link_flair_css_class")
    private String linkFlairCssClass;

    @SerializedName("author_fullname")
    private String authorFullname;

    @SerializedName("selftext")
    private String selftext;

    @SerializedName("upvote_ratio")
    private double upvoteRatio;

    @SerializedName("selftext_html")
    private Object selftextHtml;

    @SerializedName("user_reports")
    private List<Object> userReports;

    @SerializedName("is_crosspostable")
    private boolean isCrosspostable;

    @SerializedName("clicked")
    private boolean clicked;

    @SerializedName("author_flair_template_id")
    private String authorFlairTemplateId;

    @SerializedName("url")
    private String url;

    @SerializedName("url_overridden_by_dest")
    private String urlOverriddenByDest;

    @SerializedName("parent_whitelist_status")
    private String parentWhitelistStatus;

    @SerializedName("stickied")
    private boolean stickied;

    @SerializedName("quarantine")
    private boolean quarantine;

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

    @SerializedName("can_gild")
    private boolean canGild;

    @SerializedName("is_robot_indexable")
    private boolean isRobotIndexable;

    @SerializedName("author_premium")
    private boolean authorPremium;

    @SerializedName("post_hint")
    private String postHint;

    @SerializedName("locked")
    private boolean locked;

    @SerializedName("likes")
    private Boolean likes;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("downs")
    private int downs;

    @SerializedName("created")
    private int created;

    @SerializedName("author")
    private String author;

    @SerializedName("treatment_tags")
    private List<Object> treatmentTags;

    @SerializedName("link_flair_text_color")
    private String linkFlairTextColor;

    @SerializedName("gildings")
    private Gildings gildings;

    @SerializedName("report_reasons")
    private Object reportReasons;

    @SerializedName("is_video")
    private boolean isVideo;

    @SerializedName("is_original_content")
    private boolean isOriginalContent;

    @SerializedName("subreddit_name_prefixed")
    private String subredditNamePrefixed;

    @SerializedName("mod_reason_by")
    private Object modReasonBy;

    @SerializedName("name")
    private String name;

    @SerializedName("awarders")
    private List<Object> awarders;

    @SerializedName("media_only")
    private boolean mediaOnly;

    @SerializedName("preview")
    private Preview preview;

    @SerializedName("num_reports")
    private Object numReports;

    @SerializedName("pinned")
    private boolean pinned;

    @SerializedName("hidden")
    private boolean hidden;

    @SerializedName("author_patreon_flair")
    private boolean authorPatreonFlair;

    @SerializedName("mod_note")
    private Object modNote;

    @SerializedName("media")
    private Media media;

    @SerializedName("title")
    private String title;

    @SerializedName("author_flair_text")
    private String authorFlairText;

    @SerializedName("num_crossposts")
    private int numCrossposts;

    @SerializedName("thumbnail_width")
    private int thumbnailWidth;

    @SerializedName("secure_media_embed")
    private SecureMediaEmbed secureMediaEmbed;

    @SerializedName("link_flair_text")
    private String linkFlairText;

    @SerializedName("subreddit_type")
    private String subredditType;

    @SerializedName("is_meta")
    private boolean isMeta;

    @SerializedName("subreddit_subscribers")
    private int subredditSubscribers;

    @SerializedName("distinguished")
    private Object distinguished;

    @SerializedName("removed_by_category")
    private Object removedByCategory;

    @SerializedName("thumbnail_height")
    private int thumbnailHeight;

    @SerializedName("link_flair_type")
    private String linkFlairType;

    @SerializedName("all_awardings")
    private List<AllAwardingsItem> allAwardings;

    @SerializedName("visited")
    private boolean visited;

    @SerializedName("pwls")
    private int pwls;

    @SerializedName("category")
    private Object category;

    @SerializedName("banned_by")
    private Object bannedBy;

    @SerializedName("contest_mode")
    private boolean contestMode;

    @SerializedName("is_reddit_media_domain")
    private boolean isRedditMediaDomain;

    public String getModhash(){
        return modhash;
    }

    public List<ChildrenItem> getChildren(){
        return children;
    }

    public Object getBefore(){
        return before;
    }

    public int getDist(){
        return dist;
    }

    public Object getAfter(){
        return after;
    }

    public SecureMedia getSecureMedia(){
        return secureMedia;
    }

    public boolean isSaved(){
        return saved;
    }

    public boolean isHideScore(){
        return hideScore;
    }

    public int getTotalAwardsReceived(){
        return totalAwardsReceived;
    }

    public String getSubredditId(){
        return subredditId;
    }

    public int getScore(){
        return score;
    }

    public int getNumComments(){
        return numComments;
    }

    public Object getModReasonTitle(){
        return modReasonTitle;
    }

    public String getWhitelistStatus(){
        return whitelistStatus;
    }

    public Object getRemovedBy(){
        return removedBy;
    }

    public boolean isSpoiler(){
        return spoiler;
    }

    public String getId(){
        return id;
    }

    public int getCreatedUtc(){
        return createdUtc;
    }

    public String getLinkFlairTemplateId(){
        return linkFlairTemplateId;
    }

    public Object getBannedAtUtc(){
        return bannedAtUtc;
    }

    public Object getDiscussionType(){
        return discussionType;
    }

    public boolean isEdited(){
        return edited;
    }

    public boolean isAllowLiveComments(){
        return allowLiveComments;
    }

    public String getAuthorFlairBackgroundColor(){
        return authorFlairBackgroundColor;
    }

    public Object getApprovedBy(){
        return approvedBy;
    }

    public MediaEmbed getMediaEmbed(){
        return mediaEmbed;
    }

    public Object getTopAwardedType(){
        return topAwardedType;
    }

    public String getDomain(){
        return domain;
    }

    public Object getApprovedAtUtc(){
        return approvedAtUtc;
    }

    public boolean isNoFollow(){
        return noFollow;
    }

    public int getNumDuplicates(){
        return numDuplicates;
    }

    public int getUps(){
        return ups;
    }

    public String getAuthorFlairType(){
        return authorFlairType;
    }

    public String getPermalink(){
        return permalink;
    }

    public List<String> getContentCategories(){
        return contentCategories;
    }

    public int getWls(){
        return wls;
    }

    public String getAuthorFlairCssClass(){
        return authorFlairCssClass;
    }

    public List<Object> getModReports(){
        return modReports;
    }

    public int getGilded(){
        return gilded;
    }

    public Object getRemovalReason(){
        return removalReason;
    }

    public boolean isSendReplies(){
        return sendReplies;
    }

    public boolean isArchived(){
        return archived;
    }

    public String getAuthorFlairTextColor(){
        return authorFlairTextColor;
    }

    public boolean isCanModPost(){
        return canModPost;
    }

    public boolean isIsSelf(){
        return isSelf;
    }

    public String getLinkFlairCssClass(){
        return linkFlairCssClass;
    }

    public String getAuthorFullname(){
        return authorFullname;
    }

    public String getSelftext(){
        return selftext;
    }

    public double getUpvoteRatio(){
        return upvoteRatio;
    }

    public Object getSelftextHtml(){
        return selftextHtml;
    }

    public List<Object> getUserReports(){
        return userReports;
    }

    public boolean isIsCrosspostable(){
        return isCrosspostable;
    }

    public boolean isClicked(){
        return clicked;
    }

    public String getAuthorFlairTemplateId(){
        return authorFlairTemplateId;
    }

    public String getUrl(){
        return url;
    }

    public String getUrlOverriddenByDest(){
        return urlOverriddenByDest;
    }

    public String getParentWhitelistStatus(){
        return parentWhitelistStatus;
    }

    public boolean isStickied(){
        return stickied;
    }

    public boolean isQuarantine(){
        return quarantine;
    }

    public Object getViewCount(){
        return viewCount;
    }

    public List<Object> getLinkFlairRichtext(){
        return linkFlairRichtext;
    }

    public String getLinkFlairBackgroundColor(){
        return linkFlairBackgroundColor;
    }

    public List<Object> getAuthorFlairRichtext(){
        return authorFlairRichtext;
    }

    public boolean isOver18(){
        return over18;
    }

    public String getSubreddit(){
        return subreddit;
    }

    public Object getSuggestedSort(){
        return suggestedSort;
    }

    public boolean isCanGild(){
        return canGild;
    }

    public boolean isIsRobotIndexable(){
        return isRobotIndexable;
    }

    public boolean isAuthorPremium(){
        return authorPremium;
    }

    public String getPostHint(){
        return postHint;
    }

    public boolean isLocked(){
        return locked;
    }

    public Boolean getLikes(){
        return likes;
    }

    public String getThumbnail(){
        return thumbnail;
    }

    public int getDowns(){
        return downs;
    }

    public int getCreated(){
        return created;
    }

    public String getAuthor(){
        return author;
    }

    public List<Object> getTreatmentTags(){
        return treatmentTags;
    }

    public String getLinkFlairTextColor(){
        return linkFlairTextColor;
    }

    public Gildings getGildings(){
        return gildings;
    }

    public Object getReportReasons(){
        return reportReasons;
    }

    public boolean isIsVideo(){
        return isVideo;
    }

    public boolean isIsOriginalContent(){
        return isOriginalContent;
    }

    public String getSubredditNamePrefixed(){
        return subredditNamePrefixed;
    }

    public Object getModReasonBy(){
        return modReasonBy;
    }

    public String getName(){
        return name;
    }

    public List<Object> getAwarders(){
        return awarders;
    }

    public boolean isMediaOnly(){
        return mediaOnly;
    }

    public Preview getPreview(){
        return preview;
    }

    public Object getNumReports(){
        return numReports;
    }

    public boolean isPinned(){
        return pinned;
    }

    public boolean isHidden(){
        return hidden;
    }

    public boolean isAuthorPatreonFlair(){
        return authorPatreonFlair;
    }

    public Object getModNote(){
        return modNote;
    }

    public Media getMedia(){
        return media;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthorFlairText(){
        return authorFlairText;
    }

    public int getNumCrossposts(){
        return numCrossposts;
    }

    public int getThumbnailWidth(){
        return thumbnailWidth;
    }

    public SecureMediaEmbed getSecureMediaEmbed(){
        return secureMediaEmbed;
    }

    public String getLinkFlairText(){
        return linkFlairText;
    }

    public String getSubredditType(){
        return subredditType;
    }

    public boolean isIsMeta(){
        return isMeta;
    }

    public int getSubredditSubscribers(){
        return subredditSubscribers;
    }

    public Object getDistinguished(){
        return distinguished;
    }

    public Object getRemovedByCategory(){
        return removedByCategory;
    }

    public int getThumbnailHeight(){
        return thumbnailHeight;
    }

    public String getLinkFlairType(){
        return linkFlairType;
    }

    public List<AllAwardingsItem> getAllAwardings(){
        return allAwardings;
    }

    public boolean isVisited(){
        return visited;
    }

    public int getPwls(){
        return pwls;
    }

    public Object getCategory(){
        return category;
    }

    public Object getBannedBy(){
        return bannedBy;
    }

    public boolean isContestMode(){
        return contestMode;
    }

    public boolean isIsRedditMediaDomain(){
        return isRedditMediaDomain;
    }
}