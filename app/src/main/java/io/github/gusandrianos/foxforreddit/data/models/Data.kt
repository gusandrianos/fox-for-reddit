package io.github.gusandrianos.foxforreddit.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Data : Parcelable {

    @SerializedName("content_md")
    val wikiContent: String = ""

    @SerializedName("children")
    val children: List<Thing>? = null

    @SerializedName("before")
    val before: String? = null

    @SerializedName("dist")
    val dist = 0

    @SerializedName("after")
    val after: String? = null

    @SerializedName("subject")
    val subject: String? = null

    @SerializedName("dest")
    val dest: String? = null

    @SerializedName("body")
    val body: String? = null

    @SerializedName("link_title")
    val linkTitle: String? = null

    @SerializedName("secure_media")
    val secureMedia: SecureMedia? = null

    @SerializedName("poll_data")
    val pollData: PollData? = null

    @SerializedName("saved")
    var isSaved: Boolean? = null

    @SerializedName("hide_score")
    val hideScore = false

    @SerializedName("subreddit_id")
    val subredditId: String? = null

    @SerializedName("score")
    val score = 0

    @SerializedName("num_comments")
    val numComments = 0

    @SerializedName("spoiler")
    var spoiler: Boolean? = null

    @SerializedName("id")
    val id: String? = null

    @SerializedName("created_utc")
    val createdUtc: Long = 0

    @SerializedName("link_flair_template_id")
    val linkFlairTemplateId: String? = null

    @SerializedName("edited")
    val edited: Any? = null

    @SerializedName("author_flair_background_color")
    val authorFlairBackgroundColor: Any? = null

    @SerializedName("domain")
    val domain: String? = null

    @SerializedName("no_follow")
    val isNoFollow = false

    @SerializedName("ups")
    val ups = 0

    @SerializedName("selftext")
    var selftext: String? = null

    @SerializedName("author_flair_type")
    val authorFlairType: String? = null

    @SerializedName("permalink")
    val permalink: String? = null

    @SerializedName("author_flair_css_class")
    val authorFlairCssClass: Any? = null

    @SerializedName("post_hint")
    val post_hint: String? = null

    @SerializedName("mod_reports")
    val modReports: List<Any>? = null

    @SerializedName("send_replies")
    val isSendReplies = false

    @SerializedName("archived")
    val isArchived = false

    @SerializedName("author_flair_text_color")
    val authorFlairTextColor: Any? = null

    @SerializedName("is_self")
    val isSelf = false

    @SerializedName("author_fullname")
    val authorFullname: String? = null

    @SerializedName("link_flair_css_class")
    val linkFlairCssClass: Any? = null

    @SerializedName("upvote_ratio")
    val upvoteRatio = 0.0

    @SerializedName("clicked")
    val clicked = false

    @SerializedName("author_flair_template_id")
    val authorFlairTemplateId: Any? = null

    @SerializedName("url")
    val url: String? = null

    @SerializedName("url_overridden_by_dest")
    val urlOverriddenByDest: String? = null

    @SerializedName("stickied")
    val isStickied = false

    @SerializedName("view_count")
    val viewCount: Any? = null

    @SerializedName("link_flair_richtext")
    var linkFlairRichtext: List<RichtextItem>? = null

    @SerializedName("link_flair_background_color")
    var linkFlairBackgroundColor: String? = null

    @SerializedName("author_flair_richtext")
    val authorFlairRichtext: List<Any>? = null

    @SerializedName("is_gallery")
    val isGallery: Boolean? = null

    @SerializedName("gallery_data")
    val galleryData: GalleryData? = null

    @SerializedName("over_18")
    var isOver18: Boolean? = false

    @SerializedName("subreddit")
    val subreddit: Any? = null

    @SerializedName("suggested_sort")
    val suggestedSort: Any? = null

    @SerializedName("locked")
    val isLocked = false

    @SerializedName("likes")
    var likes: Boolean? = null

    @SerializedName("thumbnail")
    val thumbnail: String? = null

    @SerializedName("created")
    val created: String? = null

    @SerializedName("author")
    val author: String? = null

    @SerializedName("link_flair_text_color")
    var linkFlairTextColor: String? = null

    @SerializedName("is_video")
    val isVideo = false

    @SerializedName("subreddit_name_prefixed")
    val subredditNamePrefixed: String? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("media_only")
    val mediaOnly = false

    @SerializedName("preview")
    val preview: Preview? = null

    @SerializedName("hidden")
    var hidden = false

    @SerializedName("author_patreon_flair")
    val isAuthorPatreonFlair = false

    @SerializedName("media")
    val media: Media? = null

    @SerializedName("title")
    val title: String? = null

    @SerializedName("author_flair_text")
    val authorFlairText: Any? = null

    @SerializedName("thumbnail_width")
    val thumbnailWidth = 0

    @SerializedName("link_flair_text")
    var linkFlairText: String? = null

    @SerializedName("subreddit_subscribers")
    val subredditSubscribers = 0

    @SerializedName("thumbnail_height")
    val thumbnailHeight = 0

    @SerializedName("link_flair_type")
    var linkFlairType: String? = null

    @SerializedName("visited")
    val visited = false

    @SerializedName("is_reddit_media_domain")
    val isRedditMediaDomain = false

    @SerializedName("has_stripe_subscription")
    val isHasStripeSubscription = false

    @SerializedName("can_create_subreddit")
    val isCanCreateSubreddit = false

    @SerializedName("pref_clickgadget")
    val prefClickgadget = 0

    @SerializedName("comment_karma")
    val commentKarma = 0

    @SerializedName("pref_show_trending")
    val isPrefShowTrending = false

    @SerializedName("has_visited_new_profile")
    val isHasVisitedNewProfile = false

    @SerializedName("num_friends")
    val numFriends = 0

    @SerializedName("coins")
    val coins = 0

    @SerializedName("in_redesign_beta")
    val isInRedesignBeta = false

    @SerializedName("has_mod_mail")
    val isHasModMail = false

    @SerializedName("hide_from_robots")
    val isHideFromRobots = false

    @SerializedName("has_subscribed_to_premium")
    val isHasSubscribedToPremium = false

    @SerializedName("total_karma")
    val totalKarma = 0

    @SerializedName("has_subscribed")
    val isHasSubscribed = false

    @SerializedName("has_android_subscription")
    val isHasAndroidSubscription = false

    @SerializedName("seen_premium_adblock_modal")
    val isSeenPremiumAdblockModal = false

    @SerializedName("pref_show_snoovatar")
    val isPrefShowSnoovatar = false

    @SerializedName("can_edit_name")
    val isCanEditName = false

    @SerializedName("force_password_reset")
    val isForcePasswordReset = false

    @SerializedName("seen_give_award_tooltip")
    val isSeenGiveAwardTooltip = false

    @SerializedName("in_chat")
    val isInChat = false

    @SerializedName("linked_identities")
    val linkedIdentities: List<Any>? = null

    @SerializedName("is_gold")
    val isIsGold = false

    @SerializedName("is_mod")
    val isIsMod = false

    @SerializedName("awardee_karma")
    val awardeeKarma = 0

    @SerializedName("in_beta")
    val isInBeta = false

    @SerializedName("has_verified_email")
    val isHasVerifiedEmail = false

    @SerializedName("pref_geopopular")
    val prefGeopopular: String? = null

    @SerializedName("is_suspended")
    val isIsSuspended = false

    @SerializedName("new_modmail_exists")
    val isNewModmailExists = false

    @SerializedName("is_sponsor")
    val isIsSponsor = false

    @SerializedName("seen_redesign_modal")
    val isSeenRedesignModal = false

    @SerializedName("has_external_account")
    val isHasExternalAccount = false

    @SerializedName("suspension_expiration_utc")
    val suspensionExpirationUtc: Any? = null

    @SerializedName("has_gold_subscription")
    val isHasGoldSubscription = false

    @SerializedName("seen_layout_switch")
    val isSeenLayoutSwitch = false

    @SerializedName("pref_nightmode")
    val isPrefNightmode = false

    @SerializedName("snoovatar_img")
    val snoovatarImg: String? = null

    @SerializedName("link_karma")
    val linkKarma = 0

    @SerializedName("has_mail")
    val isHasMail = false

    @SerializedName("oauth_client_id")
    val oauthClientId: String? = null

    @SerializedName("pref_autoplay")
    val isPrefAutoplay = false

    @SerializedName("awarder_karma")
    val awarderKarma = 0

    @SerializedName("gold_creddits")
    val goldCreddits = 0

    @SerializedName("snoovatar_size")
    val snoovatarSize: Any? = null

    @SerializedName("verified")
    val isVerified = false

    @SerializedName("has_paypal_subscription")
    val isHasPaypalSubscription = false

    @SerializedName("pref_top_karma_subreddits")
    val isPrefTopKarmaSubreddits = false

    @SerializedName("pref_show_twitter")
    val isPrefShowTwitter = false

    @SerializedName("password_set")
    val isPasswordSet = false

    @SerializedName("is_employee")
    val isIsEmployee = false

    @SerializedName("pref_video_autoplay")
    val isPrefVideoAutoplay = false

    @SerializedName("pref_no_profanity")
    val isPrefNoProfanity = false

    @SerializedName("icon_img")
    val iconImg: String? = null

    @SerializedName("inbox_count")
    val inboxCount = 0

    @SerializedName("has_ios_subscription")
    val isHasIosSubscription = false

    @SerializedName("gold_expiration")
    val goldExpiration: Any? = null

    @SerializedName("seen_subreddit_chat_ftux")
    val isSeenSubredditChatFtux = false

    @SerializedName("trophies")
    val trophies: List<Thing>? = null

    @SerializedName("icon_70")
    val icon70: String? = null

    @SerializedName("modhash")
    val modhash: String? = null

    @SerializedName("user_flair_position")
    val userFlairPosition: String? = null

    @SerializedName("public_description")
    val publicDescription: String? = null

    @SerializedName("key_color")
    val keyColor: String? = null

    @SerializedName("active_user_count")
    val activeUserCount: Int? = null

    @SerializedName("accounts_active")
    val accountsActive: Int? = null

    @SerializedName("user_is_banned")
    val isUserIsBanned = false

    @SerializedName("submit_text_label")
    val submitTextLabel: String? = null

    @SerializedName("user_flair_text_color")
    val userFlairTextColor: Any? = null

    @SerializedName("emojis_enabled")
    val isEmojisEnabled = false

    @SerializedName("user_is_muted")
    val isUserIsMuted = false

    @SerializedName("disable_contributor_requests")
    val isDisableContributorRequests = false

    @SerializedName("public_description_html")
    val publicDescriptionHtml: String? = null

    @SerializedName("is_crosspostable_subreddit")
    val isIsCrosspostableSubreddit = false

    @SerializedName("user_is_subscriber")
    var userIsSubscriber = false

    @SerializedName("whitelist_status")
    val whitelistStatus: Any? = null

    @SerializedName("icon_size")
    val iconSize: List<Int>? = null

    @SerializedName("user_flair_enabled_in_sr")
    val isUserFlairEnabledInSr = false

    @SerializedName("show_media")
    val isShowMedia = false

    @SerializedName("comment_score_hide_mins")
    val commentScoreHideMins = 0

    @SerializedName("is_enrolled_in_new_modmail")
    val isEnrolledInNewModmail: Any? = null

    @SerializedName("header_title")
    val headerTitle: String? = null

    @SerializedName("restrict_commenting")
    val isRestrictCommenting = false

    @SerializedName("subscribers")
    val subscribers = 0

    @SerializedName("restrict_posting")
    val isRestrictPosting = false

    @SerializedName("community_icon")
    val communityIcon: String? = null

    @SerializedName("display_name")
    val displayName: String? = null

    @SerializedName("primary_color")
    val primaryColor: String? = null

    @SerializedName("link_flair_position")
    val linkFlairPosition: String? = null

    @SerializedName("link_flair_enabled")
    val isLinkFlairEnabled = false

    @SerializedName("user_is_contributor")
    val isUserIsContributor = false

    @SerializedName("can_assign_user_flair")
    val isCanAssignUserFlair = false

    @SerializedName("submit_link_label")
    val submitLinkLabel: String? = null

    @SerializedName("banner_img")
    val bannerImg: String? = null

    @SerializedName("user_flair_css_class")
    val userFlairCssClass: Any? = null

    @SerializedName("allow_videogifs")
    val isAllowVideogifs = false

    @SerializedName("user_flair_type")
    val userFlairType: String? = null

    @SerializedName("notification_level")
    val notificationLevel: Any? = null

    @SerializedName("description_html")
    val descriptionHtml: Any? = null

    @SerializedName("allow_polls")
    val isAllowPolls = false

    @SerializedName("user_sr_flair_enabled")
    val userSrFlairEnabled: Any? = null

    @SerializedName("wls")
    val wls: Any? = null

    @SerializedName("suggested_comment_sort")
    val suggestedCommentSort: String? = null

    @SerializedName("videostream_links_count")
    val videostreamLinksCount = 0

    @SerializedName("submit_text")
    val submitText: String? = null

    @SerializedName("user_has_favorited")
    val isUserHasFavorited = false

    @SerializedName("accounts_active_is_fuzzed")
    val isAccountsActiveIsFuzzed = false

    @SerializedName("allow_images")
    val isAllowImages = false

    @SerializedName("public_traffic")
    val isPublicTraffic = false

    @SerializedName("description")
    val description: String? = null

    @SerializedName("user_flair_text")
    val userFlairText: Any? = null

    @SerializedName("banner_background_color")
    val bannerBackgroundColor: String? = null

    @SerializedName("user_flair_template_id")
    val userFlairTemplateId: Any? = null

    @SerializedName("display_name_prefixed")
    val displayNamePrefixed: String? = null

    @SerializedName("submission_type")
    val submissionType: String? = null

    @SerializedName("spoilers_enabled")
    val isSpoilersEnabled = false

    @SerializedName("show_media_preview")
    val isShowMediaPreview = false

    @SerializedName("user_sr_theme_enabled")
    val isUserSrThemeEnabled = false

    @SerializedName("free_form_reports")
    val isFreeFormReports = false

    @SerializedName("lang")
    val lang: String? = null

    @SerializedName("user_is_moderator")
    val isUserIsModerator = false

    @SerializedName("user_flair_background_color")
    val userFlairBackgroundColor: Any? = null

    @SerializedName("allow_discovery")
    val isAllowDiscovery = false

    @SerializedName("banner_background_image")
    val bannerBackgroundImage: String? = null

    @SerializedName("subreddit_type")
    val subredditType: String? = null

    @SerializedName("banner_size")
    val bannerSize: Any? = null

    @SerializedName("allow_galleries")
    val isAllowGalleries = false

    @SerializedName("header_size")
    val headerSize: Any? = null

    @SerializedName("collapse_deleted_comments")
    val isCollapseDeletedComments = false

    @SerializedName("advertiser_category")
    val advertiserCategory: String? = null

    @SerializedName("has_menu_widget")
    val isHasMenuWidget = false

    @SerializedName("collections_enabled")
    val isCollectionsEnabled = false

    @SerializedName("original_content_tag_enabled")
    val isOriginalContentTagEnabled = false

    @SerializedName("event_posts_enabled")
    val isEventPostsEnabled = false

    @SerializedName("allow_predictions")
    val isAllowPredictions = false

    @SerializedName("all_original_content")
    val isAllOriginalContent = false

    @SerializedName("mobile_banner_image")
    val mobileBannerImage: String? = null

    @SerializedName("user_can_flair_in_sr")
    val userCanFlairInSr: Any? = null

    @SerializedName("allow_videos")
    val isAllowVideos = false

    @SerializedName("header_img")
    val headerImg: Any? = null

    @SerializedName("submit_text_html")
    val submitTextHtml: Any? = null

    @SerializedName("wiki_enabled")
    val wikiEnabled: Any? = null

    @SerializedName("quarantine")
    val isQuarantine = false

    @SerializedName("hide_ads")
    val isHideAds = false

    @SerializedName("emojis_custom_size")
    val emojisCustomSize: Any? = null

    @SerializedName("can_assign_link_flair")
    val isCanAssignLinkFlair = false

    @SerializedName("is_default_banner")
    val isIsDefaultBanner = false

    @SerializedName("is_default_icon")
    val isIsDefaultIcon = false

    @SerializedName("allow_chat_post_creation")
    val isAllowChatPostCreation = false

    @SerializedName("is_chat_post_feature_enabled")
    val isIsChatPostFeatureEnabled = false

    @SerializedName("depth")
    val depth = 0

    @SerializedName("parent_id")
    val parentId: String? = null

    @SerializedName("count")
    val count = 0

    @SerializedName("body_html")
    val bodyHtml: String? = null

    @SerializedName("controversiality")
    val controversiality = 0

    @SerializedName("total_awards_received")
    val totalAwardsReceived = 0

    @SerializedName("link_id")
    val linkId: String? = null

    @SerializedName("mod_reason_title")
    val modReasonTitle: Any? = null

    @SerializedName("is_submitter")
    val isIsSubmitter = false

    @SerializedName("can_gild")
    val isCanGild = false

    @SerializedName("author_premium")
    val isAuthorPremium = false

    @SerializedName("banned_at_utc")
    val bannedAtUtc: Any? = null

    @SerializedName("downs")
    val downs = 0

    @SerializedName("treatment_tags")
    val treatmentTags: List<Any>? = null

    @SerializedName("report_reasons")
    val reportReasons: Any? = null

    @SerializedName("approved_by")
    val approvedBy: Any? = null

    @SerializedName("score_hidden")
    val isScoreHidden = false

    @SerializedName("replies") //    @JsonAdapter(RepliesTypeAdapter.class)
    val replies: Any? = null

    @SerializedName("mod_reason_by")
    val modReasonBy: Any? = null

    @SerializedName("top_awarded_type")
    val topAwardedType: Any? = null

    @SerializedName("approved_at_utc")
    val approvedAtUtc: Any? = null

    @SerializedName("awarders")
    val awarders: List<Any>? = null

    @SerializedName("num_reports")
    val numReports: Any? = null

    @SerializedName("gilded")
    val gilded = 0

    @SerializedName("collapsed")
    val isCollapsed = false

    @SerializedName("collapsed_reason")
    val collapsedReason: Any? = null

    @SerializedName("removal_reason")
    val removalReason: Any? = null

    @SerializedName("mod_note")
    val modNote: Any? = null

    @SerializedName("can_mod_post")
    val isCanModPost = false

    @SerializedName("user_reports")
    val userReports: List<Any>? = null

    @SerializedName("associated_award")
    val associatedAward: Any? = null

    @SerializedName("distinguished")
    val distinguished: Any? = null

    @SerializedName("all_awardings")
    val allAwardings: List<Any>? = null

    @SerializedName("comment_type")
    val commentType: Any? = null

    @SerializedName("collapsed_because_crowd_control")
    val collapsedBecauseCrowdControl: Any? = null

    @SerializedName("banned_by")
    val bannedBy: Any? = null

    @SerializedName("is_original_content")
    val isOriginalContent = false

    @SerializedName("pinned")
    val isPinned = false

    @SerializedName("is_meta")
    val isMeta = false

    @SerializedName("category")
    val category: Any? = null

    @SerializedName("user_flair_richtext")
    val userFlairRichtext: List<Any?>? = null
}
