package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jaredrummler.cyanea.Cyanea;

import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.NextStepReasonsItem;
import io.github.gusandrianos.foxforreddit.data.models.RulesBundle;
import io.github.gusandrianos.foxforreddit.data.models.RulesItem;
import io.github.gusandrianos.foxforreddit.data.models.SiteRulesFlowItem;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;

@AndroidEntryPoint
public class ReportDialogFragment extends BottomSheetDialogFragment {

    RulesBundle rulesBundle;
    String reason;
    int rulesToShow;
    String subredditName;
    String thingId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button submit = view.findViewById(R.id.btn_submit_report);
        submit.setBackgroundColor(Cyanea.getInstance().getAccent());
        Button backButton = view.findViewById(R.id.btn_back_report);
        backButton.setBackgroundColor(Cyanea.getInstance().getAccent());

        ReportDialogFragmentArgs reportDialogFragmentArgs = ReportDialogFragmentArgs.fromBundle(requireArguments());
        rulesBundle = reportDialogFragmentArgs.getRulesBundle();
        reason = reportDialogFragmentArgs.getReason();
        rulesToShow = reportDialogFragmentArgs.getRulesToShow();
        subredditName = reportDialogFragmentArgs.getSubredditName();
        thingId = reportDialogFragmentArgs.getThingId();


        setUpBackButton(reason, view);
        deActivateSubmitButton(view);
        if (rulesToShow == Constants.SUBREDDIT_RULES)
            setUpSubredditRules(view);
        else
            setUpRedditRules(view);
    }

    private void setUpSubredditRules(View view) {
        List<String> reasonToShowList = new ArrayList<>();
        List<Boolean> hasNextList = new ArrayList<>();
        List<String> reasonList = new ArrayList<>();

        if (rulesBundle.getRules() != null) {
            for (RulesItem rulesItem : rulesBundle.getRules()) {
                reasonToShowList.add(StringEscapeUtils.unescapeXml(rulesItem.getShortName()));
                hasNextList.add(false);
                reasonList.add(StringEscapeUtils.unescapeXml(rulesItem.getViolationReason()));
            }
            setUpReport("Which rule has been broken?", reasonToShowList, reasonList, hasNextList, view);
        }
    }

    private void setUpRedditRules(View view) {
        List<String> reasonToShowList = new ArrayList<>();
        List<Boolean> hasNextList = new ArrayList<>();
        List<String> reasonList = new ArrayList<>();

        if (rulesToShow == Constants.ALL_RULES && rulesBundle.getRules() != null && !rulesBundle.getRules().isEmpty()) {
            String subredditRules = "It breaks " + subredditName + " rules";
            reasonToShowList.add(subredditRules);
            hasNextList.add(true);
            reasonList.add(Constants.NAVIGATE_TO_SUBREDDIT);
        }

        if (reason == null) {
            for (SiteRulesFlowItem siteRulesFlowItem : rulesBundle.getSiteRulesFlow()) {
                if (siteRulesFlowItem.getNextStepReasons() == null) {
                    reasonToShowList.add(siteRulesFlowItem.getReasonTextToShow());
                    hasNextList.add(false);
                    reasonList.add(siteRulesFlowItem.getReasonText());
                } else {
                    reasonToShowList.add(siteRulesFlowItem.getReasonTextToShow());
                    hasNextList.add(true);
                    reasonList.add(siteRulesFlowItem.getReasonText());
                }
            }
            setUpReport(Constants.REPORT_POST, reasonToShowList, reasonList, hasNextList, view);
        } else {
            for (SiteRulesFlowItem siteRulesFlowItem : rulesBundle.getSiteRulesFlow()) {
                if (siteRulesFlowItem.getReasonTextToShow().equals(reason)) {
                    for (NextStepReasonsItem nextStepReasonsItem : siteRulesFlowItem.getNextStepReasons()) {
                        if (nextStepReasonsItem.getNextStepReasons() == null) {
                            reasonToShowList.add(nextStepReasonsItem.getReasonTextToShow());
                            hasNextList.add(false);
                            reasonList.add(nextStepReasonsItem.getReasonText());
                        } else {
                            reasonToShowList.add(nextStepReasonsItem.getReasonTextToShow());
                            hasNextList.add(true);
                            reasonList.add(nextStepReasonsItem.getReasonText());
                        }
                    }
                    setUpReport(siteRulesFlowItem.getNextStepHeader(), reasonToShowList, reasonList, hasNextList, view);
                } else {
                    if (siteRulesFlowItem.getNextStepReasons() != null)
                        for (NextStepReasonsItem nextStepReasonsItem : siteRulesFlowItem.getNextStepReasons())
                            findChild(nextStepReasonsItem, reason, reasonToShowList, reasonList, hasNextList, view);
                }
            }
        }
    }

    private void setUpReport(String header, List<String> reasonToShowList, List<String> reasonList, List<Boolean> hasNextList, View view) {
        TextView txtHeader = view.findViewById(R.id.txt_report_header);
        txtHeader.setText(header);

        RadioGroup radioGroup = view.findViewById(R.id.radio_group_report);
        for (int i = 0; i < reasonToShowList.size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setId(i);
            radioButton.setText(reasonToShowList.get(i));
            radioButton.setTag(R.string.report_has_next, hasNextList.get(i));
            radioButton.setTag(R.string.report_reason, reasonList.get(i));
            radioGroup.addView(radioButton);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = (RadioButton) group.getChildAt(checkedId);
            if ((boolean) radioButton.getTag(R.string.report_has_next)) {
                deActivateSubmitButton(view);
                if (radioButton.getTag(R.string.report_reason).equals(Constants.NAVIGATE_TO_SUBREDDIT))
                    navigateToSubredditRules(radioButton.getTag(R.string.report_reason).toString());
                else
                    navigateToRedditRules(radioButton.getText().toString());
            } else
                activateSubmitButton(radioButton.getTag(R.string.report_reason).toString(), view);
        });
    }

    private void findChild(NextStepReasonsItem nextStepReasonsItem, String reason, List<String> reasonToShowList, List<String> reasonList, List<Boolean> hasNextList, View view) {
        if (nextStepReasonsItem.getNextStepReasons() != null) {
            if (nextStepReasonsItem.getReasonTextToShow().equals(reason)) {
                for (NextStepReasonsItem item : nextStepReasonsItem.getNextStepReasons()) {
                    if (item.getNextStepReasons() == null) {
                        reasonToShowList.add(item.getReasonTextToShow());
                        hasNextList.add(false);
                        reasonList.add(item.getReasonText());
                    } else {
                        reasonToShowList.add(item.getReasonTextToShow());
                        hasNextList.add(true);
                        reasonList.add(item.getReasonText());
                    }
                }
                setUpReport(nextStepReasonsItem.getNextStepHeader(), reasonToShowList, reasonList, hasNextList, view);
            } else {
                for (NextStepReasonsItem item : nextStepReasonsItem.getNextStepReasons())
                    findChild(item, reason, reasonToShowList, reasonList, hasNextList, view);
            }
        }
    }


    private void activateSubmitButton(String reason, View view) {
        Button submit = view.findViewById(R.id.btn_submit_report);
        submit.setAlpha(1f);
        submit.setClickable(true);

        PostViewModel viewModel = new ViewModelProvider(this).get(PostViewModel.class);

        submit.setOnClickListener(v -> viewModel.reportPost(thingId, reason).observe(getViewLifecycleOwner(), succeed -> {
            String message = (succeed) ? "Report has been sent" : "Failed to report";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            dismiss();
        }));
    }

    private void deActivateSubmitButton(View view) {
        Button submit = view.findViewById(R.id.btn_submit_report);
        submit.setAlpha(0.5f);
        submit.setClickable(false);
    }

    private void setUpBackButton(String reason, View view) {
        Button backButton = view.findViewById(R.id.btn_back_report);
        backButton.setOnClickListener(v -> navigateBack(reason));
    }

    private void navigateToRedditRules(String choice) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        navController.navigate(ReportDialogFragmentDirections.actionReportDialogFragmentSelf(rulesBundle, choice, Constants.REDDIT_RULES, subredditName, thingId));
    }


    private void navigateToSubredditRules(String choice) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        navController.navigate(ReportDialogFragmentDirections.actionReportDialogFragmentSelf(rulesBundle, choice, Constants.SUBREDDIT_RULES, subredditName, thingId));
    }

    private void navigateBack(String reason) {
        if (reason == null)
            dismiss();
        else {
            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
            navController.navigate(ReportDialogFragmentDirections.actionReportDialogFragmentSelf(rulesBundle, null, Constants.ALL_RULES, subredditName, thingId));
        }
    }
}
