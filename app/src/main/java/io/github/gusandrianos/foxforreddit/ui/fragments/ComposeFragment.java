package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.viewmodels.FoxSharedViewModel;

import io.github.gusandrianos.foxforreddit.Constants;

public class ComposeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText subredditTextField = view.findViewById(R.id.edit_compose_subredddit_field);
        FoxSharedViewModel foxViewModel = ((MainActivity) requireActivity()).getFoxSharedViewModel();
        int composeType = ComposeFragmentArgs.fromBundle(requireArguments()).getComposeType();

        setUpNavigation(view, composeType);
        setUpBodyStub(view, composeType);

        foxViewModel.getSubredditChoice().observe(getViewLifecycleOwner(), subredditTextField::setText);

        MaterialButton chooseSubredditButton = view.findViewById(R.id.button_compose_choose_subreddit);
        chooseSubredditButton.setOnClickListener(view1 -> {
            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
            navController.navigate(ComposeFragmentDirections.actionComposeFragmentToSubredditListFragment());
        });
    }

    private void setUpBodyStub(View view, int type) {
        ViewStub stub = view.findViewById(R.id.stub_compose_body);

        if (type == Constants.COMPOSE_TEXT) {
            stub.setLayoutResource(R.layout.stub_compose_text);
        } else if (type == Constants.COMPOSE_IMAGE) {
            stub = view.findViewById(R.id.stub_compose_body);
            stub.setLayoutResource(R.layout.stub_compose_image);
        } else if (type == Constants.COMPOSE_LINK) {
            stub = view.findViewById(R.id.stub_compose_body);
            stub.setLayoutResource(R.layout.stub_compose_link);
        } else {
            stub = view.findViewById(R.id.stub_compose_body);
            stub.setLayoutResource(R.layout.stub_compose_video);
        }

        View inflated = stub.inflate();
    }

    private void setUpNavigation(View view, int type) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_compose);
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);
        NavigationUI.setupWithNavController(toolbar, navController);

        String toolbarTitle = "New";
        if (type == Constants.COMPOSE_TEXT)
            toolbarTitle += " text ";
        else if (type == Constants.COMPOSE_IMAGE)
            toolbarTitle += " image ";
        else if (type == Constants.COMPOSE_LINK)
            toolbarTitle += " link ";
        else
            toolbarTitle += " video ";
        toolbarTitle += "post";

        toolbar.setTitle(toolbarTitle);
    }
}
