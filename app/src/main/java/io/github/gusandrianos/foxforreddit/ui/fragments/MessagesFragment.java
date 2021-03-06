package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
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

import com.jaredrummler.cyanea.Cyanea;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.MessagesAdapter;
import io.github.gusandrianos.foxforreddit.utilities.PostLoadStateAdapter;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import kotlin.Unit;

@AndroidEntryPoint
public class MessagesFragment extends Fragment implements MessagesAdapter.MessagesItemClickListener {

    MessagesAdapter mMessagesRecyclerViewAdapter;
    RecyclerView mMessagesRecyclerView;
    SwipeRefreshLayout pullToRefresh;
    private View mView;
    private String where;

    public static MessagesFragment newInstance(String where) {
        MessagesFragment fragment = new MessagesFragment();

        Bundle args = new Bundle();
        args.putString(Constants.PREFS_WHERE, where);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
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
        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        if (requestChanged)
            viewModel.deleteCached();

        viewModel.getMessagesWhere(where).observe(getViewLifecycleOwner(), this::submitToAdapter);
    }

    private void submitToAdapter(PagingData pagingData) {
        mMessagesRecyclerViewAdapter.submitData(getViewLifecycleOwner().getLifecycle(), pagingData);
        mMessagesRecyclerViewAdapter.addLoadStateListener(loadStates -> {
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
        int color = Cyanea.getInstance().getAccent();
        pullToRefresh.setColorSchemeColors(color, color, color);

        pullToRefresh.setOnRefreshListener(() -> {
            mMessagesRecyclerViewAdapter.refresh();
            mMessagesRecyclerView.smoothScrollToPosition(0);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMessagesRecyclerViewAdapter.refresh();
    }

    @Override
    public void onMessageItemClicked(@NotNull Data item, String action, View view) {

        switch (action) {
            case Constants.OPEN_MESSAGES_WITH_USER:
                NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
                navController.navigate(InboxFragmentDirections.actionInboxFragmentToConversationFragment(item, item.getAuthor()));
                break;
            case Constants.THING_MORE_ACTIONS:
                UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
                PostViewModel postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

                PopupMenu menu = new PopupMenu(requireContext(), view);
                menu.inflate(R.menu.private_messages_popup);

                menu.getMenu().findItem(R.id.private_message_delete).setOnMenuItemClickListener(messageDelete -> {
                    popUpMenuDelete(userViewModel, postViewModel, item.getName(), item.getAuthor());
                    return true;
                });

                menu.show();
                break;
            default:
        }
    }

    private void popUpMenuDelete(UserViewModel userViewModel, PostViewModel postViewModel, String fullname, String author) {
        String currentUser = ((MainActivity) requireActivity()).getFoxSharedViewModel().getCurrentUserUsername();
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.ConfirmationAlertDialog);

        builder.setMessage("Are you sure?")
                .setCancelable(false)
                .setNegativeButton("Nope", (dialog, id) -> dialog.cancel())
                .setPositiveButton("Do it!", (dialog, id) -> {
                    if (author.equals(currentUser))
                        postViewModel.deleteSubmission(fullname)
                                .observe(getViewLifecycleOwner(), success -> {
                                    if (success) {
                                        mMessagesRecyclerViewAdapter.refresh();
                                    }
                                });
                    else
                        userViewModel.deleteMsg(fullname)
                                .observe(getViewLifecycleOwner(), success -> {
                                    if (success) {
                                        mMessagesRecyclerViewAdapter.refresh();
                                    }
                                });
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
