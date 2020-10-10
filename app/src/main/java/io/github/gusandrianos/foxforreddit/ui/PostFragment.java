package io.github.gusandrianos.foxforreddit.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import org.jetbrains.annotations.NotNull;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;
import kotlin.Unit;


public class PostFragment extends Fragment implements PostAdapter.OnItemClickListener{
    // Add RecyclerView member
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
        mToken = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();
        page = getArguments().getInt("page", 0);
        subreddit = getArguments().getString("subreddit", "");
        filter = getArguments().getString("filter", "");
        initRecycleView();
        initializeUI();
        Stetho.initializeWithDefaults(getActivity());
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
        viewModel.getPosts(subreddit, filter, mToken).observe(getViewLifecycleOwner(), postPagingData -> {
            mPostRecyclerViewAdapter.submitData(getViewLifecycleOwner().getLifecycle(), postPagingData);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Token token = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();
        if (!mToken.getAccessToken().equals(token.getAccessToken())) {
            mToken = token;
            initRecycleView();
            initializeUI();
            Stetho.initializeWithDefaults(getActivity());
        }
    }

    @Override
    public void onItemClick(@NotNull Post post, @NotNull String clicked) {
        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();

        switch (clicked){
            case Constants.POST_SUBREDDIT:
                Toast.makeText(getActivity(), "Subreddit", Toast.LENGTH_SHORT).show();
                break;
            case Constants.POST_USER:
                Toast.makeText(getActivity(), "user", Toast.LENGTH_SHORT).show();
                break;
            case Constants.POST_THUMBNAIL:
                Toast.makeText(getActivity(), "Thumbnail", Toast.LENGTH_SHORT).show();
                break;
            case Constants.POST_VOTE_UP:
                Toast.makeText(getActivity(), "VoteUp", Toast.LENGTH_SHORT).show();
                break;
            case Constants.POST_VOTE_DOWN:
                Toast.makeText(getActivity(), "VoteDown", Toast.LENGTH_SHORT).show();
                break;
            case Constants.POST_COMMENTS_NUM:
                Toast.makeText(getActivity(), "CommentsNum", Toast.LENGTH_SHORT).show();
                break;
            case Constants.POST_SHARE:
                Toast.makeText(getActivity(), "Share", Toast.LENGTH_SHORT).show();
                break;
            case Constants.POST_VOTE_NOW:
                Toast.makeText(getActivity(), "Vote Now", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), post.getAuthor(), Toast.LENGTH_SHORT).show();
        }
    }

}