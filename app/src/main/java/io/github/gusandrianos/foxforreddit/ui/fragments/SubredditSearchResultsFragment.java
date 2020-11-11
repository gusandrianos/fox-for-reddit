package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ViewPagerAdapter;

import static io.github.gusandrianos.foxforreddit.Constants.KIND_POST;

public class SubredditSearchResultsFragment extends Fragment {

    String mSubreddit;
    String mQuery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results_search_subreddit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SubredditSearchResultsFragmentArgs subredditSearchResultsFragmentArgs = SubredditSearchResultsFragmentArgs.fromBundle(requireArguments());
        mSubreddit = subredditSearchResultsFragmentArgs.getSubreddit();
        mQuery = subredditSearchResultsFragmentArgs.getQuery();

        setUpNavigation(view);

        ViewPager2 viewPager2 = view.findViewById(R.id.view_pager_fragment_results_search_subreddit);

        ArrayList<Fragment> searchResultsFragments = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        searchResultsFragments.add(PostFragment.newSearchInstance(mQuery, "best", "", true, KIND_POST, mSubreddit));
        tabTitles.add("Posts");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(searchResultsFragments, tabTitles, this);
        viewPager2.setAdapter(viewPagerAdapter);
    }

    private void setUpNavigation(View view) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = view.findViewById(R.id.toolbar_fragment_results_search_subreddit);
        toolbar.inflateMenu(R.menu.sorting_and_search_bar);
        toolbar.getMenu().getItem(1).getSubMenu().getItem(0).setVisible(true);

        MenuItem searchItem = toolbar.getMenu().getItem(0);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search in " + mSubreddit);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                navController.navigate(SubredditSearchResultsFragmentDirections.actionSubredditSearchResultsFragmentSelf(mSubreddit, query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        NavigationUI.setupWithNavController(toolbar, navController);

        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);
    }
}
