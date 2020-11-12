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
import io.github.gusandrianos.foxforreddit.viewmodels.SearchViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SearchViewModelFactory;
import kotlin.Unit;

import static io.github.gusandrianos.foxforreddit.Constants.ACTION_POST;
import static io.github.gusandrianos.foxforreddit.Constants.ACTION_SEARCH;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_FILTER_NAME;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_QUERY_STRING;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_SR_RESTRICT_BOOLEAN;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_SUBREDDIT_NAME;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_TIME_NAME;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_SEARCH_TYPE;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_TYPE_OF_ACTION;


public class PostFragment extends Fragment implements PostAdapter.OnItemClickListener {
    private View mView;
    private Token mToken;

    String type_of_action;
    String subreddit;
    String filter;
    String timedFilter;
    String time;

    String query;
    boolean sr_restrict;
    String searchType;

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
        type_of_action = getArguments().getString(ARG_TYPE_OF_ACTION, ACTION_POST);

        if (type_of_action.equals(ACTION_SEARCH)) {
            query = getArguments().getString(ARG_QUERY_STRING, "");
            sr_restrict = getArguments().getBoolean(ARG_SR_RESTRICT_BOOLEAN);
            searchType = getArguments().getString(ARG_SEARCH_TYPE);
        }
        subreddit = getArguments().getString(Constants.ARG_SUBREDDIT_NAME, "");
        filter = getArguments().getString(Constants.ARG_FILTER_NAME, "");
        time = getArguments().getString(Constants.ARG_TIME_NAME, "");
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
        if (type_of_action.equals(ACTION_POST)) {
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
        } else {
            SearchViewModelFactory factory = InjectorUtils.getInstance().provideSearchViewModelFactory();
            SearchViewModel viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);

            if (requestChanged)
                viewModel.deleteCached();
            viewModel.searchResults(query, filter, time, sr_restrict, searchType, subreddit, getActivity().getApplication()).observe(getViewLifecycleOwner(), searchPostPagingData -> {
                mPostRecyclerViewAdapter.submitData(getViewLifecycleOwner().getLifecycle(), searchPostPagingData);
                mPostRecyclerViewAdapter.addLoadStateListener(loadStates -> {
                    if (loadStates.getRefresh() instanceof LoadState.Loading)
                        pullToRefresh.setRefreshing(true);
                    else if (loadStates.getRefresh() instanceof LoadState.NotLoading)
                        pullToRefresh.setRefreshing(false);
                    return Unit.INSTANCE;
                });
            });
        }
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
                startActivity(Intent.createChooser(FoxToolkit.INSTANCE.shareLink(post), Constants.SHARE_TEXT));
                break;
            case Constants.POST_VOTE_NOW:
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                customTabsIntent.launchUrl(requireContext(), Uri.parse(post.getUrl()));
                break;
            case Constants.POST_MORE_ACTIONS:
                navController.navigate(NavGraphDirections.actionGlobalPopUpMoreActionsDialogFragment());
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
        else if (getParentFragment() instanceof SearchResultsFragment)
            return requireActivity().findViewById(R.id.toolbar_fragment_results_search);
        else if (getParentFragment() instanceof SubredditSearchResultsFragment)
            return requireActivity().findViewById(R.id.toolbar_fragment_results_search_subreddit);
        else
            return requireActivity().findViewById(R.id.toolbar_main);
    }

    void setMenuItemClickForCurrentFragment() {
        Toolbar toolbar = getCurrentToolbar();

        toolbar.setOnMenuItemClickListener(menuItem -> {
            int itemItemId = menuItem.getItemId();
            if (itemItemId == R.id.sort_best) {
                filter = Constants.SORTING_BEST;
                loadPosts(true);
                return true;
            } else if (itemItemId == R.id.sort_hot) {
                filter = Constants.SORTING_HOT;
                loadPosts(true);
                return true;
            } else if (itemItemId == R.id.sort_new) {
                filter = Constants.SORTING_NEW;
                loadPosts(true);
                return true;
            } else if (itemItemId == R.id.sort_top) {
                timedFilter = Constants.SORTING_TOP;
            } else if (itemItemId == R.id.sort_controversial) {
                timedFilter = Constants.SORTING_CONTROVERSIAL;
            } else if (itemItemId == R.id.sort_rising) {
                filter = Constants.SORTING_RISING;
                loadPosts(true);
                return true;
            } else if (itemItemId == R.id.sort_hour) {
                filter = timedFilter;
                time = Constants.TIME_HOUR;
                loadPosts(true);
                return true;
            } else if (itemItemId == R.id.sort_24h) {
                filter = timedFilter;
                time = Constants.TIME_DAY;
                loadPosts(true);
                return true;
            } else if (itemItemId == R.id.sort_week) {
                filter = timedFilter;
                time = Constants.TIME_WEEK;
                loadPosts(true);
                return true;
            } else if (itemItemId == R.id.sort_month) {
                filter = timedFilter;
                time = Constants.TIME_MONTH;
                loadPosts(true);
                return true;
            } else if (itemItemId == R.id.sort_year) {
                filter = timedFilter;
                time = Constants.TIME_YEAR;
                loadPosts(true);
                return true;
            } else if (itemItemId == R.id.sort_all_time) {
                filter = timedFilter;
                time = Constants.TIME_ALL;
                loadPosts(true);
                return true;
            }
            return false;
        });
    }

    public static PostFragment newInstance(String subreddit, String filter, String time) {
        PostFragment fragment = new PostFragment();

        Bundle args = new Bundle();
        args.putString(Constants.ARG_SUBREDDIT_NAME, subreddit);
        args.putString(Constants.ARG_FILTER_NAME, filter);
        args.putString(Constants.ARG_TIME_NAME, time);
        args.putString(ARG_TYPE_OF_ACTION, ACTION_POST);
        fragment.setArguments(args);

        return fragment;
    }

    public static PostFragment newSearchInstance(String query, String sort, String time, Boolean restrict_sr, String type, String subreddit) {
        PostFragment fragment = new PostFragment();

        Bundle args = new Bundle();
        args.putString(ARG_SUBREDDIT_NAME, subreddit);
        args.putString(ARG_QUERY_STRING, query);
        args.putString(ARG_FILTER_NAME, sort);
        args.putString(ARG_TIME_NAME, time);
        args.putBoolean(ARG_SR_RESTRICT_BOOLEAN, restrict_sr);
        args.putString(ARG_SEARCH_TYPE, type);
        args.putString(ARG_TYPE_OF_ACTION, ACTION_SEARCH);
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
