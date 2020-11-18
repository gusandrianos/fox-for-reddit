package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.utilities.ViewPagerAdapter;

public class NotificationsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 viewPager = view.findViewById(R.id.view_pager_messages);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_messages);

        ArrayList<Fragment> messagesFragment = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        messagesFragment.add(MessagesFragment.newInstance("messaging"));
        tabTitles.add("Messages");

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(messagesFragment, tabTitles, this);

        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getFragmentTitle(position))
        ).attach();
    }
}