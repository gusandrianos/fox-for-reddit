package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;


import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.GalleryItem;
import io.github.gusandrianos.foxforreddit.data.models.OptionsItem;
import io.github.gusandrianos.foxforreddit.data.models.ResolutionsItem;
import io.github.gusandrianos.foxforreddit.data.models.Token;

import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentGroup;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentItem;
import io.github.gusandrianos.foxforreddit.utilities.ImageGalleryAdapter;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;

import static io.github.gusandrianos.foxforreddit.utilities.PostAdapterKt.formatValue;
import static io.github.gusandrianos.foxforreddit.utilities.PostAdapterKt.getPollEndingDate;

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
        int postType = SinglePostFragmentArgs.fromBundle(getArguments()).getPostType();
        ImageView mImgPostSubreddit = view.findViewById(R.id.img_post_subreddit);
        TextView mTxtPostSubreddit = view.findViewById(R.id.txt_post_subreddit);
        TextView mTxtPostUser = view.findViewById(R.id.txt_post_user);
        TextView mTxtTimePosted = view.findViewById(R.id.txt_time_posted);
        TextView txtPostTitle = view.findViewById(R.id.stub_txt_post_title);
        TextView mTxtPostScore = view.findViewById(R.id.txt_post_score);
        ImageButton mImgBtnPostVoteUp = view.findViewById(R.id.imgbtn_post_vote_up);
        ImageButton mImgBtnPostVoteDown = view.findViewById(R.id.imgbtn_post_vote_down);
        Button mBtnPostNumComments = view.findViewById(R.id.btn_post_num_comments);
        Button mBtnPostShare = view.findViewById(R.id.btn_post_share);
        mTxtPostSubreddit.setText(singlePostData.getSubredditNamePrefixed());
        String user = "Posted by User " + singlePostData.getAuthor();
        mTxtPostUser.setText(user);
        mTxtTimePosted.setText(DateUtils.getRelativeTimeSpanString((long) singlePostData.getCreatedUtc() * 1000).toString());
        txtPostTitle.setText(singlePostData.getTitle());
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
        switch (postType) {
            case Constants.SELF:
                bindAsSelf(singlePostData, view);
                break;
            case Constants.LINK: //Todo Change this
                bindAsLink(singlePostData, view);
                break;
            case Constants.IMAGE:
                Toast.makeText(getActivity(), "Image", Toast.LENGTH_SHORT).show();
                bindAsImage(singlePostData, view);
                break;
            case Constants.VIDEO:
                Toast.makeText(getActivity(), "VIDEO", Toast.LENGTH_SHORT).show();
                bindAsVideo(singlePostData, view);
                break;
            case Constants.POLL:
                bindAsPoll(singlePostData, view);
                break;
            case Constants.COMMENT:
                Toast.makeText(getActivity(), "COMMENT", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), "SELF by default", Toast.LENGTH_SHORT).show();
                break;
        }
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

    private void bindAsSelf(Data singlePostData, View view) {
        ViewStub stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_self);
        View inflated = stub.inflate();
        if (singlePostData.getSelftext() != null) {
            TextView txtPostBody = view.findViewById(R.id.stub_txt_post_body);
            txtPostBody.setText(singlePostData.getSelftext());
            txtPostBody.setVisibility(View.VISIBLE);
        }
    }

    private void bindAsLink(Data singlePostData, View view) {
        ViewStub stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_link);
        View inflated = stub.inflate();
        ImageView imgPostThumbnail = inflated.findViewById(R.id.stub_img_post_thumbnail);
        TextView txtPostDomain = inflated.findViewById(R.id.stub_txt_post_domain);
        txtPostDomain.setText(singlePostData.getDomain());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        Toast.makeText(getActivity(), String.valueOf(width) + " " + String.valueOf(height), Toast.LENGTH_SHORT).show();
        int bestWidthDiff = width;
        int widthDiff;
        int res = 0;
        int i = 0;
        for (ResolutionsItem item : singlePostData.getPreview().getImages().get(0).getResolutions()) {
            widthDiff = Math.abs(width - item.getWidth());
            if (widthDiff < bestWidthDiff) {
                bestWidthDiff = widthDiff;
                res = i;
            }
            i++;
        }
        String url = singlePostData.getPreview().getImages().get(0).getResolutions().get(res).getUrl();
        url = url.replace("amp;", "");
        Glide.with(inflated).load(url).into(imgPostThumbnail);
    }

    private void bindAsImage(Data singlePostData, View view) {
        if (singlePostData.isGallery() == null || !singlePostData.isGallery()) {
            ViewStub stub = view.findViewById(R.id.view_stub);
            stub.setLayoutResource(R.layout.stub_image);
            View inflated = stub.inflate();
            ImageView imgPostImage = inflated.findViewById(R.id.stub_img_post_image);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            int bestWidthDiff = width;
            int widthDiff;
            int res = 0;
            int i = 0;
            for (ResolutionsItem item : singlePostData.getPreview().getImages().get(0).getResolutions()) {
                widthDiff = Math.abs(width - item.getWidth());
                if (widthDiff < bestWidthDiff) {
                    bestWidthDiff = widthDiff;
                    res = i;
                }
                i++;
            }
            String url = singlePostData.getPreview().getImages().get(0).getResolutions().get(res).getUrl();
            url = url.replace("amp;", "");
            Glide.with(inflated).load(url).into(imgPostImage);
        } else {
            List<String> imagesId = new ArrayList<>();
            if(singlePostData.getGalleryData()!=null) {
                for (GalleryItem galleryItem : singlePostData.getGalleryData().getItems()) {
                    imagesId.add(galleryItem.getMediaId());
                    Toast.makeText(getActivity(), galleryItem.getMediaId(), Toast.LENGTH_SHORT).show();
                }
                ViewStub stub = view.findViewById(R.id.view_stub);
                stub.setLayoutResource(R.layout.stub_view_pager_image_gallery);
                View inflated = stub.inflate();
                ImageGalleryAdapter adapter = new ImageGalleryAdapter(imagesId);
                ViewPager2 viewPager = inflated.findViewById(R.id.viewpager_image_gallery);
                viewPager.setAdapter(adapter);
                TabLayout tabLayout = inflated.findViewById(R.id.tab_dots);
                new TabLayoutMediator(tabLayout, viewPager,
                        (tab, position) -> {}
                ).attach();
            }
        }
    }

    private void bindAsVideo(Data singlePostData, View view) {
        if (!singlePostData.isVideo()) {
            ViewStub stub = view.findViewById(R.id.view_stub);
            stub.setLayoutResource(R.layout.stub_image);
            View inflated = stub.inflate();
            ImageView imgPostImage = inflated.findViewById(R.id.stub_img_post_image);
            ImageView imgPostPlayButton = inflated.findViewById(R.id.stub_img_post_play_button);
            imgPostPlayButton.setVisibility(View.VISIBLE);
            Glide.with(inflated).load(singlePostData.getMedia().getOembed().getThumbnailUrl()).placeholder(R.drawable.ic_launcher_background).into(imgPostImage);
        } else {
            Toast.makeText(getActivity(), "Not ready yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindAsPoll(Data singlePostData, View view) {
        ViewStub stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_poll);
        View inflated = stub.inflate();
        TextView txtPostTitle = view.findViewById(R.id.stub_txt_post_title);
        RadioGroup rgChoices = inflated.findViewById(R.id.stub_rg_post_choices);
        Button btnVote = inflated.findViewById(R.id.stub_btn_post_vote);
        TextView txtVoteNum = inflated.findViewById(R.id.stub_txt_vote_num);
        TextView txtVoteTimeLeft = inflated.findViewById(R.id.stub_txt_post_vote_time_left);
        txtPostTitle.setText(singlePostData.getTitle());
        if (singlePostData.getSelftext() != null) {
            TextView txtPostBody = view.findViewById(R.id.stub_txt_post_body);
            txtPostBody.setText(singlePostData.getSelftext());
            txtPostBody.setVisibility(View.VISIBLE);
        }
        txtVoteTimeLeft.setText(getPollEndingDate(singlePostData.getPollData().getVotingEndTimestamp()));
        String votes = singlePostData.getPollData().getTotalVoteCount() + " Votes";
        txtVoteNum.setText(votes);
        for (OptionsItem optionsItem : singlePostData.getPollData().getOptions()) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setId(View.generateViewId());
            radioButton.setTag(optionsItem.getId());
            radioButton.setText(optionsItem.getText());
            rgChoices.addView(radioButton);
        }
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
}