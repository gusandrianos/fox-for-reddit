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
import androidx.paging.LoadState;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;

import io.github.gusandrianos.foxforreddit.utilities.MessagesAdapter;
import io.github.gusandrianos.foxforreddit.utilities.PostLoadStateAdapter;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;
import kotlin.Unit;


public class MessagesFragment extends Fragment implements MessagesAdapter.MessagesItemClickListener {

    private View mView;

    private String where;

    MessagesAdapter mMessagesRecyclerViewAdapter;
    RecyclerView mMessagesRecyclerView;
    SwipeRefreshLayout pullToRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;
        where = getArguments().getString(Constants.PREFS_WHERE);

        pullToRefresh = view.findViewById(R.id.swipe_refresh_layout_posts);
        initRecycleView();
        loadMessagesList(false);
        initSwipeToRefresh();
    }

    void loadMessagesList(boolean requestChanged) {
        UserViewModelFactory factory = InjectorUtils.getInstance().provideUserViewModelFactory();
        UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

        if (requestChanged)
            viewModel.deleteCached();

        viewModel.getMessagesWhere(getActivity().getApplication(), where).observe(getViewLifecycleOwner(), this::submitToAdapter);
    }

    private void submitToAdapter(PagingData pagingData) {
        mMessagesRecyclerViewAdapter.submitData(getViewLifecycleOwner().getLifecycle(), pagingData);
        mMessagesRecyclerViewAdapter    .addLoadStateListener(loadStates -> {
            if (loadStates.getRefresh() instanceof LoadState.Loading)
                pullToRefresh.setRefreshing(true);
            else if (loadStates.getRefresh() instanceof LoadState.NotLoading)
                pullToRefresh.setRefreshing(false);
            return Unit.INSTANCE;
        });
    }

    private void initRecycleView() {
        mMessagesRecyclerView = mView.findViewById(R.id.recyclerview);
        mMessagesRecyclerViewAdapter = new MessagesAdapter(this);
        mMessagesRecyclerViewAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY); //keep recyclerview on position
        mMessagesRecyclerView.setHasFixedSize(true);
        mMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessagesRecyclerView.addItemDecoration(new DividerItemDecoration(mMessagesRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        PostLoadStateAdapter postLoadStateAdapter = new PostLoadStateAdapter(() -> {
            mMessagesRecyclerViewAdapter.retry();
            return Unit.INSTANCE;
        });
        mMessagesRecyclerView.setAdapter(mMessagesRecyclerViewAdapter.withLoadStateFooter(postLoadStateAdapter));
    }

    private void initSwipeToRefresh() {
        pullToRefresh.setOnRefreshListener(() -> {
            mMessagesRecyclerViewAdapter.refresh();
            mMessagesRecyclerView.smoothScrollToPosition(0);
        });
    }


    public static MessagesFragment newInstance(String where) {
        MessagesFragment fragment = new MessagesFragment();

        Bundle args = new Bundle();
        args.putString(Constants.PREFS_WHERE, where);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onMessageItemClicked(@NotNull Data item) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        navController.navigate(NavGraphDirections.actionGlobalMessagesWithUserFragment(item, item.getAuthor()));
    }
}
