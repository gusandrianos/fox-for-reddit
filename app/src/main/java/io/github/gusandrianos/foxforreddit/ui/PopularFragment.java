package io.github.gusandrianos.foxforreddit.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.stetho.Stetho;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.ChildrenItem;
import io.github.gusandrianos.foxforreddit.data.models.Listing;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;


public class PopularFragment extends Fragment {
    // Add RecyclerView member
    private View mView;
    private RecyclerView mPostRecyclerView;
    private List<Post> mPosts;
    private Token mToken;
    PostRecyclerViewAdapter mPostRecyclerViewAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mView = inflater.inflate(R.layout.fragment_popular, container, false);
        mToken = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();
        initializeUI(mView);
        Stetho.initializeWithDefaults(getActivity());
        // Inflate the layout for this fragment
        return mView;
    }

    private void initRecycleView(@NotNull View view, List<Post> posts) {

        mPostRecyclerView = view.findViewById(R.id.recyclerview);
        mPostRecyclerViewAdapter = new PostRecyclerViewAdapter(getContext(), posts);
        mPostRecyclerViewAdapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY); //keep recyclerview on position
        mPostRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPostRecyclerView.setAdapter(mPostRecyclerViewAdapter);
    }

    public void initializeUI(View view) {
        PostViewModelFactory factory = InjectorUtils.getInstance().providePopularFragmentViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        viewModel.getPosts(mToken).observe(getViewLifecycleOwner(), new Observer<Listing>() {
            @Override
            public void onChanged(Listing listing) {
                List<Post> posts = new ArrayList<>();
                for (ChildrenItem child : listing.getTreeData().getChildren()) {
                    posts.add(child.getPost());
                }
                Log.d("POSTS", String.valueOf(posts.size()));
                initRecycleView(view, posts);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Token token = InjectorUtils.getInstance().provideTokenRepository(getActivity().getApplication()).getToken();
        if (!mToken.getAccessToken().equals(token.getAccessToken())) {
            mToken = token;
            initializeUI(mView);
        }
    }
}