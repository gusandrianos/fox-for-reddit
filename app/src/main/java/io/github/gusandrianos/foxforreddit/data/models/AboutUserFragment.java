//package io.github.gusandrianos.foxforreddit.data.models;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import io.github.gusandrianos.foxforreddit.R;
//
//public class AboutUserFragment extends Fragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_about_user, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    public static AboutUserFragment newInstance(String param1, String param2) {
//        AboutUserFragment fragment = new AboutUserFragment();
//        Bundle args = new Bundle();
//        args.putString(, param1);
//        args.putString(, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//}