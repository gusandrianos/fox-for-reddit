package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.app.Dialog;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;

/*
    Fullscreen Video Player
 */
public class VideoDialogFragment extends BottomSheetDialogFragment {

    NavHostFragment navHostFragment;
    NavController navController;

    private boolean wasPlaying;

    SimpleExoPlayer player = null;
    ImageView imgPlay;
    SeekBar videoSeekBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dialog_video, container, false);
    }

    /*
        Set dialog to be fullscreen
     */
    @Nullable
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog dialogc = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = dialogc.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottomSheetBehavior.setFitToContents(true);

        });
        return bottomSheetDialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        VideoDialogFragmentArgs videoDialogFragmentArgs = VideoDialogFragmentArgs.fromBundle(requireArguments());
        Data post = videoDialogFragmentArgs.getPost();

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

        PlayerView playerView = view.findViewById(R.id.fullscreen_video_player);
        View viewPlayerHolder = view.findViewById(R.id.view_player_holder);
        imgPlay = playerView.findViewById(R.id.exo_img_play);
        TextView txtVideoOpenInNew = playerView.findViewById(R.id.txt_video_open_in_new);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        videoSeekBar = view.findViewById(R.id.video_seek_bar);
        TextView txtVideoCurrentTime = view.findViewById(R.id.txt_video_current_time);
        TextView txtVideoDuration = view.findViewById(R.id.txt_video_duration);

        viewPlayerHolder.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        viewPlayerHolder.getLayoutParams().height = Resources.getSystem().getDisplayMetrics().heightPixels;

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
                        progressBar.setVisibility(View.INVISIBLE);
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

    /*
        Keep progress of video
     */
    private void changeSeekBar(SimpleExoPlayer player, SeekBar videoSeekBar, TextView videoCurrentTime, Handler handler) {
        videoSeekBar.setProgress((int) player.getCurrentPosition() / 1000);
        videoCurrentTime.setText(FoxToolkit.INSTANCE.getTimeOfVideo((int) player.getCurrentPosition() / 1000));
        Runnable runnable = () -> changeSeekBar(player, videoSeekBar, videoCurrentTime, handler);
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            wasPlaying = player.isPlaying();
            player.setPlayWhenReady(false);
            imgPlay.setImageResource(R.drawable.exo_icon_play);
        }
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
