package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.Token;
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
    String timedFilter;
    String time;
    int page;
    PostAdapter mPostRecyclerViewAdapter;
    RecyclerView mPostRecyclerView;
    SwipeRefreshLayout pullToRefresh;

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
        time = getArguments().getString("time", "");
        pullToRefresh = view.findViewById(R.id.swipe_refresh_layout_posts);
        initRecycleView();
        loadPosts(false);
        initSwipeToRefresh();
    }

    private void initRecycleView() {
        mPostRecyclerView = mView.findViewById(R.id.recyclerview);
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

    void loadPosts(boolean requestChanged) {
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        if (requestChanged)
            viewModel.deleteCached();

        viewModel.getPosts(subreddit, filter, time, getActivity().getApplication()).observe(getViewLifecycleOwner(), postPagingData -> {
            mPostRecyclerViewAdapter.submitData(getViewLifecycleOwner().getLifecycle(), postPagingData);
            mPostRecyclerViewAdapter.addLoadStateListener(loadStates -> {
                if (loadStates.getRefresh() instanceof LoadState.Loading)
                    pullToRefresh.setRefreshing(true);
                else if (loadStates.getRefresh() instanceof LoadState.NotLoading)
                    pullToRefresh.setRefreshing(false);
                return Unit.INSTANCE;
            });
        });
    }

    private void initSwipeToRefresh() {
        pullToRefresh.setOnRefreshListener(() -> {
            mPostRecyclerViewAdapter.refresh();
            mPostRecyclerView.smoothScrollToPosition(0);
        });
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

                switch (postType) {
                    case Constants.IMAGE:
                        FoxToolkit.INSTANCE.fullscreenImage(post, requireContext());
                        break;
                    case Constants.VIDEO:
                        if (FoxToolkit.INSTANCE.getTypeOfVideo(post) == Constants.PLAYABLE_VIDEO) {
                            navController.navigate(VideoDialogFragmentDirections.actionGlobalVideoDialogFragment(postType, post));
                            break;
                        } else {
                            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                            customTabsIntent.launchUrl(requireContext(), Uri.parse(post.getUrl()));
                        }
                        break;
                    case Constants.LINK:
                        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                        customTabsIntent.launchUrl(requireContext(), Uri.parse(post.getUrl()));
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
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                customTabsIntent.launchUrl(requireContext(), Uri.parse(post.getUrl()));
                break;
            default:
                NavGraphDirections.ActionGlobalSinglePostFragment action = NavGraphDirections.actionGlobalSinglePostFragment(post, postType);
                navController.navigate(action);
        }
    }

    private Toolbar getCurrentToolbar() {
        if (getParentFragment() instanceof UserFragment)
            return requireActivity().findViewById(R.id.profile_toolbar);
        else if (getParentFragment() instanceof SubredditFragment)
            return requireActivity().findViewById(R.id.subreddit_toolbar);
        else
            return requireActivity().findViewById(R.id.toolbar_main);
    }

    void setMenuItemClickForCurrentFragment() {
        Toolbar toolbar = getCurrentToolbar();

        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.sort_best:
                    filter = "best";
                    loadPosts(true);
                    return true;

                case R.id.sort_hot:
                    filter = "hot";
                    loadPosts(true);
                    return true;

                case R.id.sort_new:
                    filter = "new";
                    loadPosts(true);
                    return true;

                case R.id.sort_top:
                    timedFilter = "top";
                    break;

                case R.id.sort_controversial:
                    timedFilter = "controversial";
                    break;

                case R.id.sort_rising:
                    filter = "rising";
                    loadPosts(true);
                    return true;

                case R.id.sort_hour:
                    filter = timedFilter;
                    time = "hour";
                    loadPosts(true);
                    return true;

                case R.id.sort_24h:
                    filter = timedFilter;
                    time = "day";
                    loadPosts(true);
                    return true;

                case R.id.sort_week:
                    filter = timedFilter;
                    time = "week";
                    loadPosts(true);
                    return true;

                case R.id.sort_month:
                    filter = timedFilter;
                    time = "month";
                    loadPosts(true);
                    return true;

                case R.id.sort_year:
                    filter = timedFilter;
                    time = "year";
                    loadPosts(true);
                    return true;

                case R.id.sort_all_time:
                    filter = timedFilter;
                    time = "all";
                    loadPosts(true);
                    return true;
            }
            return false;
        });
    }

    public static PostFragment newInstance(String subreddit, String filter, String time) {
        PostFragment fragment = new PostFragment();

        Bundle args = new Bundle();
        args.putString("subreddit", subreddit);
        args.putString("filter", filter);
        args.putString("time", time);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Token token = InjectorUtils.getInstance().provideTokenRepository().getToken(requireActivity().getApplication());
        if (!mToken.getAccessToken().equals(token.getAccessToken())) {
            mToken = token;
            initRecycleView();
            loadPosts(true);
        }
        setMenuItemClickForCurrentFragment();
    }

    @Override
    public void onPause() {
        Toolbar toolbar = getCurrentToolbar();
        toolbar.setOnMenuItemClickListener(null);
        super.onPause();
    }
}