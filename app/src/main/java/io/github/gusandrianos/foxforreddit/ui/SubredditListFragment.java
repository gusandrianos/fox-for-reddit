package io.github.gusandrianos.foxforreddit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.UserViewModelFactory;

public class SubredditListFragment extends Fragment {

    SubredditListAdapter mSubredditListAdapter;
    RecyclerView mSubredditsRV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subreddit_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycleView(view);
        initializeUI();
    }

    private void initializeUI() {
        UserViewModelFactory factory = InjectorUtils.getInstance().provideUserViewModelFactory(getActivity().getApplication());
        UserViewModel viewModel = new ViewModelProvider(this, factory).get(UserViewModel.class);
        viewModel.getSubreddits(getActivity().getApplication(), "subscriber").observe(getViewLifecycleOwner(), subredditPagingData -> {
            mSubredditListAdapter.submitData(getViewLifecycleOwner().getLifecycle(), subredditPagingData);
        });
    }

    private void initRecycleView(View view) {
        mSubredditsRV = view.findViewById(R.id.subreddit_list_recycler);
        mSubredditListAdapter = new SubredditListAdapter();
        mSubredditListAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY); //keep recyclerview on position
        mSubredditsRV.setHasFixedSize(true);
        mSubredditsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSubredditsRV.setAdapter(mSubredditListAdapter);
    }
}
