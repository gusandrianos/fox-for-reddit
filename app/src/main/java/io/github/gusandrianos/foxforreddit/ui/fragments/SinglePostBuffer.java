package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;

@AndroidEntryPoint
public class SinglePostBuffer extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_post_buffer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) requireActivity();
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);

        SinglePostBufferArgs args = SinglePostBufferArgs.fromBundle(requireArguments());
        String subreddit = args.getSubreddit();
        String subdir = args.getSubdir();

        String permalink = "r/" + subreddit + "/comments/" + subdir;

        PostViewModel viewModel = new ViewModelProvider(this).get(PostViewModel.class);

        viewModel.getSinglePost(permalink)
                .observe(getViewLifecycleOwner(), post -> {
                    Data postData = post.getData().getData().getChildren().get(0).getData();
                    NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
                    SinglePostBufferDirections.ActionSinglePostBufferToSinglePostFragment action =
                            SinglePostBufferDirections.
                                    actionSinglePostBufferToSinglePostFragment(
                                            postData, FoxToolkit.INSTANCE.getPostType(postData));
                    navController.navigate(action);
                });
    }
}