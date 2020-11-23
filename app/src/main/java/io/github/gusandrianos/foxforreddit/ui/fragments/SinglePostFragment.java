package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.cyanea.Cyanea;
import com.jaredrummler.cyanea.tinting.CyaneaTinter;
import com.libRG.CustomTextView;
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
import io.github.gusandrianos.foxforreddit.data.models.Flair;
import io.github.gusandrianos.foxforreddit.data.models.GalleryItem;
import io.github.gusandrianos.foxforreddit.data.models.MoreChildrenList;
import io.github.gusandrianos.foxforreddit.data.models.ResolutionsItem;

import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentGroup;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentItem;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.ImageGalleryAdapter;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;
import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.linkify.LinkifyPlugin;

public class SinglePostFragment extends Fragment implements ExpandableCommentItem.OnItemClickListener {
    private boolean wasPlaying;
    private boolean isFullscreen;
    private int orientation;

    NavHostFragment navHostFragment;
    NavController navController;

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
        Data singlePostData = getArguments().getParcelable("data");
        int postType = getArguments().getInt("postType");

        if (singlePostData == null) {
            postType = SinglePostFragmentArgs.fromBundle(requireArguments()).getPostType();
            singlePostData = SinglePostFragmentArgs.fromBundle(requireArguments()).getPost();
        }

        switch (postType) {
            case Constants.LINK:
                return inflater.inflate(R.layout.fragment_single_post_link, container, false);
            case Constants.IMAGE:
                if (FoxToolkit.INSTANCE.getTypeOfImage(singlePostData) == Constants.IS_GALLERY)
                    return inflater.inflate(R.layout.fragment_single_post_gallery, container, false);
                return inflater.inflate(R.layout.fragment_single_post_image, container, false);
            case Constants.VIDEO:
                if (FoxToolkit.INSTANCE.getTypeOfVideo(singlePostData) == Constants.UNPLAYABLE_VIDEO)
                    return inflater.inflate(R.layout.fragment_single_post_image, container, false);
                return inflater.inflate(R.layout.fragment_single_post_video, container, false);
            case Constants.POLL:
                return inflater.inflate(R.layout.fragment_single_post_poll, container, false);
            default:
                return inflater.inflate(R.layout.fragment_single_post_self, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        markwon = Markwon.builder(requireContext())
                .usePlugin(TablePlugin.create(requireContext()))
                .usePlugin(LinkifyPlugin.create())
                .build();

        Data singlePostData = getArguments().getParcelable("data");
        int postType = getArguments().getInt("postType");

        if (singlePostData == null) {
            postType = SinglePostFragmentArgs.fromBundle(requireArguments()).getPostType();
            singlePostData = SinglePostFragmentArgs.fromBundle(requireArguments()).getPost();
        }

        mCommentsRecyclerView = view.findViewById(R.id.recyclerview_single_post);

        if (savedInstanceState != null)
            isFullscreen = savedInstanceState.getBoolean("isFullscreen");

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        orientation = getResources().getConfiguration().orientation;

        setUpNavigation(view, singlePostData, postType);
        initializeUI(singlePostData, view, postType);

        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        String permalink = singlePostData.getPermalink().substring(1, singlePostData.getPermalink().length() - 1);

        Data finalSinglePostData = singlePostData;
        viewModel.getSinglePostComments(permalink, requireActivity().getApplication())
                .observe(getViewLifecycleOwner(), commentListing -> {
                    groupAdapter = new GroupAdapter<>();
                    initRecyclerView(groupAdapter);
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
                        groupAdapter.add(new ExpandableCommentGroup(item,
                                Objects.requireNonNull(item.getData()).getDepth(),
                                finalSinglePostData.getName(),
                                SinglePostFragment.this, (MainActivity) requireActivity()));
                    }
                });
    }

    private void initializeUI(Data singlePostData, View view, int postType) {
        bindHeaderAndFooter(singlePostData, view);

        switch (postType) {
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
            default: //Self by default  //ToDo Comment
                bindAsSelf(singlePostData, view);
                break;
        }
    }

    private void bindHeaderAndFooter(Data singlePostData, View view) {
        TextView mTxtPostSubreddit = view.findViewById(R.id.txt_post_subreddit);
        TextView mTxtPostUser = view.findViewById(R.id.txt_post_user);
        TextView mTxtTimePosted = view.findViewById(R.id.txt_time_posted);
        TextView txtIsSpoiler = view.findViewById(R.id.txt_post_is_spoiler);
        TextView txtIsOver18 = view.findViewById(R.id.txt_post_is_over_18);
        TextView txtPostTitle = view.findViewById(R.id.txt_single_post_title);
        CustomTextView customTxtPostFlair = view.findViewById(R.id.single_post_link_flair);
        TextView mTxtPostScore = view.findViewById(R.id.txt_post_score);
        ImageButton mImgBtnPostVoteUp = view.findViewById(R.id.imgbtn_post_vote_up);
        ImageButton mImgBtnPostVoteDown = view.findViewById(R.id.imgbtn_post_vote_down);
        TextView mBtnPostNumComments = view.findViewById(R.id.btn_post_num_comments);
        TextView mBtnPostShare = view.findViewById(R.id.btn_post_share);

        mTxtPostSubreddit.setText(singlePostData.getSubredditNamePrefixed());
        String user = "u/" + singlePostData.getAuthor();
        mTxtPostUser.setText(user);
        mTxtTimePosted.setText(DateUtils.getRelativeTimeSpanString((long) singlePostData.getCreatedUtc() * 1000).toString());
        txtPostTitle.setText(singlePostData.getTitle());

        if (singlePostData.getLinkFlairText() != null && !singlePostData.getLinkFlairText().isEmpty())
            FoxToolkit.INSTANCE.makeFlair(singlePostData.getLinkFlairType(),
                    singlePostData.getLinkFlairRichtext(), singlePostData.getLinkFlairText(),
                    singlePostData.getLinkFlairTextColor(), singlePostData.getLinkFlairBackgroundColor(),
                    customTxtPostFlair);

        mTxtPostScore.setText(FoxToolkit.INSTANCE.formatValue(singlePostData.getScore()));

        if (singlePostData.getLikes() != null) {
            if (singlePostData.getLikes()) {
                mImgBtnPostVoteUp.setImageResource(R.drawable.ic_round_arrow_upward_24_orange);
                mTxtPostScore.setTextColor(Color.parseColor("#FFE07812"));
            } else {
                mImgBtnPostVoteDown.setImageResource(R.drawable.ic_round_arrow_downward_24_blue);
                mTxtPostScore.setTextColor(Color.parseColor("#FF5AA4FF"));
            }
        }

        if (singlePostData.isOver18() != null && singlePostData.isOver18())
            txtIsOver18.setVisibility(View.VISIBLE);
        if (singlePostData.getSpoiler() != null && singlePostData.getSpoiler())
            txtIsSpoiler.setVisibility(View.VISIBLE);

        mBtnPostNumComments.setText(FoxToolkit.INSTANCE.formatValue(singlePostData.getNumComments()));

        int currentDestinationID = Objects.requireNonNull(navController.getCurrentDestination()).getId();
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);

        mTxtPostSubreddit.setOnClickListener(view1 -> {
            if (currentDestinationID != R.id.subredditFragment) {
                String subredditNamePrefixed = singlePostData.getSubredditNamePrefixed();
                NavGraphDirections.ActionGlobalSubredditFragment action = NavGraphDirections.actionGlobalSubredditFragment(subredditNamePrefixed);
                navController.navigate(action);
            }
        });

        mTxtPostUser.setOnClickListener(view1 -> {
            String authorUsername = singlePostData.getAuthor();
            NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(authorUsername);
            navController.navigate(action);
        });

        mImgBtnPostVoteUp.setOnClickListener(view1 -> {
            if (!FoxToolkit.INSTANCE.isAuthorized(requireActivity().getApplication()))
                FoxToolkit.INSTANCE.promptLogIn((MainActivity) requireActivity());
            else {
                FoxToolkit.INSTANCE.upVoteColor(singlePostData.getLikes(), mImgBtnPostVoteUp,
                        mImgBtnPostVoteDown, mTxtPostScore, (MainActivity) requireActivity(), txtPostTitle.getCurrentTextColor());
                FoxToolkit.INSTANCE.upVoteModel(viewModel, requireActivity().getApplication(), singlePostData);
            }
        });

        mImgBtnPostVoteDown.setOnClickListener(view1 -> {
            if (!FoxToolkit.INSTANCE.isAuthorized(requireActivity().getApplication()))
                FoxToolkit.INSTANCE.promptLogIn((MainActivity) requireActivity());
            else {
                FoxToolkit.INSTANCE.downVoteColor(singlePostData.getLikes(), mImgBtnPostVoteUp,
                        mImgBtnPostVoteDown, mTxtPostScore, (MainActivity) requireActivity(), txtPostTitle.getCurrentTextColor());
                FoxToolkit.INSTANCE.downVoteModel(viewModel, requireActivity().getApplication(), singlePostData);
            }
        });

        mBtnPostShare.setOnClickListener(view1 -> startActivity(Intent.createChooser(FoxToolkit.INSTANCE.shareLink(singlePostData), "Share via")));
    }

    private void bindAsSelf(Data singlePostData, View view) {
        if (singlePostData.getSelftext() != null) {
            TextView txtPostBody = view.findViewById(R.id.stub_txt_post_body);
            markwon.setMarkdown(txtPostBody, singlePostData.getSelftext());
            txtPostBody.setVisibility(View.VISIBLE);
        }
    }

    private void bindAsLink(Data singlePostData, View view) {
        ImageView imgPostThumbnail = view.findViewById(R.id.stub_img_post_thumbnail);
        TextView txtPostDomain = view.findViewById(R.id.stub_txt_post_domain);
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
            Glide.with(view).load(url).into(imgPostThumbnail);
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
            ImageGalleryAdapter adapter = new ImageGalleryAdapter(imagesUrl, requireContext());
            ViewPager2 viewPager = view.findViewById(R.id.viewpager_image_gallery);
            viewPager.setAdapter(adapter);
            TabLayout tabLayout = view.findViewById(R.id.tab_dots);
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> {
                    }
            ).attach();
        }
    }

    private void bindAsGif(Data singlePostData, View view) {
        ImageView imgPostImage = view.findViewById(R.id.stub_img_post_image);
        Glide.with(view).load(singlePostData.getUrlOverriddenByDest()).into(imgPostImage);

        imgPostImage.setOnClickListener(view1 -> FoxToolkit.INSTANCE.fullscreenImage(singlePostData, requireContext()));
    }

    private void bindAsImage(Data singlePostData, View view) {
        ImageView imgPostImage = view.findViewById(R.id.stub_img_post_image);
        Glide.with(view).load(singlePostData.getUrlOverriddenByDest()).into(imgPostImage);

        imgPostImage.setOnClickListener(view1 -> FoxToolkit.INSTANCE.fullscreenImage(singlePostData, requireContext()));
    }

    private void bindAsPoll(Data singlePostData, View view) {
        if (singlePostData.getSelftext() != null) {
            TextView txtPostBody = view.findViewById(R.id.stub_txt_post_body);
            markwon.setMarkdown(txtPostBody, singlePostData.getSelftext());
            txtPostBody.setVisibility(View.VISIBLE);
        }
    }

    private void bindAsUnplayableVideo(Data singlePostData, View view) {
        ImageView imgPostImage = view.findViewById(R.id.stub_img_post_image);
        ImageView imgPostPlayButton = view.findViewById(R.id.stub_img_post_play_button);
        imgPostPlayButton.setVisibility(View.VISIBLE);
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        imgPostImage.getLayoutParams().height = Math.round(displayMetrics.widthPixels * .5625f);

        Glide.with(view).load(singlePostData.getMedia().getOembed().getThumbnailUrl()).placeholder(R.drawable.ic_launcher_background).into(imgPostImage);

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

        PlayerView playerView = view.findViewById(R.id.video_player);
        TextView txtVideoOpenInNew = playerView.findViewById(R.id.txt_video_open_in_new);
        TextView txtVideoCurrentTime = view.findViewById(R.id.txt_video_current_time);
        TextView txtVideoDuration = view.findViewById(R.id.txt_video_duration);
        ImageView imgFullscreen = view.findViewById(R.id.img_fullscreen);

        imgPlay = playerView.findViewById(R.id.exo_img_play);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        videoSeekBar = view.findViewById(R.id.video_seek_bar);

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
        AppBarLayout appBarLayout = view.findViewById(R.id.appBarLayout_fragment_single_post);
        PlayerView playerView = view.findViewById(R.id.video_player);
        TextView singlePostTitle = view.findViewById(R.id.txt_single_post_title);
        ConstraintLayout singlePostHeader = view.findViewById(R.id.include_header_single_post);
        ConstraintLayout singlePostFooter = view.findViewById(R.id.include_single_post_footer);
        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.single_post_collapsing_toolbar);
        Toolbar toolbar = view.findViewById(R.id.single_post_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        ImageView imgFullscreen = view.findViewById(R.id.img_fullscreen);

        imgFullscreen.setImageResource(R.drawable.exo_ic_fullscreen_enter);

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
        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.single_post_collapsing_toolbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL);
        collapsingToolbar.setLayoutParams(params);
        AppBarLayout appBarLayout = view.findViewById(R.id.appBarLayout_fragment_single_post);
        PlayerView playerView = view.findViewById(R.id.video_player);
        ImageView imgFullscreen = view.findViewById(R.id.img_fullscreen);

        imgFullscreen.setImageResource(R.drawable.exo_ic_fullscreen_exit);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ConstraintLayout singlePostFooter = view.findViewById(R.id.include_single_post_footer);

            appBarLayout.setLayoutParams((new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)));
            playerView.setLayoutParams((new FrameLayout.LayoutParams(PlayerView.LayoutParams.MATCH_PARENT, PlayerView.LayoutParams.MATCH_PARENT)));

            collapsingToolbar.setVisibility(View.GONE);
            singlePostFooter.setVisibility(View.GONE);
        } else {
            TextView singlePostTitle = view.findViewById(R.id.txt_single_post_title);
            ConstraintLayout singlePostHeader = view.findViewById(R.id.include_header_single_post);
            ConstraintLayout singlePostFooter = view.findViewById(R.id.include_single_post_footer);
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

    private void initRecyclerView(GroupAdapter<GroupieViewHolder> groupAdapter) {
        GridLayoutManager groupLayoutManager = new GridLayoutManager(getActivity(), groupAdapter.getSpanCount());
        groupLayoutManager.setSpanSizeLookup(groupAdapter.getSpanSizeLookup());
        mCommentsRecyclerView.setLayoutManager(groupLayoutManager);
        mCommentsRecyclerView.setAdapter(groupAdapter);
    }

    @Override
    public void onClick(@NotNull String linkId, ArrayList<String> moreChildren,
                        ChildrenItem comment, String actionType, View view) {
        if (moreChildren != null) {
            StringBuilder loadChildren = new StringBuilder(moreChildren.get(0));
            List<String> moreChildrenArray = new ArrayList<>();

            for (int i = 1; i < moreChildren.size(); i++)
                if (i < 100)
                    loadChildren.append(",").append(moreChildren.get(i));
                else
                    moreChildrenArray.add(moreChildren.get(i));

            MoreChildrenList moreChildrenList = new MoreChildrenList();
            moreChildrenList.setMoreChildrenList(moreChildrenArray);

            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
            navController.navigate(SinglePostFragmentDirections.actionSinglePostFragmentToCommentsFragment(linkId, loadChildren.toString(), moreChildrenList));
        } else {
            PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
            PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);

            switch (actionType) {
                case Constants.THING_VOTE_UP:
                    if (!FoxToolkit.INSTANCE.isAuthorized(requireActivity().getApplication()))
                        FoxToolkit.INSTANCE.promptLogIn((MainActivity) requireActivity());
                    else
                        FoxToolkit.INSTANCE.upVoteCommentModel(viewModel,
                                requireActivity().getApplication(), comment.getData());
                    break;
                case Constants.THING_VOTE_DOWN:
                    if (!FoxToolkit.INSTANCE.isAuthorized(requireActivity().getApplication()))
                        FoxToolkit.INSTANCE.promptLogIn((MainActivity) requireActivity());
                    else
                        FoxToolkit.INSTANCE.downVoteCommentModel(viewModel,
                                requireActivity().getApplication(), comment.getData());
                    break;
                case Constants.THING_REPLY:
                    navController.navigate(
                            SinglePostFragmentDirections
                                    .actionSinglePostFragmentToComposeReplyToUserMessageFragment(
                                            comment.getData().getName(), "New comment"));
                    break;
                case Constants.THING_AUTHOR:
                    String authorUsername = comment.getData().getAuthor();
                    NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(authorUsername);
                    navController.navigate(action);
                    break;
                case Constants.THING_MORE_ACTIONS:
                    PopupMenu menu = new PopupMenu(requireContext(), view);
                    menu.inflate(R.menu.comment_popup);

                    if (comment.getData().isSaved())
                        menu.getMenu().findItem(R.id.comment_save).setTitle("Unsave");
                    else
                        menu.getMenu().findItem(R.id.comment_save).setTitle("Save");

                    if (comment.getData().getAuthor()
                            .equals(((MainActivity) requireActivity()).getFoxSharedViewModel()
                                    .getCurrentUserUsername())) {
                        menu.getMenu().findItem(R.id.comment_edit).setVisible(true);
                        menu.getMenu().findItem(R.id.comment_delete).setVisible(true);
                    } else {
                        menu.getMenu().findItem(R.id.comment_edit).setVisible(false);
                        menu.getMenu().findItem(R.id.comment_delete).setVisible(false);
                    }

                    if (menu.getMenu().findItem(R.id.comment_edit).isVisible()) {
                        menu.getMenu().findItem(R.id.comment_edit).setOnMenuItemClickListener(edit -> {
                            goToEditThing(comment.getData().getName(), comment.getData().getBody());
                            return true;
                        });

                        menu.getMenu().findItem(R.id.comment_delete).setOnMenuItemClickListener(edit -> {
                            deleteThingAction(viewModel, comment.getData().getName());
                            return true;
                        });
                    }

                    menu.getMenu().findItem(R.id.comment_save).setOnMenuItemClickListener(save -> {
                        popUpMenuSave(comment, viewModel);
                        return true;
                    });

                    menu.getMenu().findItem(R.id.comment_report).setOnMenuItemClickListener(commentReport -> {
                        popUpMenuReport(comment);
                        return true;
                    });

                    menu.show();
                    break;
                default:
            }
        }
    }

    private void popUpMenuSave(ChildrenItem comment, PostViewModel viewModel) {
        if (comment.getData().isSaved())
            viewModel.unSavePost(comment.getData().getName(), requireActivity().getApplication()).observe(getViewLifecycleOwner(), succeed -> {
                if (succeed)
                    comment.getData().setSaved(false);
            });
        else
            viewModel.savePost(comment.getData().getName(), requireActivity().getApplication()).observe(getViewLifecycleOwner(), succeed -> {
                if (succeed)
                    comment.getData().setSaved(true);
            });
    }

    private void popUpMenuReport(ChildrenItem comment) {
        SubredditViewModelFactory subredditFactory = InjectorUtils.getInstance().provideSubredditViewModelFactory();
        SubredditViewModel subredditViewModel = new ViewModelProvider(this, subredditFactory).get(SubredditViewModel.class);
        subredditViewModel.getSubredditRules(comment.getData().getSubredditNamePrefixed(),
                requireActivity().getApplication()).observe(getViewLifecycleOwner(),
                rulesBundle -> {
                    if (rulesBundle.getSiteRulesFlow() != null && rulesBundle.getRules() != null)
                        navController.navigate(NavGraphDirections.actionGlobalReportDialogFragment(
                                rulesBundle,
                                null, Constants.ALL_RULES,
                                comment.getData().getSubredditNamePrefixed(),
                                comment.getData().getName()));
                    else {
                        Toast.makeText(getContext(), "Failed to report", Toast.LENGTH_SHORT).show();
                    }
                });
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
    public void onDetach() {
        if (player != null)
            player.release();
        super.onDetach();
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

    private void setUpMenu(Toolbar toolbar, Data postData, int postType, View view, MainActivity mainActivity) {
        toolbar.inflateMenu(R.menu.self_single_post_menu);
        Menu menu = toolbar.getMenu();

        if (mainActivity.getFoxSharedViewModel().getCurrentUserUsername().equals(postData.getAuthor())) {

            menu.findItem(R.id.self_single_post_edit)
                    .setVisible(postType != Constants.IMAGE
                            && postType != Constants.VIDEO
                            && postType != Constants.LINK
                            && postType != Constants.POLL);

            PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
            PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);

            setUpEditFlairMenu(view, postData, toolbar);

            if (menu.findItem(R.id.self_single_post_edit).isVisible()) {
                menu.findItem(R.id.self_single_post_edit).setOnMenuItemClickListener(edit -> {
                    goToEditThing(postData.getName(), postData.getSelftext());
                    return true;
                });

                getParentFragmentManager().setFragmentResultListener("editThing", getViewLifecycleOwner(), (key, bundle) -> {
                    String result = bundle.getString("updatedText");
                    if (result != null) {
                        postData.setSelftext(result);
                        bindAsSelf(postData, view);
                    }
                });
            }

            menu.findItem(R.id.self_single_post_mark_nsfw).setOnMenuItemClickListener(markNSFW -> {
                Boolean isNSFW = postData.isOver18();

                viewModel.markNSFW(postData.getName(), isNSFW, requireActivity().getApplication())
                        .observe(getViewLifecycleOwner(), success -> {
                            if (success) {
                                TextView txtIsOver18 = view.findViewById(R.id.txt_post_is_over_18);
                                postData.setOver18(!postData.isOver18());
                                if (postData.isOver18()) {
                                    markNSFW.setTitle("Unmark NSFW");
                                    txtIsOver18.setVisibility(View.VISIBLE);
                                } else {
                                    markNSFW.setTitle("Mark NSFW");
                                    txtIsOver18.setVisibility(View.GONE);
                                }
                            }
                        });
                return true;
            });

            menu.findItem(R.id.self_single_post_mark_spoiler).setOnMenuItemClickListener(markSpoiler -> {
                Boolean isSpoiler = postData.getSpoiler();

                viewModel.markSpoiler(postData.getName(), isSpoiler, requireActivity().getApplication())
                        .observe(getViewLifecycleOwner(), success -> {
                            if (success) {
                                TextView txtIsSpoiler = view.findViewById(R.id.txt_post_is_spoiler);
                                postData.setSpoiler(!postData.getSpoiler());
                                if (postData.getSpoiler()) {
                                    markSpoiler.setTitle("Unmark Spoiler");
                                    txtIsSpoiler.setVisibility(View.VISIBLE);
                                } else {
                                    markSpoiler.setTitle("Unmark Spoiler");
                                    txtIsSpoiler.setVisibility(View.GONE);
                                }
                            }
                        });
                return true;
            });

            menu.findItem(R.id.self_single_post_delete).setOnMenuItemClickListener(delete -> {
                deleteThingAction(viewModel, postData.getName());
                return true;
            });
        } else {
            menu.findItem(R.id.self_single_post_edit).setVisible(false);
            menu.findItem(R.id.self_single_post_mark_nsfw).setVisible(false);
            menu.findItem(R.id.self_single_post_mark_spoiler).setVisible(false);
            menu.findItem(R.id.self_single_post_delete).setVisible(false);
            menu.findItem(R.id.self_single_post_edit_flair).setVisible(false);
        }

        menu.findItem(R.id.single_post_reply).setVisible(true).setOnMenuItemClickListener(reply -> {
            navController.navigate(
                    SinglePostFragmentDirections
                            .actionSinglePostFragmentToComposeReplyToUserMessageFragment(
                                    postData.getName(), "New comment"));
            return true;
        });
    }

    private void goToEditThing(String fullname, String selftext) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        navController.navigate(SinglePostFragmentDirections
                .actionSinglePostFragmentToEditThingFragment(Constants.EDIT_POST_TEXT,
                        fullname, selftext));
    }

    private void deleteThingAction(PostViewModel viewModel, String fullname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Are you sure?")
                .setCancelable(false)
                .setNegativeButton("Nope", (dialog, id) -> dialog.cancel())
                .setPositiveButton("Do it!", (dialog, id) ->
                        viewModel.deleteSubmission(fullname, requireActivity().getApplication())
                                .observe(getViewLifecycleOwner(), success -> {
                                    if (success) {
                                        requireActivity().onBackPressed();
                                    }
                                }));

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setUpEditFlairMenu(View view, Data postData, Toolbar toolbar) {
        toolbar.getMenu().findItem(R.id.self_single_post_edit_flair).setOnMenuItemClickListener(editFlair -> {
            NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
            if (postData.getSubredditNamePrefixed() != null)
                navController.navigate(
                        SinglePostFragmentDirections
                                .actionSinglePostFragmentToLinkFlairListFragment(
                                        postData.getSubredditNamePrefixed()
                                ));
            return true;
        });

        getParentFragmentManager().setFragmentResultListener("flairChoice", getViewLifecycleOwner(), (key, bundle) -> {
            Flair result = bundle.getParcelable("flair");
            if (result != null) {
                PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
                PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);

                viewModel.selectFlair(postData.getSubredditNamePrefixed(), postData.getName(),
                        result.getId(), requireActivity().getApplication())
                        .observe(getViewLifecycleOwner(), success -> {

                            CustomTextView customTxtPostFlair = view.findViewById(R.id.single_post_link_flair);

                            if (success && !result.getId().isEmpty()) {
                                setFlairData(result, postData);

                                if (postData.getLinkFlairText() != null && !postData.getLinkFlairText().isEmpty())
                                    FoxToolkit.INSTANCE.makeFlair(postData.getLinkFlairType(),
                                            postData.getLinkFlairRichtext(), postData.getLinkFlairText(),
                                            postData.getLinkFlairTextColor(), postData.getLinkFlairBackgroundColor(),
                                            customTxtPostFlair);
                            } else if (result.getId().isEmpty()) {
                                setFlairData(result, postData);
                                customTxtPostFlair.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    private void setFlairData(Flair flair, Data postData) {
        postData.setLinkFlairBackgroundColor(flair.getBackgroundColor());
        postData.setLinkFlairRichtext(flair.getRichtext());
        postData.setLinkFlairText(flair.getText());
        postData.setLinkFlairTextColor(flair.getTextColor());
        postData.setLinkFlairType(flair.getType());
    }

    private void setUpNavigation(View view, Data postData, int postType) {
        CollapsingToolbarLayout collapsingToolbar = requireActivity().findViewById(R.id.single_post_collapsing_toolbar);
        AppBarLayout appBarLayout = view.findViewById(R.id.appBarLayout_fragment_single_post);
        Toolbar toolbar = view.findViewById(R.id.single_post_toolbar);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());
        collapsingToolbar.setBackgroundColor(Cyanea.getInstance().getBackgroundColor());

        ConstraintLayout includeHeader = view.findViewById(R.id.include_header_single_post);

        int color = Cyanea.getInstance().getMenuIconColor();
        int colorRed = Color.red(color);
        int colorGreen = Color.green(color);
        int colorBlue = Color.blue(color);

        appBarLayout.addOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener) (appBarLayout1, verticalOffset) -> {
            float normalize = (float) (1 - ((float) -verticalOffset / includeHeader.getMeasuredHeight())) * 255;
            if (Math.abs(verticalOffset) >= includeHeader.getMeasuredHeight()) {
                toolbar.setTitleTextColor(Color.argb(255, colorRed, colorGreen, colorBlue));
            } else {
                toolbar.setTitleTextColor(Color.argb((int) -normalize, colorRed, colorGreen, colorBlue));
            }
        });

        toolbar.setTitle(postData.getSubredditNamePrefixed());
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        setUpMenu(toolbar, postData, postType, view, mainActivity);

        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);
        NavigationUI.setupWithNavController(collapsingToolbar, toolbar, navController);
    }

    public static SinglePostFragment newInstance(Data data, int postType) {
        SinglePostFragment fragment = new SinglePostFragment();

        Bundle args = new Bundle();
        args.putParcelable("data", data);
        args.putInt("postType", postType);
        fragment.setArguments(args);

        return fragment;
    }
}
