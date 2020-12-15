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
import android.widget.TextView;

import com.jaredrummler.cyanea.Cyanea;

import org.apache.commons.text.StringEscapeUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.db.TokenDao;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;
import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.linkify.LinkifyPlugin;

@AndroidEntryPoint
public class WikiFragment extends Fragment {

    @Inject
    TokenDao mTokenDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wiki, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpNavigation(view);
        setUpContent(view);
    }

    void setUpContent(View view) {
        SubredditViewModelFactory factory = InjectorUtils.getInstance(mTokenDao).provideSubredditViewModelFactory();
        SubredditViewModel viewModel = new ViewModelProvider(this, factory).get(SubredditViewModel.class);

        String contentText = "This looks empty";
        TextView content = view.findViewById(R.id.text_wiki_body);
        content.setText(contentText);

        String subreddit = ((MainActivity) requireActivity()).getFoxSharedViewModel().getCurrentSubreddit();

        viewModel.getSubredditWiki(subreddit, requireActivity().getApplication()).observe(getViewLifecycleOwner(), subredditInfo ->
        {
            Markwon markwon = Markwon.builder(requireContext())
                    .usePlugin(TablePlugin.create(requireContext()))
                    .usePlugin(LinkifyPlugin.create())
                    .build();

            if (subredditInfo.getWikiContent() != null && !subredditInfo.getWikiContent().isEmpty()) {
                markwon.setMarkdown(content, StringEscapeUtils.unescapeXml(subredditInfo.getWikiContent()));
            }
        });
    }

    private void setUpNavigation(View view) {
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_wiki);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());
        NavigationUI.setupWithNavController(toolbar, navController);
    }
}
