package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
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
import com.jaredrummler.cyanea.Cyanea;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.db.TokenDao;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.Subreddit;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.ViewPagerAdapter;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.FoxSharedViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

import io.github.gusandrianos.foxforreddit.Constants;

@AndroidEntryPoint
public class UserFragment extends Fragment {
    @Inject
    TokenDao mTokenDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserFragmentArgs args = UserFragmentArgs.fromBundle(requireArguments());

        String username = args.getUsername();

        MainActivity mainActivity = (MainActivity) requireActivity();
        FoxSharedViewModel sharedViewModel = mainActivity.getFoxSharedViewModel();

        if (sharedViewModel.getCurrentUserUsername().isEmpty() && FoxToolkit.INSTANCE.isAuthorized(mTokenDao)) {
            UserViewModelFactory factory = InjectorUtils.getInstance(mTokenDao).provideUserViewModelFactory();
            UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);

            viewModel.getMe(requireActivity().getApplication()).observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    String currentUserUsername = user.getName();
                    if (currentUserUsername != null) {
                        sharedViewModel.setCurrentUserUsername(currentUserUsername);
                        initializeUI(sharedViewModel, username, view);
                    }
                }
                //TODO: Handle this by showing appropriate error
            });
        } else
            initializeUI(sharedViewModel, username, view);
    }

    private void initializeUI(FoxSharedViewModel sharedViewModel, String username, View view) {
        UserViewModelFactory factory = InjectorUtils.getInstance(mTokenDao).provideUserViewModelFactory();
        UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
        sharedViewModel.setViewingSelf(username.equals(sharedViewModel.getCurrentUserUsername()));

        setUpNavigation(view);

        if (!username.isEmpty()) {
            viewModel.getUser(requireActivity().getApplication(), username)
                    .observe(getViewLifecycleOwner(), data -> buildUserProfile(data, view));

            setUpContent(username, view, username.equals(sharedViewModel.getCurrentUserUsername()));
        }
    }

    private void setUpContent(String username, View view, boolean isSelf) {
        ViewPager2 viewPager = view.findViewById(R.id.profile_view_pager);
        TabLayout tabLayout = view.findViewById(R.id.profile_tab_layout);
        tabLayout.setBackgroundColor(Cyanea.getInstance().getBackgroundColor());

        if (isSelf)
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        else
            tabLayout.setTabMode(TabLayout.MODE_FIXED);

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

        ArrayList<Fragment> userFragments = new ArrayList<>();
        ArrayList<String> tabTitles = new ArrayList<>();

        userFragments.add(PostFragment.newInstance(buildURL(username, "/submitted"), "", ""));
        tabTitles.add(Constants.USER_UI_TAB_POSTS);
        userFragments.add(PostFragment.newInstance(buildURL(username, "/comments"), "", ""));
        tabTitles.add(Constants.USER_UI_TAB_COMMENTS);
        userFragments.add(AboutUserFragment.newInstance(username));
        tabTitles.add(Constants.USER_UI_TAB_ABOUT);

        if (isSelf) {
            userFragments.add(PostFragment.newInstance(buildURL(username, "/upvoted"), "", ""));
            tabTitles.add(Constants.USER_UI_TAB_UPVOTED);
            userFragments.add(PostFragment.newInstance(buildURL(username, "/downvoted"), "", ""));
            tabTitles.add(Constants.USER_UI_TAB_DOWNVOTED);
            userFragments.add(PostFragment.newInstance(buildURL(username, "/hidden"), "", ""));
            tabTitles.add(Constants.USER_UI_TAB_HIDDEN);
            userFragments.add(PostFragment.newInstance(buildURL(username, "/saved"), "", ""));
            tabTitles.add(Constants.USER_UI_TAB_SAVED);
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(userFragments, tabTitles, this);
        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getFragmentTitle(position))
        ).attach();
    }

    private void buildUserProfile(Data user, View view) {
        setUpUserNames(view, user, user.getName());

        ImageView profilePic = view.findViewById(R.id.profile_picture);
        ImageView coverPic = view.findViewById(R.id.profile_cover);
        ImageView gradient = view.findViewById(R.id.profile_cover_gradient);

        Glide.with(view).load(Objects.requireNonNull(user.getIconImg()).split("\\?")[0]).into(profilePic);

        Type subredditType = new TypeToken<Subreddit>() {
        }.getType();
        Gson gson = new Gson();
        Subreddit subreddit = gson.fromJson(gson.toJsonTree(user.getSubreddit()).getAsJsonObject(), subredditType);

        Glide.with(view).load(Objects.requireNonNull(subreddit.getBannerImg()).split("\\?")[0]).into(coverPic);
        Glide.with(view).load(R.drawable.cover_gradient).into(gradient);

        Data userSubreddit = getUserSubreddit(user);

        MaterialButton profileButton = view.findViewById(R.id.button_profile_button);

        setupButton(userSubreddit, view);
        MainActivity mainActivity = (MainActivity) requireActivity();
        if (FoxToolkit.INSTANCE.isAuthorized(mTokenDao))
            profileButton.setOnClickListener(button -> {
                if (!mainActivity.getFoxSharedViewModel().getViewingSelf()) {
                    SubredditViewModel viewModel = new ViewModelProvider(this).get(SubredditViewModel.class);
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
                    customTabsIntent.launchUrl(requireContext(), Uri.parse(Constants.EDIT_PROFILE_URL));
                }
            });
        else
            profileButton.setOnClickListener(button -> FoxToolkit.INSTANCE.promptLogIn(mainActivity));

        AppBarLayout appBarLayout = view.findViewById(R.id.fragment_profile_appbar);
        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);

        int color = Cyanea.getInstance().getPrimary();

        int colorRed = Color.red(color);
        int colorGreen = Color.green(color);
        int colorBlue = Color.blue(color);

        toolbar.setBackgroundColor(Color.argb(0, colorRed, colorGreen, colorBlue));

        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            int alpha;

            if (Math.abs(verticalOffset) >= 248)
                alpha = 255;
            else
                alpha = 0;

            toolbar.setBackgroundColor(Color.argb(alpha, colorRed, colorGreen, colorBlue));
        });

        NavController navController = NavHostFragment.findNavController(this);

        if (FoxToolkit.INSTANCE.isAuthorized(mTokenDao))
            setUpMenu(mainActivity.getFoxSharedViewModel().getViewingSelf(), toolbar, mainActivity, navController, user);
    }

    private String buildURL(String username, String location) {
        return "u/" + username + location;
    }

    void setupButton(Data userSubreddit, View view) {
        MaterialButton profileButton = view.findViewById(R.id.button_profile_button);
        MainActivity mainActivity = (MainActivity) requireActivity();
        profileButton.setBackgroundColor(Cyanea.getInstance().getAccent());

        if (mainActivity.getFoxSharedViewModel().getViewingSelf()) {
            profileButton.setText(Constants.USER_UI_BUTTON_EDIT);
            return;
        }

        if (userSubreddit.getUserIsSubscriber())
            profileButton.setText(Constants.USER_UI_BUTTON_UNFOLLOW);
        else
            profileButton.setText(Constants.USER_UI_BUTTON_FOLLOW);
    }

    int getFinalAction(Data userSubreddit) {
        if (userSubreddit.getUserIsSubscriber())
            return Constants.ACTION_UNSUBSCRIBE;
        else
            return Constants.ACTION_SUBSCRIBE;
    }

    void setUpUserNames(@NotNull View view, Data user, String username) {
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

    private void setUpMenu(boolean isSelf, Toolbar toolbar, MainActivity mainActivity, NavController navController, Data user) {
        toolbar.getMenu().findItem(R.id.log_out).setVisible(isSelf);
        toolbar.getMenu().findItem(R.id.message_user).setVisible(!isSelf);
        toolbar.getMenu().findItem(R.id.block_user).setVisible(!isSelf);

        if (isSelf) {
            toolbar.getMenu().findItem(R.id.log_out).setOnMenuItemClickListener(menuItem -> {
                InjectorUtils.getInstance(mTokenDao).provideTokenRepository().logOut();
                mainActivity.mToken = null;
                navigateHome(navController);
                return true;
            });
        } else {
            toolbar.getMenu().findItem(R.id.message_user).setOnMenuItemClickListener(menuItem -> {
                navController.navigate(ComposeMessageFragmentDirections.actionGlobalComposeMessageFragment(user.getName()));
                return true;
            });

            // TODO: Show appropriate error when blocking someone and trying to load their profile again
            toolbar.getMenu().findItem(R.id.block_user).setOnMenuItemClickListener(menuItem -> {
                UserViewModelFactory factory = InjectorUtils.getInstance(mTokenDao).provideUserViewModelFactory();
                UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
                viewModel.blockUser(requireActivity().getApplication(), user.getId(), user.getName()).observe(getViewLifecycleOwner(), status -> {
                    Toast.makeText(requireContext(), "User " + user.getName() + " successfully blocked", Toast.LENGTH_SHORT).show();
                    navigateHome(navController);
                });
                return true;
            });
        }
    }

    private void navigateHome(NavController navController) {
        NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.mainFragment, true).build();
        navController.navigate(R.id.mainFragment, null, options);
    }

    private void setUpNavigation(View view) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        CollapsingToolbarLayout profileCollapsingToolbar = view.findViewById(R.id.profile_collapsing_toolbar);
        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);
        profileCollapsingToolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());
        profileCollapsingToolbar.setContentScrimColor(Cyanea.getInstance().getPrimary());
        toolbar.inflateMenu(R.menu.user_options);

        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;

        if (mainActivity.getFoxSharedViewModel().getViewingSelf()) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationView.getMenu().findItem(R.id.userFragment).setChecked(true);
            NavigationUI.setupWithNavController(toolbar, navController, mainActivity.appBarConfiguration);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
            NavigationUI.setupWithNavController(toolbar, navController);
        }
    }
}
