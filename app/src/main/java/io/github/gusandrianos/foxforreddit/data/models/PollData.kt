package io.github.gusandrianos.foxforreddit.data.models

import com.google.gson.annotations.SerializedName

class PollData {
    @SerializedName("voting_end_timestamp")
    val votingEndTimestamp: Long = 0

    @SerializedName("total_stake_amount")
    val totalStakeAmount: Any? = null

    @SerializedName("is_prediction")
    val isIsPrediction = false

    @SerializedName("user_selection")
    val userSelection: Any? = null

    @SerializedName("options")
    val options: List<OptionsItem>? = null

    @SerializedName("resolved_option_id")
    val resolvedOptionId: Any? = null

    @SerializedName("user_won_amount")
    val userWonAmount: Any? = null

    @SerializedName("total_vote_count")
    val totalVoteCount = 0
}