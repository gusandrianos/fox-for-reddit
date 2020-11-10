package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ViewPagerAdapter;

import static io.github.gusandrianos.foxforreddit.Constants.KIND_POST;
import static io.github.gusandrianos.foxforreddit.Constants.KIND_SUBREDDIT;
import static io.github.gusandrianos.foxforreddit.Constants.KIND_USER;

public class SearchResultsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_results_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpNavigation(view);

        SearchResultsFragmentArgs searchResultsFragmentArgs = SearchResultsFragmentArgs.fromBundle(requireArguments());
        String query = searchResultsFragmentArgs.getSearchQuery();

        ViewPager2 viewPager2 = view.findViewById(R.id.view_pager_fragment_results_search);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_fragment_results_search);

        ArrayList<Fragment> searchResultsFragments = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        searchResultsFragments.add(PostFragment.newSearchInstance(query, "best", "", true, KIND_POST, ""));
        tabTitles.add("Posts");
        searchResultsFragments.add(SearchResultsListFragment.newSearchInstance(query, "best", "", false, KIND_SUBREDDIT));
        tabTitles.add("Subreddits");
        searchResultsFragments.add(SearchResultsListFragment.newSearchInstance(query, "best", "", false, KIND_USER));
        tabTitles.add("Profiles");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(searchResultsFragments, tabTitles, this);

        viewPager2.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(viewPagerAdapter.getFragmentTitle(position))
        ).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toolbar toolbar = view.findViewById(R.id.toolbar_fragment_results_search);
                toolbar.getMenu().findItem(R.id.search_sorting).setVisible(tab.getPosition() == 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    private void setUpNavigation(View view) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = view.findViewById(R.id.toolbar_fragment_results_search);
        toolbar.inflateMenu(R.menu.sorting_and_search_button);
        toolbar.getMenu().getItem(1).getSubMenu().getItem(0).setVisible(true);

        MenuItem searchButton = toolbar.getMenu().getItem(0);
        searchButton.setOnMenuItemClickListener(item -> {
            navController.navigate(NavGraphDirections.actionGlobalSearchFragment());
            return true;
        });

        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);

        NavigationUI.setupWithNavController(toolbar, navController);
    }
}
