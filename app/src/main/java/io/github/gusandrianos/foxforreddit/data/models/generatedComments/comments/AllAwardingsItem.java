package io.github.gusandrianos.foxforreddit.data.models.generatedComments.comments;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AllAwardingsItem{

    @SerializedName("icon_width")
    private int iconWidth;

    @SerializedName("end_date")
    private Object endDate;

    @SerializedName("award_sub_type")
    private String awardSubType;

    @SerializedName("days_of_drip_extension")
    private int daysOfDripExtension;

    @SerializedName("coin_reward")
    private int coinReward;

    @SerializedName("description")
    private String description;

    @SerializedName("tiers_by_required_awardings")
    private Object tiersByRequiredAwardings;

    @SerializedName("awardings_required_to_grant_benefits")
    private Object awardingsRequiredToGrantBenefits;

    @SerializedName("resized_icons")
    private List<ResizedIconsItem> resizedIcons;

    @SerializedName("coin_price")
    private int coinPrice;

    @SerializedName("subreddit_coin_reward")
    private int subredditCoinReward;

    @SerializedName("icon_format")
    private Object iconFormat;

    @SerializedName("award_type")
    private String awardType;

    @SerializedName("static_icon_url")
    private String staticIconUrl;

    @SerializedName("subreddit_id")
    private Object subredditId;

    @SerializedName("is_enabled")
    private boolean isEnabled;

    @SerializedName("penny_price")
    private Object pennyPrice;

    @SerializedName("penny_donate")
    private Object pennyDonate;

    @SerializedName("static_icon_height")
    private int staticIconHeight;

    @SerializedName("id")
    private String id;

    @SerializedName("days_of_premium")
    private int daysOfPremium;

    @SerializedName("start_date")
    private Object startDate;

    @SerializedName("icon_url")
    private String iconUrl;

    @SerializedName("is_new")
    private boolean isNew;

    @SerializedName("icon_height")
    private int iconHeight;

    @SerializedName("static_icon_width")
    private int staticIconWidth;

    @SerializedName("count")
    private int count;

    @SerializedName("resized_static_icons")
    private List<ResizedStaticIconsItem> resizedStaticIcons;

    @SerializedName("giver_coin_reward")
    private Object giverCoinReward;

    @SerializedName("name")
    private String name;

    public int getIconWidth(){
        return iconWidth;
    }

    public Object getEndDate(){
        return endDate;
    }

    public String getAwardSubType(){
        return awardSubType;
    }

    public int getDaysOfDripExtension(){
        return daysOfDripExtension;
    }

    public int getCoinReward(){
        return coinReward;
    }

    public String getDescription(){
        return description;
    }

    public Object getTiersByRequiredAwardings(){
        return tiersByRequiredAwardings;
    }

    public Object getAwardingsRequiredToGrantBenefits(){
        return awardingsRequiredToGrantBenefits;
    }

    public List<ResizedIconsItem> getResizedIcons(){
        return resizedIcons;
    }

    public int getCoinPrice(){
        return coinPrice;
    }

    public int getSubredditCoinReward(){
        return subredditCoinReward;
    }

    public Object getIconFormat(){
        return iconFormat;
    }

    public String getAwardType(){
        return awardType;
    }

    public String getStaticIconUrl(){
        return staticIconUrl;
    }

    public Object getSubredditId(){
        return subredditId;
    }

    public boolean isIsEnabled(){
        return isEnabled;
    }

    public Object getPennyPrice(){
        return pennyPrice;
    }

    public Object getPennyDonate(){
        return pennyDonate;
    }

    public int getStaticIconHeight(){
        return staticIconHeight;
    }

    public String getId(){
        return id;
    }

    public int getDaysOfPremium(){
        return daysOfPremium;
    }

    public Object getStartDate(){
        return startDate;
    }

    public String getIconUrl(){
        return iconUrl;
    }

    public boolean isIsNew(){
        return isNew;
    }

    public int getIconHeight(){
        return iconHeight;
    }

    public int getStaticIconWidth(){
        return staticIconWidth;
    }

    public int getCount(){
        return count;
    }

    public List<ResizedStaticIconsItem> getResizedStaticIcons(){
        return resizedStaticIcons;
    }

    public Object getGiverCoinReward(){
        return giverCoinReward;
    }

    public String getName(){
        return name;
    }
}