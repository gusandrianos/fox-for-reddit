package io.github.gusandrianos.foxforreddit.ui.fragments;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.ReplyThing;
import io.github.gusandrianos.foxforreddit.data.models.Thing;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.MessagesWithUserAdapter;

public class MessagesWithUserFragment extends Fragment {

    Data data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getParentFragmentManager().setFragmentResultListener("reply", getViewLifecycleOwner(), (key, bundle) -> {
            boolean success = bundle.getBoolean("success");
            if (success)
                requireActivity().onBackPressed();
        });

        return inflater.inflate(R.layout.fragment_messages_with_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpNavigation();

        MessagesWithUserFragmentArgs messagesWithUserFragmentArgs = MessagesWithUserFragmentArgs.fromBundle(requireArguments());
        data = messagesWithUserFragmentArgs.getData();

        Thing replies;

        if (data.getReplies() instanceof String) {
            replies = null;
        } else {
            Type type = new TypeToken<Thing>() {
            }.getType();
            Gson gson = new Gson();
            replies = gson.fromJson(gson.toJsonTree(data.getReplies()).getAsJsonObject(), type);
        }

        if (replies != null) {
            RecyclerView messagesRecyclerView = view.findViewById(R.id.recyclerview_messages_with_user);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
            linearLayoutManager.setStackFromEnd(true);

            messagesRecyclerView.addItemDecoration(new DividerItemDecoration(messagesRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
            messagesRecyclerView.setHasFixedSize(true);

            messagesRecyclerView.setLayoutManager(linearLayoutManager);

            ReplyThing replyThing = new ReplyThing(data);
            Thing extraReply;

            Type type = new TypeToken<Thing>() {
            }.getType();
            Gson gson = new Gson();
            extraReply = gson.fromJson(gson.toJsonTree(replyThing).getAsJsonObject(), type);

            replies.getData().getChildren().add(0, extraReply);

            MessagesWithUserAdapter adapter = new MessagesWithUserAdapter(replies);
            adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

            messagesRecyclerView.setAdapter(adapter);
        } else {
            TextView txtUser = view.findViewById(R.id.txt_messages_with_user_item_username);
            TextView txtTimeSent = view.findViewById(R.id.txt_messages_with_user_item_time_sent);
            TextView txtBody = view.findViewById(R.id.txt_messages_with_user_item_body);

            txtUser.setText(data.getAuthor());
            txtTimeSent.setText(DateUtils.getRelativeTimeSpanString(data.getCreatedUtc() * 1000).toString());
            txtBody.setText(data.getBody());

            ScrollView scrollView = view.findViewById(R.id.container_messages_with_user_item);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void setUpNavigation() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        Toolbar toolbar = requireActivity().findViewById(R.id.messages_with_user_toolbar);
        toolbar.inflateMenu(R.menu.reply_message);

        MenuItem messageButton = toolbar.getMenu().findItem(R.id.reply_message);
        messageButton.setOnMenuItemClickListener(item -> {

            navController.navigate(MessagesWithUserFragmentDirections.actionMessagesWithUserFragmentToComposeReplyToUserMessageFragment(data.getName()));
            return true;
        });

        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);

        NavigationUI.setupWithNavController(toolbar, navController);
    }
}
