package io.github.gusandrianos.foxforreddit.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.github.gusandrianos.foxforreddit.data.models.Listing;
import io.github.gusandrianos.foxforreddit.data.models.Post;
import io.github.gusandrianos.foxforreddit.data.models.Token;
import io.github.gusandrianos.foxforreddit.data.repositories.PostRepository;

public class PostViewModel extends ViewModel {

//    private MutableLiveData<List<Post>> mPosts;
//    private PostRepository mPostRepository;
////
//    public void init() {        //Retrieve the data
//        if (mPosts != null) {
//            return; //We already retrieved the data
//        }
//        mPostRepository = PostRepository.getInstance();
//        mPosts = mPostRepository.getPosts();
//    }
//
//    public LiveData<List<Post>> getPosts() {
//        return mPosts;
//    }


    private PostRepository mPostRepository;
    private MutableLiveData<List<Post>> mPosts;

    public PostViewModel(PostRepository postRepository) {
        mPostRepository = postRepository;
    }

    public LiveData<List<Post>> getPosts(Token token) {
        return mPostRepository.getPosts(token);
    }

    public LiveData<List<Post>> getPosts(Token token, String subreddit, String filter) {
        return mPostRepository.getPosts(token, subreddit, filter);
    }
}
