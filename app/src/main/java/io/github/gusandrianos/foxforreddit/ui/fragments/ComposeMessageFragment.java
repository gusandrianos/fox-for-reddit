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
import com.jaredrummler.cyanea.Cyanea;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;

@AndroidEntryPoint
public class ComposeMessageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ComposeMessageFragmentArgs composeMessageFragmentArgs = ComposeMessageFragmentArgs.fromBundle(requireArguments());
        String userToSent = composeMessageFragmentArgs.getSendToUser();

        if (userToSent != null) {
            TextInputEditText userTextField = view.findViewById(R.id.edit_message_compose_user_field);
            userTextField.setText(userToSent);
        }

        setUpNavigation(view);
    }

    private void setUpNavigation(View view) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = view.findViewById(R.id.compose_messages_toolbar);
        toolbar.inflateMenu(R.menu.button_send_message);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());

        MenuItem sendItem = toolbar.getMenu().findItem(R.id.send_message);
        sendItem.setOnMenuItemClickListener(item -> checkFields(view));
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);
        NavigationUI.setupWithNavController(toolbar, navController);
    }

    private boolean checkFields(View view) {
        TextInputEditText userTextField = view.findViewById(R.id.edit_message_compose_user_field);
        TextInputLayout userTextInput = view.findViewById(R.id.input_message_compose_user_field);
        TextInputEditText subjectTextField = view.findViewById(R.id.edit_message_compose_subject_field);
        TextInputLayout subjectTextInput = view.findViewById(R.id.input_message_compose_subject_field);
        TextInputEditText messageTextField = view.findViewById(R.id.edit_message_compose_message_field);
        TextInputLayout messageTextInput = view.findViewById(R.id.input_message_compose_message_field);

        userTextInput.setErrorIconDrawable(null);
        subjectTextInput.setErrorIconDrawable(null);
        messageTextInput.setErrorIconDrawable(null);

        boolean flag = false;

        if (userTextField.getText() != null && userTextField.getText().toString().isEmpty()) {
            userTextInput.setError("Username is required.");
            userTextInput.setErrorEnabled(true);
            flag = true;
        } else
            userTextInput.setErrorEnabled(false);

        if (subjectTextField.getText() != null && subjectTextField.getText().toString().isEmpty()) {
            subjectTextInput.setError("Subject is required.");
            userTextInput.setErrorEnabled(true);
            flag = true;
        } else
            subjectTextInput.setErrorEnabled(false);

        if (messageTextField.getText() != null && messageTextField.getText().toString().isEmpty()) {
            messageTextInput.setError("Message is required.");
            userTextInput.setErrorEnabled(true);
            flag = true;
        } else
            messageTextInput.setErrorEnabled(false);

        if (flag)
            return false;

        String toUser = "u/" + userTextField.getText().toString();
        String subject = subjectTextField.getText().toString();
        String text = messageTextField.getText().toString();

        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.messageCompose(toUser, subject, text).observe(getViewLifecycleOwner(), success -> {
            if (success == null)
                Toast.makeText(getContext(), "User does not exist.", Toast.LENGTH_SHORT).show();
            else if (success)
                Toast.makeText(getContext(), "Message has been sent.", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "That didn't work...", Toast.LENGTH_SHORT).show();

            requireActivity().onBackPressed();
        });

        return true;
    }
}
