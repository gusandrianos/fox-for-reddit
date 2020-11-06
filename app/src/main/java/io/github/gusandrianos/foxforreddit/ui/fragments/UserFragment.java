package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.Subreddit;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ViewPagerAdapter;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

import static io.github.gusandrianos.foxforreddit.Constants.ACTION_SUBSCRIBE;
import static io.github.gusandrianos.foxforreddit.Constants.ACTION_UNSUBSCRIBE;
import static io.github.gusandrianos.foxforreddit.Constants.EDIT_PROFILE_URL;
import static io.github.gusandrianos.foxforreddit.Constants.USER_UI_BUTTON_EDIT;
import static io.github.gusandrianos.foxforreddit.Constants.USER_UI_BUTTON_FOLLOW;
import static io.github.gusandrianos.foxforreddit.Constants.USER_UI_BUTTON_UNFOLLOW;
import static io.github.gusandrianos.foxforreddit.Constants.USER_UI_TAB_ABOUT;
import static io.github.gusandrianos.foxforreddit.Constants.USER_UI_TAB_COMMENTS;
import static io.github.gusandrianos.foxforreddit.Constants.USER_UI_TAB_DOWNVOTED;
import static io.github.gusandrianos.foxforreddit.Constants.USER_UI_TAB_HIDDEN;
import static io.github.gusandrianos.foxforreddit.Constants.USER_UI_TAB_POSTS;
import static io.github.gusandrianos.foxforreddit.Constants.USER_UI_TAB_SAVED;
import static io.github.gusandrianos.foxforreddit.Constants.USER_UI_TAB_UPVOTED;

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

        setUpNavigation(view);

        if (user != null) {
            setUserNames(view, user, username);
            buildUserProfile(user, view, true);
        } else if (!username.isEmpty()) {
            UserViewModelFactory factory = InjectorUtils.getInstance().provideUserViewModelFactory();
            UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
            viewModel.getUser(requireActivity().getApplication(), username).observe(getViewLifecycleOwner(), data -> buildUserProfile(data, view, Objects.equals(data.getName(), mainActivity.currentUserUsername)));
        }
    }

    private void buildUserProfile(Data user, View view, boolean isSelf) {
        setUserNames(view, user, user.getName());

        ViewPager2 viewPager = view.findViewById(R.id.profile_view_pager);
        TabLayout tabLayout = view.findViewById(R.id.profile_tab_layout);

        if (isSelf)
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        else
            tabLayout.setTabMode(TabLayout.MODE_FIXED);

        ArrayList<Fragment> userFragments = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        userFragments.add(PostFragment.newInstance(buildURL(user.getName(), "/submitted"), "", ""));
        tabTitles.add(USER_UI_TAB_POSTS);
        userFragments.add(PostFragment.newInstance(buildURL(user.getName(), "/comments"), "", ""));
        tabTitles.add(USER_UI_TAB_COMMENTS);
        userFragments.add(AboutUserFragment.newInstance(user.getName(), user.getLinkKarma(), user.getCommentKarma()));
        tabTitles.add(USER_UI_TAB_ABOUT);

        // TODO: Use appropriate API Endpoint to make this meaningful
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                Toolbar toolbar = view.findViewById(R.id.profile_toolbar);
//
//                if (tab.getPosition() < 2) {
//                    toolbar = view.findViewById(R.id.profile_toolbar);
//                    toolbar.getMenu().setGroupVisible(0, true);
//                } else
//                    toolbar.getMenu().setGroupVisible(0, false);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });

        if (isSelf) {
            userFragments.add(PostFragment.newInstance(buildURL(user.getName(), "/upvoted"), "", ""));
            tabTitles.add(USER_UI_TAB_UPVOTED);
            userFragments.add(PostFragment.newInstance(buildURL(user.getName(), "/downvoted"), "", ""));
            tabTitles.add(USER_UI_TAB_DOWNVOTED);
            userFragments.add(PostFragment.newInstance(buildURL(user.getName(), "/hidden"), "", ""));
            tabTitles.add(USER_UI_TAB_HIDDEN);
            userFragments.add(PostFragment.newInstance(buildURL(user.getName(), "/saved"), "", ""));
            tabTitles.add(USER_UI_TAB_SAVED);
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(userFragments, tabTitles, this);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getFragmentTitle(position))
        ).attach();

        ImageView profilePic = view.findViewById(R.id.profile_picture);
        ImageView coverPic = view.findViewById(R.id.profile_cover);
        Glide.with(view).load(Objects.requireNonNull(user.getIconImg()).split("\\?")[0]).into(profilePic);

        Type subredditType = new TypeToken<Subreddit>() {
        }.getType();
        Gson gson = new Gson();
        Subreddit subreddit = gson.fromJson(gson.toJsonTree(user.getSubreddit()).getAsJsonObject(), subredditType);

        Glide.with(view).load(Objects.requireNonNull(subreddit.getBannerImg()).split("\\?")[0]).into(coverPic);

        Data userSubreddit = getUserSubreddit(user);

        MaterialButton profileButton = view.findViewById(R.id.button_profile_button);

        setupButton(userSubreddit, view);
        MainActivity mainActivity = (MainActivity) requireActivity();
        profileButton.setOnClickListener(button -> {
            if (!mainActivity.viewingSelf) {
                SubredditViewModelFactory factory = InjectorUtils.getInstance().provideSubredditViewModelFactory();
                SubredditViewModel viewModel = new ViewModelProvider(this, factory).get(SubredditViewModel.class);
                viewModel.toggleSubscribed(getFinalAction(userSubreddit),
                        userSubreddit.getDisplayName(),
                        requireActivity().getApplication())
                        .observe(getViewLifecycleOwner(), status -> {
                            if (status) {
                                userSubreddit.setUserIsSubscriber(!userSubreddit.getUserIsSubscriber());
                                setupButton(userSubreddit, view);
                            }
                        });
            } else {
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                customTabsIntent.launchUrl(requireContext(), Uri.parse(EDIT_PROFILE_URL));
            }
        });

        AppBarLayout appBarLayout = view.findViewById(R.id.fragment_profile_appbar);
        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);
        toolbar.setBackgroundColor(Color.argb(0, 255, 255, 255));

        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            int alpha;

            if (Math.abs(verticalOffset) >= 248)
                alpha = 255;
            else
                alpha = 0;

            toolbar.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
        });
    }

    private String buildURL(String username, String location) {
        return "u/" + username + location;
    }

    void setupButton(Data userSubreddit, View view) {
        MaterialButton profileButton = view.findViewById(R.id.button_profile_button);
        MainActivity mainActivity = (MainActivity) requireActivity();

        if (mainActivity.viewingSelf) {
            profileButton.setText(USER_UI_BUTTON_EDIT);
            return;
        }

        if (userSubreddit.getUserIsSubscriber())
            profileButton.setText(USER_UI_BUTTON_UNFOLLOW);
        else
            profileButton.setText(USER_UI_BUTTON_FOLLOW);
    }

    int getFinalAction(Data userSubreddit) {
        if (userSubreddit.getUserIsSubscriber())
            return ACTION_UNSUBSCRIBE;
        else
            return ACTION_SUBSCRIBE;
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

    private void setUpNavigation(View view) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);
//        toolbar.inflateMenu(R.menu.sorting);
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        if (mainActivity.viewingSelf) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.userFragment);
            item.setChecked(true);
            NavigationUI.setupWithNavController(toolbar, navController, mainActivity.appBarConfiguration);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
            NavigationUI.setupWithNavController(toolbar, navController);
        }
    }
}
