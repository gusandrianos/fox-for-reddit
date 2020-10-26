package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
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
import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.GalleryItem;
import io.github.gusandrianos.foxforreddit.data.models.ResolutionsItem;
import io.github.gusandrianos.foxforreddit.data.models.Token;

import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentGroup;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentItem;
import io.github.gusandrianos.foxforreddit.utilities.ImageGalleryAdapter;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;

import static io.github.gusandrianos.foxforreddit.utilities.PostAdapterKt.formatValue;

public class SinglePostFragment extends Fragment implements ExpandableCommentItem.OnItemClickListener {

    SimpleExoPlayer player = null;
    DisplayMetrics displayMetrics = new DisplayMetrics();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SinglePostFragmentArgs singlePostFragmentArgs = SinglePostFragmentArgs.fromBundle(requireArguments());
        Data singlePostData = singlePostFragmentArgs.getPost();
        int postType = singlePostFragmentArgs.getPostType();

        bindHeaderAndFooter(singlePostData, view);

        switch (postType) {
            case Constants.SELF:
                bindAsSelf(singlePostData, view);
                break;
            case Constants.LINK:
                bindAsLink(singlePostData, view);
                break;
            case Constants.IMAGE:
                bindAsImage(singlePostData, view);
                break;
            case Constants.VIDEO:
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

        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        String permalink = singlePostData.getPermalink();
        viewModel.getSinglePost(Objects.requireNonNull(permalink).substring(1, permalink.length() - 1), requireActivity().getApplication())
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
                        groupAdapter.add(new ExpandableCommentGroup(item, Objects.requireNonNull(item.getData()).getDepth(), "t3_jbmf8f", SinglePostFragment.this));
                    }
                });
    }

    private void bindHeaderAndFooter(Data singlePostData, View view) {
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

        if (singlePostData.getLikes() != null) {
            if (singlePostData.getLikes()) {
                mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24_orange);
                mTxtPostScore.setTextColor(Color.parseColor("#FFE07812"));
            } else {
                mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24_blue);
                mTxtPostScore.setTextColor(Color.parseColor("#FF5AA4FF"));
            }
        }

        mBtnPostNumComments.setText(formatValue(singlePostData.getNumComments()));
    }

    private void bindAsSelf(Data singlePostData, View view) {
        ViewStub stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_self);
        View inflated = stub.inflate();

        if (singlePostData.getSelftext() != null) {
            TextView txtPostBody = inflated.findViewById(R.id.stub_txt_post_body);
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

        if (singlePostData.getPreview() != null && singlePostData.getPreview().getImages() != null) {
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
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
        } else {
            imgPostThumbnail.setVisibility(View.GONE);
        }
    }

    private void bindAsImage(Data singlePostData, View view) {
        if (singlePostData.isGallery() == null || !singlePostData.isGallery()) {
            ViewStub stub = view.findViewById(R.id.view_stub);
            stub.setLayoutResource(R.layout.stub_image);
            View inflated = stub.inflate();
            ImageView imgPostImage = inflated.findViewById(R.id.stub_img_post_image);
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
//            if (height >= singlePostData.getPreview().getImages().get(0).getResolutions().get(res).getHeight())
            imgPostImage.getLayoutParams().height = singlePostData.getPreview().getImages().get(0).getResolutions().get(res).getHeight();
//            else
//                imgPostImage.getLayoutParams().height = height;
            Toast.makeText(getActivity(), displayMetrics.widthPixels + " " + displayMetrics.heightPixels, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), singlePostData.getPreview().getImages().get(0).getResolutions().get(res).getWidth() + " " + singlePostData.getPreview().getImages().get(0).getResolutions().get(res).getHeight(), Toast.LENGTH_SHORT).show();
            Glide.with(inflated).load(url).into(imgPostImage);

        } else {
            List<String> imagesId = new ArrayList<>();

            if (singlePostData.getGalleryData() != null) {
                for (GalleryItem galleryItem : singlePostData.getGalleryData().getItems()) {
                    imagesId.add(galleryItem.getMediaId());
                }
                ViewStub stub = view.findViewById(R.id.view_stub);
                stub.setLayoutResource(R.layout.stub_view_pager_image_gallery);
                View inflated = stub.inflate();
                ImageGalleryAdapter adapter = new ImageGalleryAdapter(imagesId);
                ViewPager2 viewPager = inflated.findViewById(R.id.viewpager_image_gallery);
                viewPager.setAdapter(adapter);
                TabLayout tabLayout = inflated.findViewById(R.id.tab_dots);
                new TabLayoutMediator(tabLayout, viewPager,
                        (tab, position) -> {
                        }
                ).attach();
            }
        }
    }

    private void bindAsPoll(Data singlePostData, View view) {
        ViewStub stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_self);
        View inflated = stub.inflate();

        if (singlePostData.getSelftext() != null) {
            TextView txtPostBody = inflated.findViewById(R.id.stub_txt_post_body);
            txtPostBody.setText(singlePostData.getSelftext());
            txtPostBody.setVisibility(View.VISIBLE);
        }
    }

    private void bindAsVideo(Data singlePostData, View view) {
        if (singlePostData.getPreview() != null && singlePostData.getPreview().getRedditVideoPreview() == null && !singlePostData.isVideo()) {
            ViewStub stub = view.findViewById(R.id.view_stub);
            stub.setLayoutResource(R.layout.stub_image);
            View inflated = stub.inflate();
            ImageView imgPostImage = inflated.findViewById(R.id.stub_img_post_image);
            ImageView imgPostPlayButton = inflated.findViewById(R.id.stub_img_post_play_button);
            imgPostPlayButton.setVisibility(View.VISIBLE);
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            imgPostImage.getLayoutParams().height = Math.round(displayMetrics.widthPixels * .5625f);

            Glide.with(inflated).load(singlePostData.getMedia().getOembed().getThumbnailUrl()).placeholder(R.drawable.ic_launcher_background).into(imgPostImage);
        } else {
            ViewStub stub = view.findViewById(R.id.view_stub);
            stub.setLayoutResource(R.layout.stub_video);
            View inflated = stub.inflate();
            Handler handler = new Handler();
            PlayerView playerView = inflated.findViewById(R.id.video_player);
            ImageView imgPlay = playerView.findViewById(R.id.exo_img_play);
            ImageView imgFullScreen = playerView.findViewById(R.id.exo_img_fullscreen);
            ProgressBar progressBar = inflated.findViewById(R.id.progress_bar);
            SeekBar videoSeekBar = inflated.findViewById(R.id.video_seek_bar);
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            playerView.getLayoutParams().height = Math.round(displayMetrics.widthPixels * .5625f);
            Uri videoUri;
            if (singlePostData.isVideo()) {
                player = new SimpleExoPlayer.Builder(requireActivity()).build();
                requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                if (singlePostData.getSecureMedia().getRedditVideo().getHlsUrl() != null) {
                    videoUri = Uri.parse(singlePostData.getSecureMedia().getRedditVideo().getHlsUrl());
                } else {
                    videoUri = Uri.parse(singlePostData.getSecureMedia().getRedditVideo().getFallbackUrl());
                }
            } else {
                player = new SimpleExoPlayer.Builder(requireActivity()).build();
                requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

                if (singlePostData.getPreview().getRedditVideoPreview().getHlsUrl() != null) {
                    videoUri = Uri.parse(singlePostData.getPreview().getRedditVideoPreview().getHlsUrl());
                } else {
                    videoUri = Uri.parse(singlePostData.getPreview().getRedditVideoPreview().getFallbackUrl());
                }
            }

            MediaItem mediaItem = MediaItem.fromUri(videoUri);
            playerView.setPlayer(player);
            playerView.setKeepScreenOn(true);
            player.setMediaItem(mediaItem);
            player.prepare();

            player.addListener(new Player.EventListener() {
                @Override
                public void onPlaybackStateChanged(int state) {
                    switch (state) {
                        case Player.STATE_IDLE:
                            break;
                        case Player.STATE_BUFFERING:
                            progressBar.setVisibility(View.VISIBLE);
                            break;
                        case Player.STATE_READY:
                            progressBar.setVisibility(View.GONE);
                            imgPlay.setImageResource(R.drawable.exo_icon_pause);
                            videoSeekBar.setVisibility(View.VISIBLE);
                            videoSeekBar.setMax((int) player.getContentDuration() / 1000);
                            player.play();
                            changeSeekBar(player, videoSeekBar, handler);
                            break;
                        case Player.STATE_ENDED:
                            imgPlay.setImageResource(R.drawable.exo_icon_play);
                            player.setPauseAtEndOfMediaItems(true);
                        default:
                    }
                }
            });

            videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    if (b)
                        player.seekTo(progress * 1000);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            imgPlay.setOnClickListener(view1 -> {
                if (player.getPauseAtEndOfMediaItems()) {
                    player.setPauseAtEndOfMediaItems(false);
                    player.seekTo(0);
                    imgPlay.setImageResource(R.drawable.exo_icon_pause);
                } else if (player.isPlaying()) {
                    imgPlay.setImageResource(R.drawable.exo_icon_play);
                    player.setPlayWhenReady(false);
                } else {
                    imgPlay.setImageResource(R.drawable.exo_icon_pause);
                    player.setPlayWhenReady(true);
                }
            });

        }
    }

    private void changeSeekBar(SimpleExoPlayer player, SeekBar videoSeekBar, Handler handler) {
        videoSeekBar.setProgress((int) player.getCurrentPosition() / 1000);
        Runnable runnable = () -> changeSeekBar(player, videoSeekBar, handler);
        handler.postDelayed(runnable, 1000);
    }

    private void initRecyclerView(View view, GroupAdapter<GroupieViewHolder> groupAdapter) {
        RecyclerView mCommentsRecyclerView = view.findViewById(R.id.recyclerview);
        GridLayoutManager groupLayoutManager = new GridLayoutManager(getActivity(), groupAdapter.getSpanCount());
        groupLayoutManager.setSpanSizeLookup(groupAdapter.getSpanSizeLookup());
        mCommentsRecyclerView.setLayoutManager(groupLayoutManager);
        mCommentsRecyclerView.setAdapter(groupAdapter);
    }

    @Override
    public void onLoadMoreClicked(@NotNull String linkId, @NotNull String moreChildren, int position) {
        Token mToken = InjectorUtils.getInstance().provideTokenRepository().getToken(requireActivity().getApplication());
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        Log.i("LISTENER", "Before observe " + position);
        viewModel.getMoreChildren(linkId, moreChildren, requireActivity().getApplication());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null)
            player.setPlayWhenReady(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null)
            player.setPlayWhenReady(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (player != null)
            player.release();
    }

    @Override
    public void onDestroy() {
//        if (player != null)
//            player.release();
        super.onDestroy();
    }
}
