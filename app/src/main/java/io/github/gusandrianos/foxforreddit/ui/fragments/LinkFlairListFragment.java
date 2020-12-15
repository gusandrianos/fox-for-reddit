package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jaredrummler.cyanea.Cyanea;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Flair;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.LinkFlairListAdapter;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;

@AndroidEntryPoint
public class LinkFlairListFragment extends Fragment implements LinkFlairListAdapter.OnItemClickListener {
    SubredditViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_link_flair_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SubredditViewModel.class);
        String subredditName = LinkFlairListFragmentArgs.fromBundle(requireArguments()).getSubredditName();
        setUpNavigation(view);
        setUpRecyclerView(view, subredditName);
    }

    void setUpRecyclerView(View view, String subredditName) {

        viewModel.getSubredditLinkFlair(subredditName).observe(getViewLifecycleOwner(), linkFlair -> {
            if (linkFlair != null) {
                RecyclerView linkFlairRV = view.findViewById(R.id.recycler_link_flairs);
                LinkFlairListAdapter adapter = new LinkFlairListAdapter(linkFlair, this);
                adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
                linkFlairRV.setHasFixedSize(true);
                linkFlairRV.setLayoutManager(new LinearLayoutManager(requireActivity()));
                linkFlairRV.setAdapter(adapter);
            }
        });
    }

    private void setCurrentFlair(Flair flair) {
        int mode = LinkFlairListFragmentArgs.fromBundle(requireArguments()).getMode();
        MainActivity mainActivity = (MainActivity) requireActivity();
        if (mode == 0)
            mainActivity.getFoxSharedViewModel().setCurrentFlair(flair);
        else {
            Bundle result = new Bundle();
            result.putParcelable("flair", flair);
            getParentFragmentManager().setFragmentResult("flairChoice", result);
        }
        requireActivity().onBackPressed();
    }

    @Override
    public void onItemClick(@NotNull Flair flair) {
        setCurrentFlair(flair);
    }

    void setUpNavigation(View view) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_link_flairs);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());

        toolbar.inflateMenu(R.menu.button_clear_link_flair);
        toolbar.getMenu().findItem(R.id.button_clear_link_flair).setOnMenuItemClickListener(clear -> {
            int mode = LinkFlairListFragmentArgs.fromBundle(requireArguments()).getMode();

            if (mode == 0)
                setCurrentFlair(null);
            else {
                setCurrentFlair(new Flair());
            }

            return true;
        });

        NavigationUI.setupWithNavController(toolbar, navController);
    }
}
