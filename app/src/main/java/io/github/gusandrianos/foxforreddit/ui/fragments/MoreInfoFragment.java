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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;
import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.linkify.LinkifyPlugin;

public class MoreInfoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int mode = MoreInfoFragmentArgs.fromBundle(requireArguments()).getMode();
        setUpNavigation(view, mode);
        setUpContent(view, mode);
    }

    void setUpContent(View view, int mode) {
        SubredditViewModelFactory factory = InjectorUtils.getInstance().provideSubredditViewModelFactory();
        SubredditViewModel viewModel = new ViewModelProvider(this, factory).get(SubredditViewModel.class);

        String contentText = "This looks empty";
        TextView content = view.findViewById(R.id.text_more_info);
        content.setText(contentText);

        String subreddit = ((MainActivity) requireActivity()).getFoxSharedViewModel().getCurrentSubreddit();

        if (mode == Constants.MODE_SHOW_WIKI)
            viewModel.getSubredditWiki(subreddit, requireActivity().getApplication()).observe(getViewLifecycleOwner(), subredditInfo ->
            {
                Markwon markwon = Markwon.builder(requireContext())
                        .usePlugin(TablePlugin.create(requireContext()))
                        .usePlugin(LinkifyPlugin.create())
                        .build();

                if (subredditInfo.getWikiContent() != null && !subredditInfo.getWikiContent().isEmpty()) {
                    markwon.setMarkdown(content, subredditInfo.getWikiContent());
                }
            });
//        else if (mode == Constants.MODE_SHOW_RULES)
//            viewModel.getSubredditRules(subreddit, requireActivity().getApplication()).observe(getViewLifecycleOwner(), subredditInfo ->
//            {
//                TextView content = view.findViewById(R.id.text_more_info);
//                content.setText(subredditInfo.getWikiContent());
//            });
    }


    private void setUpNavigation(View view, int mode) {
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = view.findViewById(R.id.toolbar_more_info);
        if (mode == Constants.MODE_SHOW_WIKI)
            toolbar.setTitle("Wiki");
        else if (mode == Constants.MODE_SHOW_RULES)
            toolbar.setTitle("Rules");

        NavigationUI.setupWithNavController(toolbar, navController);
    }
}
