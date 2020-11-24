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
import com.jaredrummler.cyanea.Cyanea;

import org.apache.commons.text.StringEscapeUtils;

import java.lang.reflect.Type;

import io.github.gusandrianos.foxforreddit.Constants;
import io.github.gusandrianos.foxforreddit.NavGraphDirections;
import io.github.gusandrianos.foxforreddit.R;
import io.github.gusandrianos.foxforreddit.data.models.Data;
import io.github.gusandrianos.foxforreddit.data.models.ReplyThing;
import io.github.gusandrianos.foxforreddit.data.models.Thing;
import io.github.gusandrianos.foxforreddit.ui.MainActivity;
import io.github.gusandrianos.foxforreddit.utilities.MessagesWithUserAdapter;

public class ConversationFragment extends Fragment implements MessagesWithUserAdapter.UserClickedListener {

    Data data;
    String currentUser;
    String replyToFullname = null;
    String replyTo = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getParentFragmentManager().setFragmentResultListener(Constants.MESSAGE_REQUEST_KEY, getViewLifecycleOwner(), (key, bundle) -> {
            boolean success = bundle.getBoolean(Constants.MESSAGE_SENT_SUCCESS);
            if (success)
                requireActivity().onBackPressed();
        });

        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpNavigation(view);

        ConversationFragmentArgs messagesWithUserFragmentArgs = ConversationFragmentArgs.fromBundle(requireArguments());
        data = messagesWithUserFragmentArgs.getData();
        currentUser = ((MainActivity) requireActivity()).getFoxSharedViewModel().getCurrentUserUsername();

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

            MessagesWithUserAdapter adapter = new MessagesWithUserAdapter(replies, this);
            adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);

            messagesRecyclerView.setAdapter(adapter);

            for (int i = replies.getData().getChildren().size() - 1; i >= 0; i--) {
                if (!replies.getData().getChildren().get(i).getData().getAuthor().equals(currentUser)) {
                    replyToFullname = replies.getData().getChildren().get(i).getData().getName();
                    replyTo = replies.getData().getChildren().get(i).getData().getAuthor();
                    break;
                }
            }

        } else {
            TextView txtUser = view.findViewById(R.id.txt_messages_with_user_item_username);
            TextView txtTimeSent = view.findViewById(R.id.txt_messages_with_user_item_time_sent);
            TextView txtBody = view.findViewById(R.id.txt_messages_with_user_item_body);

            txtUser.setText(data.getAuthor());
            txtTimeSent.setText(DateUtils.getRelativeTimeSpanString(data.getCreatedUtc() * 1000).toString());
            String escapedText = StringEscapeUtils.unescapeXml(data.getBody());
            txtBody.setText(escapedText);

            txtUser.setOnClickListener(v -> navigateToUser(txtUser.getText().toString()));

            ScrollView scrollView = view.findViewById(R.id.container_messages_with_user_item);
            scrollView.setVisibility(View.VISIBLE);

            if (!data.getAuthor().equals(currentUser)) {
                replyToFullname = data.getName();
                replyTo = data.getAuthor();
            }
        }

        if (replyToFullname != null)
            setUpMessageAction(view);
    }

    private void navigateToUser(String user) {
        NavController navController = NavHostFragment.findNavController(this);
        NavGraphDirections.ActionGlobalUserFragment action = NavGraphDirections.actionGlobalUserFragment(user);
        navController.navigate(action);
    }

    private void setUpMessageAction(View view) {
        NavController navController = NavHostFragment.findNavController(this);
        Toolbar toolbar = view.findViewById(R.id.messages_with_user_toolbar);
        toolbar.inflateMenu(R.menu.reply_message);

        MenuItem messageButton = toolbar.getMenu().findItem(R.id.reply_message);
        messageButton.setVisible(true);
        messageButton.setOnMenuItemClickListener(item -> {
            navController.navigate(ConversationFragmentDirections.actionConversationFragmentToComposeReplyToUserMessageFragment(replyToFullname, replyTo));
            return true;
        });
    }

    private void setUpNavigation(View view) {
        MainActivity mainActivity = (MainActivity) requireActivity();
        NavController navController = NavHostFragment.findNavController(this);

        BottomNavigationView bottomNavigationView = mainActivity.bottomNavView;
        bottomNavigationView.setVisibility(View.GONE);

        Toolbar toolbar = view.findViewById(R.id.messages_with_user_toolbar);
        toolbar.setBackgroundColor(Cyanea.getInstance().getPrimary());

        NavigationUI.setupWithNavController(toolbar, navController);
    }

    @Override
    public void onUserClicked(String user) {
        navigateToUser(user);
    }
}
