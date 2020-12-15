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
import com.jaredrummler.cyanea.Cyanea;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ViewPagerAdapter;

@AndroidEntryPoint
public class InboxFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.view_pager_messages);
        setUpNavigation();

        ArrayList<Fragment> messagesFragment = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        messagesFragment.add(MessagesFragment.newInstance("messages"));
        tabTitles.add("Messages");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(messagesFragment, tabTitles, this);

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void setUpNavigation() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar_fragment_messages);
        toolbar.inflateMenu(R.menu.message);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());

        MenuItem messageButton = toolbar.getMenu().getItem(0);
        messageButton.setOnMenuItemClickListener(item -> {
            navController.navigate(ComposeMessageFragmentDirections.actionGlobalComposeMessageFragment(null));
            return true;
        });

        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.VISIBLE);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.inboxFragment);
        item.setChecked(true);

        NavigationUI.setupWithNavController(toolbar, navController, mainActivity.appBarConfiguration);
    }
}
