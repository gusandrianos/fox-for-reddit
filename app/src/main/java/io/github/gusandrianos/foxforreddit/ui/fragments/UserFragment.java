package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Objects;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.Subreddit;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ViewPagerAdapter;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

public class UserFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserFragmentArgs args = UserFragmentArgs.fromBundle(requireArguments());
        Data user = args.getUser();
        String username = args.getUsername();

        if (username.isEmpty() && user != null)
            username = user.getName();

        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.viewingSelf = username.equals(mainActivity.currentUserUsername);

        setUpToolbar(view);

        if (user != null) {
            setUserNames(view, user, username);
            buildUserProfile(user, view, true);
        } else if (!username.isEmpty()) {
            Button button = view.findViewById(R.id.button_follow_unfollow);
            button.setVisibility(View.VISIBLE);
            UserViewModelFactory factory = InjectorUtils.getInstance().provideUserViewModelFactory();
            UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
            viewModel.getUser(requireActivity().getApplication(), username).observe(getViewLifecycleOwner(), data -> buildUserProfile(data, view, Objects.equals(data.getName(), mainActivity.currentUserUsername)));
        }
    }

    private void buildUserProfile(Data user, View view, boolean isSelf) {
        setUserNames(view, user, user.getName());
        ViewPager viewPager = view.findViewById(R.id.profile_view_pager);
        viewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = view.findViewById(R.id.profile_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        if (isSelf)
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        else
            tabLayout.setTabMode(TabLayout.MODE_FIXED);

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
        ImageView profilePic = view.findViewById(R.id.profile_picture);
        ImageView coverPic = view.findViewById(R.id.profile_cover);
        Glide.with(view).load(Objects.requireNonNull(user.getIconImg()).split("\\?")[0]).into(profilePic);

        Type subredditType = new TypeToken<Subreddit>() {
        }.getType();
        Gson gson = new Gson();
        Subreddit subreddit = gson.fromJson(gson.toJsonTree(user.getSubreddit()).getAsJsonObject(), subredditType);

        Glide.with(view).load(Objects.requireNonNull(subreddit.getBannerImg()).split("\\?")[0]).into(coverPic);

        Data userSubreddit = getUserSubreddit(user);

        Button button = view.findViewById(R.id.button_follow_unfollow);
        if (userSubreddit.getUserIsSubscriber())
            button.setText("Unfollow");
        else
            button.setText("Follow");
    }

    void setUserNames(@NotNull View view, Data user, String username) {
        CollapsingToolbarLayout collapsingToolbar = view.findViewById(R.id.profile_collapsing_toolbar);
        TextView displayNameTextView = view.findViewById(R.id.text_user_display_name);
        TextView usernameTextView = view.findViewById(R.id.text_user_username);
        collapsingToolbar.setTitle(username);
        usernameTextView.setText(username);

        Data userSubreddit = getUserSubreddit(user);

        String displayName = userSubreddit.getTitle() != null ? userSubreddit.getTitle() : user.getName();
        if (!displayName.equals(username)) {
            displayNameTextView.setVisibility(View.VISIBLE);
            displayNameTextView.setText(displayName);
        }
    }

    Data getUserSubreddit(Data user) {
        Type subredditType = new TypeToken<Data>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(gson.toJsonTree(user.getSubreddit()).getAsJsonObject(), subredditType);
    }

    private void setUpToolbar(View view) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);
        if (mainActivity.viewingSelf)
            NavigationUI.setupWithNavController(toolbar, navController, mainActivity.appBarConfiguration);
        else {
            mainActivity.setSupportActionBar(toolbar);
            DrawerLayout drawer = mainActivity.drawer;
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            NavigationUI.setupActionBarWithNavController(mainActivity, navController);
        }
    }
}
