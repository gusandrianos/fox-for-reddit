package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;

public class PopUpMoreActionsDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_actions_more_popup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PopUpMoreActionsDialogFragmentArgs popUpMoreActionsDialogFragmentArgs = PopUpMoreActionsDialogFragmentArgs.fromBundle(requireArguments());
        Data data = popUpMoreActionsDialogFragmentArgs.getData();

        TextView txtPopupSave = view.findViewById(R.id.popup_save);
        TextView txtPopupHide = view.findViewById(R.id.popup_hide);
        TextView txtPopupReport = view.findViewById(R.id.popup_report);

        if (data.isSaved()) {
            txtPopupSave.setText("Unsave");
            txtPopupSave.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_round_bookmark_24, 0, 0, 0);
        } else {
            txtPopupSave.setText("Save");
            txtPopupSave.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_round_bookmark_border_24, 0, 0, 0);
        }
        if (data.getHidden()) {
            txtPopupHide.setText("Unhide");
            txtPopupHide.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_round_visibility_off_24, 0, 0, 0);
        } else {
            txtPopupHide.setText("Hide");
            txtPopupHide.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_round_visibility_24, 0, 0, 0);
        }

        txtPopupSave.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Save Pressed", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        txtPopupHide.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Hide Pressed", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        txtPopupReport.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "Report Pressed", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }
}
