package io.github.gusandrianos.foxforreddit.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PopularFragmentViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PopularFragmentViewModelFactory;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {

    private ArrayList<String> mtxt_subredit;
    private ArrayList<String> mtxt_user;
    private ArrayList<Integer> mtxt_votes;
    private View view;

    public PostListAdapter( View view, ArrayList<String> mtxt_subredit, ArrayList<String> txt_user, ArrayList<Integer> txt_votes) {
        this.mtxt_subredit = mtxt_subredit;
        this.mtxt_user = txt_user;
        this.mtxt_votes = txt_votes;
        this.view = view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_popular, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_subreddit.setText(mtxt_subredit.get(position));
        holder.txt_user.setText(mtxt_user.get(position));
        holder.txt_votes.setText(mtxt_votes.get(position));
    }

    @Override
    public int getItemCount() {
        return mtxt_subredit.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_subreddit;
        private TextView txt_user;
        private TextView txt_votes;

        public ViewHolder(@NonNull View itemView) {
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

}
