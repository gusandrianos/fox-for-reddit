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

public class PopUpMoreActionsDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_actions_more_popup,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txtPopupSave = view.findViewById(R.id.popup_save);
        TextView txtPopupHide = view.findViewById(R.id.popup_hide);
        TextView txtPopupReport = view.findViewById(R.id.popup_report);
        TextView txtPopupBlockUser = view.findViewById(R.id.popup_block_account);

        txtPopupSave.setOnClickListener(view1 -> Toast.makeText(getContext(), "Save Pressed", Toast.LENGTH_SHORT).show());
        txtPopupHide.setOnClickListener(view1 -> Toast.makeText(getContext(), "Hide Pressed", Toast.LENGTH_SHORT).show());
        txtPopupReport.setOnClickListener(view1 -> Toast.makeText(getContext(), "Report Pressed", Toast.LENGTH_SHORT).show());
        txtPopupBlockUser.setOnClickListener(view1 -> Toast.makeText(getContext(), "Block Pressed", Toast.LENGTH_SHORT).show());
    }
}
