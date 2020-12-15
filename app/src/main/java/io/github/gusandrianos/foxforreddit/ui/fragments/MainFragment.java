package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.content.Intent;
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
import com.jaredrummler.cyanea.Cyanea;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.db.TokenDao;
import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.ui.ThemeSettings;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.ViewPagerAdapter;

@AndroidEntryPoint
public class MainFragment extends Fragment {
    @Inject
    TokenDao mTokenDao;
    @Inject
    TokenRepository mTokenRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Cyanea.getInstance().getBackgroundColor());
        setUpNavigation();

        ArrayList<Fragment> homeFragments = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        if (FoxToolkit.INSTANCE.isAuthorized(mTokenDao, mTokenRepository)) {
            homeFragments.add(PostFragment.newInstance("", "best", ""));
            tabTitles.add("Home");
        } else {
            homeFragments.add(PostFragment.newInstance("r/popular", "best", ""));
            tabTitles.add("Popular");
        }

        homeFragments.add(PostFragment.newInstance("r/all", "hot", ""));
        tabTitles.add("All");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(homeFragments, tabTitles, this);

        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getFragmentTitle(position))
        ).attach();
    }

    private void setUpNavigation() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_main);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());
        toolbar.inflateMenu(R.menu.sorting_and_search_button);
        toolbar.getMenu().findItem(R.id.search_sorting).getSubMenu().getItem(0).setVisible(true);

        toolbar.getMenu().findItem(R.id.search).setOnMenuItemClickListener(item -> {
            navController.navigate(NavGraphDirections.actionGlobalSearchFragment());
            return true;
        });

        toolbar.getMenu().findItem(R.id.cyanea_settings).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(requireContext(), ThemeSettings.class);
            startActivity(intent);
            return true;
        });

        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.VISIBLE);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.mainFragment);
        item.setChecked(true);

        NavigationUI.setupWithNavController(toolbar, navController, mainActivity.appBarConfiguration);
    }
}
