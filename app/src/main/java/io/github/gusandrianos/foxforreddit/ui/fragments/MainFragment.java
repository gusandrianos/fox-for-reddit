package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ViewPagerAdapter;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        setUpToolbar();

        ArrayList<Fragment> homeFragments = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        homeFragments.add(PostFragment.newInstance("", "best", ""));
        tabTitles.add("Home");
        homeFragments.add(PostFragment.newInstance("r/all", "hot", ""));
        tabTitles.add("All");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(homeFragments, tabTitles, this);

        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getFragmentTitle(position))
        ).attach();
    }

    private void setUpToolbar() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_main);
        toolbar.inflateMenu(R.menu.sorting);
        toolbar.getMenu().getItem(0).getSubMenu().getItem(0).setVisible(true);
        NavigationUI.setupWithNavController(toolbar, navController, mainActivity.appBarConfiguration);
    }
}
