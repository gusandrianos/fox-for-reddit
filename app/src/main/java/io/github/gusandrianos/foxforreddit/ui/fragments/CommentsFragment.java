package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;
import io.github.gusandrianos.foxforreddit.data.models.MoreChildrenList;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentGroup;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentItem;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;

public class CommentsFragment extends Fragment implements ExpandableCommentItem.OnItemClickListener {

    RecyclerView mCommentsRecyclerView;
    GroupAdapter<GroupieViewHolder> groupAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CommentsFragmentArgs commentsDialogFragmentArgs = CommentsFragmentArgs.fromBundle(requireArguments());
        String linkId = commentsDialogFragmentArgs.getLinkId();
        String loadChildren = commentsDialogFragmentArgs.getLoadChildren();
        MoreChildrenList moreChildrenList = commentsDialogFragmentArgs.getMoreChildren();
        List<String> moreChildren;

        setUpNavigation(view);

        mCommentsRecyclerView = view.findViewById(R.id.recycler_view_more_comments);
        TextView txtMoreComments = view.findViewById(R.id.txt_more_comments);
        txtMoreComments.setTag("0");

        if (moreChildrenList.getMoreChildrenList() != null && !moreChildrenList.getMoreChildrenList().isEmpty()) {
            moreChildren = moreChildrenList.getMoreChildrenList();
            String moreReplies = "Show more";
            txtMoreComments.setText(moreReplies);
            txtMoreComments.setTag("1");

            txtMoreComments.setOnClickListener(v -> {
                StringBuilder loadChildren1 = new StringBuilder(moreChildren.get(0));
                List<String> moreChildrenArray = new ArrayList<>();

                for (int i = 1; i < moreChildren.size(); i++)
                    if (i < 100)
                        loadChildren1.append(",").append(moreChildren.get(i));
                    else
                        moreChildrenArray.add(moreChildren.get(i));

                MoreChildrenList moreChildrenList1 = new MoreChildrenList();
                moreChildrenList1.setMoreChildrenList(moreChildrenArray);

                NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
                navController.navigate(CommentsFragmentDirections.actionCommentsFragmentSelf(linkId, loadChildren1.toString(), moreChildrenList1));

            });
        }

        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        viewModel.getMoreChildren(linkId, loadChildren, requireActivity().getApplication()).observe(getViewLifecycleOwner(), commentListing -> {
            groupAdapter = new GroupAdapter<>();
            initRecyclerView(groupAdapter);
            for (Object child : commentListing.getJson().getData().getChildren()) {
                ChildrenItem item;
                if (child instanceof String) {
                    item = new ChildrenItem((String) child);
                } else {
                    Type childType = new TypeToken<ChildrenItem>() {
                    }.getType();
                    Gson gson = new Gson();
                    item = gson.fromJson(gson.toJsonTree(child).getAsJsonObject(), childType);
                }
                groupAdapter.add(new ExpandableCommentGroup(item,
                        Objects.requireNonNull(item.getData()).getDepth(),
                        linkId, this, (MainActivity) requireActivity()));
            }
            if (txtMoreComments.getTag().equals("1"))
                txtMoreComments.setVisibility(View.VISIBLE);
        });


    }

    private void initRecyclerView(GroupAdapter<GroupieViewHolder> groupAdapter) {
        GridLayoutManager groupLayoutManager = new GridLayoutManager(getActivity(), groupAdapter.getSpanCount());
        groupLayoutManager.setSpanSizeLookup(groupAdapter.getSpanSizeLookup());
        mCommentsRecyclerView.setLayoutManager(groupLayoutManager);
        mCommentsRecyclerView.setAdapter(groupAdapter);
    }

    @Override
    public void onClick(@NotNull String linkId, ArrayList<String> moreChildren,
                        ChildrenItem comment, String actionType, int position) {
        if (moreChildren != null) {
            StringBuilder loadChildren = new StringBuilder(moreChildren.get(0));
            List<String> moreChildrenArray = new ArrayList<>();

            for (int i = 1; i < moreChildren.size(); i++)
                if (i < 100)
                    loadChildren.append(",").append(moreChildren.get(i));
                else
                    moreChildrenArray.add(moreChildren.get(i));

            MoreChildrenList moreChildrenList = new MoreChildrenList();
            moreChildrenList.setMoreChildrenList(moreChildrenArray);

            NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
            navController.navigate(SinglePostFragmentDirections.actionSinglePostFragmentToCommentsFragment(linkId, loadChildren.toString(), moreChildrenList));
        } else {
            //TODO
        }
    }

    private void setUpNavigation(View view) {
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_more_children);
        NavigationUI.setupWithNavController(toolbar, navController);
    }
}
