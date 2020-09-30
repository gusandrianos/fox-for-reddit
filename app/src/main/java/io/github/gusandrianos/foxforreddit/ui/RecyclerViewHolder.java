package io.github.gusandrianos.foxforreddit.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PopularFragmentViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PopularFragmentViewModelFactory;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView txt_subreddit;
    private TextView txt_user;
    private TextView txt_votes;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_subreddit = itemView.findViewById(R.id.txt_subreddit);
        txt_user = itemView.findViewById(R.id.txt_user);
        txt_votes = itemView.findViewById(R.id.txt_score);
    }

    public void setView(List<Post> posts) {
        for (Post post: posts) {
            txt_subreddit.setText(post.getSubreddit());
            txt_user.setText(post.getAuthor());
            txt_votes.setText(post.getScore());
        }

    }




}
