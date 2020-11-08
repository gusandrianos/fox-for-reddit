package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.viewmodels.FoxSharedViewModel;

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

        subredditTextField.setText(foxViewModel.getCurrentSubreddit());
        foxViewModel.getSubredditChoice().observe(getViewLifecycleOwner(), subredditTextField::setText);

        MaterialButton chooseSubredditButton = view.findViewById(R.id.button_compose_choose_subreddit);
        chooseSubredditButton.setOnClickListener(view1 -> {
            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
            navController.navigate(ComposeFragmentDirections.actionComposeFragmentToSubredditListFragment());
        });
    }
}