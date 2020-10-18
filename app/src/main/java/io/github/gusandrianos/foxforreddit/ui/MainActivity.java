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
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static Data mUser;
    int LAUNCH_SECOND_ACTIVITY = 1;
    Token mToken;
    private NavController navController;
    AppBarConfiguration appBarConfiguration;
    NavigationView navigationView;
    NavOptions options;
    List<Integer> topLevelDestinationIds;
    int itemSelectedID = 0;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        topLevelDestinationIds = Arrays.asList(R.id.mainFragment, R.id.userFragment);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        drawer = findViewById(R.id.drawer_layout);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.mainFragment, R.id.userFragment).setOpenableLayout(drawer).build();

        navigationView = findViewById(R.id.nav_view);

        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.mainFragment);
        }

        navigationView.setNavigationItemSelectedListener(this);
        drawer.addDrawerListener(new DrawerListener());

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (topLevelDestinationIds.contains(destination.getId())) {
                navigationView.setCheckedItem(destination.getId());
            }
        });

        setAuthorizedUI();
    }

    private void setAuthorizedUI() {
        if (mToken == null) {
            mToken = InjectorUtils.getInstance().provideTokenRepository(getApplication()).getToken();
        }
        if (mToken.getRefreshToken() != null) {
            getCurrentUser();
            navigationView.getMenu().getItem(1).setVisible(true);
            navigationView.getMenu().getItem(3).getSubMenu().getItem(0).setVisible(false);
        }
    }

    private void getCurrentUser() {
        UserViewModelFactory factory = InjectorUtils.getInstance().provideUserViewModelFactory(getApplication());
        UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
        viewModel.getMe().observe(this, user -> {
            if (user != null) {
                String username = user.getName();
                if (username != null)
                    mUser = user;
            }
            //TODO: Handle this by showing appropriate error
        });
    }

    boolean isValidDestination(int dest_id) {
        return dest_id != Objects.requireNonNull(Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination()).getId();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration);
    }

    // TODO: Update navigation to make this work again
    public void loadLogInWebpage() {
        Intent load = new Intent(this, LoginActivity.class);
        load.putExtra("URL", "https://www.reddit.com/api/v1/authorize.compact?client_id=n1R0bc_lPPTtVg&response_type=code&state=ggfgfgfgga&redirect_uri=https://gusandrianos.github.io/login&duration=permanent&scope=*");
        startActivityForResult(load, LAUNCH_SECOND_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = Objects.requireNonNull(data).getStringExtra("result");
                String[] inputs = result.split("\\?")[1].split("&");
                String code = inputs[1].split("=")[1];
                mToken = InjectorUtils.getInstance().provideTokenRepository(getApplication()).getNewToken(code, "https://gusandrianos.github.io/login");
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
                    if (isValidDestination(itemSelectedID))
                        navController.navigate(R.id.userFragment);
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