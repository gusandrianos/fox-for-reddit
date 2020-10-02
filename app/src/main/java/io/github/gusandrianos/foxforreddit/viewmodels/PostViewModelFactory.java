package io.github.gusandrianos.foxforreddit.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.github.gusandrianos.foxforreddit.data.repositories.PostRepository;

public class PostViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private PostRepository mPostRepository;

    public PostViewModelFactory(PostRepository postRepository) {
        mPostRepository = postRepository;
    }

    @SuppressWarnings("unchecked") //suppress return
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return ((T) new PostViewModel(mPostRepository));
    }
}
