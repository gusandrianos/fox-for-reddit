package io.github.gusandrianos.foxforreddit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.User;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

public class UserFragment extends Fragment {

    private User mUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUser = MainActivity.mUser;
        String username;

        if (getArguments() != null)
            username = getArguments().getString("username", "");
        else
            username = mUser.getName();

        setUpToolbar();

        CollapsingToolbarLayout collapsingToolbar = requireActivity().findViewById(R.id.profile_collapsing_toolbar);

        if (username.equals(mUser.getName())) {
            collapsingToolbar.setTitle(mUser.getName());
            buildUserProfile(mUser, view, true);
        } else {
            UserViewModelFactory factory = InjectorUtils.getInstance().provideUserViewModelFactory(requireActivity().getApplication());
            UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
            collapsingToolbar.setTitle(username);
            viewModel.getUser(username).observe(getViewLifecycleOwner(), user -> buildUserProfile(user, view, user.getName().equals(mUser.getName())));
        }
    }

    private void buildUserProfile(User user, View view, boolean isSelf) {
        ViewPager viewPager = view.findViewById(R.id.profile_view_pager);
        TabLayout tabLayout = view.findViewById(R.id.profile_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        if (isSelf)
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);

        viewPagerAdapter.addFragment(PostFragment.newInstance("u/" + user.getName() + "/submitted", ""), "Posts");
        viewPagerAdapter.addFragment(PostFragment.newInstance("u/" + user.getName() + "/comments", ""), "Comments");
        viewPagerAdapter.addFragment(AboutUserFragment.newInstance(user.getName(), user.getLinkKarma(), user.getCommentKarma()), "About");

        viewPager.setAdapter(viewPagerAdapter);
        if (isSelf) {
            viewPagerAdapter.addFragment(PostFragment.newInstance("u/" + user.getName() + "/upvoted", ""), "Upvoted");
            viewPagerAdapter.addFragment(PostFragment.newInstance("u/" + user.getName() + "/downvoted", ""), "Downvoted");
            viewPagerAdapter.addFragment(PostFragment.newInstance("u/" + user.getName() + "/hidden", ""), "Hidden");
            viewPagerAdapter.addFragment(PostFragment.newInstance("u/" + user.getName() + "/saved", ""), "Saved");
            viewPagerAdapter.notifyDataSetChanged();
        }
        ImageView profilePic = requireActivity().findViewById(R.id.profile_picture);
        ImageView coverPic = requireActivity().findViewById(R.id.profile_cover);
        Glide.with(view).load(user.getIconImg().split("\\?")[0]).into(profilePic);
        Glide.with(view).load(user.getSubreddit().getBannerImg().split("\\?")[0]).into(coverPic);
    }

    private void setUpToolbar() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        AppBarConfiguration appBarConfiguration = mainActivity.appBarConfiguration;
        Toolbar toolbar = requireActivity().findViewById(R.id.profile_toolbar);
        CollapsingToolbarLayout collapsingToolbar = requireActivity().findViewById(R.id.profile_collapsing_toolbar);
        NavigationUI.setupWithNavController(collapsingToolbar, toolbar, navController, appBarConfiguration);
    }
}
