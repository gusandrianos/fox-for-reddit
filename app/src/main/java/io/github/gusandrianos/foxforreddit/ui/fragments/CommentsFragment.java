package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.cyanea.Cyanea;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.GroupieViewHolder;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;
import io.github.gusandrianos.foxforreddit.data.models.MoreChildrenList;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentGroup;
import io.github.gusandrianos.foxforreddit.utilities.ExpandableCommentItem;
import io.github.gusandrianos.foxforreddit.utilities.FoxToolkit;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.SubredditViewModelFactory;
import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.linkify.LinkifyPlugin;

/*
    CommentsFragment provides more comments of a post
 */
public class CommentsFragment extends Fragment implements ExpandableCommentItem.OnItemClickListener {

    RecyclerView mCommentsRecyclerView;
    GroupAdapter<GroupieViewHolder> groupAdapter;

    NavController navController;
    Markwon markwon;

    String subreddit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        markwon = Markwon.builder(requireContext())
                .usePlugin(TablePlugin.create(requireContext()))
                .usePlugin(LinkifyPlugin.create())
                .build();

        CommentsFragmentArgs commentsDialogFragmentArgs = CommentsFragmentArgs.fromBundle(requireArguments());
        subreddit = commentsDialogFragmentArgs.getSubreddit();
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
            txtMoreComments.setTag("1"); //In case of tag equals "1", there are more comments to be loaded
            //in the end of the comments

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
                navController.navigate(CommentsFragmentDirections.actionCommentsFragmentSelf(linkId, loadChildren1.toString(), moreChildrenList1, subreddit));

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
                        linkId, this, (MainActivity) requireActivity(), markwon));
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


    /*
        ExpandableCommentGroup's listener
     */
    @Override
    public void onClick(@NotNull String linkId, ArrayList<String> moreChildren,
                        ChildrenItem comment, String actionType, View view) {

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

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


            navController.navigate(CommentsFragmentDirections.actionCommentsFragmentSelf(linkId, loadChildren.toString(), moreChildrenList, subreddit));
        } else {
            PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
            PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);

            switch (actionType) {
                case Constants.THING_VOTE_UP:
                    if (!FoxToolkit.INSTANCE.isAuthorized(requireActivity().getApplication()))
                        FoxToolkit.INSTANCE.promptLogIn((MainActivity) requireActivity());
                    else
                        FoxToolkit.INSTANCE.upVoteCommentModel(viewModel,
                                requireActivity().getApplication(), comment.getData());
                    break;
                case Constants.THING_VOTE_DOWN:
                    if (!FoxToolkit.INSTANCE.isAuthorized(requireActivity().getApplication()))
                        FoxToolkit.INSTANCE.promptLogIn((MainActivity) requireActivity());
                    else
                        FoxToolkit.INSTANCE.downVoteCommentModel(viewModel,
                                requireActivity().getApplication(), comment.getData());
                    break;
                case Constants.THING_REPLY:
                    if (!FoxToolkit.INSTANCE.isAuthorized(requireActivity().getApplication()))
                        FoxToolkit.INSTANCE.promptLogIn((MainActivity) requireActivity());
                    else
                        navController.navigate(
                                CommentsFragmentDirections
                                        .actionCommentsFragmentToComposeReplyFragment(
                                                comment.getData().getName(), "New comment"));
                    break;
                case Constants.THING_AUTHOR:
                    String authorUsername = comment.getData().getAuthor();
                    NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(authorUsername);
                    navController.navigate(action);
                    break;
                case Constants.THING_MORE_ACTIONS:
                    if (!FoxToolkit.INSTANCE.isAuthorized(requireActivity().getApplication()))
                        FoxToolkit.INSTANCE.promptLogIn((MainActivity) requireActivity());
                    else {
                        PopupMenu menu = new PopupMenu(requireContext(), view);
                        menu.inflate(R.menu.comment_popup);

                        if (comment.getData().isSaved())
                            menu.getMenu().findItem(R.id.comment_save).setTitle("Unsave");
                        else
                            menu.getMenu().findItem(R.id.comment_save).setTitle("Save");

                        if (comment.getData().getAuthor()
                                .equals(((MainActivity) requireActivity()).getFoxSharedViewModel()
                                        .getCurrentUserUsername())) {
                            menu.getMenu().findItem(R.id.comment_edit).setVisible(true);
                            menu.getMenu().findItem(R.id.comment_delete).setVisible(true);
                        } else {
                            menu.getMenu().findItem(R.id.comment_edit).setVisible(false);
                            menu.getMenu().findItem(R.id.comment_delete).setVisible(false);
                        }

                        if (menu.getMenu().findItem(R.id.comment_edit).isVisible()) {
                            menu.getMenu().findItem(R.id.comment_edit).setOnMenuItemClickListener(edit -> {
                                goToEditThing(comment.getData().getName(), comment.getData().getBody());
                                return true;
                            });

                            menu.getMenu().findItem(R.id.comment_delete).setOnMenuItemClickListener(edit -> {
                                deleteThingAction(viewModel, comment.getData().getName());
                                return true;
                            });
                        }

                        menu.getMenu().findItem(R.id.comment_save).setOnMenuItemClickListener(save -> {
                            popUpMenuSave(comment, viewModel);
                            return true;
                        });

                        menu.getMenu().findItem(R.id.comment_report).setOnMenuItemClickListener(commentReport -> {
                            popUpMenuReport(comment);
                            return true;
                        });

                        menu.show();
                    }
                    break;
                default:
            }
        }
    }

    private void goToEditThing(String fullname, String selftext) {
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        navController.navigate(CommentsFragmentDirections
                .actionCommentsFragmentToEditThingFragment(Constants.EDIT_POST_TEXT,
                        fullname, selftext));
    }

    private void deleteThingAction(PostViewModel viewModel, String fullname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.ConfirmationAlertDialog);
        builder.setMessage("Are you sure?")
                .setCancelable(false)
                .setNegativeButton("Nope", (dialog, id) -> dialog.cancel())
                .setPositiveButton("Do it!", (dialog, id) ->
                        viewModel.deleteSubmission(fullname, requireActivity().getApplication())
                                .observe(getViewLifecycleOwner(), success -> {
                                    if (success) {
                                        requireActivity().onBackPressed();
                                    }
                                }));

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void popUpMenuSave(ChildrenItem comment, PostViewModel viewModel) {
        if (comment.getData().isSaved())
            viewModel.unSavePost(comment.getData().getName(), requireActivity().getApplication()).observe(getViewLifecycleOwner(), succeed -> {
                if (succeed)
                    comment.getData().setSaved(false);
            });
        else
            viewModel.savePost(comment.getData().getName(), requireActivity().getApplication()).observe(getViewLifecycleOwner(), succeed -> {
                if (succeed)
                    comment.getData().setSaved(true);
            });
    }

    private void popUpMenuReport(ChildrenItem comment) {
        SubredditViewModelFactory subredditFactory = InjectorUtils.getInstance().provideSubredditViewModelFactory();
        SubredditViewModel subredditViewModel = new ViewModelProvider(this, subredditFactory).get(SubredditViewModel.class);
        subredditViewModel.getSubredditRules(comment.getData().getSubredditNamePrefixed(),
                requireActivity().getApplication()).observe(getViewLifecycleOwner(),
                rulesBundle -> {
                    if (rulesBundle.getSiteRulesFlow() != null && rulesBundle.getRules() != null)
                        navController.navigate(NavGraphDirections.actionGlobalReportDialogFragment(
                                rulesBundle,
                                null, Constants.ALL_RULES,
                                comment.getData().getSubredditNamePrefixed(),
                                comment.getData().getName()));
                    else {
                        Toast.makeText(getContext(), "Failed to report", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpNavigation(View view) {
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.toolbar_more_children);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());

        MainActivity mainActivity = (MainActivity) requireActivity();
        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);

        NavigationUI.setupWithNavController(toolbar, navController);
    }
}
