package io.github.gusandrianos.foxforreddit.data.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("children")
    private List<Thing> children;

    @SerializedName("before")
    private String before;

    @SerializedName("dist")
    private int dist;

    @SerializedName("after")
    private String after;

    @SerializedName("body")
    private String body;

    @SerializedName("link_title")
    private String linkTitle;

    @SerializedName("secure_media")
    private Object secureMedia;

    @SerializedName("poll_data")
    private PollData pollData;

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
    private Object subreddit;

    @SerializedName("suggested_sort")
    private Object suggestedSort;

    @SerializedName("locked")
    private boolean locked;

    @SerializedName("likes")
    private Object likes;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("created")
    private String created;

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
    private Media media;

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

    @SerializedName("has_stripe_subscription")
    private boolean hasStripeSubscription;

    @SerializedName("can_create_subreddit")
    private boolean canCreateSubreddit;

    @SerializedName("pref_clickgadget")
    private int prefClickgadget;

    @SerializedName("comment_karma")
    private int commentKarma;

    @SerializedName("pref_show_trending")
    private boolean prefShowTrending;

    @SerializedName("has_visited_new_profile")
    private boolean hasVisitedNewProfile;

    @SerializedName("num_friends")
    private int numFriends;

    @SerializedName("coins")
    private int coins;

    @SerializedName("in_redesign_beta")
    private boolean inRedesignBeta;

    @SerializedName("has_mod_mail")
    private boolean hasModMail;

    @SerializedName("hide_from_robots")
    private boolean hideFromRobots;

    @SerializedName("has_subscribed_to_premium")
    private boolean hasSubscribedToPremium;

    @SerializedName("total_karma")
    private int totalKarma;

    @SerializedName("has_subscribed")
    private boolean hasSubscribed;

    @SerializedName("has_android_subscription")
    private boolean hasAndroidSubscription;

    @SerializedName("seen_premium_adblock_modal")
    private boolean seenPremiumAdblockModal;

    @SerializedName("pref_show_snoovatar")
    private boolean prefShowSnoovatar;

    @SerializedName("can_edit_name")
    private boolean canEditName;

    @SerializedName("force_password_reset")
    private boolean forcePasswordReset;

    @SerializedName("seen_give_award_tooltip")
    private boolean seenGiveAwardTooltip;

    @SerializedName("in_chat")
    private boolean inChat;

    @SerializedName("linked_identities")
    private List<Object> linkedIdentities;

    @SerializedName("is_gold")
    private boolean isGold;

    @SerializedName("is_mod")
    private boolean isMod;

    @SerializedName("awardee_karma")
    private int awardeeKarma;

    @SerializedName("in_beta")
    private boolean inBeta;

    @SerializedName("has_verified_email")
    private boolean hasVerifiedEmail;

    @SerializedName("pref_geopopular")
    private String prefGeopopular;

    @SerializedName("is_suspended")
    private boolean isSuspended;

    @SerializedName("new_modmail_exists")
    private boolean newModmailExists;

    @SerializedName("is_sponsor")
    private boolean isSponsor;

    @SerializedName("seen_redesign_modal")
    private boolean seenRedesignModal;

    @SerializedName("has_external_account")
    private boolean hasExternalAccount;

    @SerializedName("suspension_expiration_utc")
    private Object suspensionExpirationUtc;

    @SerializedName("has_gold_subscription")
    private boolean hasGoldSubscription;

    @SerializedName("seen_layout_switch")
    private boolean seenLayoutSwitch;

    @SerializedName("pref_nightmode")
    private boolean prefNightmode;

    @SerializedName("snoovatar_img")
    private String snoovatarImg;

    @SerializedName("link_karma")
    private int linkKarma;

    @SerializedName("has_mail")
    private boolean hasMail;

    @SerializedName("oauth_client_id")
    private String oauthClientId;

    @SerializedName("pref_autoplay")
    private boolean prefAutoplay;

    @SerializedName("awarder_karma")
    private int awarderKarma;

    @SerializedName("gold_creddits")
    private int goldCreddits;

    @SerializedName("snoovatar_size")
    private Object snoovatarSize;

    @SerializedName("verified")
    private boolean verified;

    @SerializedName("has_paypal_subscription")
    private boolean hasPaypalSubscription;

    @SerializedName("pref_top_karma_subreddits")
    private boolean prefTopKarmaSubreddits;

    @SerializedName("pref_show_twitter")
    private boolean prefShowTwitter;

    @SerializedName("password_set")
    private boolean passwordSet;

    @SerializedName("is_employee")
    private boolean isEmployee;

    @SerializedName("pref_video_autoplay")
    private boolean prefVideoAutoplay;

    @SerializedName("pref_no_profanity")
    private boolean prefNoProfanity;

    @SerializedName("icon_img")
    private String iconImg;

    @SerializedName("inbox_count")
    private int inboxCount;

    @SerializedName("has_ios_subscription")
    private boolean hasIosSubscription;

    @SerializedName("gold_expiration")
    private Object goldExpiration;

    @SerializedName("seen_subreddit_chat_ftux")
    private boolean seenSubredditChatFtux;

    @SerializedName("trophies")
    private List<Thing> trophies;

    @SerializedName("icon_70")
    private String icon70;

    @SerializedName("modhash")
    private String modhash;

    @SerializedName("user_flair_position")
    private String userFlairPosition;

    @SerializedName("public_description")
    private String publicDescription;

    @SerializedName("key_color")
    private String keyColor;

    @SerializedName("active_user_count")
    private Object activeUserCount;

    @SerializedName("accounts_active")
    private Object accountsActive;

    @SerializedName("user_is_banned")
    private boolean userIsBanned;

    @SerializedName("submit_text_label")
    private String submitTextLabel;

    @SerializedName("user_flair_text_color")
    private Object userFlairTextColor;

    @SerializedName("emojis_enabled")
    private boolean emojisEnabled;

    @SerializedName("user_is_muted")
    private boolean userIsMuted;

    @SerializedName("disable_contributor_requests")
    private boolean disableContributorRequests;

    @SerializedName("public_description_html")
    private String publicDescriptionHtml;

    @SerializedName("is_crosspostable_subreddit")
    private boolean isCrosspostableSubreddit;

    @SerializedName("user_is_subscriber")
    private boolean userIsSubscriber;

    @SerializedName("whitelist_status")
    private Object whitelistStatus;

    @SerializedName("icon_size")
    private List<Integer> iconSize;

    @SerializedName("user_flair_enabled_in_sr")
    private boolean userFlairEnabledInSr;

    @SerializedName("show_media")
    private boolean showMedia;

    @SerializedName("comment_score_hide_mins")
    private int commentScoreHideMins;

    @SerializedName("is_enrolled_in_new_modmail")
    private Object isEnrolledInNewModmail;

    @SerializedName("header_title")
    private String headerTitle;

    @SerializedName("restrict_commenting")
    private boolean restrictCommenting;

    @SerializedName("subscribers")
    private int subscribers;

    @SerializedName("restrict_posting")
    private boolean restrictPosting;

    @SerializedName("community_icon")
    private String communityIcon;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("primary_color")
    private String primaryColor;

    @SerializedName("link_flair_position")
    private String linkFlairPosition;

    @SerializedName("link_flair_enabled")
    private boolean linkFlairEnabled;

    @SerializedName("user_is_contributor")
    private boolean userIsContributor;

    @SerializedName("can_assign_user_flair")
    private boolean canAssignUserFlair;

    @SerializedName("submit_link_label")
    private String submitLinkLabel;

    @SerializedName("banner_img")
    private String bannerImg;

    @SerializedName("user_flair_css_class")
    private Object userFlairCssClass;

    @SerializedName("allow_videogifs")
    private boolean allowVideogifs;

    @SerializedName("user_flair_type")
    private String userFlairType;

    @SerializedName("notification_level")
    private Object notificationLevel;

    @SerializedName("description_html")
    private Object descriptionHtml;

    @SerializedName("allow_polls")
    private boolean allowPolls;

    @SerializedName("user_sr_flair_enabled")
    private Object userSrFlairEnabled;

    @SerializedName("wls")
    private Object wls;

    @SerializedName("suggested_comment_sort")
    private String suggestedCommentSort;

    @SerializedName("videostream_links_count")
    private int videostreamLinksCount;

    @SerializedName("submit_text")
    private String submitText;

    @SerializedName("user_has_favorited")
    private boolean userHasFavorited;

    @SerializedName("accounts_active_is_fuzzed")
    private boolean accountsActiveIsFuzzed;

    @SerializedName("allow_images")
    private boolean allowImages;

    @SerializedName("public_traffic")
    private boolean publicTraffic;

    @SerializedName("description")
    private String description;

    @SerializedName("user_flair_text")
    private Object userFlairText;

    @SerializedName("banner_background_color")
    private String bannerBackgroundColor;

    @SerializedName("user_flair_template_id")
    private Object userFlairTemplateId;

    @SerializedName("display_name_prefixed")
    private String displayNamePrefixed;

    @SerializedName("submission_type")
    private String submissionType;

    @SerializedName("spoilers_enabled")
    private boolean spoilersEnabled;

    @SerializedName("show_media_preview")
    private boolean showMediaPreview;

    @SerializedName("user_sr_theme_enabled")
    private boolean userSrThemeEnabled;

    @SerializedName("free_form_reports")
    private boolean freeFormReports;

    @SerializedName("lang")
    private String lang;

    @SerializedName("user_is_moderator")
    private boolean userIsModerator;

    @SerializedName("user_flair_background_color")
    private Object userFlairBackgroundColor;

    @SerializedName("allow_discovery")
    private boolean allowDiscovery;

    @SerializedName("banner_background_image")
    private String bannerBackgroundImage;

    @SerializedName("subreddit_type")
    private String subredditType;

    @SerializedName("banner_size")
    private Object bannerSize;

    @SerializedName("allow_galleries")
    private boolean allowGalleries;

    @SerializedName("header_size")
    private Object headerSize;

    @SerializedName("collapse_deleted_comments")
    private boolean collapseDeletedComments;

    @SerializedName("advertiser_category")
    private String advertiserCategory;

    @SerializedName("has_menu_widget")
    private boolean hasMenuWidget;

    @SerializedName("collections_enabled")
    private boolean collectionsEnabled;

    @SerializedName("original_content_tag_enabled")
    private boolean originalContentTagEnabled;

    @SerializedName("event_posts_enabled")
    private boolean eventPostsEnabled;

    @SerializedName("allow_predictions")
    private boolean allowPredictions;

    @SerializedName("all_original_content")
    private boolean allOriginalContent;

    @SerializedName("mobile_banner_image")
    private String mobileBannerImage;

    @SerializedName("user_can_flair_in_sr")
    private Object userCanFlairInSr;

    @SerializedName("allow_videos")
    private boolean allowVideos;

    @SerializedName("header_img")
    private Object headerImg;

    @SerializedName("submit_text_html")
    private Object submitTextHtml;

    @SerializedName("wiki_enabled")
    private Object wikiEnabled;

    @SerializedName("quarantine")
    private boolean quarantine;

    @SerializedName("hide_ads")
    private boolean hideAds;

    @SerializedName("emojis_custom_size")
    private Object emojisCustomSize;

    @SerializedName("can_assign_link_flair")
    private boolean canAssignLinkFlair;

    @SerializedName("is_default_banner")
    private boolean isDefaultBanner;

    @SerializedName("is_default_icon")
    private boolean isDefaultIcon;

    @SerializedName("allow_chat_post_creation")
    private boolean allowChatPostCreation;

    @SerializedName("is_chat_post_feature_enabled")
    private boolean isChatPostFeatureEnabled;

    public List<Thing> getChildren() {
        return children;
    }

    public String getBefore() {
        return before;
    }

    public int getDist() {
        return dist;
    }

    public String getAfter() {
        return after;
    }

    public String getTitle() {
        return title;
    }

    public int getScore() {
        return score;
    }

    public Object getSubreddit() {
        return subreddit;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isStickied() {
        return stickied;
    }

    public String getUrl() {
        return url;
    }

    public String getEdited() {
        return edited;
    }

    public Object getLikes() {
        return likes;
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

    public boolean isSelf() {
        return isSelf;
    }

    public int getNumComments() {
        return numComments;
    }

    public String getDomain() {
        return domain;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Media getMedia() {
        return media;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public PollData getPollData() {
        return pollData;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public String getAuthorFullname() {
        return authorFullname;
    }

    public void setLikes(Object likes) {
        this.likes = likes;
    }

    public boolean isHasStripeSubscription() {
        return hasStripeSubscription;
    }

    public boolean isCanCreateSubreddit() {
        return canCreateSubreddit;
    }

    public boolean isOver18() {
        return over18;
    }

    public int getPrefClickgadget() {
        return prefClickgadget;
    }

    public int getCommentKarma() {
        return commentKarma;
    }

    public boolean isPrefShowTrending() {
        return prefShowTrending;
    }

    public boolean isHasVisitedNewProfile() {
        return hasVisitedNewProfile;
    }

    public int getNumFriends() {
        return numFriends;
    }

    public int getCoins() {
        return coins;
    }

    public boolean isInRedesignBeta() {
        return inRedesignBeta;
    }

    public boolean isHasModMail() {
        return hasModMail;
    }

    public boolean isHideFromRobots() {
        return hideFromRobots;
    }

    public boolean isHasSubscribedToPremium() {
        return hasSubscribedToPremium;
    }

    public int getTotalKarma() {
        return totalKarma;
    }

    public boolean isHasSubscribed() {
        return hasSubscribed;
    }

    public boolean isHasAndroidSubscription() {
        return hasAndroidSubscription;
    }

    public boolean isSeenPremiumAdblockModal() {
        return seenPremiumAdblockModal;
    }

    public boolean isPrefShowSnoovatar() {
        return prefShowSnoovatar;
    }

    public boolean isCanEditName() {
        return canEditName;
    }

    public boolean isForcePasswordReset() {
        return forcePasswordReset;
    }

    public boolean isSeenGiveAwardTooltip() {
        return seenGiveAwardTooltip;
    }

    public boolean isInChat() {
        return inChat;
    }

    public List<Object> getLinkedIdentities() {
        return linkedIdentities;
    }

    public boolean isIsGold() {
        return isGold;
    }

    public boolean isIsMod() {
        return isMod;
    }

    public int getAwardeeKarma() {
        return awardeeKarma;
    }

    public boolean isInBeta() {
        return inBeta;
    }

    public boolean isHasVerifiedEmail() {
        return hasVerifiedEmail;
    }

    public String getPrefGeopopular() {
        return prefGeopopular;
    }

    public boolean isIsSuspended() {
        return isSuspended;
    }

    public boolean isNewModmailExists() { return newModmailExists; }

    public boolean isIsSponsor() {
        return isSponsor;
    }

    public boolean isSeenRedesignModal() {
        return seenRedesignModal;
    }

    public boolean isHasExternalAccount() {
        return hasExternalAccount;
    }

    public Object getSuspensionExpirationUtc() {
        return suspensionExpirationUtc;
    }

    public boolean isHasGoldSubscription() {
        return hasGoldSubscription;
    }

    public boolean isSeenLayoutSwitch() {
        return seenLayoutSwitch;
    }

    public boolean isPrefNightmode() {
        return prefNightmode;
    }

    public String getSnoovatarImg() {
        return snoovatarImg;
    }

    public int getLinkKarma() {
        return linkKarma;
    }

    public boolean isHasMail() {
        return hasMail;
    }

    public String getOauthClientId() {
        return oauthClientId;
    }

    public boolean isPrefAutoplay() {
        return prefAutoplay;
    }

    public int getAwarderKarma() {
        return awarderKarma;
    }

    public int getGoldCreddits() {
        return goldCreddits;
    }

    public Object getSnoovatarSize() {
        return snoovatarSize;
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean isHasPaypalSubscription() {
        return hasPaypalSubscription;
    }

    public boolean isPrefTopKarmaSubreddits() {
        return prefTopKarmaSubreddits;
    }

    public boolean isPrefShowTwitter() {
        return prefShowTwitter;
    }

    public boolean isPasswordSet() {
        return passwordSet;
    }

    public boolean isIsEmployee() {
        return isEmployee;
    }

    public boolean isPrefVideoAutoplay() {
        return prefVideoAutoplay;
    }

    public boolean isPrefNoProfanity() {
        return prefNoProfanity;
    }

    public String getIconImg() { return iconImg; }

    public int getInboxCount() {
        return inboxCount;
    }

    public boolean isHasIosSubscription() {
        return hasIosSubscription;
    }

    public Object getGoldExpiration() {
        return goldExpiration;
    }

    public boolean isSeenSubredditChatFtux() {
        return seenSubredditChatFtux;
    }

    public List<Thing> getTrophies() { return trophies; }

    public String getIcon70() { return icon70; }

    public String getModhash(){
        return modhash;
    }

    public String getUserFlairPosition(){
        return userFlairPosition;
    }

    public String getPublicDescription(){
        return publicDescription;
    }

    public String getKeyColor(){
        return keyColor;
    }

    public Object getActiveUserCount(){
        return activeUserCount;
    }

    public Object getAccountsActive(){
        return accountsActive;
    }

    public boolean isUserIsBanned(){
        return userIsBanned;
    }

    public String getSubmitTextLabel(){
        return submitTextLabel;
    }

    public Object getUserFlairTextColor(){
        return userFlairTextColor;
    }

    public boolean isEmojisEnabled(){
        return emojisEnabled;
    }

    public boolean isUserIsMuted(){
        return userIsMuted;
    }

    public boolean isDisableContributorRequests(){
        return disableContributorRequests;
    }

    public String getPublicDescriptionHtml(){
        return publicDescriptionHtml;
    }

    public boolean isIsCrosspostableSubreddit(){
        return isCrosspostableSubreddit;
    }

    public boolean isUserIsSubscriber(){
        return userIsSubscriber;
    }

    public Object getWhitelistStatus(){
        return whitelistStatus;
    }

    public List<Integer> getIconSize(){
        return iconSize;
    }

    public boolean isUserFlairEnabledInSr(){
        return userFlairEnabledInSr;
    }

    public boolean isShowMedia(){
        return showMedia;
    }

    public int getCommentScoreHideMins(){
        return commentScoreHideMins;
    }

    public Object getIsEnrolledInNewModmail(){
        return isEnrolledInNewModmail;
    }

    public String getHeaderTitle(){
        return headerTitle;
    }

    public boolean isRestrictCommenting(){
        return restrictCommenting;
    }

    public int getSubscribers(){
        return subscribers;
    }

    public boolean isRestrictPosting(){
        return restrictPosting;
    }

    public String getCommunityIcon(){
        return communityIcon;
    }

    public String getDisplayName(){
        return displayName;
    }

    public String getPrimaryColor(){
        return primaryColor;
    }

    public String getLinkFlairPosition(){
        return linkFlairPosition;
    }

    public boolean isLinkFlairEnabled(){
        return linkFlairEnabled;
    }

    public boolean isUserIsContributor(){
        return userIsContributor;
    }

    public boolean isCanAssignUserFlair(){
        return canAssignUserFlair;
    }

    public String getSubmitLinkLabel(){
        return submitLinkLabel;
    }

    public String getBannerImg(){
        return bannerImg;
    }

    public Object getUserFlairCssClass(){
        return userFlairCssClass;
    }

    public boolean isAllowVideogifs(){
        return allowVideogifs;
    }

    public String getUserFlairType(){
        return userFlairType;
    }

    public Object getNotificationLevel(){
        return notificationLevel;
    }

    public Object getDescriptionHtml(){
        return descriptionHtml;
    }

    public boolean isAllowPolls(){
        return allowPolls;
    }

    public Object getUserSrFlairEnabled(){
        return userSrFlairEnabled;
    }

    public Object getWls(){
        return wls;
    }

    public String getSuggestedCommentSort(){
        return suggestedCommentSort;
    }

    public int getVideostreamLinksCount(){
        return videostreamLinksCount;
    }

    public String getSubmitText(){
        return submitText;
    }

    public boolean isUserHasFavorited(){
        return userHasFavorited;
    }

    public boolean isAccountsActiveIsFuzzed(){
        return accountsActiveIsFuzzed;
    }

    public boolean isAllowImages(){
        return allowImages;
    }

    public boolean isPublicTraffic(){
        return publicTraffic;
    }

    public String getDescription(){
        return description;
    }

    public Object getUserFlairText(){
        return userFlairText;
    }

    public String getBannerBackgroundColor(){
        return bannerBackgroundColor;
    }

    public Object getUserFlairTemplateId(){
        return userFlairTemplateId;
    }

    public String getDisplayNamePrefixed(){
        return displayNamePrefixed;
    }

    public String getSubmissionType(){
        return submissionType;
    }

    public boolean isSpoilersEnabled(){
        return spoilersEnabled;
    }

    public boolean isShowMediaPreview(){
        return showMediaPreview;
    }

    public boolean isUserSrThemeEnabled(){
        return userSrThemeEnabled;
    }

    public boolean isFreeFormReports(){
        return freeFormReports;
    }

    public String getLang(){
        return lang;
    }

    public boolean isUserIsModerator(){
        return userIsModerator;
    }

    public Object getUserFlairBackgroundColor(){
        return userFlairBackgroundColor;
    }

    public boolean isAllowDiscovery(){
        return allowDiscovery;
    }

    public String getBannerBackgroundImage(){
        return bannerBackgroundImage;
    }

    public String getSubredditType(){
        return subredditType;
    }

    public Object getBannerSize(){
        return bannerSize;
    }

    public boolean isAllowGalleries(){
        return allowGalleries;
    }

    public Object getHeaderSize(){
        return headerSize;
    }

    public boolean isCollapseDeletedComments(){
        return collapseDeletedComments;
    }

    public String getAdvertiserCategory(){
        return advertiserCategory;
    }

    public boolean isHasMenuWidget(){
        return hasMenuWidget;
    }

    public boolean isCollectionsEnabled(){
        return collectionsEnabled;
    }

    public boolean isOriginalContentTagEnabled(){
        return originalContentTagEnabled;
    }

    public boolean isEventPostsEnabled(){
        return eventPostsEnabled;
    }

    public boolean isAllowPredictions(){
        return allowPredictions;
    }

    public boolean isAllOriginalContent(){
        return allOriginalContent;
    }

    public String getMobileBannerImage(){
        return mobileBannerImage;
    }

    public Object getUserCanFlairInSr(){
        return userCanFlairInSr;
    }

    public boolean isAllowVideos(){
        return allowVideos;
    }

    public Object getHeaderImg(){
        return headerImg;
    }

    public Object getSubmitTextHtml(){
        return submitTextHtml;
    }

    public Object getWikiEnabled(){
        return wikiEnabled;
    }

    public boolean isQuarantine(){
        return quarantine;
    }

    public boolean isHideAds(){
        return hideAds;
    }

    public Object getEmojisCustomSize(){
        return emojisCustomSize;
    }

    public boolean isCanAssignLinkFlair(){
        return canAssignLinkFlair;
    }

    public boolean isIsDefaultBanner(){
        return isDefaultBanner;
    }

    public boolean isIsDefaultIcon(){
        return isDefaultIcon;
    }

    public boolean isAllowChatPostCreation(){
        return allowChatPostCreation;
    }

    public boolean isIsChatPostFeatureEnabled(){
        return isChatPostFeatureEnabled;
    }
}