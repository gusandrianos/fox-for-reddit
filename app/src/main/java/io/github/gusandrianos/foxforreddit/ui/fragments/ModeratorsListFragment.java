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
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaredrummler.cyanea.Cyanea;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.utilities.ModeratorsListAdapter;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;

public class ModeratorsListFragment extends Fragment implements ModeratorsListAdapter.OnItemClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_moderators, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String subreddit = ModeratorsListFragmentArgs.fromBundle(requireArguments()).getSubredditName();
        setUpNavigation(view);
        setUpRecyclerView(view, subreddit);
    }

    void setUpRecyclerView(View view, String subreddit) {
        SubredditViewModelFactory factory = InjectorUtils.getInstance().provideSubredditViewModelFactory();
        SubredditViewModel viewModel = new ViewModelProvider(this, factory).get(SubredditViewModel.class);

        viewModel.getSubredditModerators(subreddit, requireActivity().getApplication()).observe(getViewLifecycleOwner(), moderators -> {
            if (moderators != null) {
                RecyclerView moderatorsRV = view.findViewById(R.id.recycler_moderators);
                ModeratorsListAdapter adapter = new ModeratorsListAdapter(moderators.getModerators(), this);
                adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
                moderatorsRV.addItemDecoration(new DividerItemDecoration(moderatorsRV.getContext(), DividerItemDecoration.VERTICAL));
                moderatorsRV.setHasFixedSize(true);
                moderatorsRV.setLayoutManager(new LinearLayoutManager(requireActivity()));
                moderatorsRV.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onItemClick(@NotNull String username) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(username);
        navController.navigate(action);
    }

    void setUpNavigation(View view) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.VISIBLE);
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_moderators);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());

        NavigationUI.setupWithNavController(toolbar, navController);
    }
}
