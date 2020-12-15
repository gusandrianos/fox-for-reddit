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
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaredrummler.cyanea.Cyanea;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.SubredditListAdapter;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;

@AndroidEntryPoint
public class SubredditListFragment extends Fragment implements SubredditListAdapter.OnItemClickListener {

    SubredditListAdapter mSubredditListAdapter;
    RecyclerView mSubredditsRV;
    @Inject
    TokenRepository mTokenRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subreddit_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpNavigation();
        initRecycleView(view);
        initializeUI();
    }

    private void initializeUI() {
        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        Token token = mTokenRepository.getToken();
        String location = Constants.AUTHORIZED_SUB_LIST_LOCATION;
        if (token.getRefreshToken() == null) {
            location = Constants.VISITOR_SUB_LIST_LOCATION;
        }

        viewModel.getSubreddits(location).observe(getViewLifecycleOwner(), subredditPagingData -> {
            mSubredditListAdapter.submitData(getViewLifecycleOwner().getLifecycle(), subredditPagingData);
        });
    }

    private void initRecycleView(View view) {
        mSubredditsRV = view.findViewById(R.id.subreddit_list_recycler);
        mSubredditListAdapter = new SubredditListAdapter(this);
        mSubredditListAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY); //keep recyclerview on position
        mSubredditsRV.setHasFixedSize(true);
        mSubredditsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSubredditsRV.setAdapter(mSubredditListAdapter);
    }

    @Override
    public void onItemClick(@NotNull Data item) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        String subredditNamePrefixed = item.getDisplayNamePrefixed();
        boolean modeSelectSubreddit = SubredditListFragmentArgs.fromBundle(requireArguments()).getModeSelectSubreddit();

        if (modeSelectSubreddit) {
            MainActivity mainActivity = (MainActivity) requireActivity();
            if (!mainActivity.getFoxSharedViewModel().getCurrentSubreddit().equals(subredditNamePrefixed)) {
                mainActivity.getFoxSharedViewModel().setCurrentFlair(null);
                mainActivity.getFoxSharedViewModel().setCurrentSubreddit(subredditNamePrefixed);
            }
            requireActivity().onBackPressed();
        } else {
            if (subredditNamePrefixed.startsWith("r/")) {
                NavGraphDirections.ActionGlobalSubredditFragment action = NavGraphDirections.actionGlobalSubredditFragment(subredditNamePrefixed);
                navController.navigate(action);
            } else {
                NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(subredditNamePrefixed.split("/")[1]);
                navController.navigate(action);
            }
        }
    }

    private void setUpNavigation() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.VISIBLE);
        AppBarConfiguration appBarConfiguration = mainActivity.appBarConfiguration;
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_subreddits);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }
}
