package io.github.gusandrianos.foxforreddit.data.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("has_stripe_subscription")
	private boolean hasStripeSubscription;

	@SerializedName("can_create_subreddit")
	private boolean canCreateSubreddit;

	@SerializedName("over_18")
	private boolean over18;

	@SerializedName("pref_clickgadget")
	private int prefClickgadget;

	@SerializedName("comment_karma")
	private int commentKarma;

	@SerializedName("subreddit")
	private Subreddit subreddit;

	@SerializedName("pref_show_trending")
	private boolean prefShowTrending;

	@SerializedName("id")
	private String id;

	@SerializedName("has_visited_new_profile")
	private boolean hasVisitedNewProfile;

	@SerializedName("created_utc")
	private double createdUtc;

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

	@SerializedName("created")
	private double created;

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

	@SerializedName("name")
	private String name;

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

	public boolean isHasStripeSubscription(){
		return hasStripeSubscription;
	}

	public boolean isCanCreateSubreddit(){
		return canCreateSubreddit;
	}

	public boolean isOver18(){
		return over18;
	}

	public int getPrefClickgadget(){
		return prefClickgadget;
	}

	public int getCommentKarma(){
		return commentKarma;
	}

	public Subreddit getSubreddit(){
		return subreddit;
	}

	public boolean isPrefShowTrending(){
		return prefShowTrending;
	}

	public String getId(){
		return id;
	}

	public boolean isHasVisitedNewProfile(){
		return hasVisitedNewProfile;
	}

	public double getCreatedUtc(){
		return createdUtc;
	}

	public int getNumFriends(){
		return numFriends;
	}

	public int getCoins(){
		return coins;
	}

	public boolean isInRedesignBeta(){
		return inRedesignBeta;
	}

	public boolean isHasModMail(){
		return hasModMail;
	}

	public boolean isHideFromRobots(){
		return hideFromRobots;
	}

	public boolean isHasSubscribedToPremium(){
		return hasSubscribedToPremium;
	}

	public double getCreated(){
		return created;
	}

	public int getTotalKarma(){
		return totalKarma;
	}

	public boolean isHasSubscribed(){
		return hasSubscribed;
	}

	public boolean isHasAndroidSubscription(){
		return hasAndroidSubscription;
	}

	public boolean isSeenPremiumAdblockModal(){
		return seenPremiumAdblockModal;
	}

	public boolean isPrefShowSnoovatar(){
		return prefShowSnoovatar;
	}

	public boolean isCanEditName(){
		return canEditName;
	}

	public boolean isForcePasswordReset(){
		return forcePasswordReset;
	}

	public String getName(){
		return name;
	}

	public boolean isSeenGiveAwardTooltip(){
		return seenGiveAwardTooltip;
	}

	public boolean isInChat(){
		return inChat;
	}

	public List<Object> getLinkedIdentities(){
		return linkedIdentities;
	}

	public boolean isIsGold(){
		return isGold;
	}

	public boolean isIsMod(){
		return isMod;
	}

	public int getAwardeeKarma(){
		return awardeeKarma;
	}

	public boolean isInBeta(){
		return inBeta;
	}

	public boolean isHasVerifiedEmail(){
		return hasVerifiedEmail;
	}

	public String getPrefGeopopular(){
		return prefGeopopular;
	}

	public boolean isIsSuspended(){
		return isSuspended;
	}

	public boolean isNewModmailExists(){
		return newModmailExists;
	}

	public boolean isIsSponsor(){
		return isSponsor;
	}

	public boolean isSeenRedesignModal(){
		return seenRedesignModal;
	}

	public boolean isHasExternalAccount(){
		return hasExternalAccount;
	}

	public Object getSuspensionExpirationUtc(){
		return suspensionExpirationUtc;
	}

	public boolean isHasGoldSubscription(){
		return hasGoldSubscription;
	}

	public boolean isSeenLayoutSwitch(){
		return seenLayoutSwitch;
	}

	public boolean isPrefNightmode(){
		return prefNightmode;
	}

	public String getSnoovatarImg(){
		return snoovatarImg;
	}

	public int getLinkKarma(){
		return linkKarma;
	}

	public boolean isHasMail(){
		return hasMail;
	}

	public String getOauthClientId(){
		return oauthClientId;
	}

	public boolean isPrefAutoplay(){
		return prefAutoplay;
	}

	public int getAwarderKarma(){
		return awarderKarma;
	}

	public int getGoldCreddits(){
		return goldCreddits;
	}

	public Object getSnoovatarSize(){
		return snoovatarSize;
	}

	public boolean isVerified(){
		return verified;
	}

	public boolean isHasPaypalSubscription(){
		return hasPaypalSubscription;
	}

	public boolean isPrefTopKarmaSubreddits(){
		return prefTopKarmaSubreddits;
	}

	public boolean isPrefShowTwitter(){
		return prefShowTwitter;
	}

	public boolean isPasswordSet(){
		return passwordSet;
	}

	public boolean isIsEmployee(){
		return isEmployee;
	}

	public boolean isPrefVideoAutoplay(){
		return prefVideoAutoplay;
	}

	public boolean isPrefNoProfanity(){
		return prefNoProfanity;
	}

	public String getIconImg(){
		return iconImg;
	}

	public int getInboxCount(){
		return inboxCount;
	}

	public boolean isHasIosSubscription(){
		return hasIosSubscription;
	}

	public Object getGoldExpiration(){
		return goldExpiration;
	}

	public boolean isSeenSubredditChatFtux(){
		return seenSubredditChatFtux;
	}
}