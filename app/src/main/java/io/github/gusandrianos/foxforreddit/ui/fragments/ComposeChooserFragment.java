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
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;

/*
    Fragment used as a dialog for choosing what to post (link/text)
 */
public class ComposeChooserFragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_chooser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpPromptText(view);
        composeOptionsListeners(view);
    }

    void setUpPromptText(View view) {
        String postTo = ComposeChooserFragmentArgs.fromBundle(requireArguments()).getPostTo();
        TextView chooserPrompt = view.findViewById(R.id.text_compose_chooser_prompt);
        String promptText = "Post to " + postTo;
        chooserPrompt.setText(promptText);
    }

    void composeOptionsListeners(View view) {
        MaterialButton textOption = view.findViewById(R.id.button_compose_text);
        MaterialButton linkOption = view.findViewById(R.id.button_compose_link);

        textOption.setOnClickListener(view1 -> compose(Constants.COMPOSE_TEXT));
        linkOption.setOnClickListener(view3 -> compose(Constants.COMPOSE_LINK));
    }

    void compose(int postType) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                .getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

        ComposeChooserFragmentDirections.ActionComposeChooserFragmentToComposeFragment action =
                ComposeChooserFragmentDirections
                        .actionComposeChooserFragmentToComposeFragment(postType);
        navController.navigate(action);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        MainActivity mainActivity = (MainActivity) requireActivity();
        MenuItem bottomNavMenuItem = mainActivity.bottomNavView.getMenu()
                .findItem(mainActivity.getFoxSharedViewModel().getPreviousDestination());
        bottomNavMenuItem.setChecked(true);
    }
}