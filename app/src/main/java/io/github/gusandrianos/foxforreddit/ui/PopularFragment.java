package io.github.gusandrianos.foxforreddit.ui;

import android.os.Bundle;

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

import java.util.List;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PostViewModelFactory;
import io.github.gusandrianos.foxforreddit.viewmodels.TokenViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.TokenViewModelFactory;


public class PopularFragment extends Fragment {
    // Add RecyclerView member
    private View mView;
    private RecyclerView mRecyclerView;
    private List<Post> mPosts;
    private Token mToken;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mView = inflater.inflate(R.layout.fragment_popular, container, false);
        getCachedToken(mView);
        Stetho.initializeWithDefaults(getActivity());
        // Inflate the layout for this fragment
        return mView;

    }

    private void initRecycleView(@NotNull View view, List<Post> posts) {

        mRecyclerView = view.findViewById(R.id.recyclerview);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), posts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initializeUI(View view) {
        PostViewModelFactory factory = InjectorUtils.getInstance().providePopularFragmentViewModelFactory();
        PostViewModel viewModel = new ViewModelProvider(this, factory).get(PostViewModel.class);
        viewModel.getPosts(mToken).observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                Log.d("POSTS", String.valueOf(posts.size()));
                initRecycleView(view, posts);
            }
        });
    }

    void getAuthorizedUserToken(String c, View view) {
        TokenViewModelFactory factory = InjectorUtils.getInstance().provideTokenViewModelFactory(getActivity().getApplication());
        TokenViewModel viewModel = new ViewModelProvider(this, factory).get(TokenViewModel.class);
        viewModel.getToken(c, "https://gusandrianos.github.io/login").observe(getViewLifecycleOwner(), new Observer<Token>() {
            @Override
            public void onChanged(Token token) {
                mToken = token;
                Log.i("getAuthorizedUserToken", "Successfully got a new AuthorizedUser token");
                initializeUI(view);
            }
        });
    }

    void getUserlessToken(View view) {
        TokenViewModelFactory factory = InjectorUtils.getInstance().provideTokenViewModelFactory(getActivity().getApplication());
        TokenViewModel viewModel = new ViewModelProvider(this, factory).get(TokenViewModel.class);
        viewModel.getToken().observe(getViewLifecycleOwner(), new Observer<Token>() {
            @Override
            public void onChanged(Token token) {
                mToken = token;
                Log.i("getUserlessToken", "Successfully got a new Userless token");
                initializeUI(view);
            }
        });
    }

    void getCachedToken(View view) {
        TokenViewModelFactory factory = InjectorUtils.getInstance().provideTokenViewModelFactory(getActivity().getApplication());
        TokenViewModel viewModel = new ViewModelProvider(this, factory).get(TokenViewModel.class);
        viewModel.getCachedToken().observe(getViewLifecycleOwner(), new Observer<List<Token>>() {
            @Override
            public void onChanged(List<Token> tokens) {
                if (tokens.size() > 0) {
                    mToken = tokens.get(0);
                    Log.i("getCachedToken", "Passed Cached Token");
                    initializeUI(view);
                } else {
                    Log.i("getCachedToken", "Trying to get a Userless token");
                    getUserlessToken(view);
                }
            }
        });
    }
}