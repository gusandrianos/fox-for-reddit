package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

class Subreddit {

    @SerializedName("public_description")
    val publicDescription: String? = null

    @SerializedName("key_color")
    val keyColor: String? = null

    @SerializedName("over_18")
    val isOver18 = false

    @SerializedName("user_is_banned")
    val isUserIsBanned = false

    @SerializedName("description")
    val description: String? = null

    @SerializedName("title")
    val title: String? = null

    @SerializedName("submit_text_label")
    val submitTextLabel: String? = null

    @SerializedName("is_default_banner")
    val isIsDefaultBanner = false

    @SerializedName("user_is_muted")
    val isUserIsMuted = false

    @SerializedName("is_default_icon")
    val isIsDefaultIcon = false

    @SerializedName("disable_contributor_requests")
    val isDisableContributorRequests = false

    @SerializedName("display_name_prefixed")
    val displayNamePrefixed: String? = null

    @SerializedName("user_is_subscriber")
    val isUserIsSubscriber = false

    @SerializedName("icon_size")
    val iconSize: List<Int>? = null

    @SerializedName("previous_names")
    val previousNames: List<Any>? = null

    @SerializedName("free_form_reports")
    val isFreeFormReports = false

    @SerializedName("show_media")
    val isShowMedia = false

    @SerializedName("default_set")
    val isDefaultSet = false

    @SerializedName("user_is_moderator")
    val isUserIsModerator = false

    @SerializedName("banner_size")
    val bannerSize: List<Int>? = null

    @SerializedName("subreddit_type")
    val subredditType: String? = null

    @SerializedName("restrict_commenting")
    val isRestrictCommenting = false

    @SerializedName("coins")
    val coins = 0

    @SerializedName("subscribers")
    val subscribers = 0

    @SerializedName("header_size")
    val headerSize: Any? = null

    @SerializedName("restrict_posting")
    val isRestrictPosting = false

    @SerializedName("community_icon")
    val communityIcon: Any? = null

    @SerializedName("display_name")
    val displayName: String? = null

    @SerializedName("primary_color")
    val primaryColor: String? = null

    @SerializedName("url")
    val url: String? = null

    @SerializedName("user_is_contributor")
    val isUserIsContributor = false

    @SerializedName("link_flair_position")
    val linkFlairPosition: String? = null

    @SerializedName("link_flair_enabled")
    val isLinkFlairEnabled = false

    @SerializedName("submit_link_label")
    val submitLinkLabel: String? = null

    @SerializedName("header_img")
    val headerImg: Any? = null

    @SerializedName("icon_color")
    val iconColor: String? = null

    @SerializedName("icon_img")
    val iconImg: String? = null

    @SerializedName("banner_img")
    val bannerImg: String? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("quarantine")
    val isQuarantine = false
}
