package io.github.gusandrianos.foxforreddit.utilities;

import android.util.Log;

import io.github.gusandrianos.foxforreddit.data.repositories.PostRepository;
import io.github.gusandrianos.foxforreddit.viewmodels.PopularFragmentViewModelFactory;

public class InjectorUtils {

    private static InjectorUtils instance;

    public static InjectorUtils getInstance() {
        if (instance == null) {
            instance = new InjectorUtils();
            Log.i("INSTANCE-INJECTOR", "created new instance");
        }else{Log.i("INSTANCE-INJECTOR", "passed same instance");}
        return instance;
    }

    private InjectorUtils() {

    }

    public PopularFragmentViewModelFactory providePopularFragmentViewModelFactory(){
        PostRepository postRepository = PostRepository.getInstance();
        return new PopularFragmentViewModelFactory(postRepository);
    }
}

