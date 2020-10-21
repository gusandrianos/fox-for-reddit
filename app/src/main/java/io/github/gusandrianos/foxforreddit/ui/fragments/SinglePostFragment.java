package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;


import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.Token;

import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentGroup;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentItem;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;

public class SinglePostFragment extends Fragment implements ExpandableCommentItem.OnItemClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Data singlePostData = SinglePostFragmentArgs.fromBundle(getArguments()).getPost();

        ImageView mImgPostSubreddit = view.findViewById(R.id.img_post_subreddit);
        TextView mTxtPostSubreddit = view.findViewById(R.id.txt_post_subreddit);
        TextView mTxtPostUser = view.findViewById(R.id.txt_post_user);
        TextView mTxtTimePosted = view.findViewById(R.id.txt_time_posted);
        TextView mTxtPostTitle = view.findViewById(R.id.txt_single_post_title);
        TextView mTxtPostScore = view.findViewById(R.id.txt_post_score);
        ImageButton mImgBtnPostVoteUp = view.findViewById(R.id.imgbtn_post_vote_up);
        ImageButton mImgBtnPostVoteDown = view.findViewById(R.id.imgbtn_post_vote_down);
        Button mBtnPostNumComments = view.findViewById(R.id.btn_post_num_comments);
        Button mBtnPostShare = view.findViewById(R.id.btn_post_share);

        mTxtPostSubreddit.setText(singlePostData.getSubredditNamePrefixed());
        String user = "Posted by User " + singlePostData.getAuthor();
        mTxtPostUser.setText(user);
        mTxtTimePosted.setText(DateUtils.getRelativeTimeSpanString((long) singlePostData.getCreatedUtc() * 1000).toString());
        mTxtPostTitle.setText(singlePostData.getTitle());
        mTxtPostScore.setText(formatValue(singlePostData.getScore()));
        if (singlePostData.getLikes() != null)
            if (singlePostData.getLikes()) {
                mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24_orange);
                mTxtPostScore.setTextColor(Color.parseColor("#FFE07812"));
            } else {
                mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24_blue);
                mTxtPostScore.setTextColor(Color.parseColor("#FF5AA4FF"));
            }
        mBtnPostNumComments.setText(formatValue(singlePostData.getNumComments()));


        Token mToken = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        String permalink = singlePostData.getPermalink();

        viewModel.getSinglePost(permalink.substring(1, permalink.length() - 1), requireActivity().getApplication())
                .observe(getViewLifecycleOwner(), commentListing -> {

                    GroupAdapter<GroupieViewHolder> groupAdapter = new GroupAdapter<>();
                    initRecyclerView(view, groupAdapter);
                    for (Object child : commentListing.getData().getChildren()) {
                        ChildrenItem item;
                        if (child instanceof String) {
                            item = new ChildrenItem((String) child);
                        } else {
                            Type childType = new TypeToken<ChildrenItem>() {
                            }.getType();
                            Gson gson = new Gson();
                            item = gson.fromJson(gson.toJsonTree(child).getAsJsonObject(), childType);
                        }

                        groupAdapter.add(new ExpandableCommentGroup(item, item.getData().getDepth(), "t3_jbmf8f", SinglePostFragment.this::onLoadMoreClicked));
                    }
                });

    }

    private void initRecyclerView(View view, GroupAdapter<GroupieViewHolder> groupAdapter) {
        RecyclerView mCommentsRecyclerView = view.findViewById(R.id.recyclerview);
        GridLayoutManager groupLayoutManager = new GridLayoutManager(getActivity(), groupAdapter.getSpanCount());
        groupLayoutManager.setSpanSizeLookup(groupAdapter.getSpanSizeLookup());
        mCommentsRecyclerView.setLayoutManager(groupLayoutManager);
        mCommentsRecyclerView.setAdapter(groupAdapter);

//        Token mToken = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();
//        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
//        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
//        Log.i("LISTENER", "Before observe ");
//        viewModel.getMoreChildren("jbmf8f","g936mtd",mToken)
//                .observe(getViewLifecycleOwner(), new Observer<MoreChildren>() {
//                    @Override
//                    public void onChanged(MoreChildren moreChildren) {
//                        for (ChildrenItem more: moreChildren.getJson().getData().getChildren()) {
//                            Log.i("LISTENER", "DuringObserve");
//                            groupAdapter.add(new ExpandableCommentGroup(more, more.getData().getDepth(),"t3_jbmf8f",SinglePostFragment.this::onLoadMoreClicked));
//
//                        }
//                    }
//                });
    }

    @Override
    public void onLoadMoreClicked(@NotNull String linkId, @NotNull String moreChildren, int position) {

        Token mToken = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        Log.i("LISTENER", "Before observe " + position);
        viewModel.getMoreChildren(linkId, moreChildren, mToken);
    }

    public static String formatValue(double number) {
        int power;
        double value = number;
        if (value == 0.0) {
            return "0";
        }
        String suffix = " kmbt";
        String formattedNumber = "";
        NumberFormat formatter = new DecimalFormat("#,###.#");
        power = (int) StrictMath.log10(value);
        value = value / (Math.pow(10, (power / 3) * 3));
        formattedNumber = formatter.format(value);
        formattedNumber = formattedNumber + suffix.charAt(power / 3);
        return formattedNumber.length() > 4 ? formattedNumber.replaceAll("\\.[0-9]+", "") : formattedNumber;
    }
}