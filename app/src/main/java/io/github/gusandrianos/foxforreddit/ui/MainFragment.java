package io.github.gusandrianos.foxforreddit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;

public class MainFragment extends Fragment {

    Token mToken;
    private ArrayList<PostFragment> homeFragments;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpToolbar();

        mToken = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        homeFragments = new ArrayList<>();
        homeFragments.add(newPostFragment("", ""));
        homeFragments.add(newPostFragment("r/all", "hot"));

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(homeFragments.get(0), "HOME");
        viewPagerAdapter.addFragment(homeFragments.get(1), "POPULAR");

        viewPager.setAdapter(viewPagerAdapter);
    }

    private PostFragment newPostFragment(String subreddit, String filter) {
        PostFragment fragment = new PostFragment();

        Bundle args = new Bundle();
        args.putString("subreddit", subreddit);
        args.putString("filter", filter);
        fragment.setArguments(args);

        return fragment;
    }

    private void setUpToolbar() {
        MainActivity mainActivity = (MainActivity) getActivity();
        NavigationView navigationView = ((AppCompatActivity) getActivity()).findViewById(R.id.nav_view);
        NavController navController = NavHostFragment.findNavController(this);
        AppBarConfiguration appBarConfiguration = mainActivity.appBarConfiguration;
        Toolbar toolbar = ((AppCompatActivity) getActivity()).findViewById(R.id.toolbar_main);

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
}
