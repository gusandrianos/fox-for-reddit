package io.github.gusandrianos.foxforreddit.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.FoxSharedViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

import static io.github.gusandrianos.foxforreddit.Constants.BASE_OAUTH_URL;
import static io.github.gusandrianos.foxforreddit.Constants.CLIENT_ID;
import static io.github.gusandrianos.foxforreddit.Constants.LAUNCH_SECOND_ACTIVITY;
import static io.github.gusandrianos.foxforreddit.Constants.REDIRECT_URI;
import static io.github.gusandrianos.foxforreddit.Constants.STATE;


public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemReselectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {
    private Data mUser;
    private Token mToken;
    private NavController navController;
    public AppBarConfiguration appBarConfiguration;
    public BottomNavigationView bottomNavView;
    NavOptions options;
    List<Integer> topLevelDestinationIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topLevelDestinationIds = Arrays.asList(R.id.mainFragment, R.id.userFragment, R.id.subredditListFragment);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        List<Integer> navID = new ArrayList<>();
        navID.add(R.id.mainFragment);
        navID.add(R.id.userFragment);
        navID.add(R.id.subredditListFragment);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.mainFragment, R.id.userFragment, R.id.subredditListFragment).build();

        bottomNavView = findViewById(R.id.bottom_nav_view);
        bottomNavView.setOnNavigationItemSelectedListener(this);
        bottomNavView.setOnNavigationItemReselectedListener(this);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() != R.id.userFragment) {
                getFoxSharedViewModel().setViewingSelf(false);
            }
        });

        setAuthorizedUI();
    }

    private void setAuthorizedUI() {
        if (mToken == null) {
            mToken = InjectorUtils.getInstance().provideTokenRepository().getToken(getApplication());
            MenuItem bottomNavMenuItem = bottomNavView.getMenu().findItem(R.id.userFragment);
            bottomNavMenuItem.setEnabled(true);
        }
        if (mToken.getRefreshToken() != null)
            getCurrentUser();
    }

    private void getCurrentUser() {
        UserViewModelFactory factory = InjectorUtils.getInstance().provideUserViewModelFactory();
        UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
        viewModel.getMe(getApplication()).observe(this, user -> {
            if (user != null) {
                String username = user.getName();
                if (username != null) {
                    mUser = user;
                    getFoxSharedViewModel().setCurrentUserUsername(user.getName());
                    MenuItem bottomNavMenuItem = bottomNavView.getMenu().findItem(R.id.userFragment);
                    bottomNavMenuItem.setEnabled(true);
                }
            }
            //TODO: Handle this by showing appropriate error
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public FoxSharedViewModel getFoxSharedViewModel() {
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph);
        return new ViewModelProvider(backStackEntry).get(FoxSharedViewModel.class);
    }

    public void loadLogInWebpage() {
        Intent load = new Intent(this, LoginActivity.class);
        load.putExtra("URL", constructOAuthURL());
        startActivityForResult(load, LAUNCH_SECOND_ACTIVITY);
    }

    private String constructOAuthURL() {
        return BASE_OAUTH_URL + "?client_id=" + CLIENT_ID + "&response_type=code&state=" + STATE + "&redirect_uri=" + REDIRECT_URI + "&duration=permanent&scope=*";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = Objects.requireNonNull(data).getStringExtra("result");
                String[] inputs = result.split("\\?")[1].split("&");
                String state = inputs[0].split("=")[1];
                if (state.equals(STATE)) {
                    String code = inputs[1].split("=")[1];
                    mToken = InjectorUtils.getInstance().provideTokenRepository().getNewToken(getApplication(), code, REDIRECT_URI);
                }
                setAuthorizedUI();
            }
        }
        MenuItem bottomNavMenuItem = bottomNavView.getMenu().findItem(getFoxSharedViewModel().getDestinationBeforeLoginAttempt());
        bottomNavMenuItem.setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mainFragment) {
            options = new NavOptions.Builder().setPopUpTo(R.id.mainFragment, true).build();
            navController.navigate(R.id.mainFragment, null, options);
            return true;
        } else if (id == R.id.userFragment) {
            if (mToken.getRefreshToken() != null && !mToken.getRefreshToken().isEmpty()) {
                NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(mUser, "");
                navController.navigate(action);
            } else
                FoxToolkit.INSTANCE.promptLogIn(this);
            return true;
        } else if (id == R.id.subredditListFragment) {
            navController.navigate(R.id.subredditListFragment);
            return true;
        } else if (id == R.id.composeChooserFragment) {
            String subreddit = "";
            if (navController.getCurrentDestination().getId() == R.id.subredditFragment) {
                subreddit = getFoxSharedViewModel().getCurrentSubreddit();
            }
            NavGraphDirections.ActionGlobalComposeChooserFragment action = NavGraphDirections.actionGlobalComposeChooserFragment(subreddit);
            getFoxSharedViewModel().setDestinationBeforeLoginAttempt(bottomNavView.getSelectedItemId());
            navController.navigate(action);
            return true;
        } else if (id == R.id.messagesFragment) {
            navController.navigate(R.id.messagesFragment);
            return true;
        }
        return false;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        int id = item.getItemId();
        int currentItemID = Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId();
        if (id == R.id.subredditListFragment && id != currentItemID) {
            navController.navigate(R.id.subredditListFragment);
        }
    }
}