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
import androidx.lifecycle.ViewModelProvider;
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
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;

public class MainFragment extends Fragment {

    Token mToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpToolbar();

        mToken = InjectorUtils.getInstance().provideTokenRepository(requireActivity().getApplication()).getToken();

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        ArrayList<PostFragment> homeFragments = new ArrayList<>();
        homeFragments.add(PostFragment.newInstance("", ""));
        homeFragments.add(PostFragment.newInstance("r/all", "hot"));

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(homeFragments.get(0), "HOME");
        viewPagerAdapter.addFragment(homeFragments.get(1), "POPULAR");
        viewPagerAdapter.addFragment(new SinglePostFragment(), "Comments");

        viewPager.setAdapter(viewPagerAdapter);

    }

    private void setUpToolbar() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        AppBarConfiguration appBarConfiguration = mainActivity.appBarConfiguration;
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_main);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }
}
