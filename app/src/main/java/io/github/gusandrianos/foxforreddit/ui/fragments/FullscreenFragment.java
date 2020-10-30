package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.GalleryItem;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.ImageGalleryAdapter;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenFragment extends Fragment {
    private boolean wasPlaying;

    SimpleExoPlayer player = null;
    ImageView imgPlay;
    SeekBar videoSeekBar;

    ViewStub stub;
    View inflated;


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

            Activity activity = getActivity();
            if (activity != null
                    && activity.getWindow() != null) {
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }

        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private View mContentView;
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            if (mControlsView != null)
                mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fullscreen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FullscreenFragmentArgs fullscreenFragmentArgs = FullscreenFragmentArgs.fromBundle(requireArguments());
        Data post = fullscreenFragmentArgs.getPost();
        int postType = fullscreenFragmentArgs.getPostType();

        initializeUI(post, view, postType);

        mVisible = true;

        mControlsView = view.findViewById(R.id.fullscreen_content_controls);
        mContentView = view.findViewById(R.id.fl_contentView);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        view.findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }
    
    private void bindAsFullScreenImage(Data post, View view){
        ViewStub stub = view.findViewById(R.id.stub_fullsceen);
        stub.setLayoutResource(R.layout.stub_image);
        View inflated = stub.inflate();
        ImageView imgPostImage = inflated.findViewById(R.id.stub_img_post_image);
        imgPostImage.setAdjustViewBounds(true);
        imgPostImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgPostImage.setClickable(false);
        Glide.with(inflated).load(post.getUrlOverriddenByDest()).into(imgPostImage);
    }
    
    private void bindAsFullScreenGallery(Data post, View view){
        List<String> imagesId = new ArrayList<>();

        if (post.getGalleryData() != null) {
            for (GalleryItem galleryItem : post.getGalleryData().getItems()) {
                imagesId.add(galleryItem.getMediaId());
            }
            ViewStub stub = view.findViewById(R.id.stub_fullsceen);
            stub.setLayoutResource(R.layout.stub_view_pager_image_gallery);
            View inflated = stub.inflate();
            ImageGalleryAdapter adapter = new ImageGalleryAdapter(imagesId, Constants.FULLSCREEN);
            ViewPager2 viewPager = inflated.findViewById(R.id.viewpager_image_gallery);
            viewPager.setAdapter(adapter);
            TabLayout tabLayout = inflated.findViewById(R.id.tab_dots);
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> {
                    }
            ).attach();
        }
    }
    private void initializeUI(Data post, View view, int postType) {


        if (postType == Constants.IMAGE) {
            if (FoxToolkit.INSTANCE.getTypeOfImage(post) == Constants.IS_GALLERY) {     
                bindAsFullScreenGallery(post, view);    //Image Gallery
            } else {                                                                    
                bindAsFullScreenImage(post, view);  //Image or Gif                                      
            }
        } else {
            bindAsFullScreenVideo(post, view);
        }
    }

    private void bindAsFullScreenVideo(Data post, View view) {
        int videoType;
        if (post.isVideo()) {
            videoType = Constants.REDDIT_VIDEO;
        } else {
            videoType = Constants.PLAYABLE_VIDEO;
        }
        createVideoPlayer(post, view, videoType);
    }

    private void createVideoPlayer(Data post, View view, int videoType) {
        Handler handler = new Handler();

        stub = view.findViewById(R.id.stub_fullsceen);
        stub.setLayoutResource(R.layout.stub_video);
        inflated = stub.inflate();
        PlayerView playerView = inflated.findViewById(R.id.video_player);

        playerView.setLayoutParams(new PlayerView.LayoutParams(PlayerView.LayoutParams.MATCH_PARENT, PlayerView.LayoutParams.MATCH_PARENT));
        imgPlay = playerView.findViewById(R.id.exo_img_play);
        TextView txtVideoOpenInNew = playerView.findViewById(R.id.txt_video_open_in_new);
        ProgressBar progressBar = inflated.findViewById(R.id.progress_bar);
        videoSeekBar = inflated.findViewById(R.id.video_seek_bar);
        TextView txtVideoCurrentTime = inflated.findViewById(R.id.txt_video_current_time);
        TextView txtVideoDuration = inflated.findViewById(R.id.txt_video_duration);

        Uri videoUri;
        int videoDuration;

        if (videoType == Constants.REDDIT_VIDEO) {
            if (post.getSecureMedia().getRedditVideo().getHlsUrl() != null) {
                videoUri = Uri.parse(post.getSecureMedia().getRedditVideo().getHlsUrl());
            } else {
                videoUri = Uri.parse(post.getSecureMedia().getRedditVideo().getFallbackUrl());
            }
            videoDuration = (int) post.getSecureMedia().getRedditVideo().getDuration();
        } else {
            if (post.getPreview().getRedditVideoPreview().getHlsUrl() != null) {
                videoUri = Uri.parse(post.getPreview().getRedditVideoPreview().getHlsUrl());
            } else {
                videoUri = Uri.parse(post.getPreview().getRedditVideoPreview().getFallbackUrl());
            }
            videoDuration = (int) post.getPreview().getRedditVideoPreview().getDuration();
            String domain = post.getDomain();
            txtVideoOpenInNew.setVisibility(View.VISIBLE);
            txtVideoOpenInNew.setText(domain);
            txtVideoOpenInNew.setText(domain);
            txtVideoOpenInNew.setOnClickListener(view12 -> {
                if (videoType == Constants.REDDIT_VIDEO) {
                    Toast.makeText(requireActivity(), "Open new Fragment Fullscreen", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireActivity(), "Open Original", Toast.LENGTH_SHORT).show();
                }
            });
        }

        videoSeekBar.setMax(videoDuration);
        txtVideoDuration.setText(getTimeOfVideo(videoDuration));

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
                    txtVideoCurrentTime.setText(getTimeOfVideo(progress));
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
    }

    private void changeSeekBar(SimpleExoPlayer player, SeekBar videoSeekBar, TextView videoCurrentTime, Handler handler) {
        videoSeekBar.setProgress((int) player.getCurrentPosition() / 1000);
        videoCurrentTime.setText(getTimeOfVideo((int) player.getCurrentPosition() / 1000));
        Runnable runnable = () -> changeSeekBar(player, videoSeekBar, videoCurrentTime, handler);
        handler.postDelayed(runnable, 1000);
    }

    private String getTimeOfVideo(int time) {
        int min = time / 60;
        int sec = time - min * 60;
        String minStr = min < 10 ? "0" + min : String.valueOf(min);
        String secStr = sec < 10 ? "0" + sec : String.valueOf(sec);
        return minStr + ":" + secStr;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            // Clear the systemUiVisibility flag
            getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
        }

        if (player != null) {
            wasPlaying = player.isPlaying();
            player.setPlayWhenReady(false);
            imgPlay.setImageResource(R.drawable.exo_icon_play);
        }
        show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContentView = null;
        mControlsView = null;
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (mControlsView != null)
            mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Nullable
    private ActionBar getSupportActionBar() {
        ActionBar actionBar = null;
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            actionBar = activity.getSupportActionBar();
        }
        return actionBar;
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
}