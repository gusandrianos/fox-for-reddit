package io.github.gusandrianos.foxforreddit.data.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Subreddit{

	@SerializedName("public_description")
	private String publicDescription;

	@SerializedName("key_color")
	private String keyColor;

	@SerializedName("over_18")
	private boolean over18;

	@SerializedName("user_is_banned")
	private boolean userIsBanned;

	@SerializedName("description")
	private String description;

	@SerializedName("title")
	private String title;

	@SerializedName("submit_text_label")
	private String submitTextLabel;

	@SerializedName("is_default_banner")
	private boolean isDefaultBanner;

	@SerializedName("user_is_muted")
	private boolean userIsMuted;

	@SerializedName("is_default_icon")
	private boolean isDefaultIcon;

	@SerializedName("disable_contributor_requests")
	private boolean disableContributorRequests;

	@SerializedName("display_name_prefixed")
	private String displayNamePrefixed;

	@SerializedName("user_is_subscriber")
	private boolean userIsSubscriber;

	@SerializedName("icon_size")
	private List<Integer> iconSize;

	@SerializedName("previous_names")
	private List<Object> previousNames;

	@SerializedName("free_form_reports")
	private boolean freeFormReports;

	@SerializedName("show_media")
	private boolean showMedia;

	@SerializedName("default_set")
	private boolean defaultSet;

	@SerializedName("user_is_moderator")
	private boolean userIsModerator;

	@SerializedName("banner_size")
	private List<Integer> bannerSize;

	@SerializedName("subreddit_type")
	private String subredditType;

	@SerializedName("restrict_commenting")
	private boolean restrictCommenting;

	@SerializedName("coins")
	private int coins;

	@SerializedName("subscribers")
	private int subscribers;

	@SerializedName("header_size")
	private Object headerSize;

	@SerializedName("restrict_posting")
	private boolean restrictPosting;

	@SerializedName("community_icon")
	private Object communityIcon;

	@SerializedName("display_name")
	private String displayName;

	@SerializedName("primary_color")
	private String primaryColor;

	@SerializedName("url")
	private String url;

	@SerializedName("user_is_contributor")
	private boolean userIsContributor;

	@SerializedName("link_flair_position")
	private String linkFlairPosition;

	@SerializedName("link_flair_enabled")
	private boolean linkFlairEnabled;

	@SerializedName("submit_link_label")
	private String submitLinkLabel;

	@SerializedName("header_img")
	private Object headerImg;

	@SerializedName("icon_color")
	private String iconColor;

	@SerializedName("icon_img")
	private String iconImg;

	@SerializedName("banner_img")
	private String bannerImg;

	@SerializedName("name")
	private String name;

	@SerializedName("quarantine")
	private boolean quarantine;

	public String getPublicDescription(){
		return publicDescription;
	}

	public String getKeyColor(){
		return keyColor;
	}

	public boolean isOver18(){
		return over18;
	}

	public boolean isUserIsBanned(){
		return userIsBanned;
	}

	public String getDescription(){
		return description;
	}

	public String getTitle(){
		return title;
	}

	public String getSubmitTextLabel(){
		return submitTextLabel;
	}

	public boolean isIsDefaultBanner(){
		return isDefaultBanner;
	}

	public boolean isUserIsMuted(){
		return userIsMuted;
	}

	public boolean isIsDefaultIcon(){
		return isDefaultIcon;
	}

	public boolean isDisableContributorRequests(){
		return disableContributorRequests;
	}

	public String getDisplayNamePrefixed(){
		return displayNamePrefixed;
	}

	public boolean isUserIsSubscriber(){
		return userIsSubscriber;
	}

	public List<Integer> getIconSize(){
		return iconSize;
	}

	public List<Object> getPreviousNames(){
		return previousNames;
	}

	public boolean isFreeFormReports(){
		return freeFormReports;
	}

	public boolean isShowMedia(){
		return showMedia;
	}

	public boolean isDefaultSet(){
		return defaultSet;
	}

	public boolean isUserIsModerator(){
		return userIsModerator;
	}

	public List<Integer> getBannerSize(){
		return bannerSize;
	}

	public String getSubredditType(){
		return subredditType;
	}

	public boolean isRestrictCommenting(){
		return restrictCommenting;
	}

	public int getCoins(){
		return coins;
	}

	public int getSubscribers(){
		return subscribers;
	}

	public Object getHeaderSize(){
		return headerSize;
	}

	public boolean isRestrictPosting(){
		return restrictPosting;
	}

	public Object getCommunityIcon(){
		return communityIcon;
	}

	public String getDisplayName(){
		return displayName;
	}

	public String getPrimaryColor(){
		return primaryColor;
	}

	public String getUrl(){
		return url;
	}

	public boolean isUserIsContributor(){
		return userIsContributor;
	}

	public String getLinkFlairPosition(){
		return linkFlairPosition;
	}

	public boolean isLinkFlairEnabled(){
		return linkFlairEnabled;
	}

	public String getSubmitLinkLabel(){
		return submitLinkLabel;
	}

	public Object getHeaderImg(){
		return headerImg;
	}

	public String getIconColor(){
		return iconColor;
	}

	public String getIconImg(){
		return iconImg;
	}

	public String getBannerImg(){
		return bannerImg;
	}

	public String getName(){
		return name;
	}

	public boolean isQuarantine(){
		return quarantine;
	}
}