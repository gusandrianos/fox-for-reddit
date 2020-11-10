package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Listing;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.utilities.SearchAdapter;
import io.github.gusandrianos.foxforreddit.viewmodels.SearchViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SearchViewModelFactory;

public class SearchFragment extends Fragment implements SearchAdapter.OnSearchItemClickListener {
    TextView txtResultsFromSearch;

    RecyclerView search;
    SearchAdapter searchAdapter;

    MenuItem searchBarItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpNavigation();

        search = view.findViewById(R.id.recycler_fragment_search);
        setUpSearchView(searchBarItem);

        txtResultsFromSearch = view.findViewById(R.id.txt_results_from_search);
        txtResultsFromSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResultsFromSearch(txtResultsFromSearch.getTag().toString());
            }
        });
    }

    private void initRecyclerView(Listing searchData) {
        searchAdapter = new SearchAdapter(this, searchData);
        searchAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

        search.setHasFixedSize(true);
        search.setLayoutManager(new LinearLayoutManager(requireActivity()));
        search.setAdapter(searchAdapter);
    }

    private void setUpNavigation() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_fragment_search);
        toolbar.inflateMenu(R.menu.search);
        searchBarItem = toolbar.getMenu().getItem(0);
        NavigationUI.setupWithNavController(toolbar, navController);

        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void setUpSearchView(MenuItem searchBarItem) {
        SearchView searchView = (SearchView) searchBarItem.getActionView();

        searchView.setQueryHint("Search...");
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);

        SearchViewModelFactory factory = InjectorUtils.getInstance().provideSearchViewModelFactory();
        SearchViewModel viewModel = new ViewModelProvider(this, factory).get(SearchViewModel.class);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                openResultsFromSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.searchTopSubreddits(newText, true, true, requireActivity().getApplication()).observe(getViewLifecycleOwner(), searchData -> {
                    initRecyclerView(searchData);
                    if (!newText.trim().isEmpty()) {
                        String resultsFromSearch = "Results for \"" + newText.trim() + "\"";
                        txtResultsFromSearch.setText(resultsFromSearch);
                        txtResultsFromSearch.setTag(newText);
                        txtResultsFromSearch.setVisibility(View.VISIBLE);
                    } else {
                        txtResultsFromSearch.setVisibility(View.GONE);
                    }
                });
                return true;
            }
        });

        searchView.setOnCloseListener(() -> true);

        searchView.setOnQueryTextFocusChangeListener((view1, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view1.findFocus(), 0);
            }
        });
    }

    @Override
    public void onSearchItemClick(@NotNull String destination, @NotNull String type) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

        if (type.equals("t5")) {
            NavGraphDirections.ActionGlobalSubredditFragment action = NavGraphDirections.actionGlobalSubredditFragment(destination);
            navController.navigate(action);
        } else {
            NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(null, destination);
            navController.navigate(action);
        }
    }

    public void openResultsFromSearch(String searched) {
        if (!searched.trim().isEmpty()) {
            if (searched.length() < 3) {    //ifsearch String is < 3,  Reddit Search returns empty String and app stops
                StringBuilder searchedBuilder = new StringBuilder(searched);
                while (searchedBuilder.length() < 3)
                    searchedBuilder.append(" ");
                searched = searchedBuilder.toString();
            }
            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
            navController.navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment(searched));
        }
    }
}
