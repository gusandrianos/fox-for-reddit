package io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import io.github.gusandrianos.foxforreddit.data.models.singlepost.comments.Gildings;

public class DataMoreChildren {

    @SerializedName("body_html")
    private String bodyHtml;

    @SerializedName("author_flair_richtext")
    private List<Object> authorFlairRichtext;

    @SerializedName("saved")
    private boolean saved;

    @SerializedName("controversiality")
    private int controversiality;

    @SerializedName("body")
    private String body;

    @SerializedName("total_awards_received")
    private int totalAwardsReceived;

    @SerializedName("link_id")
    private String linkId;

    @SerializedName("subreddit_id")
    private String subredditId;

    @SerializedName("subreddit")
    private String subreddit;

    @SerializedName("score")
    private int score;

    @SerializedName("mod_reason_title")
    private Object modReasonTitle;

    @SerializedName("is_submitter")
    private boolean isSubmitter;

    @SerializedName("can_gild")
    private boolean canGild;

    @SerializedName("id")
    private String id;

    @SerializedName("author_premium")
    private boolean authorPremium;

    @SerializedName("locked")
    private boolean locked;

    @SerializedName("created_utc")
    private int createdUtc;

    @SerializedName("likes")
    private Object likes;

    @SerializedName("banned_at_utc")
    private Object bannedAtUtc;

    @SerializedName("downs")
    private int downs;

    @SerializedName("edited")
    private boolean edited;

    @SerializedName("author")
    private String author;

    @SerializedName("created")
    private int created;

    @SerializedName("treatment_tags")
    private List<Object> treatmentTags;

    @SerializedName("author_flair_background_color")
    private Object authorFlairBackgroundColor;

    @SerializedName("gildings")
    private Gildings gildings;

    @SerializedName("report_reasons")
    private Object reportReasons;

    @SerializedName("approved_by")
    private Object approvedBy;

    @SerializedName("score_hidden")
    private boolean scoreHidden;

    @SerializedName("replies")
    private String replies;

    @SerializedName("subreddit_name_prefixed")
    private String subredditNamePrefixed;

    @SerializedName("mod_reason_by")
    private Object modReasonBy;

    @SerializedName("parent_id")
    private String parentId;

    @SerializedName("top_awarded_type")
    private Object topAwardedType;

    @SerializedName("approved_at_utc")
    private Object approvedAtUtc;

    @SerializedName("no_follow")
    private boolean noFollow;

    @SerializedName("name")
    private String name;

    @SerializedName("ups")
    private int ups;

    @SerializedName("author_flair_type")
    private String authorFlairType;

    @SerializedName("awarders")
    private List<Object> awarders;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("author_flair_css_class")
    private Object authorFlairCssClass;

    @SerializedName("num_reports")
    private Object numReports;

    @SerializedName("mod_reports")
    private List<Object> modReports;

    @SerializedName("gilded")
    private int gilded;

    @SerializedName("author_patreon_flair")
    private boolean authorPatreonFlair;

    @SerializedName("collapsed")
    private boolean collapsed;

    @SerializedName("collapsed_reason")
    private Object collapsedReason;

    @SerializedName("removal_reason")
    private Object removalReason;

    @SerializedName("mod_note")
    private Object modNote;

    @SerializedName("send_replies")
    private boolean sendReplies;

    @SerializedName("author_flair_text")
    private Object authorFlairText;

    @SerializedName("archived")
    private boolean archived;

    @SerializedName("author_flair_text_color")
    private Object authorFlairTextColor;

    @SerializedName("can_mod_post")
    private boolean canModPost;

    @SerializedName("author_fullname")
    private String authorFullname;

    @SerializedName("subreddit_type")
    private String subredditType;

    @SerializedName("user_reports")
    private List<Object> userReports;

    @SerializedName("associated_award")
    private Object associatedAward;

    @SerializedName("distinguished")
    private Object distinguished;

    @SerializedName("author_flair_template_id")
    private Object authorFlairTemplateId;

    @SerializedName("depth")
    private int depth;

    @SerializedName("stickied")
    private boolean stickied;

    @SerializedName("all_awardings")
    private List<Object> allAwardings;

    @SerializedName("comment_type")
    private Object commentType;

    @SerializedName("collapsed_because_crowd_control")
    private Object collapsedBecauseCrowdControl;

    @SerializedName("banned_by")
    private Object bannedBy;



    public String getBodyHtml(){
        return bodyHtml;
    }

    public List<Object> getAuthorFlairRichtext(){
        return authorFlairRichtext;
    }

    public boolean isSaved(){
        return saved;
    }

    public int getControversiality(){
        return controversiality;
    }

    public String getBody(){
        return body;
    }

    public int getTotalAwardsReceived(){
        return totalAwardsReceived;
    }

    public String getLinkId(){
        return linkId;
    }

    public String getSubredditId(){
        return subredditId;
    }

    public String getSubreddit(){
        return subreddit;
    }

    public int getScore(){
        return score;
    }

    public Object getModReasonTitle(){
        return modReasonTitle;
    }

    public boolean isIsSubmitter(){
        return isSubmitter;
    }

    public boolean isCanGild(){
        return canGild;
    }

    public String getId(){
        return id;
    }

    public boolean isAuthorPremium(){
        return authorPremium;
    }

    public boolean isLocked(){
        return locked;
    }

    public int getCreatedUtc(){
        return createdUtc;
    }

    public Object getLikes(){
        return likes;
    }

    public Object getBannedAtUtc(){
        return bannedAtUtc;
    }

    public int getDowns(){
        return downs;
    }

    public boolean isEdited(){
        return edited;
    }

    public String getAuthor(){
        return author;
    }

    public int getCreated(){
        return created;
    }

    public List<Object> getTreatmentTags(){
        return treatmentTags;
    }

    public Object getAuthorFlairBackgroundColor(){
        return authorFlairBackgroundColor;
    }

    public Gildings getGildings(){
        return gildings;
    }

    public Object getReportReasons(){
        return reportReasons;
    }

    public Object getApprovedBy(){
        return approvedBy;
    }

    public boolean isScoreHidden(){
        return scoreHidden;
    }

    public String getReplies(){
        return replies;
    }

    public String getSubredditNamePrefixed(){
        return subredditNamePrefixed;
    }

    public Object getModReasonBy(){
        return modReasonBy;
    }

    public String getParentId(){
        return parentId;
    }

    public Object getTopAwardedType(){
        return topAwardedType;
    }

    public Object getApprovedAtUtc(){
        return approvedAtUtc;
    }

    public boolean isNoFollow(){
        return noFollow;
    }

    public String getName(){
        return name;
    }

    public int getUps(){
        return ups;
    }

    public String getAuthorFlairType(){
        return authorFlairType;
    }

    public List<Object> getAwarders(){
        return awarders;
    }

    public String getPermalink(){
        return permalink;
    }

    public Object getAuthorFlairCssClass(){
        return authorFlairCssClass;
    }

    public Object getNumReports(){
        return numReports;
    }

    public List<Object> getModReports(){
        return modReports;
    }

    public int getGilded(){
        return gilded;
    }

    public boolean isAuthorPatreonFlair(){
        return authorPatreonFlair;
    }

    public boolean isCollapsed(){
        return collapsed;
    }

    public Object getCollapsedReason(){
        return collapsedReason;
    }

    public Object getRemovalReason(){
        return removalReason;
    }

    public Object getModNote(){
        return modNote;
    }

    public boolean isSendReplies(){
        return sendReplies;
    }

    public Object getAuthorFlairText(){
        return authorFlairText;
    }

    public boolean isArchived(){
        return archived;
    }

    public Object getAuthorFlairTextColor(){
        return authorFlairTextColor;
    }

    public boolean isCanModPost(){
        return canModPost;
    }

    public String getAuthorFullname(){
        return authorFullname;
    }

    public String getSubredditType(){
        return subredditType;
    }

    public List<Object> getUserReports(){
        return userReports;
    }

    public Object getAssociatedAward(){
        return associatedAward;
    }

    public Object getDistinguished(){
        return distinguished;
    }

    public Object getAuthorFlairTemplateId(){
        return authorFlairTemplateId;
    }

    public int getDepth(){
        return depth;
    }

    public boolean isStickied(){
        return stickied;
    }

    public List<Object> getAllAwardings(){
        return allAwardings;
    }

    public Object getCommentType(){
        return commentType;
    }

    public Object getCollapsedBecauseCrowdControl(){
        return collapsedBecauseCrowdControl;
    }

    public Object getBannedBy(){
        return bannedBy;
    }
}