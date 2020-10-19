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

import com.google.android.material.appbar.CollapsingToolbarLayout;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.SubredditListAdapter;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

public class SubredditListFragment extends Fragment {

    SubredditListAdapter mSubredditListAdapter;
    RecyclerView mSubredditsRV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subreddit_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpToolbar();
        initRecycleView(view);
        initializeUI();
    }

    private void initializeUI() {
        UserViewModelFactory factory = InjectorUtils.getInstance().provideUserViewModelFactory(getActivity().getApplication());
        UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
        Token token = InjectorUtils.getInstance().provideTokenRepository(requireActivity().getApplication()).getToken();
        String location = "mine/subscriber";
        if(token.getRefreshToken() == null){
            location = "default";
        }

        viewModel.getSubreddits(getActivity().getApplication(), location).observe(getViewLifecycleOwner(), subredditPagingData -> {
            mSubredditListAdapter.submitData(getViewLifecycleOwner().getLifecycle(), subredditPagingData);
        });
    }

    private void initRecycleView(View view) {
        mSubredditsRV = view.findViewById(R.id.subreddit_list_recycler);
        mSubredditListAdapter = new SubredditListAdapter();
        mSubredditListAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY); //keep recyclerview on position
        mSubredditsRV.setHasFixedSize(true);
        mSubredditsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSubredditsRV.setAdapter(mSubredditListAdapter);
    }

    private void setUpToolbar() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        AppBarConfiguration appBarConfiguration = mainActivity.appBarConfiguration;
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_subreddits);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }
}
