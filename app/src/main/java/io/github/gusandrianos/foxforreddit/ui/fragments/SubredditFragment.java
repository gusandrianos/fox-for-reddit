package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;


public class SubredditFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subreddit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SubredditFragmentArgs args = SubredditFragmentArgs.fromBundle(requireArguments());
        String subredditName = args.getSubredditName();

        CollapsingToolbarLayout collapsingToolbar = requireActivity().findViewById(R.id.subreddit_collapsing_toolbar);
        collapsingToolbar.setTitle(subredditName);

        SubredditViewModelFactory factory = InjectorUtils.getInstance().provideSubredditViewModelFactory();
        SubredditViewModel viewModel = new ViewModelProvider(this, factory).get(SubredditViewModel.class);

        viewModel.getSubreddit(subredditName, requireActivity().getApplication()).observe(getViewLifecycleOwner(), subredditInfo ->
        {
            setupHeader(subredditInfo, view);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.subreddit_posts_fragment,
                            PostFragment.newInstance(subredditName, ""),
                            "SubredditPostFragment")
                    .commitNow();
        });
    }

    void setupHeader(Data subredditInfo, View view) {
        setupImages(subredditInfo, view);
    }

    void setupImages(Data subredditInfo, View view) {
        ImageView pic = view.findViewById(R.id.subreddit_picture);
        ImageView cover = view.findViewById(R.id.subreddit_cover);
        ImageView gradient = view.findViewById(R.id.subreddit_cover_gradient);

        String iconImageURI = subredditInfo.getIconImg();
        String communityIconURI = subredditInfo.getCommunityIcon();
        String bannerImageURI = subredditInfo.getBannerImg();
        String bannerBackgroundImageURI = subredditInfo.getBannerBackgroundImage();

        if (iconImageURI == null)
            iconImageURI = "";

        if (communityIconURI == null)
            communityIconURI = "";

        if (bannerImageURI == null)
            bannerImageURI = "";

        if (bannerBackgroundImageURI == null)
            bannerBackgroundImageURI = "";

        if (!iconImageURI.isEmpty())
            Glide.with(view).load(iconImageURI.split("\\?")[0]).into(pic);
        else if (!communityIconURI.isEmpty())
            Glide.with(view).load(communityIconURI.split("\\?")[0]).into(pic);
        else
            pic.setImageResource(R.drawable.default_subreddit_image);

        if (!bannerImageURI.isEmpty())
            Glide.with(view).load(bannerImageURI.split("\\?")[0]).into(cover);
        else if (!bannerBackgroundImageURI.isEmpty())
            Glide.with(view).load(bannerBackgroundImageURI.split("\\?")[0]).into(cover);
        else
            cover.setImageResource(0);

        Glide.with(view).load(R.drawable.cover_gradient).into(gradient);
    }
}