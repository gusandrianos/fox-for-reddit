package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.utilities.PostLoadStateAdapter;
import io.github.gusandrianos.foxforreddit.utilities.SearchResultsAdapter;
import io.github.gusandrianos.foxforreddit.viewmodels.SearchViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SearchViewModelFactory;
import kotlin.Unit;

import static io.github.gusandrianos.foxforreddit.Constants.ACTION_SEARCH;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_FILTER_NAME;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_QUERY_STRING;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_SEARCH_TYPE;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_SR_RESTRICT_BOOLEAN;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_TIME_NAME;
import static io.github.gusandrianos.foxforreddit.Constants.ARG_TYPE_OF_ACTION;
import static io.github.gusandrianos.foxforreddit.Constants.KIND_SUBREDDIT;

public class SearchResultsListFragment extends Fragment implements SearchResultsAdapter.OnSearchResultsItemClickListener {

    private View mView;
    private Token mToken;

    String filter;
    String time;

    String query;
    boolean sr_restrict;
    String searchType;

    SearchResultsAdapter mPostRecyclerViewAdapter;
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


        query = getArguments().getString(ARG_QUERY_STRING, "");
        sr_restrict = getArguments().getBoolean(ARG_SR_RESTRICT_BOOLEAN);
        searchType = getArguments().getString(ARG_SEARCH_TYPE);
        filter = getArguments().getString(ARG_FILTER_NAME, "");
        time = getArguments().getString(ARG_TIME_NAME, "");
        pullToRefresh = view.findViewById(R.id.swipe_refresh_layout_posts);

        initRecycleView();
        loadSearchList(false);
        initSwipeToRefresh();
    }

    private void initRecycleView() {
        mPostRecyclerView = mView.findViewById(R.id.recyclerview);
        mPostRecyclerViewAdapter = new SearchResultsAdapter(searchType, this);
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

    void loadSearchList(boolean requestChanged) {
        SearchViewModelFactory factory = InjectorUtils.getInstance().provideSearchViewModelFactory();
        SearchViewModel viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);

        if (requestChanged)
            viewModel.deleteCached();

        viewModel.searchResults(query, filter, time, sr_restrict, searchType, "", getActivity().getApplication()).observe(getViewLifecycleOwner(), searchPostPagingData -> {
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


    private void initSwipeToRefresh() {
        pullToRefresh.setOnRefreshListener(() -> {
            mPostRecyclerViewAdapter.refresh();
            mPostRecyclerView.smoothScrollToPosition(0);
        });
    }


    private Toolbar getCurrentToolbar() {
        if (getParentFragment() instanceof SearchResultsFragment)
            return requireActivity().findViewById(R.id.toolbar_fragment_results_search);
        else
            return requireActivity().findViewById(R.id.toolbar_main);
    }


    public static SearchResultsListFragment newSearchInstance(String query, String sort, String time, Boolean restrict_sr, String type) {
        SearchResultsListFragment fragment = new SearchResultsListFragment();

        Bundle args = new Bundle();
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
            loadSearchList(true);
        }
    }

    @Override
    public void onPause() {
        Toolbar toolbar = getCurrentToolbar();
        toolbar.setOnMenuItemClickListener(null);
        super.onPause();
    }

    @Override
    public void onSearchItemClick(@NotNull Data item) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        if (searchType.equals(KIND_SUBREDDIT)) {
            NavGraphDirections.ActionGlobalSubredditFragment action = NavGraphDirections.actionGlobalSubredditFragment(Objects.requireNonNull(item.getDisplayNamePrefixed()));
            navController.navigate(action);
        } else {
            NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(null, Objects.requireNonNull(item.getName()));
            navController.navigate(action);
        }
    }
}
