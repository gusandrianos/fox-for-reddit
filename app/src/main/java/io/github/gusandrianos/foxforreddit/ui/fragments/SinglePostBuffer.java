package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;

public class SinglePostBuffer extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_post_buffer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SinglePostBufferArgs args = SinglePostBufferArgs.fromBundle(requireArguments());
        String subreddit = args.getSubreddit();
        String subdir = args.getSubdir();

        String permalink = "r/" + subreddit + "/comments/" + subdir;
        ProgressBar progressBar = view.findViewById(R.id.post_buffer_progress);

        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);

        viewModel.getSinglePost(permalink, requireActivity().getApplication())
                .observe(getViewLifecycleOwner(), post -> {
                    Data postData = post.getData().getData().getChildren().get(0).getData();
                    SinglePostFragment singlePostFragment = SinglePostFragment.newInstance(postData, FoxToolkit.INSTANCE.getPostType(postData));
                    progressBar.setVisibility(View.GONE);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.single_post_buffer,
                                    singlePostFragment,
                                    "singlePostFragment")
                            .commitNow();
                });
    }
}