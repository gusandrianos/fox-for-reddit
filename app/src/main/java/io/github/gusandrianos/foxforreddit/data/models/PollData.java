package io.github.gusandrianos.foxforreddit.data.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PollData{

    @SerializedName("voting_end_timestamp")
    private long votingEndTimestamp;

    @SerializedName("total_stake_amount")
    private Object totalStakeAmount;

    @SerializedName("is_prediction")
    private boolean isPrediction;

    @SerializedName("user_selection")
    private Object userSelection;

    @SerializedName("options")
    private List<OptionsItem> options;

    @SerializedName("resolved_option_id")
    private Object resolvedOptionId;

    @SerializedName("user_won_amount")
    private Object userWonAmount;

    @SerializedName("total_vote_count")
    private int totalVoteCount;

    public long getVotingEndTimestamp(){
        return votingEndTimestamp;
    }

    public Object getTotalStakeAmount(){
        return totalStakeAmount;
    }

    public boolean isIsPrediction(){
        return isPrediction;
    }

    public Object getUserSelection(){
        return userSelection;
    }

    public List<OptionsItem> getOptions(){
        return options;
    }

    public Object getResolvedOptionId(){
        return resolvedOptionId;
    }

    public Object getUserWonAmount(){
        return userWonAmount;
    }

    public int getTotalVoteCount(){
        return totalVoteCount;
    }
}