package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.media.Image;
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
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;
import com.stfalcon.imageviewer.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.GalleryItem;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.ImageGalleryAdapter;

public class FullscreenFragment extends Fragment {
    private boolean wasPlaying;

    SimpleExoPlayer player = null;
    ImageView imgPlay;
    SeekBar videoSeekBar;

    ViewStub stub;
    View inflated;

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
        lockDrawer();

        FullscreenFragmentArgs fullscreenFragmentArgs = FullscreenFragmentArgs.fromBundle(requireArguments());
        Data post = fullscreenFragmentArgs.getPost();
        int postType = fullscreenFragmentArgs.getPostType();

        initializeUI(post, view, postType);

    }

    private void bindAsFullScreenImage(Data post, View view) {

        List<String> images = new ArrayList<>();
        images.add(post.getUrlOverriddenByDest());

        new StfalconImageViewer.Builder<>(requireContext(), images, new ImageLoader<String>() {
            @Override
            public void loadImage(ImageView imageView, String image) {
                Glide.with(requireContext()).load(image).into(imageView);
            }
        }).show();
    }

    private void bindAsFullScreenGallery(Data post, View view) {
        List<String> imagesId = new ArrayList<>();

//        if (post.getGalleryData() != null) {
//            for (GalleryItem galleryItem : post.getGalleryData().getItems()) {
//                imagesId.add(galleryItem.getMediaId());
//            }
//            ViewStub stub = view.findViewById(R.id.stub_fullsceen);
//            stub.setLayoutResource(R.layout.stub_view_pager_image_gallery);
//            View inflated = stub.inflate();
//            ImageGalleryAdapter adapter = new ImageGalleryAdapter(imagesId, Constants.FULLSCREEN);
//            ViewPager2 viewPager = inflated.findViewById(R.id.viewpager_image_gallery);
//            viewPager.setAdapter(adapter);
//            TabLayout tabLayout = inflated.findViewById(R.id.tab_dots);
//            new TabLayoutMediator(tabLayout, viewPager,
//                    (tab, position) -> {
//                    }
//            ).attach();


        List<String> images = new ArrayList<>();
        if (post.getGalleryData() != null) {
            for (GalleryItem galleryItem : post.getGalleryData().getItems()) {
                String url = "https://i.redd.it/"+galleryItem.getMediaId()+".jpg";
                images.add(url);
            }

            FoxToolkit.INSTANCE.fullscreenImage(post,requireContext());
        }
    }


    private void lockDrawer() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        DrawerLayout drawer = mainActivity.drawer;
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private void initializeUI(Data post, View view, int postType) {

        if (postType == Constants.IMAGE) {
            FoxToolkit.INSTANCE.fullscreenImage(post,requireContext());
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

        imgPlay = playerView.findViewById(R.id.exo_img_play);
        TextView txtVideoOpenInNew = playerView.findViewById(R.id.txt_video_open_in_new);
        ProgressBar progressBar = inflated.findViewById(R.id.progress_bar);
        videoSeekBar = inflated.findViewById(R.id.video_seek_bar);
        TextView txtVideoCurrentTime = inflated.findViewById(R.id.txt_video_current_time);
        TextView txtVideoDuration = inflated.findViewById(R.id.txt_video_duration);

        Uri videoUri;

        if (videoType == Constants.REDDIT_VIDEO) {
            if (post.getSecureMedia().getRedditVideo().getHlsUrl() != null) {
                videoUri = Uri.parse(post.getSecureMedia().getRedditVideo().getHlsUrl());
            } else {
                videoUri = Uri.parse(post.getSecureMedia().getRedditVideo().getFallbackUrl());
            }
        } else {
            if (post.getPreview().getRedditVideoPreview().getHlsUrl() != null) {
                videoUri = Uri.parse(post.getPreview().getRedditVideoPreview().getHlsUrl());
            } else {
                videoUri = Uri.parse(post.getPreview().getRedditVideoPreview().getFallbackUrl());
            }
            String domain = post.getDomain();
            txtVideoOpenInNew.setVisibility(View.VISIBLE);
            txtVideoOpenInNew.setText(domain);
            txtVideoOpenInNew.setOnClickListener(view12 -> {
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                customTabsIntent.launchUrl(requireContext(), Uri.parse(post.getUrl()));
            });
        }

        player = new SimpleExoPlayer.Builder(requireActivity()).build();
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
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
    }

    private void changeSeekBar(SimpleExoPlayer player, SeekBar videoSeekBar, TextView videoCurrentTime, Handler handler) {
        videoSeekBar.setProgress((int) player.getCurrentPosition() / 1000);
        videoCurrentTime.setText(FoxToolkit.INSTANCE.getTimeOfVideo((int) player.getCurrentPosition() / 1000));
        Runnable runnable = () -> changeSeekBar(player, videoSeekBar, videoCurrentTime, handler);
        handler.postDelayed(runnable, 1000);
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
        delayedHide(500);
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

    }

    private void toggle() {

    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    private void show() {
        // Show the system bar

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