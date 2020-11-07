package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;


public class ComposeChooserFragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_chooser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        composeOptionsListeners(view);
    }

    void composeOptionsListeners(View view) {
        MaterialButton textOption = view.findViewById(R.id.button_compose_text);
        MaterialButton imageOption = view.findViewById(R.id.button_compose_image);
        MaterialButton linkOption = view.findViewById(R.id.button_compose_link);
        MaterialButton videoOption = view.findViewById(R.id.button_compose_video);

        textOption.setOnClickListener(view1 -> compose(Constants.COMPOSE_TEXT));
        imageOption.setOnClickListener(view2 -> compose(Constants.COMPOSE_IMAGE));
        linkOption.setOnClickListener(view3 -> compose(Constants.COMPOSE_LINK));
        videoOption.setOnClickListener(view4 -> compose(Constants.COMPOSE_VIDEO));
    }

    void compose(int postType) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        String subreddit = ComposeChooserFragmentArgs.fromBundle(requireArguments()).getSubreddit();

        ComposeChooserFragmentDirections.ActionComposeChooserFragmentToComposeFragment action = ComposeChooserFragmentDirections.actionComposeChooserFragmentToComposeFragment(postType, subreddit);
        navController.navigate(action);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        MainActivity mainActivity = (MainActivity) requireActivity();
        MenuItem bottomNavMenuItem = mainActivity.bottomNavView.getMenu().findItem(mainActivity.getFoxSharedViewModel().getDestinationBeforeLoginAttempt());
        bottomNavMenuItem.setChecked(true);
    }
}