package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.libRG.CustomTextView;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Flair;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.FoxSharedViewModel;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;

public class ComposeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText subredditTextField = view.findViewById(R.id.edit_compose_subreddit_field);
        TextInputLayout subredditTextInput = view.findViewById(R.id.input_compose_subredddit_field);
        subredditTextInput.setErrorIconDrawable(null);
        CustomTextView flairChooser = view.findViewById(R.id.custom_text_link_flair);

        FoxSharedViewModel foxViewModel = ((MainActivity) requireActivity()).getFoxSharedViewModel();
        int composeType = ComposeFragmentArgs.fromBundle(requireArguments()).getComposeType();

        setUpNavigation(view, composeType);
        setUpBodyStub(view, composeType);

        foxViewModel.getSubredditChoice().observe(getViewLifecycleOwner(), subredditTextField::setText);
        foxViewModel.getFlairChoice().observe(getViewLifecycleOwner(), flair -> {
            if (flair != null)
                FoxToolkit.INSTANCE.makeFlair(flair.getType(), flair.getRichtext(), flair.getText(),
                        flair.getTextColor(), flair.getBackgroundColor(), flairChooser);
        });

        subredditTextField.setOnClickListener(subredditText -> {
            NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment);
            NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
            navController.navigate(ComposeFragmentDirections.actionComposeFragmentToSubredditListFragment());
        });

        MaterialButton subredditRules = view.findViewById(R.id.button_compose_subreddit_rules);
        subredditRules.setOnClickListener(view1 -> {
            if (subredditTextField.getText() != null && !subredditTextField.getText().toString().isEmpty()) {
                NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
                NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
                navController.navigate(ComposeFragmentDirections.actionComposeFragmentToRulesFragment(subredditTextField.getText().toString()));
            } else
                subredditTextInput.setError("Please choose a subreddit first");
        });

        CustomTextView nsfw = view.findViewById(R.id.custom_text_nsfw);
        CustomTextView spoiler = view.findViewById(R.id.custom_text_spoiler);

        flairChooser.setOnClickListener(listener -> {
            if (subredditTextField.getText() != null && !subredditTextField.getText().toString().isEmpty()) {
                NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
                NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
                if (subredditTextField.getText() != null)
                    navController.navigate(ComposeFragmentDirections.actionComposeFragmentToLinkFlairListFragment(subredditTextField.getText().toString()));
            } else
                subredditTextInput.setError("Please choose a subreddit first");
        });

        MainActivity mainActivity = (MainActivity) requireActivity();

        // This is needed to survive rotation changes
        setUpNSFW(mainActivity, nsfw);
        setUpSpoiler(mainActivity, spoiler);

        nsfw.setOnClickListener(nsfwToggle -> {
            boolean isNSFW = mainActivity.getFoxSharedViewModel().isNSFW();
            isNSFW = !isNSFW;
            mainActivity.getFoxSharedViewModel().setNSFW(isNSFW);
            setUpNSFW(mainActivity, nsfw);
        });

        spoiler.setOnClickListener(spoilerToggle -> {
            boolean isSpoiler = mainActivity.getFoxSharedViewModel().isSpoiler();
            isSpoiler = !isSpoiler;
            mainActivity.getFoxSharedViewModel().setSpoiler(isSpoiler);
            setUpSpoiler(mainActivity, spoiler);
        });
    }

    private void setUpNSFW(MainActivity mainActivity, CustomTextView nsfw) {
        boolean isNSFW = mainActivity.getFoxSharedViewModel().isNSFW();
        if (isNSFW) {
            nsfw.setBorderColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
            nsfw.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
        } else {
            nsfw.setBorderColor(ContextCompat.getColor(requireContext(), android.R.color.tab_indicator_text));
            nsfw.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.tab_indicator_text));
        }
    }

    private void setUpSpoiler(MainActivity mainActivity, CustomTextView spoiler) {
        boolean isSpoiler = mainActivity.getFoxSharedViewModel().isSpoiler();
        if (isSpoiler) {
            spoiler.setBorderColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark));
            spoiler.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark));
        } else {
            spoiler.setBorderColor(ContextCompat.getColor(requireContext(), android.R.color.tab_indicator_text));
            spoiler.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.tab_indicator_text));
        }
    }

    private void setUpBodyStub(View view, int type) {
        ViewStub stub = view.findViewById(R.id.stub_compose_body);
        Toolbar toolbar = view.findViewById(R.id.toolbar_compose);

        if (type == Constants.COMPOSE_TEXT)
            stub.setLayoutResource(R.layout.stub_compose_text);
        else
            stub.setLayoutResource(R.layout.stub_compose_link);

        View inflated = stub.inflate();

        TextInputEditText titleTextField = view.findViewById(R.id.edit_compose_title_field);
        TextInputLayout titleTextInput = view.findViewById(R.id.input_compose_title_field);

        TextInputEditText subredditTextField = view.findViewById(R.id.edit_compose_subreddit_field);
        TextInputLayout subredditTextInput = view.findViewById(R.id.input_compose_subredddit_field);

        titleTextInput.setErrorIconDrawable(null);

        toolbar.getMenu().findItem(R.id.button_submit_post).setOnMenuItemClickListener(menuItem -> {
            boolean titlePresent = titleTextField.getText() != null && !titleTextField.getText().toString().isEmpty();
            boolean subredditPresent = subredditTextField.getText() != null && !subredditTextField.getText().toString().isEmpty();
            if (titlePresent && subredditPresent)
                postAction(view, inflated, type);
            else {
                if (!titlePresent)
                    titleTextInput.setError("Title cannot be empty");
                if (!subredditPresent)
                    subredditTextInput.setError("Please choose a subreddit first");
            }
            return true;
        });
    }

    private boolean postAction(View view, View inflated, int composeType) {
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        TextInputEditText subredditTextField = view.findViewById(R.id.edit_compose_subreddit_field);
        TextInputEditText titleTextField = view.findViewById(R.id.edit_compose_title_field);
        MainActivity mainActivity = (MainActivity) requireActivity();

        String subreddit = subredditTextField.getText().toString().trim();
        String title = titleTextField.getText().toString().trim();
        String text = "";
        String url = "";
        String type = "self";
        boolean isNSFW = mainActivity.getFoxSharedViewModel().isNSFW();
        boolean isSpoiler = mainActivity.getFoxSharedViewModel().isSpoiler();
        String flairId = "";
        String flairText = "";
        Flair flair = mainActivity.getFoxSharedViewModel().getCurrentFlair();

        if (flair != null) {
            flairId = flair.getId();
            flairText = flair.getText();
        }

        if (composeType == Constants.COMPOSE_TEXT) {
            TextInputEditText textBody = inflated.findViewById(R.id.edit_compose_text_body);
            text = textBody.getText().toString().trim();
        } else {
            TextInputEditText linkBody = inflated.findViewById(R.id.edit_compose_link_body);
            url = linkBody.getText().toString().trim();
            type = "link";
        }


        viewModel.submitText(type, subreddit, title, url, text, isNSFW, isSpoiler, flairId,
                flairText, requireActivity().getApplication()).observe(getViewLifecycleOwner(),
                posted -> {
                    if (posted.getJson().getErrors().isEmpty()) {
                        // TODO: SinglePost needs refactoring to support navigating to it from links, for now, navigating back.
                        Toast.makeText(requireContext(), "Posted to " + subredditTextField.getText().toString(), Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                    } else
                        Toast.makeText(requireContext(), "That didn't work", Toast.LENGTH_SHORT).show();
                });

        // TODO: https://www.reddit.com/dev/api/#GET_api_v1_{subreddit}_post_requirements
        /* TODO: In a land of myth and a time of magic, one could upload pictures and videos from storage
            but reddit decided not to make the API public and make you jump through hoops to do it yourself.
            Someday, we will implement this functionality.
         */
        return true;
    }

    private void setUpNavigation(View view, int type) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_compose);
        toolbar.inflateMenu(R.menu.button_submit_post);
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);
        NavigationUI.setupWithNavController(toolbar, navController);

        String toolbarTitle = "New";
        if (type == Constants.COMPOSE_TEXT)
            toolbarTitle += " text ";
        else
            toolbarTitle += " link ";
        toolbarTitle += "post";

        toolbar.setTitle(toolbarTitle);
    }
}
