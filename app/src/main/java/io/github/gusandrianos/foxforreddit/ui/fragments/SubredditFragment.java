package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;

import static io.github.gusandrianos.foxforreddit.Constants.ACTION_SUBSCRIBE;
import static io.github.gusandrianos.foxforreddit.Constants.ACTION_UNSUBSCRIBE;
import static io.github.gusandrianos.foxforreddit.Constants.SUBREDDIT_POST_FRAGMENT_TAG;

public class SubredditFragment extends Fragment {
    FoxToolkit toolkit = FoxToolkit.INSTANCE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subreddit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SubredditFragmentArgs args = SubredditFragmentArgs.fromBundle(requireArguments());
        String subredditName = args.getSubredditName();

        TextView titleTextView = view.findViewById(R.id.text_subreddit_title);
        titleTextView.setText(subredditName);
        setUpNavigation(view, subredditName);

        SubredditViewModelFactory factory = InjectorUtils.getInstance().provideSubredditViewModelFactory();
        SubredditViewModel viewModel = new ViewModelProvider(this, factory).get(SubredditViewModel.class);

        viewModel.getSubreddit(subredditName, requireActivity().getApplication()).observe(getViewLifecycleOwner(), subredditInfo ->
        {
            setupHeader(subredditInfo, view);
            PostFragment subredditPostFragment = (PostFragment) getChildFragmentManager().findFragmentByTag(SUBREDDIT_POST_FRAGMENT_TAG);

            if (subredditPostFragment == null) {
                subredditPostFragment = PostFragment.newInstance(subredditName, "", "");
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.subreddit_posts_fragment,
                                subredditPostFragment,
                                SUBREDDIT_POST_FRAGMENT_TAG)
                        .commitNow();
            }
        });
    }

    void setupHeader(Data subredditInfo, View view) {
        setupImages(subredditInfo, view);
        setupUserCounters(subredditInfo, view);
        MaterialButton subUnsubButton = view.findViewById(R.id.button_subreddit_sub_unsub);

        setupButton(subredditInfo, view);
        if (FoxToolkit.INSTANCE.isAuthorized(requireActivity().getApplication()))
            subUnsubButton.setOnClickListener(button -> {
                SubredditViewModelFactory factory = InjectorUtils.getInstance().provideSubredditViewModelFactory();
                SubredditViewModel viewModel = new ViewModelProvider(this, factory).get(SubredditViewModel.class);
                viewModel.toggleSubscribed(getFinalAction(subredditInfo),
                        subredditInfo.getDisplayName(),
                        requireActivity().getApplication())
                        .observe(getViewLifecycleOwner(), status -> {
                            if (status) {
                                subredditInfo.setUserIsSubscriber(!subredditInfo.getUserIsSubscriber());
                                setupButton(subredditInfo, view);
                            }
                        });
            });
        else
            subUnsubButton.setOnClickListener(button -> FoxToolkit.INSTANCE.promptLogIn((MainActivity) requireActivity()));

        AppBarLayout appBarLayout = view.findViewById(R.id.fragment_subreddit_appbar);
        Toolbar toolbar = view.findViewById(R.id.subreddit_toolbar);
        toolbar.setBackgroundColor(Color.argb(0, 255, 255, 255));

        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            int alpha;

            if (Math.abs(verticalOffset) >= 200)
                alpha = 255;
            else
                alpha = 0;

            toolbar.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
        });
    }

    void setupButton(Data subredditInfo, View view) {
        MaterialButton subUnsubButton = view.findViewById(R.id.button_subreddit_sub_unsub);
        if (subredditInfo.getUserIsSubscriber())
            subUnsubButton.setIconResource(R.drawable.unsubscribe_subreddit);
        else
            subUnsubButton.setIconResource(R.drawable.subscribe_subreddit);
    }

    int getFinalAction(Data subredditInfo) {
        if (subredditInfo.getUserIsSubscriber())
            return ACTION_UNSUBSCRIBE;
        else
            return ACTION_SUBSCRIBE;
    }

    void setupUserCounters(Data subredditInfo, View view) {
        TextView members = view.findViewById(R.id.text_member_count);
        TextView online = view.findViewById(R.id.text_users_online_count);

        members.setText(NumberFormat.getInstance().format(subredditInfo.getSubscribers()));
        if (subredditInfo.getAccountsActive() != null)
            online.setText(NumberFormat.getInstance().format(subredditInfo.getAccountsActive()));
        else if (subredditInfo.getActiveUserCount() != null)
            online.setText(NumberFormat.getInstance().format(subredditInfo.getActiveUserCount()));
        else {
            TextView divider = view.findViewById(R.id.text_count_divider);
            TextView onlineDescription = view.findViewById(R.id.text_users_online_description);
            divider.setVisibility(View.GONE);
            online.setVisibility(View.GONE);
            onlineDescription.setVisibility(View.GONE);
        }
    }

    void setupImages(Data subredditInfo, View view) {
        ImageView pic = view.findViewById(R.id.subreddit_picture);
        ImageView cover = view.findViewById(R.id.subreddit_cover);
        ImageView gradient = view.findViewById(R.id.subreddit_cover_gradient);

        String iconImageURI = subredditInfo.getIconImg();
        String communityIconURI = subredditInfo.getCommunityIcon();
        String bannerImageURI = subredditInfo.getBannerImg();
        String bannerBackgroundImageURI = subredditInfo.getBannerBackgroundImage();

        iconImageURI = iconImageURI == null ? "" : iconImageURI;
        communityIconURI = communityIconURI == null ? "" : communityIconURI;
        bannerImageURI = bannerImageURI == null ? "" : bannerImageURI;
        bannerBackgroundImageURI = bannerBackgroundImageURI == null ? "" : bannerBackgroundImageURI;

        if (!iconImageURI.isEmpty())
            Glide.with(view).load(toolkit.getRawImageURI(iconImageURI)).into(pic);
        else if (!communityIconURI.isEmpty())
            Glide.with(view).load(toolkit.getRawImageURI(communityIconURI)).into(pic);
        else
            pic.setImageResource(R.drawable.default_subreddit_image);

        if (!bannerImageURI.isEmpty())
            Glide.with(view).load(toolkit.getRawImageURI(bannerImageURI)).into(cover);
        else if (!bannerBackgroundImageURI.isEmpty())
            Glide.with(view).load(toolkit.getRawImageURI(bannerBackgroundImageURI)).into(cover);
        else
            cover.setImageResource(0);

        Glide.with(view).load(R.drawable.cover_gradient).into(gradient);
    }

    private void setUpNavigation(View view, String subreddit) {
        CollapsingToolbarLayout collapsingToolbar = requireActivity().findViewById(R.id.subreddit_collapsing_toolbar);
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = view.findViewById(R.id.subreddit_toolbar);
        toolbar.inflateMenu(R.menu.sorting_and_search_bar);

        MenuItem searchItem = toolbar.getMenu().getItem(0);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search in " + subreddit);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                navController.navigate(SubredditFragmentDirections.actionSubredditFragmentToSubredditSearchResultsFragment(subreddit, query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.VISIBLE);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.subredditListFragment);
        item.setChecked(true);
        NavigationUI.setupWithNavController(collapsingToolbar, toolbar, navController, mainActivity.appBarConfiguration);
    }
}
