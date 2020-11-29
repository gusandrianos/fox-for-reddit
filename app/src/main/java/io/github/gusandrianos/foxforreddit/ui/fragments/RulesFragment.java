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

import com.jaredrummler.cyanea.Cyanea;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.utilities.RulesListAdapter;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;

/*
    Shows the list of rules from the current subreddit
 */
public class RulesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rules, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String subreddit = RulesFragmentArgs.fromBundle(requireArguments()).getSubredditName();
        setUpNavigation(view);
        setUpRecyclerView(view, subreddit);
    }

    private void setUpRecyclerView(View view, String subreddit) {
        SubredditViewModelFactory factory = InjectorUtils.getInstance()
                .provideSubredditViewModelFactory();
        SubredditViewModel viewModel = new ViewModelProvider(this, factory)
                .get(SubredditViewModel.class);

        viewModel.getSubredditRules(subreddit, requireActivity().getApplication())
                .observe(getViewLifecycleOwner(), rules -> {
                    if (rules != null) {
                        RecyclerView rulesRV = view.findViewById(R.id.recycler_rules);
                        RulesListAdapter adapter = new RulesListAdapter(rules.getRules());
                        adapter.setStateRestorationPolicy(
                                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
                        rulesRV.addItemDecoration(new DividerItemDecoration(rulesRV.getContext(),
                                DividerItemDecoration.VERTICAL));
                        rulesRV.setHasFixedSize(true);
                        rulesRV.setLayoutManager(new LinearLayoutManager(requireActivity()));
                        rulesRV.setAdapter(adapter);
                    }
                });
    }

    private void setUpNavigation(View view) {
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_rules);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());
        NavigationUI.setupWithNavController(toolbar, navController);
    }
}
