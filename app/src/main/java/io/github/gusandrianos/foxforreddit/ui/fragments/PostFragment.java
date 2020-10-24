package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.PostAdapter;
import io.github.gusandrianos.foxforreddit.utilities.PostLoadStateAdapter;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;
import kotlin.Unit;


public class PostFragment extends Fragment implements PostAdapter.OnItemClickListener {
    private View mView;
    private Token mToken;
    String subreddit;
    String filter;
    int page;
    PostAdapter mPostRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = getView();
        mToken = InjectorUtils.getInstance().provideTokenRepository().getToken(requireActivity().getApplication());
        page = getArguments().getInt("page", 0);
        subreddit = getArguments().getString("subreddit", "");
        filter = getArguments().getString("filter", "");
        initRecycleView();
        initializeUI();
    }

    private void initRecycleView() {
        RecyclerView mPostRecyclerView = mView.findViewById(R.id.recyclerview);
        mPostRecyclerViewAdapter = new PostAdapter(this);
        mPostRecyclerViewAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY); //keep recyclerview on position
        mPostRecyclerView.setHasFixedSize(true);
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPostRecyclerView.addItemDecoration(new DividerItemDecoration(mPostRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        PostLoadStateAdapter postLoadStateAdapter = new PostLoadStateAdapter(() -> {
            mPostRecyclerViewAdapter.retry();
            return Unit.INSTANCE;
        });
        mPostRecyclerView.setAdapter(mPostRecyclerViewAdapter.withLoadStateFooter(postLoadStateAdapter));
    }

    public void initializeUI() {
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        viewModel.getPosts(subreddit, filter, getActivity().getApplication()).observe(getViewLifecycleOwner(), postPagingData -> {
            mPostRecyclerViewAdapter.submitData(getViewLifecycleOwner().getLifecycle(), postPagingData);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Token token = InjectorUtils.getInstance().provideTokenRepository().getToken(requireActivity().getApplication());
        if (!mToken.getAccessToken().equals(token.getAccessToken())) {
            mToken = token;
            initRecycleView();
            initializeUI();
        }
    }

    @Override
    public void onItemClick(@NotNull Data post, @NotNull String clicked, int postType) {      //ToDo improve voting system (Binding Adapter and viewModel)
//        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        int currentDestinationID = Objects.requireNonNull(navController.getCurrentDestination()).getId();
        PostViewModelFactory factory = InjectorUtils.getInstance().providePostViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        switch (clicked) {
            case Constants.POST_SUBREDDIT:
                Toast.makeText(getActivity(), "Subreddit", Toast.LENGTH_SHORT).show();
                //Todo open post
                break;
            case Constants.POST_USER:
                Toast.makeText(getActivity(), "user", Toast.LENGTH_SHORT).show();

                if (currentDestinationID == R.id.mainFragment) {
                    MainActivity mainActivity = (MainActivity) requireActivity();
                    String authorUsername = post.getAuthor();
                    mainActivity.viewingSelf = authorUsername.equals(mainActivity.currentUserUsername);
                    MainFragmentDirections.ActionMainFragmentToUserFragment action = MainFragmentDirections.actionMainFragmentToUserFragment(null, authorUsername);
                    navController.navigate(action);
                }
                break;
            case Constants.POST_THUMBNAIL:
                Toast.makeText(getActivity(), "Thumbnail", Toast.LENGTH_SHORT).show();
                //Todo open thumbnail
                break;
            case Constants.POST_VOTE_UP:
                if (post.getLikes() == null || !((Boolean) post.getLikes())) {  //If down or no voted
                    viewModel.votePost("1", post.getName(), requireActivity().getApplication());      //then send up vote
                    post.setLikes(true);
                } else {                                                       //else (up voted)
                    viewModel.votePost("0", post.getName(), requireActivity().getApplication());      //send no vote
                    post.setLikes(null);
                }
                break;
            case Constants.POST_VOTE_DOWN:
                if (post.getLikes() == null || ((Boolean) post.getLikes())) {  //If up or no voted
                    viewModel.votePost("-1", post.getName(), requireActivity().getApplication());    //then send down vote
                    post.setLikes(false);
                } else {                                                      //else (down voted)
                    viewModel.votePost("0", post.getName(), requireActivity().getApplication());     //send no vote
                    post.setLikes(null);
                }
                break;
            case Constants.POST_COMMENTS_NUM:
                Toast.makeText(getActivity(), "CommentsNum", Toast.LENGTH_SHORT).show();
                //Todo open post
                break;
            case Constants.POST_SHARE:
                Toast.makeText(getActivity(), "Share", Toast.LENGTH_SHORT).show();
                //Todo share
                break;
            case Constants.POST_VOTE_NOW:
                Toast.makeText(getActivity(), "Vote Now", Toast.LENGTH_SHORT).show();
                //Todo open vote post
                break;
            default:
//                Toast.makeText(getActivity(), post.getAuthor(), Toast.LENGTH_SHORT).show();

                if (currentDestinationID == R.id.mainFragment) {
                    MainFragmentDirections.ActionMainFragmentToSinglePostFragment action = MainFragmentDirections.actionMainFragmentToSinglePostFragment(post, postType);
                    navController.navigate(action);
                } else if (currentDestinationID == R.id.userFragment) {
                    UserFragmentDirections.ActionUserFragmentToSinglePostFragment action = UserFragmentDirections.actionUserFragmentToSinglePostFragment(post, postType);
                    navController.navigate(action);
                }
        }
    }

    public static PostFragment newInstance(String subreddit, String filter) {
        PostFragment fragment = new PostFragment();

        Bundle args = new Bundle();
        args.putString("subreddit", subreddit);
        args.putString("filter", filter);
        fragment.setArguments(args);

        return fragment;
    }
}