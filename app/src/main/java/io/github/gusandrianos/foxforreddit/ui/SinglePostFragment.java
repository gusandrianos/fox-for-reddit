package io.github.gusandrianos.foxforreddit.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;


import org.jetbrains.annotations.NotNull;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Token;

import io.github.gusandrianos.foxforreddit.data.models.singlepost.comments.ChildrenItem;
import io.github.gusandrianos.foxforreddit.data.models.singlepost.comments.Comments;
import io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren.MoreChildren;
import io.github.gusandrianos.foxforreddit.data.models.singlepost.morechildren.ThingsItem;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;

public class SinglePostFragment extends Fragment implements ExpandableCommentItem.OnItemClickListener {
    private RecyclerView mCommentsRecyclerView;
    private View mView;
    private GroupAdapter<GroupieViewHolder> groupAdapter = new GroupAdapter<>();
    private GridLayoutManager groupLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        initRecyclerView();
        Token mToken = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        viewModel.getSinglePost("r/AskReddit", "jbmf8f", "whats_older_than_we_think", mToken)
                .observe(getViewLifecycleOwner(), new Observer<Comments>() {
                    @Override
                    public void onChanged(Comments comments) {
                        for (ChildrenItem child : comments.getData().getChildren()) {
                            groupAdapter.add(new ExpandableCommentGroup(child, child.getData().getDepth(),"t3_jbmf8f",SinglePostFragment.this::onLoadMoreClicked));
                        }
                    }
                });

    }

    private void initRecyclerView () {
        mCommentsRecyclerView = mView.findViewById(R.id.recyclerview);
        groupLayoutManager = new GridLayoutManager(getActivity(), groupAdapter.getSpanCount());
        groupLayoutManager.setSpanSizeLookup(groupAdapter.getSpanSizeLookup());
        mCommentsRecyclerView.setLayoutManager(groupLayoutManager);
        mCommentsRecyclerView.setAdapter(groupAdapter);

//        Token mToken = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();
//        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
//        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
//        Log.i("LISTENER", "Before observe ");
//        viewModel.getMoreChildren("jbmf8f","g936mtd",mToken)
//                .observe(getViewLifecycleOwner(), new Observer<MoreChildren>() {
//                    @Override
//                    public void onChanged(MoreChildren moreChildren) {
//                        for (ChildrenItem more: moreChildren.getJson().getData().getChildren()) {
//                            Log.i("LISTENER", "DuringObserve");
//                            groupAdapter.add(new ExpandableCommentGroup(more, more.getData().getDepth(),"t3_jbmf8f",SinglePostFragment.this::onLoadMoreClicked));
//
//                        }
//                    }
//                });
    }

    @Override
    public void onLoadMoreClicked(@NotNull String linkId, @NotNull String moreChildren, int position) {

        Token mToken = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        Log.i("LISTENER", "Before observe " + position);
        viewModel.getMoreChildren(linkId,moreChildren,mToken);
    }
}