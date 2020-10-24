package io.github.gusandrianos.foxforreddit.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.ui.fragments.MainFragmentDirections;
import io.github.gusandrianos.foxforreddit.ui.fragments.SinglePostFragmentDirections;
import io.github.gusandrianos.foxforreddit.ui.fragments.SubredditListFragmentDirections;
import io.github.gusandrianos.foxforreddit.ui.fragments.UserFragmentDirections;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

import static io.github.gusandrianos.foxforreddit.Constants.BASE_OAUTH_URL;
import static io.github.gusandrianos.foxforreddit.Constants.CLIENT_ID;
import static io.github.gusandrianos.foxforreddit.Constants.LAUNCH_SECOND_ACTIVITY;
import static io.github.gusandrianos.foxforreddit.Constants.REDIRECT_URI;
import static io.github.gusandrianos.foxforreddit.Constants.STATE;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Data mUser;
    public String currentUserUsername;
    public boolean viewingSelf = false;
    private Token mToken;
    private NavController navController;
    public AppBarConfiguration appBarConfiguration;
    NavigationView navigationView;
    NavOptions options;
    List<Integer> topLevelDestinationIds;
    int itemSelectedID = 0;
    public DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        topLevelDestinationIds = Arrays.asList(R.id.mainFragment, R.id.userFragment, R.id.subredditListFragment);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        drawer = findViewById(R.id.drawer_layout);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.mainFragment, R.id.userFragment, R.id.subredditListFragment).setOpenableLayout(drawer).build();

        navigationView = findViewById(R.id.nav_view);

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.mainFragment);
        }

        navigationView.setNavigationItemSelectedListener(this);
        drawer.addDrawerListener(new DrawerListener());

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() != R.id.userFragment) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                viewingSelf = false;
                navigationView.setCheckedItem(destination.getId());
            } else {
                navigationView.setCheckedItem(destination.getId());
            }
        });

        setAuthorizedUI();
    }

    private void setAuthorizedUI() {
        if (mToken == null) {
            mToken = InjectorUtils.getInstance().provideTokenRepository().getToken(getApplication());
        }
        if (mToken.getRefreshToken() != null) {
            getCurrentUser();
            navigationView.getMenu().getItem(1).setVisible(true);
            navigationView.getMenu().getItem(3).getSubMenu().getItem(0).setVisible(false);
        }
    }

    private void getCurrentUser() {
        UserViewModelFactory factory = InjectorUtils.getInstance().provideUserViewModelFactory();
        UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
        viewModel.getMe(getApplication()).observe(this, user -> {
            if (user != null) {
                String username = user.getName();
                if (username != null) {
                    mUser = user;
                    currentUserUsername = user.getName();
                }
            }
            //TODO: Handle this by showing appropriate error
        });
    }

    boolean isValidDestination(int dest_id) {
        if (!viewingSelf && dest_id == R.id.userFragment)
            return true;
        return dest_id != Objects.requireNonNull(Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination()).getId();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawer) || super.onSupportNavigateUp();
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mainFragment) {
            itemSelectedID = id;
            drawer.close();
            return true;
        } else if (id == R.id.userFragment) {
            itemSelectedID = id;
            drawer.close();
            return true;
        } else if (id == R.id.subredditListFragment) {
            itemSelectedID = id;
            drawer.close();
            return true;
        } else if (id == R.id.nav_login) {
            itemSelectedID = id;
            drawer.close();
            return true;
        }
        itemSelectedID = -1;
        return false;
    }

    private class DrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
            if (itemSelectedID != -1) {
                if (itemSelectedID == R.id.mainFragment) {
                    if (isValidDestination(itemSelectedID)) {
                        options = new NavOptions.Builder().setPopUpTo(R.id.mainFragment, true).build();
                        navController.navigate(R.id.mainFragment, null, options);
                    }
                    itemSelectedID = -1;
                } else if (itemSelectedID == R.id.userFragment) {
                    if (isValidDestination(itemSelectedID)) {
                        int currentDestinationID = Objects.requireNonNull(navController.getCurrentDestination()).getId();
                        viewingSelf = true;

                        if (currentDestinationID == R.id.mainFragment) {
                            MainFragmentDirections.ActionMainFragmentToUserFragment action = MainFragmentDirections.actionMainFragmentToUserFragment(mUser, "");
                            navController.navigate(action);
                        } else if (currentDestinationID == R.id.subredditListFragment) {
                            SubredditListFragmentDirections.ActionSubredditListFragmentToUserFragment action = SubredditListFragmentDirections.actionSubredditListFragmentToUserFragment(mUser, "");
                            navController.navigate(action);
                        } else if (currentDestinationID == R.id.userFragment) {
                            UserFragmentDirections.ActionUserFragmentSelf action = UserFragmentDirections.actionUserFragmentSelf(mUser, "");
                            navController.navigate(action);
                        } else if (currentDestinationID == R.id.singlePostFragment) {
                            SinglePostFragmentDirections.ActionSinglePostFragmentToUserFragment action = SinglePostFragmentDirections.actionSinglePostFragmentToUserFragment(mUser, "");
                            navController.navigate(action);
                        }
                    }
                    itemSelectedID = -1;
                } else if (itemSelectedID == R.id.subredditListFragment) {
                    if (isValidDestination(itemSelectedID))
                        navController.navigate(R.id.subredditListFragment);
                    itemSelectedID = -1;
                } else if (itemSelectedID == R.id.nav_login) {
                    loadLogInWebpage();
                    itemSelectedID = -1;
                }
            }
        }

        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    }
}