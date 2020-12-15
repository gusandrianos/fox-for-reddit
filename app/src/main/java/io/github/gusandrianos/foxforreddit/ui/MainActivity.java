package io.github.gusandrianos.foxforreddit.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaredrummler.cyanea.Cyanea;
import com.jaredrummler.cyanea.app.CyaneaAppCompatActivity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.repositories.TokenRepository;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.viewmodels.FoxSharedViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;

@AndroidEntryPoint
public class MainActivity extends CyaneaAppCompatActivity implements
        BottomNavigationView.OnNavigationItemReselectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    public Token mToken;
    private NavController navController;
    public AppBarConfiguration appBarConfiguration;
    public BottomNavigationView bottomNavView;
    List<Integer> topLevelDestinationIds;
    @Inject
    TokenRepository mTokenRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topLevelDestinationIds = Arrays.asList(R.id.mainFragment, R.id.userFragment, R.id.subredditListFragment, R.id.inboxFragment);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(new HashSet<>(topLevelDestinationIds)).build();

        bottomNavView = findViewById(R.id.bottom_nav_view);
        bottomNavView.setBackgroundColor(Cyanea.getInstance().getBackgroundColor());
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
            mToken = mTokenRepository.getToken();
            MenuItem bottomNavMenuItem = bottomNavView.getMenu().findItem(R.id.userFragment);
            bottomNavMenuItem.setEnabled(true);
        }
        if (FoxToolkit.INSTANCE.isAuthorized(mTokenRepository))
            getCurrentUser();
    }

    public void getCurrentUser() {
        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        viewModel.getMe().observe(this, user -> {
            if (user != null) {
                String username = user.getName();
                if (username != null) {
                    getFoxSharedViewModel().setCurrentUserUsername(user.getName());
                    MenuItem bottomNavMenuItem = bottomNavView.getMenu().findItem(R.id.userFragment);
                    bottomNavMenuItem.setEnabled(true);
                }
            }
            //TODO: Handle this by showing appropriate error
        });

        viewModel.getPrefs().observe(this, prefs ->
                getFoxSharedViewModel().setIncludeOver18(prefs.getSearchIncludeOver18()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public FoxSharedViewModel getFoxSharedViewModel() {
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_graph);
        return new ViewModelProvider(backStackEntry).get(FoxSharedViewModel.class);
    }

    public void logInOnReddit() {
        Intent load = new Intent(this, LoginActivity.class);
        load.putExtra("URL", constructOAuthURL());
        startActivityForResult(load, Constants.LAUNCH_SECOND_ACTIVITY);
    }

    private String constructOAuthURL() {
        return Constants.BASE_OAUTH_URL +
                "?client_id=" + Constants.CLIENT_ID +
                "&response_type=code&state=" + Constants.STATE +
                "&redirect_uri=" + Constants.REDIRECT_URI + "&duration=permanent&scope=*";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = Objects.requireNonNull(data).getStringExtra("result");
                String[] inputs = result.split("\\?")[1].split("&");
                String state = inputs[0].split("=")[1];
                String error = inputs[1].split("=")[0];

                // When an error is thrown, there is a line with the following formatting
                // "error=[some error]"
                //When all goes well, there is the line "code=[something]"
                if (state.equals(Constants.STATE) && error.equals("code")) {
                    String code = inputs[1].split("=")[1];
                    mToken = mTokenRepository.getNewToken(code, Constants.REDIRECT_URI);
                } else
                    Toast.makeText(this, "Log In unsuccessful", Toast.LENGTH_SHORT).show();

                setAuthorizedUI();
            }
        }
        MenuItem bottomNavMenuItem = bottomNavView.getMenu().findItem(getFoxSharedViewModel().getPreviousDestination());
        bottomNavMenuItem.setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mainFragment) {
            NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.mainFragment, true).build();
            navController.navigate(R.id.mainFragment, null, options);
            return true;
        } else if (id == R.id.userFragment) {
            if (mToken != null && FoxToolkit.INSTANCE.isAuthorized(mTokenRepository)) {
                NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(getFoxSharedViewModel().getCurrentUserUsername());
                navController.navigate(action);
            } else
                FoxToolkit.INSTANCE.promptLogIn(this);
            return true;
        } else if (id == R.id.subredditListFragment) {
            navController.navigate(R.id.subredditListFragment);
            return true;
        } else if (id == R.id.composeChooserFragment) {
            if (mToken != null && FoxToolkit.INSTANCE.isAuthorized(mTokenRepository)) {
                String postTo = "Reddit";
                if (navController.getCurrentDestination().getId() == R.id.subredditFragment) {
                    postTo = getFoxSharedViewModel().getCurrentSubreddit();
                } else {
                    getFoxSharedViewModel().clearComposeData();
                }
                NavGraphDirections.ActionGlobalComposeChooserFragment action = NavGraphDirections.actionGlobalComposeChooserFragment(postTo);
                getFoxSharedViewModel().setPreviousDestination(bottomNavView.getSelectedItemId());
                navController.navigate(action);
            } else
                FoxToolkit.INSTANCE.promptLogIn(this);
            return true;
        } else if (id == R.id.inboxFragment) {
            if (mToken != null && FoxToolkit.INSTANCE.isAuthorized(mTokenRepository))
                navController.navigate(R.id.inboxFragment);
            else
                FoxToolkit.INSTANCE.promptLogIn(this);
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