package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

public class ComposeReplyToUserMessageFragment extends Fragment {

    String thing_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_user_to_reply_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ComposeReplyToUserMessageFragmentArgs composeReplyToUserMessageFragmentArgs = ComposeReplyToUserMessageFragmentArgs.fromBundle(requireArguments());
        thing_id = composeReplyToUserMessageFragmentArgs.getThingId();
        setUpNavigation(view);
    }

    private void setUpNavigation(View view) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = view.findViewById(R.id.compose_reply_to_user_message_toolbar);
        toolbar.inflateMenu(R.menu.button_send_message);

        MenuItem sendItem = toolbar.getMenu().findItem(R.id.send_message);
        sendItem.setOnMenuItemClickListener(item -> checkField(view));
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);
        NavigationUI.setupWithNavController(toolbar, navController);
    }

    private boolean checkField(View view) {
        TextInputEditText messageTextField = view.findViewById(R.id.edit_compose_reply_to_user_message_field);
        TextInputLayout messageTextInput = view.findViewById(R.id.input_compose_reply_to_user_message_field);

        boolean flag = false;

        if (messageTextField.getText() != null && messageTextField.getText().toString().isEmpty()) {
            messageTextInput.setError("Message is required.");
            messageTextInput.setErrorEnabled(true);
            flag = true;
        } else
            messageTextInput.setErrorEnabled(false);

        if (flag)
            return false;

        String text = messageTextField.getText().toString();
        Bundle result = new Bundle();

        UserViewModelFactory factory = InjectorUtils.getInstance().provideUserViewModelFactory();
        UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
        viewModel.commentCompose(requireActivity().getApplication(), thing_id, text).observe(getViewLifecycleOwner(), success -> {

            if (success)
                Toast.makeText(getContext(), "Reply has been sent.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "That didn't work...", Toast.LENGTH_SHORT).show();

            result.putBoolean(Constants.MESSAGE_SENT_SUCCESS, success);
            getParentFragmentManager().setFragmentResult(Constants.MESSAGE_REQUEST_KEY, result);
            requireActivity().onBackPressed();
        });

        return true;
    }
}
