package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.jaredrummler.cyanea.Cyanea;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.db.TokenDao;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;

@AndroidEntryPoint
public class EditThingFragment extends Fragment {
    @Inject
    TokenDao mTokenDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_thing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpNavigation(view, 0);
        setUpContent(view);
    }

    private void setUpContent(View view) {
        String selftext = EditThingFragmentArgs.fromBundle(requireArguments()).getThingText();
        TextInputEditText bodyEditText = view.findViewById(R.id.edit_edit_thing_text_body);
        bodyEditText.setText(selftext);
        setUpSubmitAction(view);
    }

    private void setUpSubmitAction(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_edit_thing);
        toolbar.getMenu().findItem(R.id.button_submit_post).setOnMenuItemClickListener(submit -> {
            String selftext = EditThingFragmentArgs.fromBundle(requireArguments()).getThingText();
            String thingFullname = EditThingFragmentArgs.fromBundle(requireArguments()).getThingFullname();
            TextInputEditText bodyEditText = view.findViewById(R.id.edit_edit_thing_text_body);

            PostViewModel viewModel = new ViewModelProvider(this).get(PostViewModel.class);

            viewModel.editSubmission(bodyEditText.getText().toString(), thingFullname, requireActivity().getApplication())
                    .observe(getViewLifecycleOwner(), success -> {
                        if (success) {
                            Bundle result = new Bundle();
                            result.putString("updatedText", bodyEditText.getText().toString());
                            getParentFragmentManager().setFragmentResult("editThing", result);
                            requireActivity().onBackPressed();
                        }
                    });
            return true;
        });
    }

    private void setUpNavigation(View view, int type) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_edit_thing);
        toolbar.inflateMenu(R.menu.button_submit_post);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());

        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);
        NavigationUI.setupWithNavController(toolbar, navController);

        String toolbarTitle = "Edit";
        if (type == Constants.EDIT_POST_TEXT)
            toolbarTitle += " post";
        else
            toolbarTitle += " Comment";

        toolbar.setTitle(toolbarTitle);
    }
}
