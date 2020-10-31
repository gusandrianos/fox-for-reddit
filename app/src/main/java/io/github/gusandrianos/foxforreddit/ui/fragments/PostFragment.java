package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.PostAdapter;
import io.github.gusandrianos.foxforreddit.utilities.PostLoadStateAdapter;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;
import kotlin.Unit;


public class PostFragment extends Fragment implements PostAdapter.OnItemClickListener {
    private View mView;
    private Token mToken;
    String subreddit;
    String filter;
    int page;
    PostAdapter mPostRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = getView();
        mToken = InjectorUtils.getInstance().provideTokenRepository().getToken(requireActivity().getApplication());
        page = getArguments().getInt("page", 0);
        subreddit = getArguments().getString("subreddit", "");
        filter = getArguments().getString("filter", "");
        initRecycleView();
        initializeUI();
    }

    private void initRecycleView() {
        RecyclerView mPostRecyclerView = mView.findViewById(R.id.recyclerview);
        mPostRecyclerViewAdapter = new PostAdapter(this);
        mPostRecyclerViewAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY); //keep recyclerview on position
        mPostRecyclerView.setHasFixedSize(true);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPostRecyclerView.addItemDecoration(new DividerItemDecoration(mPostRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        PostLoadStateAdapter postLoadStateAdapter = new PostLoadStateAdapter(() -> {
            mPostRecyclerViewAdapter.retry();
            return Unit.INSTANCE;
        });
        mPostRecyclerView.setAdapter(mPostRecyclerViewAdapter.withLoadStateFooter(postLoadStateAdapter));
    }

    public void initializeUI() {
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        viewModel.getPosts(subreddit, filter, getActivity().getApplication()).observe(getViewLifecycleOwner(), postPagingData -> {
            mPostRecyclerViewAdapter.submitData(getViewLifecycleOwner().getLifecycle(), postPagingData);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Token token = InjectorUtils.getInstance().provideTokenRepository().getToken(requireActivity().getApplication());
        if (!mToken.getAccessToken().equals(token.getAccessToken())) {
            mToken = token;
            initRecycleView();
            initializeUI();
        }
    }

    @Override
    public void onItemClick(@NotNull Data post, @NotNull String clicked, int postType) {      //ToDo improve voting system (Binding Adapter and viewModel)
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        int currentDestinationID = Objects.requireNonNull(navController.getCurrentDestination()).getId();
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);

        switch (clicked) {
            case Constants.POST_SUBREDDIT:
                if (currentDestinationID != R.id.subredditFragment) {
                    String subredditNamePrefixed = post.getSubredditNamePrefixed();
                    NavGraphDirections.ActionGlobalSubredditFragment action = NavGraphDirections.actionGlobalSubredditFragment(subredditNamePrefixed);
                    navController.navigate(action);
                }
                break;
            case Constants.POST_USER:
                String authorUsername = post.getAuthor();
                if (currentDestinationID != R.id.userFragment) {
                    NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(null, authorUsername);
                    navController.navigate(action);
                } else if (!authorUsername.equals(subreddit.split("/")[1])) {
                    NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(null, authorUsername);
                    navController.navigate(action);
                }
                break;
            case Constants.POST_THUMBNAIL:
                NavGraphDirections.ActionGlobalFullscreenFragment fullscreenAction;

                switch (postType) {
                    case Constants.IMAGE:
                        fullscreenAction = NavGraphDirections.actionGlobalFullscreenFragment(post, postType);
                        navController.navigate(fullscreenAction);
                        break;
                    case Constants.VIDEO:
                        if (FoxToolkit.INSTANCE.getTypeOfVideo(post) == Constants.PLAYABLE_VIDEO) {
                            fullscreenAction = NavGraphDirections.actionGlobalFullscreenFragment(post, postType);
                            navController.navigate(fullscreenAction);
                            break;
                        } else {
                            startActivity(FoxToolkit.INSTANCE.visitLink(post));
                        }
                        break;
                    case Constants.LINK:
                        startActivity(FoxToolkit.INSTANCE.visitLink(post));
                        break;
                    default:
                }
                break;
            case Constants.POST_VOTE_UP:
                FoxToolkit.INSTANCE.upVote(viewModel, requireActivity().getApplication(), post);
                break;
            case Constants.POST_VOTE_DOWN:
                FoxToolkit.INSTANCE.downVote(viewModel, requireActivity().getApplication(), post);
                break;
            case Constants.POST_SHARE:
                startActivity(Intent.createChooser(FoxToolkit.INSTANCE.shareLink(post), "Share via"));
                break;
            case Constants.POST_VOTE_NOW:
                startActivity(FoxToolkit.INSTANCE.visitLink(post));
                break;
            default:
                NavGraphDirections.ActionGlobalSinglePostFragment action = NavGraphDirections.actionGlobalSinglePostFragment(post, postType);
                navController.navigate(action);
        }
    }

    public static PostFragment newInstance(String subreddit, String filter) {
        PostFragment fragment = new PostFragment();

        Bundle args = new Bundle();
        args.putString("subreddit", subreddit);
        args.putString("filter", filter);
        fragment.setArguments(args);

        return fragment;
    }
}