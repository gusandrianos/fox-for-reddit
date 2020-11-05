package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.GalleryItem;
import io.github.gusandrianos.foxforreddit.data.models.ResolutionsItem;
import io.github.gusandrianos.foxforreddit.data.models.Token;

import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentGroup;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentItem;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.ImageGalleryAdapter;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;
import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.linkify.LinkifyPlugin;

import static io.github.gusandrianos.foxforreddit.utilities.PostAdapterKt.formatValue;

public class SinglePostFragment extends Fragment implements ExpandableCommentItem.OnItemClickListener {
    private boolean wasPlaying;
    private boolean isFullscreen;
    private int orientation;

    NavHostFragment navHostFragment;
    NavController navController;

    ViewStub stub;
    View inflated;

    SimpleExoPlayer player = null;
    ImageView imgPlay;
    SeekBar videoSeekBar;

    RecyclerView mCommentsRecyclerView;
    GroupAdapter<GroupieViewHolder> groupAdapter;

    DisplayMetrics displayMetrics = new DisplayMetrics();
    Markwon markwon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        markwon = Markwon.builder(requireContext())
                .usePlugin(TablePlugin.create(requireContext()))
                .usePlugin(LinkifyPlugin.create())
                .build();

        SinglePostFragmentArgs singlePostFragmentArgs = SinglePostFragmentArgs.fromBundle(requireArguments());
        Data singlePostData = singlePostFragmentArgs.getPost();
        int postType = singlePostFragmentArgs.getPostType();
        mCommentsRecyclerView = view.findViewById(R.id.recyclerview_single_post);

        if (savedInstanceState != null)
            isFullscreen = savedInstanceState.getBoolean("isFullscreen");

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        orientation = getResources().getConfiguration().orientation;

        setUpToolbar(view, singlePostData.getSubredditNamePrefixed());
        initializeUI(singlePostData, view, postType);

        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        String permalink = singlePostData.getPermalink();
        viewModel.getSinglePost(Objects.requireNonNull(permalink).substring(1, permalink.length() - 1), requireActivity().getApplication())
                .observe(getViewLifecycleOwner(), commentListing -> {
                    groupAdapter = new GroupAdapter<>();
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

    private void initializeUI(Data singlePostData, View view, int postType) {
        bindHeaderAndFooter(singlePostData, view);

        switch (postType) {
            case Constants.SELF:
                bindAsSelf(singlePostData, view);
                break;
            case Constants.LINK:
                bindAsLink(singlePostData, view);
                break;
            case Constants.IMAGE:
                switch (FoxToolkit.INSTANCE.getTypeOfImage(singlePostData)) {
                    case Constants.IS_GIF:
                        bindAsGif(singlePostData, view);
                        break;
                    case Constants.IS_GALLERY:
                        bindAsGallery(singlePostData, view);
                        break;
                    default:
                        bindAsImage(singlePostData, view);
                }
                break;
            case Constants.VIDEO:
                if (FoxToolkit.INSTANCE.getTypeOfVideo(singlePostData) == Constants.UNPLAYABLE_VIDEO)
                    bindAsUnplayableVideo(singlePostData, view);
                else
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

        int currentDestinationID = Objects.requireNonNull(navController.getCurrentDestination()).getId();
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);

        mImgPostSubreddit.setOnClickListener(view1 -> {
            if (currentDestinationID != R.id.subredditFragment) {
                String subredditNamePrefixed = singlePostData.getSubredditNamePrefixed();
                NavGraphDirections.ActionGlobalSubredditFragment action = NavGraphDirections.actionGlobalSubredditFragment(subredditNamePrefixed);
                navController.navigate(action);
            }
        });

        mTxtPostSubreddit.setOnClickListener(view1 -> {
            if (currentDestinationID != R.id.subredditFragment) {
                String subredditNamePrefixed = singlePostData.getSubredditNamePrefixed();
                NavGraphDirections.ActionGlobalSubredditFragment action = NavGraphDirections.actionGlobalSubredditFragment(subredditNamePrefixed);
                navController.navigate(action);
            }
        });

        mTxtPostUser.setOnClickListener(view1 -> {
            String authorUsername = singlePostData.getAuthor();
            NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(null, authorUsername);
            navController.navigate(action);
        });

        mImgBtnPostVoteUp.setOnClickListener(view1 -> {
            if (singlePostData.getLikes() == null || !singlePostData.getLikes()) {
                mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24_orange);
                mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24);
                mTxtPostScore.setTextColor(Color.parseColor("#FFE07812"));
            } else {
                mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24);
                mTxtPostScore.setTextColor(Color.parseColor("#AAAAAA"));
            }
            FoxToolkit.INSTANCE.upVote(viewModel, requireActivity().getApplication(), singlePostData);
        });

        mImgBtnPostVoteDown.setOnClickListener(view1 -> {
            if (singlePostData.getLikes() == null || singlePostData.getLikes()) {
                mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24_blue);
                mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24);
                mTxtPostScore.setTextColor(Color.parseColor("#FF5AA4FF"));
            } else {
                mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24);
                mTxtPostScore.setTextColor(Color.parseColor("#AAAAAA"));
            }
            FoxToolkit.INSTANCE.downVote(viewModel, requireActivity().getApplication(), singlePostData);
        });

        mBtnPostShare.setOnClickListener(view1 -> startActivity(Intent.createChooser(FoxToolkit.INSTANCE.shareLink(singlePostData), "Share via")));
    }

    private void bindAsSelf(Data singlePostData, View view) {
        stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_self);
        inflated = stub.inflate();

        if (singlePostData.getSelftext() != null) {
            TextView txtPostBody = inflated.findViewById(R.id.stub_txt_post_body);
            markwon.setMarkdown(txtPostBody, singlePostData.getSelftext());
            txtPostBody.setVisibility(View.VISIBLE);
        }
    }

    private void bindAsLink(Data singlePostData, View view) {
        stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_link);
        inflated = stub.inflate();
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

        View.OnClickListener listener = view12 -> {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
            customTabsIntent.launchUrl(requireContext(), Uri.parse(singlePostData.getUrl()));
        };
        imgPostThumbnail.setOnClickListener(listener);
        txtPostDomain.setOnClickListener(listener);
    }

    private void bindAsGallery(Data singlePostData, View view) {
        List<String> imagesUrl = new ArrayList<>();

        if (singlePostData.getGalleryData() != null) {
            for (GalleryItem galleryItem : singlePostData.getGalleryData().getItems()) {
                String imageUrl = "https://i.redd.it/" + galleryItem.getMediaId() + ".jpg";
                imagesUrl.add(imageUrl);
            }
            ViewStub stub = view.findViewById(R.id.view_stub);
            stub.setLayoutResource(R.layout.stub_view_pager_image_gallery);
            View inflated = stub.inflate();
            ImageGalleryAdapter adapter = new ImageGalleryAdapter(imagesUrl, requireContext());
            ViewPager2 viewPager = inflated.findViewById(R.id.viewpager_image_gallery);
            viewPager.setAdapter(adapter);
            TabLayout tabLayout = inflated.findViewById(R.id.tab_dots);
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> {
                    }
            ).attach();
        }
    }

    private void bindAsGif(Data singlePostData, View view) {
        stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_image);
        inflated = stub.inflate();

        ImageView imgPostImage = inflated.findViewById(R.id.stub_img_post_image);
        Glide.with(inflated).load(singlePostData.getUrlOverriddenByDest()).into(imgPostImage);

        imgPostImage.setOnClickListener(view1 -> FoxToolkit.INSTANCE.fullscreenImage(singlePostData, requireContext()));
    }

    private void bindAsImage(Data singlePostData, View view) {
        stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_image);
        inflated = stub.inflate();

        ImageView imgPostImage = inflated.findViewById(R.id.stub_img_post_image);
        Glide.with(inflated).load(singlePostData.getUrlOverriddenByDest()).into(imgPostImage);

        imgPostImage.setOnClickListener(view1 -> FoxToolkit.INSTANCE.fullscreenImage(singlePostData, requireContext()));
    }

    private void bindAsPoll(Data singlePostData, View view) {
        stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_self);
        inflated = stub.inflate();

        if (singlePostData.getSelftext() != null) {
            TextView txtPostBody = inflated.findViewById(R.id.stub_txt_post_body);
            markwon.setMarkdown(txtPostBody, singlePostData.getSelftext());
            txtPostBody.setVisibility(View.VISIBLE);
        }
    }

    private void bindAsUnplayableVideo(Data singlePostData, View view) {
        stub = view.findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.stub_image);
        inflated = stub.inflate();

        ImageView imgPostImage = inflated.findViewById(R.id.stub_img_post_image);
        ImageView imgPostPlayButton = inflated.findViewById(R.id.stub_img_post_play_button);
        imgPostPlayButton.setVisibility(View.VISIBLE);
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        imgPostImage.getLayoutParams().height = Math.round(displayMetrics.widthPixels * .5625f);

        Glide.with(inflated).load(singlePostData.getMedia().getOembed().getThumbnailUrl()).placeholder(R.drawable.ic_launcher_background).into(imgPostImage);

        imgPostImage.setOnClickListener(view1 -> {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
            customTabsIntent.launchUrl(requireContext(), Uri.parse(singlePostData.getUrl()));
        });
    }

    private void bindAsVideo(Data singlePostData, View view) {
        int videoType;
        if (singlePostData.isVideo()) {
            videoType = Constants.REDDIT_VIDEO;
        } else {
            videoType = Constants.PLAYABLE_VIDEO;
        }
        createVideoPlayer(singlePostData, view, videoType);
    }

    private void createVideoPlayer(Data singlePostData, View view, int videoType) {
        Handler handler = new Handler();

        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            stub = view.findViewById(R.id.view_stub2);
        else
            stub = view.findViewById(R.id.view_stub);

        stub.setLayoutResource(R.layout.stub_video);
        inflated = stub.inflate();

        PlayerView playerView = inflated.findViewById(R.id.video_player);
        TextView txtVideoOpenInNew = playerView.findViewById(R.id.txt_video_open_in_new);
        TextView txtVideoCurrentTime = inflated.findViewById(R.id.txt_video_current_time);
        TextView txtVideoDuration = inflated.findViewById(R.id.txt_video_duration);
        ImageView imgFullscreen = inflated.findViewById(R.id.img_fullscreen);

        imgPlay = playerView.findViewById(R.id.exo_img_play);
        ProgressBar progressBar = inflated.findViewById(R.id.progress_bar);
        videoSeekBar = inflated.findViewById(R.id.video_seek_bar);

        imgFullscreen.setVisibility(View.VISIBLE);

        if (isFullscreen)
            hideViews(view);
        else
            showViews(view);

        Uri videoUri;
        if (videoType == Constants.REDDIT_VIDEO) {
            if (singlePostData.getSecureMedia().getRedditVideo().getHlsUrl() != null) {
                videoUri = Uri.parse(singlePostData.getSecureMedia().getRedditVideo().getHlsUrl());
            } else {
                videoUri = Uri.parse(singlePostData.getSecureMedia().getRedditVideo().getFallbackUrl());
            }
        } else {
            if (singlePostData.getPreview().getRedditVideoPreview().getHlsUrl() != null) {
                videoUri = Uri.parse(singlePostData.getPreview().getRedditVideoPreview().getHlsUrl());
            } else {
                videoUri = Uri.parse(singlePostData.getPreview().getRedditVideoPreview().getFallbackUrl());
            }

            String domain;
            domain = singlePostData.getDomain();

            txtVideoOpenInNew.setText(domain);
            txtVideoOpenInNew.setVisibility(View.VISIBLE);
            txtVideoOpenInNew.setOnClickListener(view12 -> {
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                customTabsIntent.launchUrl(requireContext(), Uri.parse(singlePostData.getUrl()));
            });
        }

        player = new SimpleExoPlayer.Builder(requireActivity()).build();
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
                        if (player.isPlaying())
                            imgPlay.setImageResource(R.drawable.exo_icon_pause);
                        else
                            imgPlay.setImageResource(R.drawable.exo_icon_play);
                        changeSeekBar(player, videoSeekBar, txtVideoCurrentTime, handler);
                        player.setPauseAtEndOfMediaItems(false);
                        videoSeekBar.setMax((int) player.getDuration() / 1000);
                        txtVideoDuration.setText(FoxToolkit.INSTANCE.getTimeOfVideo(player.getDuration() / 1000));
                        break;
                    case Player.STATE_ENDED:
                        imgPlay.setImageResource(R.drawable.exo_icon_play);
                        player.setPauseAtEndOfMediaItems(true);
                        wasPlaying = false;
                    default:
                }
            }
        });

        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (b) {
                    player.seekTo(progress * 1000);
                    txtVideoCurrentTime.setText(FoxToolkit.INSTANCE.getTimeOfVideo(progress));
                }
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

        imgFullscreen.setOnClickListener(view1 -> fullscreenMode(view));
    }

    public void fullscreenMode(View view) {
        isFullscreen = !isFullscreen;
        if (isFullscreen)
            hideViews(view);
        else
            showViews(view);
    }

    private void showViews(View view) {
        lockDrawer(false);

        AppBarLayout appBarLayout = view.findViewById(R.id.appBarLayout_fragment_single_post);
        PlayerView playerView = view.findViewById(R.id.video_player);
        TextView singlePostTitle = view.findViewById(R.id.stub_txt_post_title);
        LinearLayoutCompat singlePostHeader = view.findViewById(R.id.include_single_post_header);
        LinearLayoutCompat singlePostFooter = view.findViewById(R.id.include_single_post_footer);
        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.single_post_collapsing_toolbar);
        Toolbar toolbar = view.findViewById(R.id.single_post_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            playerView.getLayoutParams().height = Math.round(displayMetrics.widthPixels * .5625f);
            appBarLayout.setLayoutParams((new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT)));
            collapsingToolbar.setVisibility(View.VISIBLE);
        } else {
            appBarLayout.setLayoutParams((new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)));
            singlePostHeader.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            singlePostTitle.setVisibility(View.VISIBLE);
        }

        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        collapsingToolbar.setLayoutParams(params);
        singlePostFooter.setVisibility(View.VISIBLE);
        mCommentsRecyclerView.setVisibility(View.VISIBLE);
    }

    public void hideViews(View view) {
        lockDrawer(true);

        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.single_post_collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
        collapsingToolbar.setLayoutParams(params);
        AppBarLayout appBarLayout = view.findViewById(R.id.appBarLayout_fragment_single_post);
        PlayerView playerView = view.findViewById(R.id.video_player);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutCompat singlePostFooter = view.findViewById(R.id.include_single_post_footer);

            appBarLayout.setLayoutParams((new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)));
            playerView.setLayoutParams((new FrameLayout.LayoutParams(PlayerView.LayoutParams.MATCH_PARENT, PlayerView.LayoutParams.MATCH_PARENT)));

            collapsingToolbar.setVisibility(View.GONE);
            singlePostFooter.setVisibility(View.GONE);
        } else {
            TextView singlePostTitle = view.findViewById(R.id.stub_txt_post_title);
            LinearLayoutCompat singlePostHeader = view.findViewById(R.id.include_single_post_header);
            LinearLayoutCompat singlePostFooter = view.findViewById(R.id.include_single_post_footer);
            Toolbar toolbar = view.findViewById(R.id.single_post_toolbar);

            appBarLayout.setLayoutParams((new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)));
            playerView.setLayoutParams((new FrameLayout.LayoutParams(PlayerView.LayoutParams.MATCH_PARENT, PlayerView.LayoutParams.MATCH_PARENT)));

            toolbar.setVisibility(View.GONE);
            singlePostTitle.setVisibility(View.GONE);
            singlePostHeader.setVisibility(View.GONE);
            singlePostFooter.setVisibility(View.GONE);
        }
        mCommentsRecyclerView.setVisibility(View.GONE);
    }

    private void changeSeekBar(SimpleExoPlayer player, SeekBar videoSeekBar, TextView videoCurrentTime, Handler handler) {
        videoSeekBar.setProgress((int) player.getCurrentPosition() / 1000);
        videoCurrentTime.setText(FoxToolkit.INSTANCE.getTimeOfVideo(player.getCurrentPosition() / 1000));
        Runnable runnable = () -> changeSeekBar(player, videoSeekBar, videoCurrentTime, handler);
        handler.postDelayed(runnable, 1000);
    }

    private void lockDrawer(boolean lock) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        DrawerLayout drawer = mainActivity.drawer;
        if (lock) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }


    private void initRecyclerView(View view, GroupAdapter<GroupieViewHolder> groupAdapter) {
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
        if (player != null) {
            wasPlaying = player.isPlaying();
            player.setPlayWhenReady(false);
            imgPlay.setImageResource(R.drawable.exo_icon_play);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        if (player != null)
            player.release();
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (player != null) {
            outState.putLong("currentTime", player.getCurrentPosition());
            outState.putBoolean("isPlaying", wasPlaying);
            outState.putBoolean("isFullscreen", isFullscreen);
            videoSeekBar.setProgress(0);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && player != null) {
            player.seekTo(savedInstanceState.getLong("currentTime"));
            player.setPlayWhenReady(savedInstanceState.getBoolean("isPlaying"));
        }
    }

    private void setUpToolbar(View view, String subreddit) {
        CollapsingToolbarLayout collapsingToolbar = requireActivity().findViewById(R.id.single_post_collapsing_toolbar);
        AppBarLayout appBarLayout = view.findViewById(R.id.appBarLayout_fragment_single_post);
        Toolbar toolbar = view.findViewById(R.id.single_post_toolbar);
        LinearLayoutCompat includeHeader = view.findViewById(R.id.include_single_post_header);

        appBarLayout.addOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener) (appBarLayout1, verticalOffset) -> {
            float normalize = (float) (1 - ((float) -verticalOffset / includeHeader.getMeasuredHeight())) * 255;
            if (Math.abs(verticalOffset) >= includeHeader.getMeasuredHeight()) {
                toolbar.setTitleTextColor(Color.argb(255, 0, 0, 0));
            } else {
                toolbar.setTitleTextColor(Color.argb((int) -normalize, 0, 0, 0));
            }
        });

        toolbar.setTitle(subreddit);
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        mainActivity.setSupportActionBar(toolbar);
        DrawerLayout drawer = mainActivity.drawer;
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        NavigationUI.setupWithNavController(collapsingToolbar, toolbar, navController);
    }
}
