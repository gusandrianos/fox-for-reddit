package io.github.gusandrianos.foxforreddit.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.utilities.InjectorUtils;
import io.github.gusandrianos.foxforreddit.viewmodels.PopularFragmentViewModel;
import io.github.gusandrianos.foxforreddit.viewmodels.PopularFragmentViewModelFactory;
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
        TokenViewModelFactory factory = InjectorUtils.getInstance().provideTokenViewModelFactory();
        TokenViewModel viewModel = new ViewModelProvider(this, factory).get(TokenViewModel.class);
        viewModel.getToken().observe(getViewLifecycleOwner(), new Observer<Token>() {
            @Override
            public void onChanged(Token token) {
                mToken = token;
                initializeUI(mView);
            }
        });
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
        PopularFragmentViewModelFactory factory = InjectorUtils.getInstance().providePopularFragmentViewModelFactory();
        PopularFragmentViewModel viewModel = new ViewModelProvider(this, factory).get(PopularFragmentViewModel.class);
        viewModel.getPosts(mToken).observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                Log.d("POSTS", String.valueOf(posts.size()));
                initRecycleView(view, posts);
            }

//                result.setMovementMethod(new ScrollingMovementMethod());
//                result.setText("");
//
//                for (Post post : posts) {
//                    result.append("r/" + post.getSubreddit() + "\n");
//                    result.append("Posted by u/" + post.getAuthor() + "\n");
//                    result.append(post.getTitle() + "\n");
//                    String date = (String) android.text.format.DateUtils.getRelativeTimeSpanString(post.getCreatedUtc()*1000);
//                    result.append(date + "\n");
//                    result.append(post.getScore() + "\n\n");
//                }
//        }
        });
    }
}