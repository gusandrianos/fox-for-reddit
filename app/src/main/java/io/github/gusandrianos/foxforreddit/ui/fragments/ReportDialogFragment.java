package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;

public class ReportDialogFragment extends BottomSheetDialogFragment {

    String prevChoice;
    String reason;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ReportDialogFragmentArgs reportDialogFragmentArgs = ReportDialogFragmentArgs.fromBundle(requireArguments());
        prevChoice = reportDialogFragmentArgs.getPrevChoice();
        reason = reportDialogFragmentArgs.getReason();

        deActivateSubmitButton(view);
        setUpBackButton(view);
        setUpChoices(prevChoice, view);
    }

    private void setUpBackButton(View view) {
        Button backButton = view.findViewById(R.id.btn_back_report);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBackTo();
            }
        });
    }

    private void setUpChoices(String prevChoice, View view) {
        List<String> choices;
        switch (prevChoice) {
            case Constants.REPORT_REASON:
                choices = Arrays.asList(Constants.REPORT_COMMUNITY_RULES
                        , Constants.REPORT_SPAM
                        , Constants.REPORT_MISINFORMATION
                        , Constants.REPORT_ABUSIVE_OR_HARASSING
                        , Constants.REPORT_OTHER_ISSUES);
                setUpRadioGroup(choices, view);
                break;
            case Constants.REPORT_COMMUNITY_RULES:
                choices = Arrays.asList("1", "2", "3");
                setUpRadioGroup(choices, view);
                break;
            case Constants.REPORT_SPAM:
                break;
            case Constants.REPORT_MISINFORMATION:
                break;
            case Constants.REPORT_ABUSIVE_OR_HARASSING:
                choices = Arrays.asList(Constants.REPORT_TARGETED_HARASSMENT
                        , Constants.REPORT_VIOLENCE_OR_HARM
                        , Constants.REPORT_PROMOTING_HATE
                        , Constants.REPORT_RUDENESS
                        , Constants.REPORT_ABUSING_REPORT);
                setUpRadioGroup(choices, view);
                break;
            case Constants.REPORT_OTHER_ISSUES:
                choices = Arrays.asList(Constants.REPORT_INFRINGES_COPYRIGHT
                        , Constants.REPORT_INFRINGES_TRADEMARK_RIGHTS
                        , Constants.REPORT_PERSONAL_OR_CONFIDENTIAL
                        , Constants.REPORT_SEXUAL_OR_SUGGESTIVE
                        , Constants.REPORT_INVOLUNTARY_PORNOGRAPHY
                        , Constants.REPORT_TRANSACTION
                        , Constants.REPORT_UNDER_NETZDG
                        , Constants.REPORT_SELF_HARM);
                setUpRadioGroup(choices, view);
                break;
            case Constants.REPORT_TARGETED_HARASSMENT:
            case Constants.REPORT_VIOLENCE_OR_HARM:
                choices = Arrays.asList(Constants.REPORT_AT_ME
                        , Constants.REPORT_AT_SOMEONE_ELSE);
                setUpRadioGroup(choices, view);
                break;
            case Constants.REPORT_PROMOTING_HATE:
                break;
            case Constants.REPORT_RUDENESS:
                break;
            case Constants.REPORT_ABUSING_REPORT:
                break;
            case Constants.REPORT_INFRINGES_COPYRIGHT:
                break;
            case Constants.REPORT_INFRINGES_TRADEMARK_RIGHTS:
                break;
            case Constants.REPORT_PERSONAL_OR_CONFIDENTIAL:
                break;
            case Constants.REPORT_SEXUAL_OR_SUGGESTIVE:
                break;
            case Constants.REPORT_INVOLUNTARY_PORNOGRAPHY:
                choices = Arrays.asList(Constants.REPORT_IMAGE_OF_ME
                        , Constants.REPORT_DO_NOT_APPEAR_IN_IMAGE);
                setUpRadioGroup(choices, view);
                break;
            case Constants.REPORT_TRANSACTION:
                break;
            case Constants.REPORT_UNDER_NETZDG:
                break;
            case Constants.REPORT_SELF_HARM:
                break;
            default:
                break;
        }
    }

    private void setUpRadioGroup(List<String> choices, View view) {
        RadioGroup radioGroup = view.findViewById(R.id.radio_group_report);

        int i = 0;
        for (String choice : choices) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(choice);
            radioButton.setId(i);
            radioGroup.addView(radioButton);
            i++;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = (RadioButton) group.getChildAt(checkedId);
                if (radioButton.getText().equals(Constants.REPORT_OTHER_ISSUES)) {
                    deActivateSubmitButton(view);
                    navigateTo(Constants.REPORT_OTHER_ISSUES);
                } else if (radioButton.getText().equals(Constants.REPORT_ABUSIVE_OR_HARASSING)) {
                    deActivateSubmitButton(view);
                    navigateTo(Constants.REPORT_ABUSIVE_OR_HARASSING);
                } else if (radioButton.getText().equals(Constants.REPORT_COMMUNITY_RULES)) {
                    deActivateSubmitButton(view);
                    navigateTo(Constants.REPORT_COMMUNITY_RULES);
                } else if (radioButton.getText().equals(Constants.REPORT_TARGETED_HARASSMENT)) {
                    deActivateSubmitButton(view);
                    navigateTo(Constants.REPORT_TARGETED_HARASSMENT);
                } else if (radioButton.getText().equals(Constants.REPORT_VIOLENCE_OR_HARM)) {
                    deActivateSubmitButton(view);
                    navigateTo(Constants.REPORT_VIOLENCE_OR_HARM);
                } else if (radioButton.getText().equals(Constants.REPORT_INVOLUNTARY_PORNOGRAPHY)) {
                    deActivateSubmitButton(view);
                    navigateTo(Constants.REPORT_INVOLUNTARY_PORNOGRAPHY);
                } else {
                    activateSubmitButton(view, radioButton.getText().toString());
                }
            }
        });
    }

    private void activateSubmitButton(View view, String addReason) {
        Button submit = view.findViewById(R.id.btn_submit_report);
        submit.setAlpha(1f);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), reason + addReason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deActivateSubmitButton(View view) {
        Button submit = view.findViewById(R.id.btn_submit_report);
        submit.setAlpha(0.5f);
        submit.setClickable(false);
    }

    private void navigateTo(String choice) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        navController.navigate(ReportDialogFragmentDirections.actionReportDialogFragmentSelf(choice, (reason + choice)));
    }

    private void navigateBackTo() {
        String parent = findParent(prevChoice);
        if (parent.equals(Constants.REPORT_DISMISS))
            dismiss();
        else {
            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
            navController.navigate(ReportDialogFragmentDirections.actionReportDialogFragmentSelf(parent, reason.replace(prevChoice, "")));

        }
    }

    private String findParent(String prevChoice) {
        switch (prevChoice) {
            case Constants.REPORT_COMMUNITY_RULES:
            case Constants.REPORT_ABUSIVE_OR_HARASSING:
            case Constants.REPORT_OTHER_ISSUES:
                return Constants.REPORT_REASON;
            case Constants.REPORT_TARGETED_HARASSMENT:
            case Constants.REPORT_VIOLENCE_OR_HARM:
                return Constants.REPORT_ABUSIVE_OR_HARASSING;
            case Constants.REPORT_INVOLUNTARY_PORNOGRAPHY:
                return Constants.REPORT_OTHER_ISSUES;
            default:
                return Constants.REPORT_DISMISS;
        }
    }
}
